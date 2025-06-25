package Server;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import static java.lang.Integer.parseInt;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

public class HTTPRequest {
    private String type;
    private String version;
    private HTTPMethod method;
    private String uri;
    private Map<String, String> headers;
    private String body;

    public HTTPRequest(InputStream data) throws IOException, IllegalArgumentException {
        this.parse(data);
    }

    private void parse(InputStream data) throws IOException {
        InputStreamReader inReader = new InputStreamReader(data);
        BufferedReader reader = new BufferedReader(inReader);
        String requestLine = reader.readLine();

        if (requestLine == null || requestLine.isBlank()) {
            throw new IllegalArgumentException();
        }

        System.out.println(requestLine);

        String[] requestParts = requestLine.split(" ");
        this.method = HTTPMethod.fromString(requestParts[0]);
        this.uri = requestParts[1];
        if (uri.equals("/")) {
            uri = "/home.html";
        }
        String[] uriParts = uri.split("\\.");
        this.type = ContentType.get(uriParts[1]);
        this.version = requestParts[2];


        this.headers = new HashMap<>();
        String currLine = reader.readLine();
        while (currLine != null && !currLine.isEmpty()) {
            String[] headerParts = currLine.split(":");
            headers.put(headerParts[0], headerParts[1]);
            currLine = reader.readLine();
        }

        if (headers.containsKey("Content-Length")) {
            int contentLength = parseInt(headers.get("Content-Length"));
            char[] bodyChars = new char[contentLength];
            reader.read(bodyChars);
            this.body = new String(bodyChars);
        }
    }

    public void printRequest() {
        String formatHeaders = headers.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(joining("\r\n"));

        String formatRequestLine = join(" ", this.method.toStringMethod(), this.uri, this.version);

        String formatBody = (this.body == null) ? "" : this.body;

        String formatRequest = join("\r\n", formatRequestLine, formatHeaders, " ", formatBody);

        System.out.println(formatRequest);
    }

    public String getBody() {
        return body;
    }

    public String getVersion() {
        return version;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public String getType() {
        return this.type;
    }
}

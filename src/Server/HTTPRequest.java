package Server;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import static java.lang.Integer.parseInt;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

public class HTTPRequest {
    private String version;
    private HTTPMethod method;
    private String uri;
    private Map<String, String> headers;
    private byte[] body;

    public HTTPRequest(InputStream data) throws IOException, IllegalArgumentException {
        this.parse(data);
    }

    private void parse(InputStream data) throws IOException, IllegalArgumentException {
        InputStreamReader inReader = new InputStreamReader(data);
        BufferedReader reader = new BufferedReader(inReader);
        String requestLine = reader.readLine();

        if (requestLine == null || requestLine.isBlank()) {
            throw new IllegalArgumentException("Invalid request: request line is empty");
        }

        System.out.println(requestLine);

        String[] requestParts = requestLine.split(" ");
        if (requestParts.length < 3) {
            throw new IllegalArgumentException("Invalid request: request line missing parts");
        }
        this.method = HTTPMethod.fromString(requestParts[0]);
        this.uri = requestParts[1];
        this.version = requestParts[2];

        this.headers = new HashMap<>();
        String currLine = reader.readLine();
        while (currLine != null && !currLine.isEmpty()) {
            String[] headerParts = currLine.split(":");
            headers.put(headerParts[0].trim(), headerParts[1].trim());
            currLine = reader.readLine();
        }

        if (headers.containsKey("Content-Length")) {
            int contentLength = parseInt(headers.get("Content-Length").trim());
            char[] bodyChars = new char[contentLength];
            int totalRead = 0;
            while (totalRead < contentLength) {
                int read = reader.read(bodyChars, totalRead, contentLength - totalRead);
                if (read == -1) break;
                totalRead += read;
            }
            this.body = new String(bodyChars).getBytes(StandardCharsets.UTF_8);
        }
    }

    public void printRequest() {
        String formatHeaders = headers.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(joining("\r\n"));

        String formatRequestLine = join(" ", this.method.toStringMethod(), this.uri, this.version);

        String formatBody = (this.body == null) ? "" : new String(body, StandardCharsets.UTF_8);

        String formatRequest = join("\r\n", formatRequestLine, formatHeaders, " ", formatBody);

        System.out.println(formatRequest);
    }

    public byte[] getBody() {
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
}

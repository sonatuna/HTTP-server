import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HTTPRequest {

    public String version;
    public HTTPMethod method;
    public String uri;
    public Map<String, String> headers;
    public String body;

    public HTTPRequest(InputStream data) throws IOException {
        this.parse(data);
    }

    private void parse(InputStream data) throws IOException {
        InputStreamReader inReader = new InputStreamReader(data);
        BufferedReader reader = new BufferedReader(inReader);
        String requestLine = reader.readLine();

        String[] requestParts = requestLine.split(" ");
        this.method = HTTPMethod.fromString(requestParts[0]);
        this.uri = requestParts[1];
        this.version = requestParts[2];

        this.headers = new HashMap<>();

        String currLine = reader.readLine();
        while (currLine != null && !currLine.isEmpty()) {
            String[] headerParts = currLine.split(":");
            headers.put(headerParts[0], headerParts[1]);
            currLine = reader.readLine();
        }

        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] bodyChars = new char[contentLength];
            reader.read(bodyChars);
            this.body = new String(bodyChars);
        }
    }

    public void printRequest() {
        String formatHeaders = headers.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("\r\n"));

        String formatRequestLine = String.join(" ", this.method.toStringMethod(), this.uri, this.version);

        String formatBody = (this.body == null) ? "" : this.body;

        String formatResponse = String.join("\r\n", formatRequestLine, formatHeaders, " ", formatBody);

        System.out.println(formatResponse);
    }
}

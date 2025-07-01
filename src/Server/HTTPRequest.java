package Server;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import static java.lang.Integer.parseInt;

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
        String requestLine = null;
        try {
            requestLine = reader.readLine();
        } catch (IOException e) {
            throw new IOException("Failed to read HTTP request line, " + e.getMessage());
        }

        if (requestLine == null || requestLine.isBlank()) {
            throw new IllegalArgumentException("Invalid request: request line is empty");
        }

        System.out.println("[REQUEST] " + requestLine);

        String[] requestParts = requestLine.split(" ");
        if (requestParts.length < 3) {
            throw new IllegalArgumentException("Invalid request: request line missing parts");
        }
        try {
            this.method = HTTPMethod.fromString(requestParts[0]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid request: " + e.getMessage());
        }
        this.uri = requestParts[1];
        this.version = requestParts[2];

        this.headers = new HashMap<>();

        String currLine = null;
        try {
            currLine = reader.readLine();
        } catch (IOException e) {
            throw new IOException("Failed to read header line, " + e.getMessage());
        }
        while (currLine != null && !currLine.isEmpty()) {
            String[] headerParts = currLine.split(":");
            if (headerParts.length < 2) {
                throw new IllegalArgumentException("Invalid request: invalid headers");
            }
            headers.put(headerParts[0].trim(), headerParts[1].trim());
            try {
                currLine = reader.readLine();
            } catch (IOException e) {
                throw new IOException("Failed to read header line, " + e.getMessage());
            }
        }

        if (headers.containsKey("Content-Length")) {
            int contentLength = 0;
            if (headers.get("Content-Length") == null) {
                throw new IllegalArgumentException("Invalid request: invalid content-length header");
            }

            try {
                contentLength = parseInt(headers.get("Content-Length").trim());
                if (contentLength < 0 || contentLength > 10_000_000) throw new IllegalArgumentException("Content-Length is negative or exceeds allowed limit");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid request: invalid content-length value");
            }

            char[] bodyChars = new char[contentLength];
            int totalRead = 0;
            while (totalRead < contentLength) {
                int read = reader.read(bodyChars, totalRead, contentLength - totalRead);
                if (read == -1) {
                    throw new IOException("Unexpected end of stream while reading request body");
                }
                totalRead += read;
            }
            this.body = new String(bodyChars).getBytes(StandardCharsets.UTF_8);
        }
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

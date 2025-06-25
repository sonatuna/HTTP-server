package Server;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import static java.lang.String.*;
import static java.util.stream.Collectors.joining;

public class HTTPResponse {
    private final String version;
    private final HTTPStatus status;
    private byte[] body;
    private final Map<String, String> headers = new HashMap<>();
    public byte[] responseToBytes;
    private final String contentType;
    private int length;

    public HTTPResponse(HTTPStatus status, byte[] body, String contentType) {
        this.status = status;
        this.version = "HTTP/1.1";
        this.contentType = contentType;
        this.length = 0;
        if (body != null) {
            this.body = body;
            this.length = body.length;
        }
        headers.put("Content-Length", valueOf(this.length));
        headers.put("Content-Type", (contentType == null) ? "Error" : contentType);
        formatResponse();
    }

    public void formatResponse() {
        String statusString = format("%s %s %s", version, status.getCode(), status.getReason());
        String headerString = headers.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(joining("\r\n"));
        String fullHeader = statusString + "\r\n" + headerString + "\r\n\r\n";
        byte[] headerToBytes = fullHeader.getBytes(StandardCharsets.US_ASCII);
        if (this.body != null) {
            responseToBytes = new byte[headerToBytes.length + body.length];
            System.arraycopy(headerToBytes, 0, responseToBytes, 0, headerToBytes.length);
            System.arraycopy(body, 0, responseToBytes, headerToBytes.length, body.length);
        } else {
            responseToBytes = headerToBytes;
        }
    }

    public String getVersion() {
        return this.version;
    }

    public HTTPStatus getStatus() {
        return this.status;
    }

    public byte[] getBody() {
        return this.body;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public byte[] getResponse() {
        return this.responseToBytes;
    }

    public String getContentType() {
        return this.contentType;
    }

    public int getLength() {
        return this.length;
    }

}

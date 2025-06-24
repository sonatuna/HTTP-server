package Server;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.*;
import static java.util.stream.Collectors.joining;

public class HTTPResponse {
    private final String version;
    private final HTTPStatus status;
    private String body = "";
    private final Map<String, String> headers = new HashMap<>();
    public String response;

    public HTTPResponse(HTTPStatus status, byte[] body, String contentType) {
        this.status = status;
        this.version = "HTTP/1.1";
        if (body != null) {
            this.body = new String(body, StandardCharsets.UTF_8);
        }
        int length = this.body.length();
        headers.put("Content-Length", valueOf(length));
        headers.put("Content-Type", (contentType == null) ? "Error" : contentType);
        formatResponse();
    }

    public void putHeader(String headerTitle, String headerContent) {
        headers.put(headerTitle, headerContent);
    }

    public void formatResponse() {
        String statusString = format("%s %s %s", version, status.getCode(), status.getReason());
        String headerString = headers.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(joining("\r\n"));
        this.response = join("\r\n", statusString, headerString, "\r\n", body);
    }

    public void printResponse() {
        System.out.println(this.response);
    }

    public byte[] toBytes() {
        return response.getBytes(StandardCharsets.UTF_8);
    }
}

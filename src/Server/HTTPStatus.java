package Server;

import java.util.Map;
import java.util.HashMap;

public enum HTTPStatus {
    OK(200, "OK"),
    NOT_FOUND(404, "NOT FOUND"),
    BAD_REQUEST(400, "BAD REQUEST");

    private final int code;
    private final String reason;
    public static final Map<Integer, HTTPStatus> codeMap = new HashMap<>();


    HTTPStatus(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return this.code;
    }

    public String getReason() {
        return this.reason;
    }

    static {
        for (HTTPStatus status : values()) {
            codeMap.put(status.code, status);
        }
    }

    public static HTTPStatus fromCode(int code) {
        return codeMap.get(code);
    }
}

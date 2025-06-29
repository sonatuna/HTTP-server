package Server;

import java.util.Map;
import java.util.HashMap;

public enum HTTPStatus {
    OK(200, "OK"),
    CREATED(201, "CREATED"),
    NOT_FOUND(404, "NOT FOUND"),
    BAD_REQUEST(400, "BAD REQUEST"),
    NOT_IMPLEMENTED(501, "NOT IMPLEMENTED"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR");


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

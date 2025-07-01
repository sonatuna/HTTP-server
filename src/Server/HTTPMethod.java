package Server;

public enum HTTPMethod {
    GET,
    CONNECT,
    DELETE,
    HEAD,
    OPTIONS,
    PATCH,
    POST,
    PUT,
    TRACE;


    public static HTTPMethod fromString(String string) {
        try {
            return HTTPMethod.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported HTTP Method: " + string);
        }
    }

    public String toStringMethod() {
        return this.toString();
    }
}

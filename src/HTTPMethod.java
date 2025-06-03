public enum HTTPMethod {
    GET;

    public static HTTPMethod fromString(String string) {
        return HTTPMethod.valueOf(string.toUpperCase());
    }

    public String toStringMethod() {
        return this.toString();
    }
}

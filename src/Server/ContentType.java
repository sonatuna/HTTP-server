package Server;
import java.util.Map;

public class ContentType {
    public static final Map<String, String> TYPES = Map.ofEntries(
            Map.entry("html", "text/html"),
            Map.entry("htm", "text/html"),
            Map.entry("css", "text/css"),
            Map.entry("js", "application/javascript"),
            Map.entry("png", "image/png"),
            Map.entry("jpg", "image/jpeg"),
            Map.entry("jpeg", "image/jpeg"),
            Map.entry("gif", "image/gif"),
            Map.entry("svg", "image/svg+xml"),
            Map.entry("txt", "text/plain"),
            Map.entry("json", "application/json"),
            Map.entry("ico", "image/x-icon")
    );

    public static String get(String type) {
        return TYPES.getOrDefault(type.toLowerCase(), "text/html");
    }
}

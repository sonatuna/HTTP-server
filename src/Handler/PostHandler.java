package Handler;

import Dispatcher.Strategy;
import Server.HTTPRequest;
import Server.HTTPResponse;
import Server.HTTPStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PostHandler implements Strategy {
    @Override
    public HTTPResponse handleRequest(HTTPRequest request) throws IOException {
        System.out.println("[INFO] Running post handler");
        String uri = request.getUri();
        byte[] body = request.getBody();
        HTTPStatus status = null;

        Path folderPath = Paths.get("resources" + uri);
        if (Files.exists(folderPath)) {
            if (!Files.isDirectory(folderPath)) {
                throw new IOException("Invalid request: not a valid directory");
            }
        } else {
            Files.createDirectory(folderPath);
        }

        Path filePath = null;
        try {
            filePath = Path.of("resources", uri, "newfile.txt");
            Files.write(filePath, body);
            status = HTTPStatus.CREATED;
            System.out.println(String.format("[INFO] File successfully created at %s", filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HTTPResponse(status, null, null);
    }
}

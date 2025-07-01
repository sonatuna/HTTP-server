package Handler;

import Dispatcher.Strategy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import Server.ContentType;
import Server.HTTPRequest;
import Server.HTTPResponse;
import Server.HTTPStatus;

public class GetHandler implements Strategy {
    @Override
    public HTTPResponse handleRequest(HTTPRequest request) throws IOException {
        System.out.println("[INFO] Running get handler");
        HTTPStatus status;
        HTTPResponse response;
        byte[] body;

        String uri = (request.getUri().equals("/")) ? "/home.html" : request.getUri();
        String[] uriParts = uri.split("\\.");
        String type = ContentType.get(uriParts[1]);

        String filename = uri.replaceAll("^/+", "").replaceAll("/+$", "");
        Path filePath = Path.of("resources", filename);
        File file = filePath.toFile();
        if (file.exists()) {
            status = HTTPStatus.OK;
        } else {
            status = HTTPStatus.NOT_FOUND;
            filePath = Path.of("resources", "not-found.html");
        }
        body = Files.readAllBytes(filePath);
        response = new HTTPResponse(status, body, type);
        return response;
    }
}

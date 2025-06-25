package Handler;

import Dispatcher.Strategy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import Server.HTTPRequest;
import Server.HTTPResponse;
import Server.HTTPStatus;

public class GetHandler implements Strategy {
    @Override
    public HTTPResponse handleRequest(HTTPRequest request) throws IOException {
        System.out.println("running get handler");
        HTTPStatus status;
        HTTPResponse response;
        byte[] body;

        String filename = request.getUri().replaceAll("^/+", "").replaceAll("/+$", "");
        Path filePath = Path.of("resources", filename);
        File file = filePath.toFile();
        if (file.exists()) {
            status = HTTPStatus.OK;
        } else {
            status = HTTPStatus.NOT_FOUND;
            filePath = Path.of("resources", "not-found.html");
        }
        body = Files.readAllBytes(filePath);
        response = new HTTPResponse(status, body, request.getType());
        return response;
    }
}

package Handler;

import Dispatcher.Strategy;
import Server.HTTPRequest;
import Server.HTTPResponse;
import Server.HTTPStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NotImplementedHandler implements Strategy {
    @Override
    public HTTPResponse handleRequest(HTTPRequest request) throws IOException {
        System.out.println("running not implemented handler");
        Path filePath = Path.of("resources", "not-implemented.html");
        byte[] body = Files.readAllBytes(filePath);
        HTTPResponse response = new HTTPResponse(HTTPStatus.NOT_IMPLEMENTED, body, "text/html");
        return response;
    }
}

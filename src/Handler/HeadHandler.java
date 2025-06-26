package Handler;

import Dispatcher.Strategy;
import Server.ContentType;
import Server.HTTPRequest;
import Server.HTTPResponse;
import Server.HTTPStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HeadHandler implements Strategy {
    @Override
    public HTTPResponse handleRequest(HTTPRequest request) throws IOException {
        System.out.println("running head handler");
        HTTPStatus status;
        HTTPResponse response;

        String[] uriParts = request.getUri().split("\\.");
        String type = ContentType.get(uriParts[1]);

        String uri = (request.getUri() == "/") ? "home.html" : request.getUri();

        String filename = uri.replaceAll("^/+", "").replaceAll("/+$", "");
        Path filePath = Path.of("resources", filename);
        File file = filePath.toFile();
        if (file.exists()) {
            status = HTTPStatus.OK;
        } else {
            status = HTTPStatus.NOT_FOUND;
        }
        response = new HTTPResponse(status, null, type);
        return response;
    }
}

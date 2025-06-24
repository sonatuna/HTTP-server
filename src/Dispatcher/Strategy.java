package Dispatcher;
import Server.HTTPResponse;
import Server.HTTPRequest;

import java.io.IOException;

public interface Strategy {
    HTTPResponse handleRequest(HTTPRequest request) throws IOException;
}

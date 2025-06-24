package Handler;

import Dispatcher.Strategy;
import Server.HTTPRequest;
import Server.HTTPResponse;

public class NotFoundHandler implements Strategy {
    public NotFoundHandler() {

    }

    @Override
    public HTTPResponse handleRequest(HTTPRequest request) {
        System.out.println("running not found handler");
        return null;
    }
}

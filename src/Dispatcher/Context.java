package Dispatcher;
import Handler.GetHandler;
import Handler.HeadHandler;
import Handler.NotImplementedHandler;
import Handler.PostHandler;
import Server.HTTPMethod;
import Server.HTTPRequest;
import Server.HTTPResponse;
import Server.HTTPStatus;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Context {
    private final Map<String, Strategy> strategyMap = new HashMap<>();

    public Context() {
        strategyMap.put("GET", new GetHandler());
        strategyMap.put("HEAD", new HeadHandler());
        strategyMap.put("POST", new PostHandler());
    }

    public HTTPResponse dispatch(HTTPRequest request) throws IOException {
        HTTPMethod method = request.getMethod();
        if (method == null) {
            System.out.println("Invalid request: null method");
            return new HTTPResponse(HTTPStatus.BAD_REQUEST, null, null);
        }

        Strategy strategy = strategyMap.getOrDefault(method.toStringMethod(), new NotImplementedHandler());
        return strategy.handleRequest(request);
    }
}

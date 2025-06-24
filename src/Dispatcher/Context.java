package Dispatcher;
import Handler.GetHandler;
import Handler.NotFoundHandler;
import Server.HTTPRequest;
import Server.HTTPResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Context {
    private final Map<String, Strategy> strategyMap = new HashMap<>();

    public Context() {
        strategyMap.put("GET", new GetHandler());
    }

    public HTTPResponse dispatch(HTTPRequest request) throws IOException {
        Strategy strategy = strategyMap.getOrDefault(request.getMethod().toStringMethod(), new NotFoundHandler());
        return strategy.handleRequest(request);
    }


}

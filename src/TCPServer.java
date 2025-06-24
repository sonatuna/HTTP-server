import Dispatcher.Context;
import Server.HTTPRequest;
import Server.HTTPResponse;
import Server.HTTPStatus;

import java.io.*;
import java.net.*;

public class TCPServer {
    int port;

    public TCPServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.port);
        System.out.println("Server is listening at port: " + this.port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client is connected at address: " + clientSocket.getInetAddress());

            handleClientData(clientSocket);
            clientSocket.close();
        }

    }

    private void handleClientData(Socket clientSocket) {
        HTTPResponse response;
        try {
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            try {
                HTTPRequest request = new HTTPRequest(in);
                request.printRequest();

                Context context = new Context();
                response = context.dispatch(request);
            } catch (IllegalArgumentException e) {
                response = new HTTPResponse(HTTPStatus.BAD_REQUEST, null, null);
            }
            response.printResponse();
            out.write(response.toBytes());
            out.flush();

        } catch (IOException e) {
            System.out.println("Error occurred when handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Failed to close client socket " + e.getMessage());
            }
        }
    }
}

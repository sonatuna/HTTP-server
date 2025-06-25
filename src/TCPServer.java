import Dispatcher.Context;
import Server.HTTPRequest;
import Server.HTTPResponse;
import Server.HTTPStatus;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

public class TCPServer {
    int port;
    long startTime;

    public TCPServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.port);
        System.out.println("Server is listening at port: " + this.port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            startTime = System.currentTimeMillis();
            LocalDateTime connectionTime = LocalDateTime.now();
            System.out.println(String.format("[INFO] Client connected from %s at %s", clientSocket.getInetAddress(), connectionTime));
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
                System.out.println("Host: " + clientSocket.getInetAddress().getHostAddress() + "\r\n" + "User-Agent: " + request.getHeaders().get("User-Agent"));
                Context context = new Context();
                response = context.dispatch(request);
            } catch (IllegalArgumentException e) {
                response = new HTTPResponse(HTTPStatus.BAD_REQUEST, null, null);
            }
            System.out.println(String.format("[RESPONSE] %s — Content-Type: %s — Length: %d bytes", response.getStatus(), response.getContentType(), response.getLength()));
            out.write(response.responseToBytes);
            out.flush();
            long endTime = System.currentTimeMillis();
            long processingTime = endTime - startTime;
            System.out.println(String.format("[INFO] Response sent to %s in %d ms%n", clientSocket.getInetAddress(), processingTime));


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

import Dispatcher.Context;
import Server.HTTPRequest;
import Server.HTTPResponse;
import Server.HTTPStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private long startTime;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        System.out.printf("[INFO] Thread running for client %s", clientSocket.getInetAddress());
        System.out.println();
    }
    @Override
    public void run() {
        try {
            handleClientData(clientSocket);
        } catch (Exception e) {
            System.out.println("[FATAL] Unexpected error during request handling: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (clientSocket != null && !clientSocket.isClosed()) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("[ERROR] Failed to close client socket " + e.getMessage());
                }
                System.out.printf("[INFO] Thread shutting down for client %s", clientSocket.getInetAddress());
                System.out.println();
            }
        }
    }

    private void handleClientData(Socket clientSocket) {
        HTTPResponse response = null;
        try (InputStream in = clientSocket.getInputStream();
             OutputStream out = clientSocket.getOutputStream())
        {
            HTTPRequest request;
            try {
                request = new HTTPRequest(in);
                String ua = request.getHeaders().get("User-Agent");
                if (ua == null) {
                    System.out.println("[WARN] Missing User-Agent header");
                }
                System.out.println("Host: " + clientSocket.getInetAddress().getHostAddress() + "\r\n" + "User-Agent: " + ua);
                Context context = new Context();
                startTime = System.currentTimeMillis();
                response = context.dispatch(request);
            } catch (IllegalArgumentException e) {
                byte[] errorMessage = e.getMessage().getBytes();
                response = new HTTPResponse(HTTPStatus.BAD_REQUEST, errorMessage, "text/plain");
            } catch (IOException e) {
                System.out.println("[ERROR] I/O exception while handling request: " + e.getMessage());
            }

            if (response != null) {
                System.out.printf("[RESPONSE] %s %s — Content-Type: %s — Length: %d bytes%n", response.getStatus().getCode(), response.getStatus().getReason(), response.getContentType(), response.getLength());
                try {
                    out.write(response.responseToBytes);
                    out.flush();
                } catch (IOException e) {
                    System.out.println("[ERROR] I/O Error while writing response to output stream: " + e.getMessage());
                }
            }

            long endTime = System.currentTimeMillis();
            long processingTime = endTime - startTime;
            System.out.printf("[INFO] Response sent to %s in %d ms%n%n", clientSocket.getInetAddress(), processingTime);
        } catch (IOException e) {
            System.out.println("[ERROR] Error occurred getting input stream: " + e.getMessage());
        }
    }
}

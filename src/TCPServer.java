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
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.port);
            System.out.println("[INFO] Server is listening at port: " + this.port);
        } catch (BindException e) {
            System.out.println("[ERROR] Permission denied for port: " + e.getMessage());
            System.exit(1);
        } catch (UnknownHostException e) {
            System.out.println("[ERROR] Failed to resolve host: " + e.getMessage());
            System.exit(1);
        }

        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                LocalDateTime connectionTime = LocalDateTime.now();
                System.out.printf("[INFO] Client connected from %s at %s%n", clientSocket.getInetAddress(), connectionTime);
                handleClientData(clientSocket);
            } catch (IOException e) {
                System.out.println("[ERROR] I/O error while handling client: " + e.getMessage());
            } finally {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        System.out.println("[ERROR] Failed to close client socket " + e.getMessage());
                    }
                }
            }
        }
    }

    private void handleClientData(Socket clientSocket) {
        HTTPResponse response = null;
        try ( InputStream in = clientSocket.getInputStream();
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

import java.io.*;
import java.net.*;

public class TCPServer {
    int port;

    public TCPServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.port);
        System.out.println("Server is listening at port " + this.port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("client connected " + clientSocket.getInetAddress());

            handleClientData(clientSocket);
            clientSocket.close();
        }

    }

    private void handleClientData(Socket clientSocket) {
        try {
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            HTTPRequest request = new HTTPRequest(in);
            request.printRequest();

            String body = "<h1>Hello buggo :3</h1>";
            String response = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + body.length() + "\r\n" + "\r\n" + body;

            out.write(response.getBytes());
            out.flush();

        } catch (IOException e) {
            System.out.println("error handling client " + e.getMessage());
        }
    }

    private String createStatusString(int code) {
        String reason = String.valueOf(HTTPStatus.fromCode(code));
        String version = "HTTP/1.1";
        String statusString = String.format("%s %s", version, code, reason);

        return statusString;
    }
}

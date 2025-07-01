import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {
    private final int port;
    private final ExecutorService threadPool;

    public TCPServer(int port) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(10);
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
                threadPool.submit(new ClientHandler(clientSocket));
            } catch (IOException e) {
                System.out.println("[ERROR] I/O error while handling client: " + e.getMessage());
            } finally {
                threadPool.shutdown();
            }
        }
    }
}

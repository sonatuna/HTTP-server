import java.io.IOException;
import java.net.BindException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int port = 8080;
        try {
            TCPServer server = new TCPServer(port);
            server.start();
        } catch (IOException e) {
            System.out.println("[ERROR] I/O error during socket binding: " + e.getMessage());
            System.exit(1);
        }
    }
}
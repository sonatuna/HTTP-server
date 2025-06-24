import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int port = 8080;
        try {
            TCPServer server = new TCPServer(port);
            server.start();
        } catch (IOException e) {
            System.out.println("failed to connect");
        }
    }
}
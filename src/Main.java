import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            TCPServer server = new TCPServer(8080);
            server.start();
        } catch (IOException e) {
            System.out.println("failed to connect");
        }
    }
}
import java.io.IOException;

public class Server_Task5 {
    public static void main (String[] args) {
        try {
            Server server = new Server(12345);
            server.start();
        } catch (IOException e) {

        }
    }



}

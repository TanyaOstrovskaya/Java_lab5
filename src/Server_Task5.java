import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Server_Task5 {
    public static void main (String[] args) {
        try {
            int i = 0;
            DatagramSocket socket = new DatagramSocket(12345, InetAddress.getByName("localhost"));
            while (true) {
                i++;
                new Server(socket).run();
            }
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 12345");
        }
    }
}

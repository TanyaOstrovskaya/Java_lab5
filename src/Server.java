import java.io.*;
import java.net.*;
/* Lab 5, variant */

public class Server extends Thread {
    private DatagramSocket datagramSocket;

    public Server (int port) throws IOException {
        datagramSocket = new DatagramSocket(port);
    }

    @Override
    public void run() {
        try {
            this.startServer();
        } catch (Exception e) {
            System.out.println("Server is dead. " + e.getMessage());
            if ((datagramSocket != null) && (!datagramSocket.isClosed())) {
                this.datagramSocket.close();
            }
        }
    }

    private void startServer() throws IOException {
        byte[] receiveData = new byte[512];
        DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
        datagramSocket.receive(receivedPacket);
    }

    public void close() {
        if ((this.datagramSocket != null) && (!datagramSocket.isClosed())) {
            this.datagramSocket.close();
        }
    }
}

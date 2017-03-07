import java.net.* ;
import java.io.*;
import java.util.LinkedList;
import static java.util.Arrays.asList;

public class Server extends Thread {

    private final static int PACKET_SIZE = 100;

    private DatagramSocket socket;
    ByteArrayInputStream byteArrayInputStream;
    DataInputStream dataInputStream;
    double R;
    double x;
    double y;
    boolean answer;
    byte[] bytesArray;

    public Server(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Server started ...");
        try {
            DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
            socket.receive(packet);
            byteArrayInputStream = new ByteArrayInputStream(packet.getData());
            dataInputStream = new DataInputStream(byteArrayInputStream);
            R = dataInputStream.readDouble();
            x = dataInputStream.readDouble();
            y = dataInputStream.readDouble();

            answer = this.isInArea(x,y,R);
            System.out.println("point (x = " + x + "; y = " + y + ") radius = " + R +
                    ((answer == true) ? " is " : " is not ") + "in area " +
                    packet.getAddress() + " " + packet.getPort());

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            byte[] sendData = Boolean.toString(answer).getBytes("UTF-8");
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            socket.send(sendPacket);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private boolean isInArea (double x, double y, double R) {
        if  (((x>=0) && (y>=0) && (y < Math.sqrt(R*R - x*x))) ||
                ((x <= 0) && (y > 0) && (x > -R) && (y < R)) ||
                ((x > 0) && (y<=0) && (y > x-R))) {
            return true;
        } else {
            return false;
        }
    }
}

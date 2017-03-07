import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

    private final static int PACKET_SIZE = 100;
    private DatagramSocket socket;
    private boolean isServerAvaliablle;

    public Client(DatagramSocket socket) {
        this.socket = socket;
        isServerAvaliablle = false;
    }

    public boolean isServerAvaliablle() {
        return isServerAvaliablle;
    }

    public boolean doClient (double x, double y, double R) {
        boolean answer = false;

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeDouble(R);
            dataOutputStream.writeDouble(x);
            dataOutputStream.writeDouble(y);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(bytes, byteArrayOutputStream.size(), InetAddress.getByName("localhost"), 12345);
            socket.send(packet);
            isServerAvaliablle = true;
            socket.setSoTimeout(1000);
        } catch (Exception e) {
            this.isServerAvaliablle = false;
            System.out.println(e.getMessage());
        } finally {
            if (isServerAvaliablle) {
                try {
                    socket.setSoTimeout(10);
                    byte[] receiveData = new byte[PACKET_SIZE];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, PACKET_SIZE);
                    try {
                        socket.receive(receivePacket);
                        String receiveString = new String(receivePacket.getData()).trim();
                        if (receiveString.equals(new String("true")))
                            answer = true;
                        else
                            answer = false;
                    } catch (Exception e) {
                        isServerAvaliablle = false;
                        System.out.println(e.getMessage());
                    }

                } catch (Exception e) {
                    isServerAvaliablle = false;
                    System.out.println(e.getMessage());
                }
            }
        }
        return answer;
    }
}

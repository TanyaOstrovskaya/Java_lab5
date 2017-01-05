import java.io.*;
import java.net.*;
import java.util.LinkedList;
/* Lab 5, variant 50118 */

public class Client {
    private DatagramSocket datagramSocket;
    private int port;
    private InetAddress address;
    public static boolean serverIsAvailable;
    public static boolean isBusy = false;


    public Client(String address, int port) {
        try {
            this.port = port;
            this.address = InetAddress.getByName(address);
            this.datagramSocket = new DatagramSocket(port);
            System.out.println("Client: started");
        } catch (Exception e) {
            System.out.println("Unknown host or port");
        }
    }

    public LinkedList<Punctum> startClient(LinkedList<Punctum> punctums, double R) throws IOException {
        this.sendData(R);
        Punctum p = punctums.getFirst();
        p.isInside = isInside(p.getX(), p.getY());
        return punctums;
    }

    private boolean isInside(double x, double y) {
        boolean result = false;
        try {
            this.sendData(x);
            this.sendData(y);
            result = this.receiveBoolean();
        } catch (IOException e) {
        }
        return result;
    }

    <T> void sendData(T data) throws IOException {
        byte[] sendData = data.toString().getBytes("UTF-8");
        int port = datagramSocket.getPort();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
        datagramSocket.send(sendPacket);
    }

    public byte[] receiveData() throws IOException {
        byte[] buffer = new byte[512];
        DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
        datagramSocket.setSoTimeout(50);
        datagramSocket.receive(receivedPacket);
        buffer = receivedPacket.getData();
        return buffer;
    }

    private boolean receiveBoolean() throws IOException {
        byte[] isInsideBuffer = receiveData();
        String isInsideString = new String(isInsideBuffer, "UTF-8");
        boolean result = Boolean.parseBoolean(isInsideString);
        return result;
    }

    public void close() {
        if (datagramSocket != null) {
            datagramSocket.close();
        }
    }
}
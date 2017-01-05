import java.io.*;
import java.net.*;
/* Lab 5, variant */

public class Server extends Thread {

    private GraphCalculation calculator;
    private DatagramSocket datagramSocket;
    private int port;
    private InetAddress address;

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

    private byte[] receiveData () throws IOException  {
        byte[] receiveData = new byte[512];
        DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
        datagramSocket.receive(receivedPacket);
        this.port = datagramSocket.getPort();
        this.address = datagramSocket.getInetAddress();
        return receiveData;
    }

    <T> void sendData (T data) throws IOException{
        byte[] sendData = data.toString().getBytes("UTF-8");
        int port = datagramSocket.getPort();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
        datagramSocket.send(sendPacket);
    }

    private void startServer() throws IOException {
        double R = receiveDouble();
        while (datagramSocket.isConnected()) {
            double x = receiveDouble();
            double y = receiveDouble();
            boolean isInArea = this.isInArea(x,y);
            sendData(isInArea);
        }
    }

    private double receiveDouble() throws  IOException {
        byte[] radiusBytes = receiveData();
        String radiusString = new String(radiusBytes, "UTF-8");
        double result = Double.parseDouble(radiusString);
        return result;
    }

    public void close() {
        if ((this.datagramSocket != null) && (!datagramSocket.isClosed())) {
            this.datagramSocket.close();
        }
    }

    public boolean isInArea (double x, double y) { return calculator.isInArea(x, y); }

    public boolean isInArea (Punctum punctum) { return isInArea(punctum.getX(), punctum.getY()); }

}

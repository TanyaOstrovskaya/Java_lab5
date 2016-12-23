import java.io.*;
import java.net.*;
/* Lab 5, variant 50118 */

public class Client extends Thread {

    private DatagramSocket datagramSocket;
    private int port = 12345;
    private String host = "127.0.0.1";
    public static boolean serverIsAvailable;
    public static boolean isBusy = false;
    private TaskWindow window;

    public Client (String host, int port, TaskWindow window) {
        this.port = port;
        this.host = host;
        this.window = window;
    }

    public void runClient() throws IOException {
        this.datagramSocket = new DatagramSocket(port);
        System.out.println("Client: started");
    }

    public void sendData(byte[] data) throws IOException {
        try {
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName(host), port);
            datagramSocket.send(sendPacket);
        } catch (UnknownHostException ex) {
            System.out.println("Error: unknown host");
        }
    }

    public byte[] receiveData() throws IOException {
        byte[] buffer = new byte[512];
        DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
        datagramSocket.setSoTimeout(50);
        datagramSocket.receive(receivedPacket);
        buffer = receivedPacket.getData();
        return buffer;
    }

    public void close () {
        if (datagramSocket != null) {
            datagramSocket.close();
        }
    }

    @Override
    public void run () {
        serverIsAvailable = false;
        isBusy = true;
        String test = "1.0 1.0 1.0";
        while (!serverIsAvailable) {
            try {
                this.sendData(test.getBytes());
                int testAnsw = Integer.parseInt(new String(this.receiveData()).trim());
            } catch (Exception ex)  {
                serverIsAvailable = false;
                continue;
            }
            serverIsAvailable = true;
            if (!window.graph.punctumList.isEmpty())
                for (int i=0; i<window.graph.punctumList.size(); i++) {
                    String s = window.graph.punctumList.get(i).getX() + " " + window.graph.punctumList.get(i).getY() +
                            " " + window.graph.R;
                    try {
                        this.sendData(s.getBytes());
                        window.graph.punctumList.get(i).setHit(Integer.parseInt(new String(Task3.client.receiveData()).trim()));
                    } catch (Exception e) {
                    }
                }
            if (!Task3.graph..points.isEmpty())
                for (int i = 0; i < Task3.graph..points.size(); i++) {
                    String s = Task3.graph..points.get(i).getX() + " " + Task3.graph..points.get(i).getY() + " " + Task3.graph..contourRadius;
                    try {
                        Task3.client.sendData(s.getBytes());
                        Task3.graph.points.get(i).setHit(Integer.parseInt(new String(Task3.client.receiveData()).trim()));
                    } catch (Exception e) {
                    }
                }
            Task3.graph.repaint();
        }
        isBusy = false;
    }

    }
}
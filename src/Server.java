import java.net.* ;
import java.io.*;
import java.util.LinkedList;
import static java.util.Arrays.asList;

import java.net.*;
import java.io.*;

public class Server extends Thread {

    private DatagramSocket socket;
    DataInputStream iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
}




public class Server extends Thread {

    private Socket client;
    InputStream in = null;
    OutputStream out = null;
    double r = 0;
    double x = 0;
    double y = 0;

    public SampleServer(int num, Socket client){
        System.out.println("New client");
        this.client = client;
    }

    @Override
    public void run(){
        System.out.println("Starting the server");
        try {
            in = client.getInputStream();
            out = client.getOutputStream();
            DataInputStream inputStream = new DataInputStream(in);
            DataOutputStream outputStream = new DataOutputStream(out);
            r = inputStream.readDouble();
            x = inputStream.readDouble();
            y = inputStream.readDouble();
            outputStream.writeBoolean(area(x,y,r));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public boolean area(double x1, double y1, double R)
    {
        if ((x1<=R & x1>=0 & ((y1>=-R & y1<=0)||(Math.pow(x1, 2)+Math.pow(y1, 2)<= Math.pow(R, 2)/4)))|| (x1>=-R & x1<=0 & y1>=-R & y1<=0 & x1>=-R-y1))
            return true;
        else return false;
    }

    public static void main(String args[]){
        try{
            int i = 0;
            ServerSocket serverSocket = serverSocket = new ServerSocket(9010, 0, InetAddress.getByName("localhost"));
            System.out.println("Server is started");
            while (true){
                i++;
                new SampleServer(i, serverSocket.accept()).run();
            }
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 9010");
        }
    }
}







//public class Server extends Thread {
//
//    private final static int PACKETSIZE = 100 ;
//
//    @Override
//    public void run () {
//        try {
//            DatagramSocket socket = new DatagramSocket(12345,
//                    InetAddress.getByName("localhost"));
//            System.out.println("\tThe server is ready...\n");
//            do {
//                final ByteArrayInputStream byteArrayInputStream;
//                final DataInputStream dataInputStream;
//                final ByteArrayOutputStream byteArrayOutputStream;
//                final DataOutputStream dataOutputStream;
//                final double i;
//                final double R;
//                final double x;
//                final double y;
//                boolean answer;
//                final byte[] bytesArray;
//
//                DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
//                socket.receive(packet);
//                byteArrayInputStream = new ByteArrayInputStream(packet.getData());
//                dataInputStream = new DataInputStream(byteArrayInputStream);
//                i = dataInputStream.readDouble();
//                R = dataInputStream.readDouble();
//                x = dataInputStream.readDouble();
//                y = dataInputStream.readDouble();
//
//                GraphCalculation calculator = new GraphCalculation(R, new LinkedList<Figure>
//                        (asList(new FTriangle(R), new FQuaterCircle(R), new FSquare(R))));
//                answer = calculator.isInArea(x, y);
//                System.out.println( "point №" + i + " (x = " + x + "; y = " + y + ") radius = " + R +
//                        ((answer == true) ? "; the point belong to the graph; " : "; the point doesn't belong to the graph;") + " " +
//                        packet.getAddress() + " " + packet.getPort() );
//
//                byteArrayOutputStream = new ByteArrayOutputStream();
//                dataOutputStream = new DataOutputStream(byteArrayOutputStream);
//                dataOutputStream.writeInt((int)Math.round(i));
//                dataOutputStream.writeBoolean(answer);
//                dataOutputStream.close();
//                bytesArray = byteArrayOutputStream.toByteArray();
//                packet.setData(bytesArray);
//                packet.setLength(bytesArray.length);
//                packet.setPort(12345);
//                socket.send(packet);
//            } while (true);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//}
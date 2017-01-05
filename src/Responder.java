import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.*;

import static java.util.Arrays.asList;

public class Responder extends Thread {
    private DatagramSocket socket;
    private DatagramPacket packet;

    public Responder (DatagramPacket packet, DatagramSocket socket, int R) {
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        String data = new String(packet.getData()).trim();
        Scanner scanner = new Scanner(data);
        boolean isInside = isInAllArea (Double.parseDouble(scanner.next()),Double.parseDouble(scanner.next()),
                Double.parseDouble(scanner.next()));
        String sendData = Boolean.toString(isInside);
        DatagramPacket response = new DatagramPacket(sendData.getBytes(), sendData.getBytes().length,packet.getAddress(),
                packet.getPort());
        try   {
            socket.send(response);
        }  catch (Exception e) {
            System.out.print("Server crashed in send data" + e.toString());
            if(socket!=null)
                socket.close();
        }
    }

    boolean isInAllArea (double x, double y, double R)   {
        LinkedList<Figure> figurs = new LinkedList<Figure>
                (asList(new FTriangle(R), new FQuaterCircle(R), new FSquare(R)));
        for (Figure figur : figurs)  {
            if (figur.isInArea(x, y))
                return true;
        }
        return false;
    }

    boolean isInAllArea (Punctum punctum, float R) {
        return isInAllArea(punctum.getX(), punctum.getY(), R);
    }
}

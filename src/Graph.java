import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;

import static java.util.Arrays.*;
import java.lang.*;

public class Graph extends JPanel implements Runnable{
    public GraphCalculation graphCalculator;
    public int graphRadius;                    //graphic radius, default graphRadius = 50
    private int step;
    private int height;
    private int width;
    private Graphics2D graphic;
    public Color graphColor = new Color(0,0,0);
    public ArrayList<Punctum> punctumList = new ArrayList<>();

    private boolean isServerAvaliablle;
    private final static int packetSize = 100;
    DatagramSocket socket;

    public Graph(double R, int w, int h, int graphRadius)  {
        try {
            this.graphCalculator = new GraphCalculation(R,new LinkedList<Figure>
                    (asList(new FTriangle(R), new FQuaterCircle(R), new FSquare(R))));
            this.graphRadius = graphRadius;
            this.width = w;
            this.height = h;
            this.step = (int)(graphRadius/R);
            this.socket =  new DatagramSocket();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void changeGraphRadius(double R)  {
        this.graphCalculator.setR(R);
        this.graphCalculator.figures = new LinkedList<Figure>
                (asList(new FTriangle(R), new FQuaterCircle(R), new FSquare(R)));
        this.step = (int)(graphRadius/R);
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
                    byte[] receiveData = new byte[packetSize];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, packetSize );
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

    public Punctum pixelsToCoord (Punctum punctum) {
        step = (int)(graphRadius/graphCalculator.getR());
        return new Punctum((punctum.getX() - width/2)/step, (-punctum.getY()+height/2)/step);
    }

    public Punctum coordToPixels (Punctum punctum) {
        step = (int)(graphRadius/graphCalculator.getR());
        return new Punctum(punctum.getX()*step + width/2, -punctum.getY()*step + height/2);
    }

    private void paintPunctums () {
        if (!punctumList.isEmpty()) {
            for (Punctum p : punctumList) {
                Color punctumColor;
                boolean isInArea = doClient(p.getX(), p.getY(), graphCalculator.getR());
                if (!isServerAvaliablle)
                    punctumColor = new Color(100,100,100);
                else if (isInArea)
                    punctumColor = new Color(0, 255,0);
                else
                    punctumColor = new Color(255, 0, 0);
                graphic.setColor(punctumColor);
                p = coordToPixels(p);
                graphic.fillOval((int) p.getX() - 2, (int) p.getY() - 2, 4, 4);
            }
        }
    }

    @Override
    protected void paintComponent (Graphics g)  {
        setSize(width, height);
        this.graphic = (Graphics2D)g;

        super.paintComponent(graphic);
        this.setBackground(new Color(255, 254, 182));

        this.paintGraphBody();

        this.paintPunctums();
    }

    public void runAnimation(Graph graph) {
        Thread newThread = new Thread(graph);
        newThread.start();
        graphColor = new Color(0,0,0);
        repaint();
    }

    private void paintGraphBody () {
        //draw graph body
        graphic.setColor(graphColor);
        graphic.fillRect(width/2 - graphRadius, height/2 - graphRadius, graphRadius, graphRadius);  //square
        graphic.fillArc(width/2 - graphRadius, height/2 - graphRadius, 2*graphRadius, 2*graphRadius, 0, 90);                        //quater circle
        graphic.fillPolygon                                                                         //triangle
                (new int[] {width/2, width/2 + graphRadius, width/2},
                        new int[] { height/2, height/2, height/2 + graphRadius},
                        3);

        //draw coordinate axis
        graphic.setColor(new Color(150,150,150));
        graphic.drawLine(width/2, 0, width/2, height);
        graphic.drawLine(0, height/2, width, height/2);
        graphic.drawString("0", width/2 + 2, height/2 + 14 );

        step = (int)(graphRadius/graphCalculator.getR());

        //draw scale interval
        for (int i = (int)graphCalculator.getR(); i>0 ; i--) {
            graphic.drawLine(width/2 - i*step, height/2-3, width/2-i*step, height/2+3);
            graphic.drawString("-"+i, width/2-i*step, height/2+17);

            graphic.drawLine(width/2-3, height/2-i*step, width/2+3, height/2-i*step);
            graphic.drawString(""+i, width/2+17, height/2-i*step+5);

            graphic.drawLine(width-(width/2-i*step), height/2-3, width-(width/2-i*step), height/2+3);
            graphic.drawString(""+i, width-(width/2-i*step+5), height/2+17);

            graphic.drawLine(width/2-3, height-(height/2-step*i), width/2+3, height-(height/2-i*step));
            graphic.drawString("-"+i, width/2+17, height-(height/2-i*step-5));
        }
    }

    @Override
    public void run() {             //main animation
        try  {
            for (int i = 0; i < 255; i+=45) {
                graphColor = new Color(0,i,0);
                repaint();
                Thread.sleep(83);
            }

            for (int i = 255; i >= 0 ; i-=45) {
                graphColor = new Color(0,i,0);
                repaint();
                Thread.sleep(83);
            }
        }  catch (Exception e)  {
            System.out.println(e.getMessage());
        }
    }
}
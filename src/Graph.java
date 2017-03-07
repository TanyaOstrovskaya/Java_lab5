import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;

import static java.util.Arrays.*;
import java.lang.*;

public class Graph extends JPanel implements Runnable{

    public int graphRadius;                    //graphic radius, default graphRadius = 50
    private double realRadius;
    private int step;
    private int height;
    private int width;
    private Graphics2D graphic;
    public Color graphColor = new Color(0,0,0);
    public ArrayList<Punctum> punctumList = new ArrayList<>();

    Client client;

    public Graph(double R, int w, int h, int graphRadius)  {
        try {
            this.realRadius = R;
            this.graphRadius = graphRadius;
            this.width = w;
            this.height = h;
            this.step = (int)(graphRadius/R);
            this.client = new Client(new DatagramSocket());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void changeGraphRadius(double R)  {
        this.step = (int)(graphRadius/R);
    }

    public double getRealRadius() {
        return realRadius;
    }

    public Punctum pixelsToCoord (Punctum punctum) {
        step = (int)(graphRadius/realRadius);
        return new Punctum((punctum.getX() - width/2)/step, (-punctum.getY()+height/2)/step);
    }

    public Punctum coordToPixels (Punctum punctum) {
        step = (int)(graphRadius/realRadius);
        return new Punctum(punctum.getX()*step + width/2, -punctum.getY()*step + height/2);
    }

    private void paintPunctums () {
        if (!punctumList.isEmpty()) {
            for (Punctum p : punctumList) {
                Color punctumColor;
                boolean isInArea = client.doClient(p.getX(), p.getY(), realRadius);
                if (!client.isServerAvaliablle())
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

        step = (int)(graphRadius/realRadius);

        //draw scale interval
        for (int i = (int)realRadius; i>0 ; i--) {
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
                Thread.sleep(90);
            }

            for (int i = 255; i >= -45 ; i-=45) {
                graphColor = new Color(0,i,0);
                repaint();
                Thread.sleep(90);
            }


        } catch (Exception e)  {
            System.out.println(e.getMessage());
        } finally {
            graphColor = new Color(0,0,0);
            graphic.setColor(graphColor);
            repaint();
        }
    }
}
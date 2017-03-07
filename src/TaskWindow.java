import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.*;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.*;

public class TaskWindow implements ItemListener {

    double currX, currY;
    Graph graph;
    JList xList;
    JPanel yPanel;
    JCheckBox[] yJCheckBoxes;
    JLabel currentCoord;
    JLabel xyLabel;
    JLabel coordLabel;
    JLabel radiusLabel;
    JSpinner jSpinner;
    double currRadius = 4;


    String currCountry;
    String currLanguage;
    Locale currLocale;
    ResourceBundle rb;

    private JCheckBox[] fillArrayWithJCheckBoxes() {
        JCheckBox yChBox;
        JCheckBox[] resultArray = new JCheckBox[11];
        for (int i = 0; i < 11; i++) {
            yChBox = new JCheckBox(Integer.toString(i - 5));
            yChBox.addItemListener(this);
            resultArray[i] = yChBox;
        }
        return resultArray;
    }

    private void repaintGraph() {
        graph.repaint();
    }

    public Graph createGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Lab 4; Ostrovskaya Tatyana");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 750);
        frame.setVisible(true);
        frame.setLayout(new GridLayout(1, 2));

        JPanel mainPanel = new JPanel(new GridLayout(3, 1));
        JPanel toolPanel1 = new JPanel();
        JPanel toolPanel2 = new JPanel();
        JPanel toolPanel3 = new JPanel();
        mainPanel.add(toolPanel1);
        mainPanel.add(toolPanel2);
        mainPanel.add(toolPanel3);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.CENTER);

        // Main graph
        graph = new Graph(currRadius, frame.getWidth() / 2, frame.getHeight(), 240);
        graph.addMouseListener(new MouseAL());
        frame.add(graph);
        frame.add(mainPanel);

        // X and Y
        xyLabel = new JLabel("Установить точку: ");
        toolPanel1.add(xyLabel);
        String[] xArray = {"-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5"};
        xList = new JList(xArray);
        EventList(xList);
        xList.setLayoutOrientation(JList.VERTICAL);
        xList.setMinimumSize(new Dimension(40, 60));

        toolPanel1.add(xList);

        yPanel = new JPanel(new GridLayout(1, 11));
        yJCheckBoxes = fillArrayWithJCheckBoxes();
        for (int i = 0; i < yJCheckBoxes.length; i++) {
            yPanel.add(yJCheckBoxes[i]);
        }
        toolPanel1.add(yPanel);

        // Coordinates
        coordLabel = new JLabel("Установленная точка: ");
        toolPanel2.add(coordLabel);
        currentCoord = new JLabel("");
        currentCoord.setText("x = " + currX + ", y = " + currY);
        toolPanel2.add(currentCoord);

        // Check Button
        JButton check = new JButton("Check");
        check.addActionListener(new ButtonCheckListener());
        toolPanel2.add(check);

        // Radius
        radiusLabel = new JLabel("Радиус: ");
        toolPanel2.add(radiusLabel);
        jSpinner = new JSpinner(new SpinnerNumberModel(4,1,50,1));
        jSpinner.addChangeListener(new JSpinnerListener());
        toolPanel2.add(jSpinner);

        // Language Buttons
        JButton ruBttn = new JButton("RU");
        ruBttn.addActionListener(new RuButtonListener());
        toolPanel3.add(ruBttn);
        JButton svBttn = new JButton("SV");
        svBttn.addActionListener(new SvButtonListener());
        toolPanel3.add(svBttn);

        frame.pack();
        frame.setSize(1500, 750);

        return graph;
    }

    class JSpinnerListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            currRadius  = Float.parseFloat (((JSpinner) e.getSource()).getValue().toString());
            graph.changeGraphRadius(currRadius);
            repaintGraph();
        }
    }

    class MouseAL implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            Graph g = (Graph)e.getSource();
            Punctum punctumToPaint = new Punctum(e.getX(), e.getY());
            punctumToPaint = g.pixelsToCoord(punctumToPaint);
            graph.punctumList.add(punctumToPaint);
            if (graph.client.doClient(punctumToPaint.getX(), punctumToPaint.getY(), graph.getRealRadius())) {
                graph.runAnimation(graph);
            }
            graph.repaint();

            DecimalFormat decimalFormat = new DecimalFormat("##0.0");
            currentCoord.setText("x = " + decimalFormat.format(punctumToPaint.getX()) +
                    ", y = " + decimalFormat.format(punctumToPaint.getY()));

            graph.graphColor = new Color(0, 0, 0);
            graph.repaint();
        }
        @Override
        public void mousePressed(MouseEvent e) {        }
        @Override
        public void mouseReleased(MouseEvent e) {        }
        @Override
        public void mouseEntered(MouseEvent e) {        }
        @Override
        public void mouseExited(MouseEvent e) {        }
    }

    class ButtonCheckListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Punctum newPunctum = new Punctum(currX, currY);
            graph.punctumList.add(newPunctum);
            repaintGraph();
        }
    }

    private void setLocale () {
        currLocale = new Locale(currLanguage, currCountry);
        rb = ResourceBundle.getBundle("text", currLocale);
        try {
            byte[] str = rb.getString("str1").getBytes("ISO-8859-1");
            xyLabel.setText(new String(str));

            str = rb.getString("str2").getBytes("ISO-8859-1");
            coordLabel.setText(new String(str));

            str = rb.getString("str3").getBytes("ISO-8859-1");
            radiusLabel.setText(new String(str));

        } catch (Exception e1) {
            System.out.println(e1.getMessage());
        }
    }

    class RuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currCountry = "RU";
            currLanguage = "ru";
            setLocale();
        }
    }

    class SvButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currCountry = "se";
            currLanguage = "SV";
            setLocale();
        }
    }


    public void EventList(JList list)  {                                //обработчик событий для JList
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 1 || evt.getClickCount() == 2) {
                    String value = list.getSelectedValue().toString();
                    currX = Double.parseDouble(value);
                    currentCoord.setText("x = " + currX + ", y = " + currY);
                }
            }
        });
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBox checkBox = (JCheckBox) e.getItem();
        for (int i = 0; i < yJCheckBoxes.length; i++) {
            if (yJCheckBoxes[i] != checkBox)
                yJCheckBoxes[i].setEnabled(false);
        }
        if (!checkBox.isSelected())        {
            for (int i = 0; i < yJCheckBoxes.length; i++) {
                yJCheckBoxes[i].setEnabled(true);
            }
        }
        currY = Double.parseDouble(checkBox.getText());
        currentCoord.setText("x = " + currX + ", y = " + currY);
    }
}

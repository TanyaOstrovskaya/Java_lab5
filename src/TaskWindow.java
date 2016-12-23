import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;


public class TaskWindow implements ItemListener {
    float currX, currY;
    static Graph graph;
    JList xList;
    JPanel yPanel;
    JCheckBox[] yJCheckBoxes;
    JLabel currentCoord;
    JSpinner jSpinner;
    float currRadius = 4;

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

    private static void repaintGraph() {
        graph.repaint();
    }

    public Graph createGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Lab 4; Ostrovskaya Tatyana");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 750);
        frame.setVisible(true);
        frame.setLayout(new GridLayout(1, 2));

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        JPanel toolPanel1 = new JPanel();
        JPanel toolPanel2 = new JPanel();
        mainPanel.add(toolPanel1);
        mainPanel.add(toolPanel2);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.CENTER);

        // Main graph
        graph = new Graph(frame.getWidth() / 2, frame.getHeight());
        graph.addMouseListener(new MouseAL());
        frame.add(graph);
        frame.add(mainPanel);

        // X and Y
        JLabel xyLabel = new JLabel("Установить точку: ");
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
        JLabel coordLabel = new JLabel("Установленная точка: ");
        toolPanel2.add(coordLabel);
        currentCoord = new JLabel("");
        currentCoord.setText("x = " + currX + ", y = " + currY);
        toolPanel2.add(currentCoord);

        // Check Button
        JButton check = new JButton("Check");
        check.addActionListener(new ButtonCheckListener());
        toolPanel2.add(check);

        // Radius
        JLabel radiusLabel = new JLabel("Радиус: ");
        toolPanel2.add(radiusLabel);
        jSpinner = new JSpinner(new SpinnerNumberModel(4,1,50,1));
        jSpinner.addChangeListener(new JSpinnerListener());
        toolPanel2.add(jSpinner);

        frame.pack();
        frame.setSize(1500, 750);

        return graph;
    }

    class JSpinnerListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            currRadius  = Float.parseFloat (((JSpinner) e.getSource()).getValue().toString());
            graph.changeRadius(currRadius);
            repaintGraph();
        }
    }

    class MouseAL implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            Graph g = (Graph) e.getSource();
            Punctum punctumToPaint = new Punctum(e.getX(), e.getY());
            punctumToPaint = g.pixelsToCoord(punctumToPaint);
            g.punctumList.add(punctumToPaint);

            g.repaint();

            DecimalFormat decimalFormat = new DecimalFormat("##0.0");
            currentCoord.setText("x = " + decimalFormat.format(punctumToPaint.getX()) +
                    ", y = " + decimalFormat.format(punctumToPaint.getY()));
            if (g.isInAllArea(punctumToPaint.getX(), punctumToPaint.getY())) {
                g.runAnimation(g);
            }
            g.graphColor = new Color(0, 0, 0);
            g.repaint();
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


    public void EventList(JList list)   //обработчик событий для JList
    {
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 1 || evt.getClickCount() == 2)
                {
                    String value = list.getSelectedValue().toString();
                    currX = Float.parseFloat(value);
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
        if (!checkBox.isSelected())
        {
            for (int i = 0; i < yJCheckBoxes.length; i++) {
                yJCheckBoxes[i].setEnabled(true);
            }
        }
        currY = Float.parseFloat(checkBox.getText());
        currentCoord.setText("x = " + currX + ", y = " + currY);
    }
}

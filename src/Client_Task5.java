public class Client_Task5 {
    public static void main (String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TaskWindow window = new TaskWindow();
                window.createGUI();
            }
        });
    }
}
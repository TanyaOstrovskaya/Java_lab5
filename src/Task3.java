import java.io.IOException;

public class Task3
{
    public static void main (String[] args) {

        TaskWindow window = new TaskWindow();
        window.createGUI();

        Client client = new Client("127.0.0.1", 12345, window);
        try {
            client.runClient();
            client.start();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
   }
}


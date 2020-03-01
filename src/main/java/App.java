import UI.MainMenu;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        JFrame mainMenu = new JFrame("Auth window");
        mainMenu.setContentPane(new MainMenu().getMainMenuPanel());
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.pack();
        mainMenu.setSize(500, 200);
        mainMenu.setMinimumSize(new Dimension(500, 200));
        mainMenu.setVisible(true);
    }
}

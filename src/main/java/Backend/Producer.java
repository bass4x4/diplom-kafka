package Backend;

import UI.ProducerMenu;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class Producer {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        JFrame mainMenu = new JFrame("Send messages to Kafka");
        mainMenu.setContentPane(new ProducerMenu().getMainMenuPanel());
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.pack();
        mainMenu.setSize(500, 500);
//        mainMenu.setMinimumSize(new Dimension(500, 500));
        mainMenu.setVisible(true);
    }
}

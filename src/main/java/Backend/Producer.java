package Backend;

import UI.ProducerMenu;
import com.google.common.collect.Lists;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class Producer {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        JFrame mainMenu = new JFrame();
        mainMenu.setTitle("Title");
        mainMenu.setContentPane(new GraphPanel(Lists.newArrayList(1d, 2d, 3d, 4d, 1d), Lists.newArrayList(4d, 2d, 3d, 4d, 3d)));
//        mainMenu.setContentPane(new ProducerMenu().getMainMenuPanel());
//        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.pack();
        mainMenu.setSize(500, 500);
        mainMenu.setVisible(true);
//        GraphPanel graphPanel = new GraphPanel(Lists.newArrayList(1d, 2d, 3d));
//        mainMenu.setLayout(new BorderLayout());
//        graphPanel.add(graphPanel, BorderLayout.CENTER);
//        mainMenu.setVisible(true);
    }
}

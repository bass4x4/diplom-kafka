package Backend;

import UI.ErrorMonitorMenu;
import com.google.common.collect.Lists;

import javax.swing.*;

public class ErrorMonitor {
    public static void main(String[] args) {
        JFrame mainMenu = new JFrame();
        mainMenu.setTitle("Error monitor");
        mainMenu.setContentPane(new ErrorMonitorMenu().getErrorMonitorPanel());
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.pack();
        mainMenu.setSize(500, 300);
        mainMenu.setVisible(true);
    }
}

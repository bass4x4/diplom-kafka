package UI;

import Backend.GraphPanel;
import org.apache.http.annotation.GuardedBy;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

import static Backend.POJO.Error.ErrorType;

public class ErrorMonitorMenu extends JFrame {
    private JLabel warns;
    private JLabel errors;
    private JLabel debugs;
    private JButton warnsGraph;
    private JButton errorsGraph;
    private JButton debugsGraph;
    private JSpinner maxErrorsSpinner;
    private JPanel errorMonitorPanel;
    public static final KafkaConsumer<String, String> CONSUMER = createConsumer("group");

    @GuardedBy("this")
    private final Map<LocalTime, Double> errorsList = new HashMap<>();
    @GuardedBy("this")
    private final Map<LocalTime, Double> warnsList = new HashMap<>();
    @GuardedBy("this")
    private final Map<LocalTime, Double> debugsList = new HashMap<>();

    public ErrorMonitorMenu() {
        maxErrorsSpinner.setModel(new SpinnerNumberModel(0, 0, 200, 10));
        errorsGraph.addActionListener(actionEvent -> {
            JDialog mainMenu = new JDialog(this, true);
            mainMenu.setTitle("Errors graph");
            List<Double> minutes = new ArrayList<>();
            List<Double> amount = new ArrayList<>();
            synchronized (this) {
                errorsList.keySet().stream().sorted().forEach(time -> {
                    minutes.add(((double) time.getMinute()));
                    amount.add(errorsList.get(time));
                });
            }
            mainMenu.setContentPane(new GraphPanel(minutes, amount));
            mainMenu.setSize(500, 500);
            mainMenu.setVisible(true);
        });
        warnsGraph.addActionListener(actionEvent -> {
            JDialog mainMenu = new JDialog(this, true);
            mainMenu.setTitle("Warns graph");
            List<Double> minutes = new ArrayList<>();
            List<Double> amount = new ArrayList<>();
            synchronized (this) {
                warnsList.keySet().stream().sorted().forEach(time -> {
                    minutes.add(((double) time.getMinute()));
                    amount.add(warnsList.get(time));
                });
            }
            mainMenu.setContentPane(new GraphPanel(minutes, amount));
            mainMenu.setSize(500, 500);
            mainMenu.setVisible(true);
        });
        debugsGraph.addActionListener(actionEvent -> {
            JDialog mainMenu = new JDialog(this, true);
            mainMenu.setTitle("Debugs graph");
            List<Double> minutes = new ArrayList<>();
            List<Double> amount = new ArrayList<>();
            synchronized (this) {
                debugsList.keySet().stream().sorted().forEach(time -> {
                    minutes.add(((double) time.getMinute()));
                    amount.add(debugsList.get(time));
                });
            }
            mainMenu.setContentPane(new GraphPanel(minutes, amount));
            mainMenu.setSize(500, 500);
            mainMenu.setVisible(true);
        });
        CONSUMER.subscribe(Collections.singletonList("test"));
        new Thread(ErrorMonitorMenu.this::parseErrors).start();
    }

    private void parseErrors() {
        try {
            while (true) {
                ConsumerRecords<String, String> records = CONSUMER.poll(Duration.ofMillis(100));
                if (!records.isEmpty()) {
                    long start = System.currentTimeMillis();
                    LocalTime time = LocalTime.now();
                    LocalTime now = LocalTime.of(time.getHour(), time.getMinute());
                    synchronized (this) {
                        for (ConsumerRecord<String, String> record : records) {
                            switch (ErrorType.valueOf(record.value())) {
                                case ERROR:
                                    errorsList.putIfAbsent(now, 0d);
                                    errorsList.computeIfPresent(now, (localTime, integer) -> integer + 1);
                                    break;
                                case DEBUG:
                                    debugsList.putIfAbsent(now, 0d);
                                    debugsList.computeIfPresent(now, (localTime, integer) -> integer + 1);
                                    break;
                                case WARN:
                                    warnsList.putIfAbsent(now, 0d);
                                    warnsList.computeIfPresent(now, (localTime, integer) -> integer + 1);
                                    break;
                            }
                        }
                    }
                    updateCountNumber();
                    System.out.println(String.format("Took %f to process %d records.", (System.currentTimeMillis() - start) / 1000F, records.count()));
                }
            }
        } finally {
            CONSUMER.close();
        }
    }

    private synchronized void updateCountNumber() {
        OptionalDouble errorsAmount = errorsList.values().stream().mapToDouble(Double::intValue).average();
        errorsAmount.ifPresent(v -> this.errors.setText(String.valueOf(v)));
        OptionalDouble warnAmount = warnsList.values().stream().mapToDouble(Double::intValue).average();
        warnAmount.ifPresent(v -> this.warns.setText(String.valueOf(v)));
        OptionalDouble debugAmount = debugsList.values().stream().mapToDouble(Double::intValue).average();
        debugAmount.ifPresent(v -> this.debugs.setText(String.valueOf(v)));
        if (errorsAmount.isPresent() && errorsAmount.getAsDouble() > ((int) maxErrorsSpinner.getValue())) {
            errors.setForeground(Color.RED);
        } else {
            errors.setForeground(Color.BLACK);
        }
    }

    public static KafkaConsumer<String, String> createConsumer(String consumerGroup) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 1048576);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return new KafkaConsumer<>(props);
    }

    public JPanel getErrorMonitorPanel() {
        return errorMonitorPanel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        errorMonitorPanel = new JPanel();
        errorMonitorPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 5, new Insets(0, 0, 0, 0), -1, -1));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        errorMonitorPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        errorMonitorPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("WARN avg");
        errorMonitorPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        errorMonitorPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        errorMonitorPanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        warns = new JLabel();
        warns.setText("0");
        errorMonitorPanel.add(warns, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("ERROR avg");
        errorMonitorPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errors = new JLabel();
        errors.setText("0");
        errorMonitorPanel.add(errors, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("DEBUG avg");
        errorMonitorPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        debugs = new JLabel();
        debugs.setText("0");
        errorMonitorPanel.add(debugs, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        warnsGraph = new JButton();
        warnsGraph.setText("Graph");
        errorMonitorPanel.add(warnsGraph, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorsGraph = new JButton();
        errorsGraph.setText("Graph");
        errorMonitorPanel.add(errorsGraph, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        debugsGraph = new JButton();
        debugsGraph.setText("Graph");
        errorMonitorPanel.add(debugsGraph, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Max avg errors/minute");
        errorMonitorPanel.add(label4, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        errorMonitorPanel.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        maxErrorsSpinner = new JSpinner();
        errorMonitorPanel.add(maxErrorsSpinner, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return errorMonitorPanel;
    }

}

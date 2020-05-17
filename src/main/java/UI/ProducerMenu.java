package UI;

import Backend.Result;
import Backend.ResultSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProducerMenu {
    private final HashMap<String, String> acks;
    private final Random r = new Random();

    private JPanel mainMenuPanel;
    private JSpinner numberOfMessages;
    private JButton sendButton;
    private JSpinner lingerMsSpinner;
    private JSpinner batchSizeSpinner;
    private JComboBox<String> compressionTypeComboBox;
    private JComboBox<String> ackComboBox;
    private JCheckBox enableIdempotenceCheckBox;
    private JLabel lingerMsLabel;
    private JLabel batchSizeLabel;
    private JLabel compressionTypeLabel;
    private JLabel ackLabel;

    private final Producer<String, String> resultsProducer;

    public ProducerMenu() {
        numberOfMessages.setModel(new SpinnerNumberModel(0, 0, 1000000, 1));
        lingerMsSpinner.setModel(new SpinnerNumberModel(0, 0, 500, 5));
        batchSizeSpinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
        acks = new HashMap<>();
        acks.put("All", "-1");
        acks.put("None", "0");
        acks.put("Leader", "1");
        acks.keySet().forEach(ackComboBox::addItem);

        String[] compressionTypes = {"none", "lz4", "snappy", "zstd", "gzip"};
        Arrays.stream(compressionTypes).forEach(compressionTypeComboBox::addItem);
        resultsProducer = createResultsProducer();
        sendButton.addActionListener(actionEvent -> sendMessages(((int) numberOfMessages.getValue())));
    }

    private void sendMessages(int numberOfMessages) {
        try {
            Producer<String, String> producer = createProducer();
            List<String> strings = generateTestData(numberOfMessages);
            long start = System.currentTimeMillis();
            for (int i = 0; i < strings.size(); i++) {
                try {
                    ProducerRecord<String, String> record = new ProducerRecord("test", String.valueOf(r.nextInt(1000)), strings.get(i));
                    producer.send(record).get();
                } catch (Exception e) {
                    System.out.println(e);
                    JOptionPane.showMessageDialog(null, String.format("Couldn't send the message #%d. Didn't try to send %d messages.", i, numberOfMessages - i));
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, String.format("Took %f second to send %d messages", (System.currentTimeMillis() - start) / 1000F, numberOfMessages));
        } catch (KafkaException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Couldn't initialize KafkaProducer.");
        }
    }

    public Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "1000");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "AnkushevAD");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        int lingerMs = (int) lingerMsSpinner.getValue();
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        int batchSize = (int) batchSizeSpinner.getValue();
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384 * batchSize);
        String compressionType = String.valueOf(compressionTypeComboBox.getSelectedItem());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
        String ack = String.valueOf(ackComboBox.getSelectedItem());
        props.put(ProducerConfig.ACKS_CONFIG, acks.get(ack));
        boolean idempotent = enableIdempotenceCheckBox.isSelected();
        if (idempotent) {
            if (ack.equals("All")) {
                props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotent);
            } else {
                JOptionPane.showMessageDialog(null, "Please make sure you always use idempotent producer with ack=ALL.\nParameter ack was set to ALL!");
            }
        }

        Result result = new Result(lingerMs, batchSize, compressionType, ack, idempotent);
        ProducerRecord<String, String> record = new ProducerRecord("test_results", String.valueOf(r.nextInt(1000)), ResultSerializer.serialize(result));
        resultsProducer.send(record);
        return new KafkaProducer<>(props);
    }

    public Producer<String, String> createResultsProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "1000");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "AnkushevAD");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    private List<String> generateTestData(int n) {
        return IntStream.range(0, n).mapToObj(i -> generateTestString()).collect(Collectors.toList());
    }

    private String generateTestString() {
        char[] array = new char[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = (char) (r.nextInt(127) + 1);
        }
        return new String(array);
    }

    public JPanel getMainMenuPanel() {
        return mainMenuPanel;
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
        mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(10, 5, new Insets(0, 0, 0, 0), -1, -1));
        numberOfMessages = new JSpinner();
        mainMenuPanel.add(numberOfMessages, new com.intellij.uiDesigner.core.GridConstraints(7, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sendButton = new JButton();
        sendButton.setText("Send");
        mainMenuPanel.add(sendButton, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        mainMenuPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(7, 4, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        mainMenuPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        mainMenuPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        mainMenuPanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Number of records");
        mainMenuPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lingerMsLabel = new JLabel();
        lingerMsLabel.setText("linger.ms");
        mainMenuPanel.add(lingerMsLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lingerMsSpinner = new JSpinner();
        mainMenuPanel.add(lingerMsSpinner, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        batchSizeLabel = new JLabel();
        batchSizeLabel.setText("batch.size");
        mainMenuPanel.add(batchSizeLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        batchSizeSpinner = new JSpinner();
        mainMenuPanel.add(batchSizeSpinner, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        compressionTypeLabel = new JLabel();
        compressionTypeLabel.setText("compression.type");
        mainMenuPanel.add(compressionTypeLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        compressionTypeComboBox = new JComboBox();
        mainMenuPanel.add(compressionTypeComboBox, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ackLabel = new JLabel();
        ackLabel.setText("ack");
        mainMenuPanel.add(ackLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ackComboBox = new JComboBox();
        mainMenuPanel.add(ackComboBox, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enableIdempotenceCheckBox = new JCheckBox();
        enableIdempotenceCheckBox.setText("enable.idempotence");
        mainMenuPanel.add(enableIdempotenceCheckBox, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("DEFAULT * ");
        mainMenuPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        mainMenuPanel.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainMenuPanel;
    }

}

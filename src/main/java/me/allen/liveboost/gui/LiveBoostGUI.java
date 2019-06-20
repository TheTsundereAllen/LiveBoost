package me.allen.liveboost.gui;

import me.allen.liveboost.LiveBoost;
import me.allen.liveboost.gui.filter.NumberDocumentFilter;
import me.allen.liveboost.runnable.BoostHeartbeatRunnable;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LiveBoostGUI extends JFrame {

    private LiveBoost liveBoost;

    public LiveBoostGUI(LiveBoost liveBoost) {
        this.liveBoost = liveBoost;
        JPanel mainPanel = new JPanel();

        JPanel roomPanel = new JPanel();
        JLabel roomLabel = new JLabel("房间号");
        JTextField roomField = new JTextField("", 10);

        roomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        PlainDocument roomDocument = (PlainDocument) roomField.getDocument();
        roomDocument.setDocumentFilter(new NumberDocumentFilter());

        roomPanel.add(roomLabel);
        roomPanel.add(roomField);

        JPanel amountPanel = new JPanel();
        JLabel amountLabel = new JLabel("数量");
        JTextField amountField = new JTextField("100", 5);
        amountPanel.add(amountLabel);
        amountPanel.add(amountField);
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        PlainDocument amountDocument = (PlainDocument) amountField.getDocument();
        amountDocument.setDocumentFilter(new NumberDocumentFilter());

        JButton executeButton = new JButton("开始!");
        executeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                long roomId = Long.parseLong(roomField.getText());
                int amount = Integer.parseInt(amountField.getText());
                liveBoost.getSharedExecutor().execute(new BoostHeartbeatRunnable(liveBoost, roomId, amount));
            }
        });

        executeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(roomPanel);
        mainPanel.add(amountPanel);
        mainPanel.add(executeButton);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        add(mainPanel);
        setTitle("Live Boost");
        setSize(300, 400);
        setLocation(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        pack();
    }
}

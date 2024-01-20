package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogList extends JDialog {
    private String listChoice;

    public DialogList(JFrame parent, String title, String text, String[] list, int fontSize) {
        super(parent, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(450, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(Color.BLACK);
        contentPanel.setFocusTraversalKeysEnabled(true);
        add(contentPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        String[] lines = text.split("\n");
        for (String line : lines) {
            JLabel messageLabel = new JLabel(line, SwingConstants.CENTER);
            messageLabel.setForeground(Color.WHITE);
            messageLabel.setFont(FontLoader.loadPixelFont(fontSize));
            contentPanel.add(messageLabel, gbc);
        }

        JComboBox<String> comboBox = new JComboBox<>(list);
        comboBox.setBackground(Color.BLACK);
        comboBox.setForeground(Color.WHITE);
        comboBox.setFont(FontLoader.loadPixelFont(fontSize));
        contentPanel.add(comboBox);

        JButton okButton = new MenuButton("OK");
        JButton noButton = new MenuButton("CANCEL");
        getRootPane().setDefaultButton(noButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(okButton);
        buttonPanel.add(noButton);
        add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listChoice = (String) comboBox.getSelectedItem();
                dispose();
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public String getListChoice() {
        return listChoice;
    }
}
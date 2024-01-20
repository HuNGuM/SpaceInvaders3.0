package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogInfo extends JDialog {

    public DialogInfo(JFrame parent, String title, String text, int fontSize) {
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
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        String[] lines = text.split("\n");
        for (String line : lines) {
            JLabel messageLabel = new JLabel(line, SwingConstants.CENTER);
            messageLabel.setForeground(Color.WHITE);
            messageLabel.setFont(FontLoader.loadPixelFont(fontSize));
            contentPanel.add(messageLabel, gbc);
        }

        JButton okButton = new MenuButton("OK");
        getRootPane().setDefaultButton(okButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DialogYesNo extends JDialog {
    private boolean choice;

    public DialogYesNo(JFrame parent, String title, String text, int fontSize) {
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

        JButton okButton = new MenuButton("YES");
        JButton noButton = new MenuButton("NO");
        getRootPane().setDefaultButton(noButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(okButton);
        buttonPanel.add(noButton);
        add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choice = true;
                dispose();
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choice = false;
                dispose();
            }
        });
    }

    public boolean getChoice() {
        return choice;
    }
}
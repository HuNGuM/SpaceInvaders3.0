package score_panel;

import ScoreHistory.Score;
import ScoreHistory.ScoreHistoryManager;
import main.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import static file_communication.Fonts.loadFont;

public class ScorePanel extends JPanel {

    Font pixelFont;
    JScrollPane recordScroll;

    public ScorePanel() {
        this.pixelFont = loadFont("Fonts/Pixel_font3.ttf", Font.PLAIN, 26);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel(new GridLayout(1, 0));
        topPanel.setBackground(Color.BLACK);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel backButtonWrapper = new JPanel();
        backButtonWrapper.setLayout(new BoxLayout(backButtonWrapper, BoxLayout.X_AXIS));
        backButtonWrapper.setBackground(Color.BLACK);

        JButton backButton = new JButton("back");
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        backButton.setFont(pixelFont);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.setFocusPainted(false);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.showPanel("mainMenu");
            }
        });

        backButtonWrapper.add(backButton);
        topPanel.add(backButtonWrapper);

        JLabel topLabel = new JLabel("Top score", SwingConstants.CENTER);
        topLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topLabel.setFont(pixelFont);
        topLabel.setForeground(Color.WHITE);
        topPanel.add(topLabel);

//        this centers topLabel
        JPanel spacing = new JPanel();
        spacing.setBackground(Color.BLACK);
        topPanel.add(spacing);




        this.recordScroll = new JScrollPane(this.createRecordPanel());
        this.recordScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.recordScroll.getVerticalScrollBar().setUnitIncrement(10);
        this.recordScroll.setBorder(null);


        add(topPanel);
        add(recordScroll);
    }

    public void updateList() {
        this.recordScroll.setViewportView(createRecordPanel());
    }

    private JPanel createRecordPanel() {

        JPanel recordPanel = new JPanel();
        recordPanel.setLayout(new BoxLayout(recordPanel, BoxLayout.Y_AXIS));
        recordPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        recordPanel.setBackground(Color.BLACK);

        ArrayList<Score> scoreHistory = ScoreHistoryManager.readTopScoreHistory();

        scoreHistory.sort(null);
        Collections.reverse(scoreHistory);

        if (scoreHistory.isEmpty()) {
            JLabel emptyArrayLabel = new JLabel("No records", SwingConstants.CENTER);
            emptyArrayLabel.setFont(pixelFont);
            emptyArrayLabel.setForeground(Color.WHITE);
            emptyArrayLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

            recordPanel.add(emptyArrayLabel);
        } else {
            for (Score score : scoreHistory) {
                ScoreComponent scoreComponent = new ScoreComponent(score, pixelFont);

                scoreComponent.setFont(pixelFont);
                recordPanel.add(scoreComponent);
                recordPanel.add(Box.createVerticalStrut(10));
            }
        }

        return recordPanel;
    }
}

package score_panel;

import ScoreHistory.Score;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.RecordComponent;

public class ScoreComponent extends JPanel {
    public ScoreComponent(Score score, Font font) {
        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));

        add(createLabel(score.name(), "Name: ", font));
        add(Box.createHorizontalGlue());
        add(createLabel(score.score(), "Score: ", font));
        add(Box.createHorizontalGlue());
        add(createLabel(score.distance(), "Distance: ", font));
        add(Box.createHorizontalGlue());
        add(createLabel(score.leftMatch(), "Left Match: ", font));
        add(Box.createHorizontalGlue());
        add(createLabel(score.rightMatch(), "Right Match: ", font));
        add(Box.createHorizontalGlue());
        add(createLabel(score.totalMatch(), "Total Match: ", font));
    }

    private <T> JLabel createLabel(T content, String description, Font font) {
        JLabel label = new JLabel(description + content.toString());
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        label.setFont(font);
        label.setForeground(Color.WHITE);

        return label;
    }


}

package game_item.powerUp;

import game_item.Player;
import panel.GamePanel;

import java.awt.*;

public class HealthUpStrategy implements PowerUpStrategy{
    public HealthUpStrategy() {}
    int duration = 0;

    private Color color=Color.RED;
    public void applyEffect() {
        Player.getInstance().incrementLives();
    }

    public void revertEffect() {}

    public int getDuration() {
        return duration;
    }
    public void draw(Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }
}

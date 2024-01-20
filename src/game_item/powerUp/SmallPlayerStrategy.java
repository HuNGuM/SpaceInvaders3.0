package game_item.powerUp;

import game_item.Player;
import main.Game;
import panel.GamePanel;

import java.awt.*;

public class SmallPlayerStrategy implements PowerUpStrategy{
    public SmallPlayerStrategy() {}
    int duration = 4000;
    double multiplier = 0.5;
    private Color color=Color.CYAN;
    public void applyEffect() {
        Player.getInstance().setPlayerSize(multiplier);
        GamePanel.setPlayerSpeed((int) (GamePanel.getPlayerSpeed() / multiplier));
    }

    public void revertEffect() {
        Player.getInstance().resetPlayerSize();
        GamePanel.setPlayerSpeed(GamePanel.DEFAULT_PLAYER_SPEED);
    }

    public int getDuration() {
        return duration;
    }
    public void draw(Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }
}

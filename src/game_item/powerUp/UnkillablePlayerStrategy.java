package game_item.powerUp;

import game_item.Player;
import main.Game;
import panel.GamePanel;

import java.awt.*;

public class UnkillablePlayerStrategy implements PowerUpStrategy {

    public UnkillablePlayerStrategy(){}
    private int duration = 5000;
    private int blinkInterval = 50;
    private Color color=new Color(150,0,250);

    public void applyEffect() {
        Player.getInstance().setInvincibilityTime(duration);
        Player.getInstance().setBlinkInterval(blinkInterval);
        Player.getInstance().setInvincible(true);
        GamePanel.activateInvincibilityStart();
    }

    public void revertEffect() {
        Player.getInstance().resetPlayerInvincibility();
    }
    public int getDuration() {
        return duration;
    }
    public void draw(Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }
}

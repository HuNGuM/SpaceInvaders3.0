package game_item;

import game_item.powerUp.PowerUpStrategy;
import panel.GamePanel;

import java.awt.*;
import java.util.Random;
import javax.swing.Timer;

public class PowerUp extends GameItem {
    public static final int POINT_WIDTH = 15;
    public static final int POINT_HEIGHT = 15;
    private int remainingTime;
    private PowerUpStrategy effect;

    public PowerUp(int x, int y) {
        super(x, y);
        this.effect = GamePanel.POWER_UPS.get(new Random().nextInt(GamePanel.POWER_UPS.size()));
    }

    public void applyEffect() {
        effect.applyEffect();
        if(effect.getDuration() == 0) return;
        Timer powerUpTimer = new Timer(effect.getDuration(), e -> {
                effect.revertEffect();
        });
        powerUpTimer.setRepeats(false);
        powerUpTimer.start();
    }

    public void setEffect(PowerUpStrategy effect) {
        this.effect = effect;
    }

    public PowerUpStrategy getEffect() {
        return effect;
    }

    @Override
    public void draw(Graphics g) {
        effect.draw(g, x, y, POINT_WIDTH, POINT_HEIGHT);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, POINT_WIDTH, POINT_HEIGHT);
    }

}

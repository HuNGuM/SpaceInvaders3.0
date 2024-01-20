package game_item.powerUp;

import java.awt.*;

public interface PowerUpStrategy {
    public void applyEffect();
    public void revertEffect();
    public int getDuration();
    public void draw(Graphics g, int x, int y, int width, int height);

}

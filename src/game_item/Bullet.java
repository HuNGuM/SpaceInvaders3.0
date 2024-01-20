package game_item;

import javax.swing.*;
import java.awt.*;

public class Bullet extends GameItem {

    public static final int BULLET_WIDTH = 24;
    public static final int BULLET_HEIGHT = 12;
    private static final String BULLET_IMAGE_PATH = "Pictures/rocket.png";

    public Bullet(int x, int y) {
        super(x, y);
    }

    public void draw(Graphics g) {
        ImageIcon bulletIcon = new ImageIcon(BULLET_IMAGE_PATH);
        Image bulletImage = bulletIcon.getImage();
        g.drawImage(bulletImage, x, y, BULLET_WIDTH, BULLET_HEIGHT, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, BULLET_WIDTH, BULLET_HEIGHT);
    }

}
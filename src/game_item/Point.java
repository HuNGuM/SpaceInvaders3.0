package game_item;

import java.awt.*;

public class Point extends GameItem {
    public static final int POINT_WIDTH = 15;
    public static final int POINT_HEIGHT = 15;

    public Point(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillOval(x, y, POINT_WIDTH, POINT_HEIGHT);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, POINT_WIDTH, POINT_HEIGHT);
    }
}

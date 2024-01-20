package game_item;

import java.awt.*;

public abstract class GameItem {
    protected int x;
    protected int y;

    public GameItem(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public abstract void draw(Graphics g);
    public abstract Rectangle getBounds();
}

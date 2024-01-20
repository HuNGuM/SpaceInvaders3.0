package game_item;

import java.awt.*;

public class Obstacle extends GameItem {
    public static int OBSTACLE_WIDTH = 20;
    private int height;
    private int health;

    public Obstacle(int x, int y) {
        super(x, y);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public static int getObstacleWidth() {
        return OBSTACLE_WIDTH;
    }

    public static void setObstacleWidth(int obstacleWidth) {
        OBSTACLE_WIDTH = obstacleWidth;
    }

    public void takeDamage() {
        health--;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, OBSTACLE_WIDTH, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, OBSTACLE_WIDTH, height);
    }
}

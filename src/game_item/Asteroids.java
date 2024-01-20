package game_item;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

public class Asteroids extends GameItem{
    private static final String IMAGE_PATH = "Pictures/asteroids.png";
    private BufferedImage image;

    private int width;
    private int height;

    public Asteroids(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
        loadImage();
    }
    private void loadImage() {
        try {
            image = ImageIO.read(new File(IMAGE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, (ImageObserver) null);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

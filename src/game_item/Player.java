package game_item;

import main.Game;

import java.awt.*;
import javax.swing.ImageIcon;

public class Player extends GameItem {
    private static Player INSTANCE;
    public static final int DEFAULT_PLAYER_WIDTH = 77;
    public static final int DEFAULT_PLAYER_HEIGHT = 77;
    public static int PLAYER_WIDTH = DEFAULT_PLAYER_WIDTH;
    public static int PLAYER_HEIGHT = DEFAULT_PLAYER_HEIGHT;
    private static final String PLAYER_IMAGE_PATH = "Pictures/player3.png";
    public static int BLINK_INTERVAL = 200;
    public static final int DEFAULT_BLINK_INTERVAL = 200;
    public static int INVINCIBILITY_TIME = 2000;
    public static final int DEFAULT_INVINCIBILITY_TIME = 2000; // jest default, bo powerup unkillablePlayerStrategy zmienia wartosc invicibility_time
    private int score;
    private int distance;
    private int lives;
    private boolean invincible;
    private boolean shouldDrawPlayer = true;
    private long lastBlinkTime;
    private boolean isEvenCall;

    private Player(int x, int y) {
        super(x, y);
        score = 0;
        distance = 0;
        lives = 3;
        invincible = false;
    }

    public static Player getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Player(50, Game.GAME_HEIGHT / 2 - PLAYER_HEIGHT / 2);
        }
        return INSTANCE;
    }

    public void resetProps() {
        x = 50;
        y = Game.GAME_HEIGHT / 2 - PLAYER_HEIGHT / 2;
        score = 0;
        distance = 0;
        lives = 3;
        invincible = false;
        shouldDrawPlayer = true;
        isEvenCall = false;
    }

    public void incrementScore() {
        score++;
    }

    public void incrementDistance() {
        if(isEvenCall) {
            distance++;
        }
        isEvenCall = !isEvenCall;
    }

    public void decrementLives() {
        if(lives>0){
            lives--;
        }
    }
    public void incrementLives() {
        lives++;
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);

        if (x < 0) {
            x = 0;
        } else if (x > Game.GAME_WIDTH - PLAYER_WIDTH) {
            x = Game.GAME_WIDTH - PLAYER_WIDTH;
        }

        if (y < 0) {
            y = 0;
        } else if (y > Game.GAME_HEIGHT - PLAYER_HEIGHT) {
            y = Game.GAME_HEIGHT - PLAYER_HEIGHT;
        }
    }

    @Override
    public void draw(Graphics g) {
        if(shouldDrawPlayer) {
            ImageIcon playerIcon = new ImageIcon(PLAYER_IMAGE_PATH);
            Image playerImage = playerIcon.getImage();
            g.drawImage(playerImage, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, null);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public void setPlayerSize(double multiplier) {
        PLAYER_WIDTH *= multiplier;
        PLAYER_HEIGHT *= multiplier;
        move((int)(PLAYER_WIDTH * multiplier / 2), (int)(PLAYER_HEIGHT * multiplier / 2));
    }

    public void resetPlayerSize() {
        int dx = (PLAYER_WIDTH - DEFAULT_PLAYER_WIDTH) / 2;
        int dy = (PLAYER_HEIGHT - DEFAULT_PLAYER_HEIGHT) / 2;
        PLAYER_WIDTH = DEFAULT_PLAYER_WIDTH;
        PLAYER_HEIGHT = DEFAULT_PLAYER_HEIGHT;
        move(dx, dy);
    }

    public void resetPlayerInvincibility(){
        INVINCIBILITY_TIME=DEFAULT_INVINCIBILITY_TIME;
        BLINK_INTERVAL=DEFAULT_BLINK_INTERVAL;
    }

    public boolean isShouldDrawPlayer() {
        return shouldDrawPlayer;
    }

    public void setShouldDrawPlayer(boolean shouldDrawPlayer) {
        this.shouldDrawPlayer = shouldDrawPlayer;
    }

    public long getLastBlinkTime() {
        return lastBlinkTime;
    }

    public void setLastBlinkTime(long lastBlinkTime) {
        this.lastBlinkTime = lastBlinkTime;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public int getScore() {
        return score;
    }

    public int getDistance() {
        return distance;
    }

    public int getLives() {
        return lives;
    }

    public int getDefaultInvincibilityTime() {return DEFAULT_INVINCIBILITY_TIME;}
    public int getDefaultBlinkInterval() {return DEFAULT_BLINK_INTERVAL;}

    public void setInvincibilityTime(int invincibilityTime) {INVINCIBILITY_TIME = invincibilityTime;}

    public void setBlinkInterval(int blinkInterval){BLINK_INTERVAL=blinkInterval;}

    public  int getPlayerWidth() {
        return PLAYER_WIDTH;
    }
    public void setPlayerWidth(int playerWidth) {
        PLAYER_WIDTH = playerWidth;
    }
}
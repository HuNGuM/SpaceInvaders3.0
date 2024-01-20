package panel;

import game_item.*;
import game_item.Point;
import game_item.powerUp.*;
import listener.ObstacleAddedListener;
import listener.OnLeftHalfPlayerMovedListener;
import listener.OnRightHalfPlayerMovedListener;
import listener.PlayerMovedListener;
import listener.geometry.Coordinate;
import main.Game;
import view.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static int obstacleSpeed;
    private static int playerSpeed = 7;
    public static final int DEFAULT_PLAYER_SPEED = 7;
    private static int pointSpeed;
    private static int powerUpSpeed;
    private static int bulletSpeed;
    private static int pointInterval;
    private static int powerUpInterval;
    private static int obstacleInterval;
    public static ArrayList<PowerUpStrategy> POWER_UPS =  new ArrayList<>(Arrays.asList(
            new UnkillablePlayerStrategy(),
            new SmallPlayerStrategy(),
            new HealthUpStrategy(),
            new ParallelBulletModeStrategy(),
            new ConsecutiveBulletModeStrategy()
    ));
    public static final Player PLAYER = Player.getInstance();
    private final List<GameItem> gameItems;
    private boolean gameRunning;
    private final Set<Direction> currentDirections = new HashSet<>();
    private static long invincibilityStart;
    public static ObstacleAddedListener obstacleAddedListener;
    private static final List<PlayerMovedListener> PLAYER_MOVED_LISTENERS = List.of(new PlayerMovedListener(),
            new OnLeftHalfPlayerMovedListener(), new OnRightHalfPlayerMovedListener());
    private boolean displayBestRoute;
    private boolean isBulletTimerActive = false;
    private Timer bulletTimer;
    private Timer frameTimer;
    private Timer obstacleTimer;
    private Timer pointTimer;
    private Timer powerUpTimer;
    private static final int BULLET_INTERVAL = 500;
    private boolean isShooting = false;

    private static boolean bulletModeSingle = true;
    private static boolean bulletModeParallel = false;
    private static boolean bulletModeConsecutive = false;
  
    String playerName;
    private Timer rectangleTimer;
    private static boolean asteroidsEnabled = false;



    public GamePanel(boolean displayBestRoute) {
        gameItems = new ArrayList<>();
        gameRunning = false;
        this.displayBestRoute = displayBestRoute;

        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(getSize().width, getSize().height));
        bulletTimer = new Timer(bulletSpeed, e -> {
            if (gameRunning) {
                addBullet();
            }
        });
        bulletTimer = new Timer(BULLET_INTERVAL, e -> {
            if (gameRunning && isShooting) {
                addBullet();
            }
        });
        rectangleTimer = new Timer(6000, e -> addMovingRectangle());
        rectangleTimer.start();
    }

    private enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }


    public void startGame(boolean displayInfo) {
        Game.showPanel("gamePanel");

        if (displayInfo) {
            displayInfoMessage();
            DialogInput customInput = new DialogInput(Game.getInstance(), "where RODO?", "Enter your nickname:", 20);
            customInput.setVisible(true);
            this.playerName = customInput.getInput();
        }

        this.frameTimer = new Timer(15, this);
        this.frameTimer.start();

        this.pointTimer = new Timer(pointInterval, e -> addPoint());
        this.pointTimer.start();

        this.powerUpTimer = new Timer(powerUpInterval, e -> addPowerUp());
        powerUpTimer.start();

        this.obstacleTimer = new Timer(obstacleInterval, e -> addObstacle());
        this.obstacleTimer.start();

        this.rectangleTimer = new Timer(6000, e -> addMovingRectangle());
        this.rectangleTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            updateGame();
            if (gameRunning) {
                PLAYER.incrementDistance();
            }
        }
    }

    private void updateGame() {
        movePlayer();
        moveObstacles();
        movePoints();
        moveBullets();
        movePowerUps();
        checkCollisions();
        handleInvincibility();
        moveMovingRectangles();

        repaint();
    }

    private void checkCollisions() {
        Rectangle playerBounds = PLAYER.getBounds();
        if (PLAYER.getLives() <= 0) {
            endGame();
            return;
        }

        for (Point point : findItemsByType(Point.class)) {
            if (playerBounds.intersects(point.getBounds())) {
                PLAYER.incrementScore();
                gameItems.remove(point);
            }
        }

        for (Point point : findItemsByType(Point.class)) {
            if (playerBounds.intersects(point.getBounds())) {
                PLAYER.incrementScore();
                gameItems.remove(point);
            }
        }

        for (PowerUp powerUp : findItemsByType(PowerUp.class)) {
            if (playerBounds.intersects(powerUp.getBounds())) {
                powerUp.applyEffect();
                gameItems.remove(powerUp);
            }
        }

        for (Obstacle obstacle : findItemsByType(Obstacle.class)) {
            if (playerBounds.intersects(obstacle.getBounds())) {
                handleCollisionWithObstacle(obstacle);
            }
        }

        for (Bullet bullet : findItemsByType(Bullet.class)) {
            Rectangle bulletBounds = bullet.getBounds();
            for (Obstacle obstacle : findItemsByType(Obstacle.class)) {
                if (bulletBounds.intersects(obstacle.getBounds())) {
                    gameItems.remove(bullet);
                    obstacle.takeDamage();
                    if (obstacle.getHealth() <= 0) {
                        gameItems.remove(obstacle);
                    }
                }
            }
        }
        for (Asteroids rectangle : findItemsByType(Asteroids.class)) {
            if (playerBounds.intersects(rectangle.getBounds())) {
                handleCollisionWithMovingRectangle(rectangle);
            }
        }
    }

    private void movePlayer() {
        int dx = 0;
        int dy = 0;

        if (currentDirections.contains(Direction.UP)) {
            dy = -playerSpeed;
        }
        if (currentDirections.contains(Direction.DOWN)) {
            dy = playerSpeed;
        }
        if (currentDirections.contains(Direction.RIGHT)) {
            dx = playerSpeed;
        }
        if (currentDirections.contains(Direction.LEFT)) {
            dx = -playerSpeed;
        }

        PLAYER.move(dx, dy);
        if (!findItemsByType(Obstacle.class).isEmpty()) {
            PLAYER_MOVED_LISTENERS.forEach(PlayerMovedListener::calcPrecisionOfMatchWithBestRoute);
        }
    }

    private void moveObstacles() {
        for (Obstacle obstacle : findItemsByType(Obstacle.class)) {
            obstacle.move(-obstacleSpeed, 0);
            if (obstacle.getX() + Obstacle.OBSTACLE_WIDTH <= 0) {
                gameItems.remove(obstacle);
            }
        }
        if (!findItemsByType(Obstacle.class).isEmpty()) {
            obstacleAddedListener.moveSegments(-obstacleSpeed);
        }
    }

    private void movePoints() {
        for (Point point : findItemsByType(Point.class)) {
            point.move(-pointSpeed, 0);
            if (point.getX() + Point.POINT_WIDTH <= 0) {
                gameItems.remove(point);
            }
        }
    }

    private void movePowerUps() {
        for (PowerUp powerUp : findItemsByType(PowerUp.class)) {
            powerUp.move(-pointSpeed, 0);
            if (powerUp.getX() + Point.POINT_WIDTH <= 0) {
                gameItems.remove(powerUp);
            }
        }
    }

    private void moveBullets() {
        for (Bullet bullet : findItemsByType(Bullet.class)) {
            bullet.move(bulletSpeed, 0);
            if (bullet.getX() >= Game.GAME_WIDTH) {
                gameItems.remove(bullet);
            }
        }
    }

    private <T extends GameItem> List<T> findItemsByType(Class<T> gameItemType) {
        return gameItems.stream()
                .filter(gameItemType::isInstance)
                .map(gameItemType::cast)
                .collect(Collectors.toList());
    }

    private void handleCollisionWithObstacle(Obstacle obstacle) {
        if (!PLAYER.isInvincible()) {
            PLAYER.decrementLives();
            PLAYER.setInvincible(true);
            invincibilityStart = System.currentTimeMillis();
            PLAYER.setShouldDrawPlayer(true);
            PLAYER.setLastBlinkTime(invincibilityStart);
            if (PLAYER.getLives() > 0) {
                gameItems.remove(obstacle);
            }
        }
        if(PLAYER.getLives() > 0) {
            gameItems.remove(obstacle);
        }
    }

    private void handleInvincibility() {
        if (PLAYER.isInvincible()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - PLAYER.getLastBlinkTime() > Player.BLINK_INTERVAL) {
                PLAYER.setShouldDrawPlayer(!PLAYER.isShouldDrawPlayer());
                PLAYER.setLastBlinkTime(currentTime);
            }
            if (currentTime - invincibilityStart > Player.INVINCIBILITY_TIME) {
                PLAYER.setInvincible(false);
                PLAYER.setShouldDrawPlayer(true);
            }
        }
    }

    public void endGame() {
        gameRunning = false;
        OnLeftHalfPlayerMovedListener onLeftHalfListener = null;
        OnRightHalfPlayerMovedListener onRightHalfListener = null;
        PlayerMovedListener playerMovedListener = null;

        frameTimer.stop();
        obstacleTimer.stop();
        pointTimer.stop();
        powerUpTimer.stop();
        rectangleTimer.stop();

        for (PlayerMovedListener listener : PLAYER_MOVED_LISTENERS) {
            if (listener instanceof OnLeftHalfPlayerMovedListener onLeft) {
                onLeftHalfListener = onLeft;
            } else if (listener instanceof OnRightHalfPlayerMovedListener onRight) {
                onRightHalfListener = onRight;
            } else {
                playerMovedListener = listener;
            }
        }
        DialogInfo customDialog = new DialogInfo(Game.getInstance(), "Results",
                "Score: " + PLAYER.getScore() +
                        "  Distance: " + PLAYER.getDistance() +
                        "\nMatch with best route\n" +
                        "• on the left half of panel: " + Objects.requireNonNull(onLeftHalfListener).getMatchWithBestRoute() + "%\n" +
                        "• on the right half of panel: " + Objects.requireNonNull(onRightHalfListener).getMatchWithBestRoute() + "%\n" +
                        "• on the whole panel: " + Objects.requireNonNull(playerMovedListener).getMatchWithBestRoute() + "%",
                14);
        customDialog.setVisible(true);

        PLAYER.resetProps();
        for (PowerUp powerUp : findItemsByType(PowerUp.class)) {
            powerUp.getEffect().revertEffect();
        }
        PLAYER_MOVED_LISTENERS.forEach(PlayerMovedListener::resetProps);
        gameItems.clear();
        currentDirections.clear();

        DialogYesNo playAgain = new DialogYesNo(Game.getInstance(), "Play Again", "Do you want to play again", 14);
        playAgain.setVisible(true);

        if(playAgain.getChoice()) this.startGame(false);
        else Game.showPanel("mainMenu");
    }

    public void displayInfoMessage() {
        DialogInfo customDialog = new DialogInfo(Game.getInstance(), "Tutorial", "• Control spaceship with the arrow keys" +
                "\n• Use space-bar to shoot" + "\n• Collect points and avoid obstacles!"
                , 14);
        customDialog.setVisible(true);
    }

    private void addPoint() {
        Random random = new Random();
        int x = Game.GAME_WIDTH;
        int y = random.nextInt(Game.GAME_HEIGHT - Point.POINT_HEIGHT);
        gameItems.add(new Point(x, y));
    }

    private void addPowerUp() {
        Random random = new Random();
        int x = Game.GAME_WIDTH;
        int y = random.nextInt(Game.GAME_HEIGHT - Point.POINT_HEIGHT);
        gameItems.add(new PowerUp(x, y));
    }

    private void addObstacle() {
        Random random = new Random();
        int x = Game.GAME_WIDTH + Game.GAME_WIDTH / 2;
        int origin = 80;
        int height = random.nextInt(origin, origin * 3 + 1);
        int y = random.nextInt(Game.GAME_HEIGHT - height);
        Obstacle obstacle = new Obstacle(x, y);
        obstacle.setHeight(height);
        obstacle.setHealth(3*(height / origin));
        gameItems.add(obstacle);
        if (findItemsByType(Obstacle.class).size() == 1) {
            obstacleAddedListener = new ObstacleAddedListener(new Coordinate(PLAYER.getX() + Player.PLAYER_WIDTH / 2,
                    PLAYER.getY() + Player.PLAYER_HEIGHT / 2));
        }
        obstacleAddedListener.setBestRoute(obstacle);
    }

    private void addBullet() {
        if (bulletModeSingle == true && bulletModeParallel == false && bulletModeConsecutive == false) {
            int x = PLAYER.getX() + Player.PLAYER_WIDTH;
            int y = PLAYER.getY() + Player.PLAYER_HEIGHT / 2 - Bullet.BULLET_HEIGHT / 2;
            gameItems.add(new Bullet(x, y));
        } else if (bulletModeParallel == true && bulletModeConsecutive == false) {
            int x1 = PLAYER.getX() + Player.PLAYER_WIDTH;
            int y1 = PLAYER.getY() - 6 + Player.PLAYER_HEIGHT / 2 - Bullet.BULLET_HEIGHT / 2;
            gameItems.add(new Bullet(x1, y1));
            int x2 = PLAYER.getX() + Player.PLAYER_WIDTH;
            int y2 = PLAYER.getY() + 6 + Player.PLAYER_HEIGHT / 2 - Bullet.BULLET_HEIGHT / 2;
            gameItems.add(new Bullet(x2, y2));
        } else if (bulletModeConsecutive == true && bulletModeParallel == false) {
            int x1 = PLAYER.getX() + Player.PLAYER_WIDTH - 30;
            int y1 = PLAYER.getY() + Player.PLAYER_HEIGHT / 2 - Bullet.BULLET_HEIGHT / 2;
            gameItems.add(new Bullet(x1, y1));
            int x2 = PLAYER.getX() + Player.PLAYER_WIDTH + 30;
            int y2 = PLAYER.getY() + Player.PLAYER_HEIGHT / 2 - Bullet.BULLET_HEIGHT / 2;
            gameItems.add(new Bullet(x2, y2));
        }

        if (bulletModeParallel == true && bulletModeConsecutive == true) {
            int x11 = PLAYER.getX() + Player.PLAYER_WIDTH - 25;
            int y11 = PLAYER.getY() - 7 + Player.PLAYER_HEIGHT / 2 - Bullet.BULLET_HEIGHT / 2;
            gameItems.add(new Bullet(x11, y11));
            int x21 = PLAYER.getX() + Player.PLAYER_WIDTH + 25;
            int y21 = PLAYER.getY() - 7 + Player.PLAYER_HEIGHT / 2 - Bullet.BULLET_HEIGHT / 2;
            gameItems.add(new Bullet(x21, y21));
            int x12 = PLAYER.getX() + Player.PLAYER_WIDTH - 25;
            int y12 = PLAYER.getY() + 7 + Player.PLAYER_HEIGHT / 2 - Bullet.BULLET_HEIGHT / 2;
            gameItems.add(new Bullet(x12, y12));
            int x22 = PLAYER.getX() + Player.PLAYER_WIDTH + 25;
            int y22 = PLAYER.getY() + 7 + Player.PLAYER_HEIGHT / 2 - Bullet.BULLET_HEIGHT / 2;
            gameItems.add(new Bullet(x22, y22));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize().width, getSize().height);

        PLAYER.draw(g);
        gameItems.forEach(item -> item.draw(g));

        g.setColor(Color.WHITE);
        g.drawString("Score: " + PLAYER.getScore() + "    Distance: " + PLAYER.getDistance() +
                "    Lives: " + PLAYER.getLives(), 10, 20);
        if (displayBestRoute && !findItemsByType(Obstacle.class).isEmpty()) {
            g.setColor(Color.YELLOW);
            obstacleAddedListener.getSegments().forEach(s ->
                    g.drawLine(s.coordinate1().getX(), s.coordinate1().getY(),
                            s.coordinate2().getX(), s.coordinate2().getY())
            );
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (!gameRunning) {
            gameRunning = true;
        }

        switch (key) {
            case KeyEvent.VK_UP:
                currentDirections.add(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                currentDirections.add(Direction.DOWN);
                break;
            case KeyEvent.VK_RIGHT:
                currentDirections.add(Direction.RIGHT);
                break;
            case KeyEvent.VK_LEFT:
                currentDirections.add(Direction.LEFT);
                break;
            case KeyEvent.VK_SPACE:
                if (!isBulletTimerActive) {
                    addBullet();
                    bulletTimer.start();
                    isBulletTimerActive = true;
                    isShooting = true;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:
                currentDirections.remove(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                currentDirections.remove(Direction.DOWN);
                break;
            case KeyEvent.VK_RIGHT:
                currentDirections.remove(Direction.RIGHT);
                break;
            case KeyEvent.VK_LEFT:
                currentDirections.remove(Direction.LEFT);
                break;
            case KeyEvent.VK_SPACE:
                if (isBulletTimerActive) {
                    bulletTimer.stop();
                    isBulletTimerActive = false;
                    isShooting = false;
                }
                break;
        }
    }
    private void addMovingRectangle() {
        addMovingRectangle(areAsteroidsEnabled());
    }

    private void addMovingRectangle(boolean addAsteroids) {
        if (addAsteroids) {
            Random random = new Random();
            int x = Game.GAME_WIDTH + Game.GAME_WIDTH / 2;
            int height = Game.GAME_HEIGHT / 2;
            int y;

            if (random.nextBoolean()) {
                y = 0;
            } else {
                y = Game.GAME_HEIGHT - height;
            }

            Asteroids asteroid = new Asteroids(x, y, Game.GAME_WIDTH, height);
            gameItems.add(asteroid);
            if (findItemsByType(Asteroids.class).size() == 1) {
                obstacleAddedListener = new ObstacleAddedListener(new Coordinate(PLAYER.getX() + Player.PLAYER_WIDTH / 2,
                        PLAYER.getY() + Player.PLAYER_HEIGHT / 2));
            }
        }
    }
    private void moveMovingRectangles() {
        for (Asteroids rectangle : findItemsByType(Asteroids.class)) {
            rectangle.move(-obstacleSpeed, 0);
            if (rectangle.getX() + rectangle.getWidth() <= 0) {
                gameItems.remove(rectangle);
            }
        }
    }
    private void handleCollisionWithMovingRectangle(Asteroids rectangle) {
        if (!PLAYER.isInvincible()) {
            PLAYER.decrementLives();
            PLAYER.setInvincible(true);
            invincibilityStart = System.currentTimeMillis();
            PLAYER.setShouldDrawPlayer(true);
            PLAYER.setLastBlinkTime(invincibilityStart);
//            if (PLAYER.getLives() > 0) {
//                gameItems.remove(rectangle);
//            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public static int getPlayerSpeed() {
        return playerSpeed;
    }
    public static void setPlayerSpeed(int playerSpeed) {
        GamePanel.playerSpeed = playerSpeed;
    }
    public static boolean areAsteroidsEnabled() {
        return asteroidsEnabled;
    }

    public static void setAsteroidsEnabled(boolean enabled) {
        asteroidsEnabled = enabled;
    }


    public static void setObstacleSpeed(int obstacleSpeed) {
        GamePanel.obstacleSpeed = obstacleSpeed;
    }

    public static void setPointSpeed(int pointSpeed) {
        GamePanel.pointSpeed = pointSpeed;
    }

    public static void setPowerUpSpeed(int powerUpSpeed) {
        GamePanel.powerUpSpeed = powerUpSpeed;
    }

    public static void setBulletSpeed(int bulletSpeed) {
        GamePanel.bulletSpeed = bulletSpeed;
    }

    public static void setPointInterval(int pointInterval) {
        GamePanel.pointInterval = pointInterval;
    }

    public static void setPowerUpInterval(int powerUpInterval) {
        GamePanel.powerUpInterval = powerUpInterval;
    }

    public static void setObstacleInterval(int obstacleInterval) {
        GamePanel.obstacleInterval = obstacleInterval;
    }

    public static void activateInvincibilityStart(){invincibilityStart=System.currentTimeMillis();}

    public static boolean isBulletModeSingle() {
        return bulletModeSingle;
    }

    public static void setBulletModeSingle(boolean isbulletModeSingle) {
        bulletModeSingle=isbulletModeSingle;
    }

    public static boolean isBulletModeParallel() {
        return bulletModeParallel;
    }

    public static void setBulletModeParallel(boolean isbulletModeParallel) {
        bulletModeParallel = isbulletModeParallel;
    }

    public static boolean isBulletModeConsecutive() {
        return bulletModeConsecutive;
    }

    public static void setBulletModeConsecutive(boolean isbulletModeConsecutive) {
        bulletModeConsecutive = isbulletModeConsecutive;
    }
    public void setDisplayBestRoute(boolean displayBestRoute) {
        this.displayBestRoute = displayBestRoute;
    }
}
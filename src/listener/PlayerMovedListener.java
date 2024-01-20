package listener;

import game_item.Player;
import listener.geometry.Segment;
import main.Game;
import panel.GamePanel;

public class PlayerMovedListener {
    private double precisionSum;
    private int callCounter;

    public void calcPrecisionOfMatchWithBestRoute() {
        callCounter++;
        int playerCenterY = GamePanel.PLAYER.getY() + Player.PLAYER_HEIGHT / 2;
        int matchY = getMatchY();
        double precision = 0;
        if(matchY != -1) {
            if (playerCenterY > matchY) {
                precision = (double) (Game.GAME_HEIGHT - playerCenterY) / (Game.GAME_HEIGHT - matchY);
            } else if (playerCenterY < matchY) {
                precision = (double) playerCenterY / matchY;
            } else {
                precision = 1;
            }
        }
        precisionSum += precision;
    }

    public int getMatchWithBestRoute() {
        return (int) (precisionSum * 100 / callCounter);
    }

    public void resetProps() {
        precisionSum = 0;
        callCounter = 0;
    }

    private int getMatchY() {
        int playerCenterX = GamePanel.PLAYER.getX() + Player.PLAYER_WIDTH / 2;
        ObstacleAddedListener obstacleAddedListener = GamePanel.obstacleAddedListener;
        Segment segment = obstacleAddedListener.getSegments().stream()
                .filter(s -> playerCenterX >= s.coordinate1().getX() && playerCenterX <= s.coordinate2().getX())
                .findFirst()
                .orElse(null);
        if(segment != null) {
            int routeDx = Math.abs(segment.coordinate2().getX() - segment.coordinate1().getX());
            int routeDy = Math.abs(segment.coordinate2().getY() - segment.coordinate1().getY());
            int tempX = Math.abs(playerCenterX - segment.coordinate1().getX());
            int tempY = Math.round((float) tempX / routeDx) * routeDy;
            return segment.coordinate2().getY() > segment.coordinate1().getY() ?
                    segment.coordinate1().getY() + tempY : segment.coordinate1().getY() - tempY;
        }
        return -1;
    }
}

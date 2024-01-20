package listener;

import game_item.Obstacle;
import listener.geometry.Coordinate;
import listener.geometry.Segment;
import main.Game;

import java.util.ArrayList;
import java.util.List;

public class ObstacleAddedListener {
    private final List<Segment> segments = new ArrayList<>();
    private Coordinate coordinate;

    public ObstacleAddedListener(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setBestRoute(Obstacle addedObstacle) {
        removeOutOfBoundsSegments();
        Coordinate previousCoordinate = new Coordinate(coordinate.getX(), coordinate.getY());
        int topGap = addedObstacle.getY();
        int bottomGap = Game.GAME_HEIGHT - (topGap + addedObstacle.getHeight());
        coordinate = new Coordinate(addedObstacle.getX() + Obstacle.OBSTACLE_WIDTH / 2,
                topGap > bottomGap ? topGap / 2 : Game.GAME_HEIGHT - bottomGap / 2);
        segments.add(new Segment(previousCoordinate, coordinate));
    }

    public void moveSegments(int dx) {
        segments.forEach(s -> {
            s.coordinate1().setX(s.coordinate1().getX() + dx);
            s.coordinate2().setX(s.coordinate2().getX() + dx);
        });
    }

    private void removeOutOfBoundsSegments() {
        segments.removeIf(segment -> segment.coordinate1().getX() <= 0 && segment.coordinate2().getX() <= 0);
    }

    public List<Segment> getSegments() {
        return segments;
    }
}

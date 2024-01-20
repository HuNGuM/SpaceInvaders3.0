package strategy;

import static panel.GamePanel.*;

public class HardDifficultyStrategy implements DifficultyStrategy {
    private final String name;

    public HardDifficultyStrategy() {
        this.name = "HARD";
    }

    @Override
    public void setGameFields() {
        setObstacleInterval(400);
        setPointInterval(600);
        setPowerUpInterval(4000);
        setObstacleSpeed(13);
        setPointSpeed(11);
        setPowerUpSpeed(12);
        setBulletSpeed(16);
    }

    @Override
    public String getName() {
        return name;
    }
}

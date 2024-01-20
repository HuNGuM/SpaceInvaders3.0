package strategy;

import static panel.GamePanel.*;

public class MediumDifficultyStrategy implements DifficultyStrategy {
    private final String name;

    public MediumDifficultyStrategy() {
        this.name = "MEDIUM";
    }

    @Override
    public void setGameFields() {
        setObstacleInterval(600);
        setPointInterval(800);
        setPowerUpInterval(4000);
        setObstacleSpeed(11);
        setPointSpeed(9);
        setPowerUpSpeed(10);
        setBulletSpeed(13);
    }

    @Override
    public String getName() {
        return name;
    }
}

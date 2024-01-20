package strategy;

import static panel.GamePanel.*;

public class EasyDifficultyStrategy implements DifficultyStrategy {
    private final String name;

    public EasyDifficultyStrategy() {
        this.name = "EASY";
    }

    @Override
    public void setGameFields() {
        setObstacleInterval(800);
        setPointInterval(1000);
        setPowerUpInterval(3000);
        setObstacleSpeed(9);
        setPointSpeed(7);
        setPowerUpSpeed(8);
        setBulletSpeed(10);
    }

    @Override
    public String getName() {
        return name;
    }
}

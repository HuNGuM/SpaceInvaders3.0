package game_item.powerUp;

import game_item.Player;
import panel.GamePanel;

import java.awt.*;

public class ParallelBulletModeStrategy implements  PowerUpStrategy{

        public ParallelBulletModeStrategy() {}
        int duration = 10000; 
        private Color color=Color.YELLOW;
        public void applyEffect() {
            if(!GamePanel.isBulletModeParallel()){
                GamePanel.setBulletModeSingle(false);
                GamePanel.setBulletModeParallel(true);
//                GamePanel.POWER_UPS.remove(this);
            }
        }

        public void revertEffect() {
            GamePanel.setBulletModeSingle(true);
            GamePanel.setBulletModeParallel(false);
        }

        public int getDuration() {
            return duration;
        }
        public void draw(Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            g.fillOval(x, y, width, height);
        }
}

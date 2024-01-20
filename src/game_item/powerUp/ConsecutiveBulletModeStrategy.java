package game_item.powerUp;

import panel.GamePanel;

import java.awt.*;

public class ConsecutiveBulletModeStrategy implements  PowerUpStrategy{

        public ConsecutiveBulletModeStrategy() {}
        int duration = 10000; 
        private Color color = Color.ORANGE;
        public void applyEffect() {
            if(!GamePanel.isBulletModeParallel()){
                GamePanel.setBulletModeSingle(false);
                GamePanel.setBulletModeConsecutive(true);
//                GamePanel.POWER_UPS.remove(this);
            }
        }

        public void revertEffect() {
            GamePanel.setBulletModeSingle(true);
            GamePanel.setBulletModeConsecutive(false);
        }

        public int getDuration() {
            return duration;
        }
        public void draw(Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            g.fillOval(x, y, width, height);
        }
}

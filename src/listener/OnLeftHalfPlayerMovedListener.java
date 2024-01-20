package listener;

import game_item.Player;
import main.Game;
import panel.GamePanel;

public class OnLeftHalfPlayerMovedListener extends PlayerMovedListener {
    @Override
    public void calcPrecisionOfMatchWithBestRoute() {
        int playerCenterX = GamePanel.PLAYER.getX() + Player.PLAYER_WIDTH / 2;
        if(playerCenterX <= Game.GAME_WIDTH / 2) {
            super.calcPrecisionOfMatchWithBestRoute();
        }
    }
}

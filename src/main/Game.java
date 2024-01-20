package main;

import javax.swing.*;
import java.awt.*;
import java.awt.Toolkit;

import panel.GamePanel;
import score_panel.ScorePanel;
import view.MainMenu;


public class Game extends JFrame {
    public static final int GAME_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int GAME_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 60;
    private static final CardLayout CARD_LAYOUT = new CardLayout();
    private static final JPanel CARD_PANEL = new JPanel(CARD_LAYOUT);

    private static Game gameInstance;


    public Game() {
        gameInstance = this;
        setTitle("Point collector game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainMenu mainMenuPanel = new MainMenu();

        CARD_PANEL.add("mainMenu", mainMenuPanel);

        mainMenuPanel.setupGame();

        getContentPane().add(CARD_PANEL);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }

    public static Game getInstance() {
        return gameInstance;
    }

    public static void showPanel(String panelName) {
        CARD_LAYOUT.show(CARD_PANEL, panelName);
    }

    public static void addToPanel(String componentName, JPanel panel) {
        CARD_PANEL.add(componentName, panel);
    }


}
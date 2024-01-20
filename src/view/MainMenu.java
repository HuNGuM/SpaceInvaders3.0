package view;

import main.Game;
import panel.GamePanel;
import score_panel.ScorePanel;
import strategy.DifficultyStrategy;
import strategy.EasyDifficultyStrategy;
import strategy.HardDifficultyStrategy;
import strategy.MediumDifficultyStrategy;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MainMenu extends JPanel {
    private boolean bestRouteEnabled = false;
    private final DifficultyStrategy[] strategies = {new EasyDifficultyStrategy(),
            new MediumDifficultyStrategy(), new HardDifficultyStrategy()};
    private String selectedDifficulty = "EASY";
    private GamePanel gamePanel;
    private ScorePanel scorePanel;

    public MainMenu() {
        configureMainMenuPanel();
    }

    private void configureMainMenuPanel() {
        setPreferredSize(new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT));
        setLayout(new GridLayout(0, 1));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder());

        MenuButton startButton = new MenuButton("START");
//        true to display info and get name if player comes from MainMenu
        startButton.addActionListener(e -> this.gamePanel.startGame(true));

        MenuButton difficultyButton = new MenuButton("DIFFICULTY");
        difficultyButton.addActionListener(e -> selectAndApplyDifficulty());

        MenuButton pathTrackingButton = new MenuButton("BEST ROUTE");
        pathTrackingButton.addActionListener(e -> setBestRoute());

        MenuButton asteroidsButton = new MenuButton("ASTEROIDS");
        asteroidsButton.addActionListener(e -> startAsteroidsGame());

        MenuButton scoreHistoryButton = new MenuButton("SCORE HISTORY");
        scoreHistoryButton.addActionListener(e -> {
            Game.showPanel("scoreHistory");
            this.scorePanel.updateList();
        });

        MenuButton exitButton = new MenuButton("EXIT");
        exitButton.addActionListener(e -> exitGame());

        add(startButton);
        add(difficultyButton);
        add(pathTrackingButton);
        add(asteroidsButton);
        add(scoreHistoryButton);
        add(exitButton);
    };
    private void startAsteroidsGame() {
        DialogYesNo customDialog = new DialogYesNo(Game.getInstance(), "Asteroids", "Do you want the asteroids \nto be added during the game?", 15);
        customDialog.setVisible(true);
        GamePanel.setAsteroidsEnabled(customDialog.getChoice());
    }

    public void setupGame() {
        Arrays.stream(strategies)
                .filter(strategy -> strategy.getName().equalsIgnoreCase(selectedDifficulty))
                .findFirst()
                .orElseThrow()
                .setGameFields();

        this.gamePanel = new GamePanel(bestRouteEnabled);
        Game.addToPanel("gamePanel", this.gamePanel);

        this.scorePanel = new ScorePanel();
        Game.addToPanel("scoreHistory", this.scorePanel);

        revalidate();
        repaint();
    }


    private void selectAndApplyDifficulty() {
        String[] strategiesNames = new String[strategies.length];
        for(int i = 0; i < strategiesNames.length; i++) {
            strategiesNames[i] = strategies[i].getName();
        }

        DialogList customDialog = new DialogList(Game.getInstance(), "Difficulty Selection", "Select difficulty:", strategiesNames, 20);
        customDialog.setVisible(true);

        if(customDialog.getListChoice() != null) {
            selectedDifficulty = customDialog.getListChoice();
            Arrays.stream(strategies)
                    .filter(strategy -> strategy.getName().equalsIgnoreCase(selectedDifficulty))
                    .findFirst()
                    .orElseThrow()
                    .setGameFields();
        }
    }

    private void setBestRoute() {
        DialogYesNo customDialog = new DialogYesNo(Game.getInstance(), "Best Route", "Do you want the best route\nto be displayed during the game?", 15);
        customDialog.setVisible(true);
        bestRouteEnabled = customDialog.getChoice();
    }

    private void exitGame() {
        DialogYesNo customDialog = new DialogYesNo(Game.getInstance(), "Exit Game", "Are you sure you want to exit?", 17);
        customDialog.setVisible(true);
        if(customDialog.getChoice()) System.exit(0);
    }

    /*private JPanel createDifficultyMenuCard() {
        JPanel difficultyMenuPanel = new JPanel();
        difficultyMenuPanel.setLayout(new GridLayout(3, 1));

        JButton easyButton = new JButton("Easy");
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentDifficulty = "Easy";
                showMainMenu();
            }
        });

        JButton mediumButton = new JButton("Medium");
        mediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentDifficulty = "Medium";
                showMainMenu();
            }
        });

        JButton hardButton = new JButton("Hard");
        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentDifficulty = "Hard";
                showMainMenu();
            }
        });

        difficultyMenuPanel.add(easyButton);
        difficultyMenuPanel.add(mediumButton);
        difficultyMenuPanel.add(hardButton);

        return difficultyMenuPanel;
    }

    private void showPathTrackingMenu() {
        JPanel pathTrackingMenuPanel = createPathTrackingMenuCard();
        setContentPane(pathTrackingMenuPanel);
        revalidate();
        repaint();
    }

    private JPanel createPathTrackingMenuCard() {
        JPanel pathTrackingMenuPanel = new JPanel();
        pathTrackingMenuPanel.setLayout(new GridLayout(2, 1));

        JButton enableButton = new JButton("Enable Path Tracking");
        enableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pathTrackingEnabled = true;
                showMainMenu();
            }
        });

        JButton disableButton = new JButton("Disable Path Tracking");
        disableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pathTrackingEnabled = false;
                showMainMenu();
            }
        });
        pathTrackingMenuPanel.add(enableButton);
        pathTrackingMenuPanel.add(disableButton);
        return pathTrackingMenuPanel;
    }*/
}

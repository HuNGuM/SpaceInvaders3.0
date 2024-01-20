package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuButton extends JButton {
    public MenuButton(String text) {
        super(text);
        setForeground(Color.WHITE);
        setBackground(Color.BLACK);
        setFont(FontLoader.loadPixelFont(26));
        setFocusPainted(false);
    }
}

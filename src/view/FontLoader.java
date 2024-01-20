package view;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.awt.Font.createFont;

public class FontLoader {
    public static Font loadPixelFont(int size) {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/Pixel_font3.ttf"));
            return customFont.deriveFont(Font.PLAIN, size);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, size);
        }
    }
}

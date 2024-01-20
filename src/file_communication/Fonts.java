package file_communication;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Fonts {
    public static Font loadFont(String fontFileName, int style, int size) {
        try {
            // Загрузка шрифта из файла
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFileName));
            return customFont.deriveFont(style, size);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new Font("Arial", style, size);
        }
    }}

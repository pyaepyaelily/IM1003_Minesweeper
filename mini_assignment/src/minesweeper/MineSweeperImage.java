package minesweeper;

import javax.swing.*;
import java.awt.*;

public class MineSweeperImage {
    private ImageIcon smileyImageIcon;
    private ImageIcon boomImageIcon;
    private ImageIcon flagImageIcon;
    private ImageIcon deadImageIcon;

    public ImageIcon getImage(String choice) {
        switch (choice) {
            case "smiley" -> {
                smileyImageIcon = getScaledImage("mini_assignment/images/smiley.png");
                return smileyImageIcon;
            }
            case "boom" -> {
                boomImageIcon = getScaledImage("mini_assignment/images/boom.png");
                return boomImageIcon;
            }
            case "flag" -> {
                flagImageIcon = getScaledImage("mini_assignment/images/flag.png");
                return flagImageIcon;
            }
            case "dead" -> {
                deadImageIcon = getScaledImage("mini_assignment/images/dead.png");
                return deadImageIcon;
            }
        }
        return null;
    }

    public ImageIcon getScaledImage(String imageString) {
        ImageIcon imageIcon = new ImageIcon(imageString);
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);
        return imageIcon;
    }
}

package minesweeper;

import javax.swing.*;
import java.awt.*;

public class MineSweeperImage {
    private ImageIcon smileyImageIcon ;
    private ImageIcon boomImageIcon;
    private ImageIcon flagImageIcon;
    private ImageIcon num1ImageIcon;
    private ImageIcon num2ImageIcon;
    private ImageIcon num3ImageIcon;

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
            case "num1" -> {
                num1ImageIcon = getScaledImage("mini_assignment/images/1.png");
                return num1ImageIcon;
            }
            case "num2" -> {
                num1ImageIcon = getScaledImage("mini_assignment/images/2.png");
                return num2ImageIcon;
            }
            case "num3" -> {
                num1ImageIcon = getScaledImage("mini_assignment/images/3.png");
                return num3ImageIcon;
            }
        }
        return null;
    }
    public ImageIcon getScaledImage(String imageString) {
        ImageIcon imageIcon = new ImageIcon(imageString);
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);
        return imageIcon;
    }
}

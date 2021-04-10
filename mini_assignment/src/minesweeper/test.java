package minesweeper;

import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;  // Use AWT's Event handlers
import java.io.File;
import java.io.FileInputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;  // Use to generate a random int for mines position
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;     // Use Swing's Containers and Components
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * The Mine Sweeper Game. Left-click to reveal a cell. Right-click to
 * plant/remove a flag for marking a suspected mine. You win if all the cells
 * not containing mines are revealed. You lose if you reveal a cell containing a
 * mine.
 */
@SuppressWarnings("serial")
public final class test extends JFrame implements MouseListener, ActionListener {

    //From another minesweeper project
    private JFrame screen = null;
    private JPanel composite = new JPanel();
    private JPanel topPanel = new JPanel();
    private JButton smiley = new JButton("");

    ImageIcon smileyImageIcon = null;
    ImageIcon cryImageIcon = null;
    ImageIcon lossImageIcon = null;

    public static final int CELL_SIZE = 60;  // Cell width and height, in pixels

    JButton btnCells[][];
    // Location of mines. True if mine is present on this cell.
    boolean mines[][];

    // User can right-click to plant/remove a flag to mark a suspicious cell
    boolean flags[][];


    private test() {
        screen = new JFrame();
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setVisible(true);
        screen.setResizable(true);
        composite.setLayout(new BorderLayout());
        smiley.setPreferredSize(new Dimension(25, 25));

        smileyImageIcon = getScaledImage("mini_assignment/images/smiley.png");

        smiley.setIcon(smileyImageIcon);
        topPanel.add(smiley);
        composite.add(topPanel, BorderLayout.NORTH);
        smiley.addActionListener(this);
        smiley.addMouseListener(this);
        screen.add(composite);


        //screen.getContentPane().add(new JButton("OK"));
        screen.getContentPane().setLayout(new GridLayout(rows, columns, 2, 2)); // in 10x10 GridLayout

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                btnCells[row][col] = new JButton();  // Allocate each JButton of the array
                screen.getContentPane().add(btnCells[row][col]);          // add to content-pane in GridLayout
            }
        }

        // Set the size of the content-pane and pack all the components
        //  under this container.
        screen.getContentPane().setPreferredSize(new Dimension(CELL_SIZE * columns, CELL_SIZE * rows));
        screen.pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
        setTitle("Mineswepper");
        setVisible(true);
    }

    private int rows = 10;

    private int columns = 10;

//    private int mines = 8;


    private void initGame() {
        btnCells = new JButton[rows][columns];
        mines = new boolean[rows][columns];

        // Reset cells, mines, and flags
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Set all cells to un-revealed
                btnCells[row][col].setEnabled(true);  // enable button
                btnCells[row][col].setBackground(Color.WHITE);
                btnCells[row][col].setPreferredSize(new Dimension(16, 16));
                btnCells[row][col].setText("");       // display blank
                mines[row][col] = false;   // clear all the mines
                flags[row][col] = false;   // clear all the flags
            }
        }
    }


    //Scale the Image
    public ImageIcon getScaledImage(String imageString) {
        ImageIcon imageIcon = new ImageIcon(imageString);
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);
        return imageIcon;
    }

    public static void main(String[] args) {
        test ms = new test();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
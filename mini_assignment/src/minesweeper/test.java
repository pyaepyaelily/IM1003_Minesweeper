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



    private test() {
        screen = new JFrame("MineSweeper by Karthick, TutorialFlow.com");
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setVisible(true);
        screen.setResizable(false);

        composite.setLayout(new BorderLayout());
        smiley.setPreferredSize(new Dimension(25, 25));
        smiley.setIcon(smileyImageIcon);
        topPanel.add(smiley);
        composite.add(topPanel, BorderLayout.NORTH);
        smiley.addActionListener(this);
        smiley.addMouseListener(this);
        screen.add(composite);
        screen.pack();
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
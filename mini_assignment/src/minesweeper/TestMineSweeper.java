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
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

/**
 * The Mine Sweeper Game. Left-click to reveal a cell. Right-click to
 * plant/remove a flag for marking a suspected mine. You win if all the cells
 * not containing mines are revealed. You lose if you reveal a cell containing a
 * mine.
 */
@SuppressWarnings("serial")
public final class TestMineSweeper implements ActionListener, MouseListener{
    //From another minesweeper project
    private JFrame screen = null;
    private JPanel contentPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JPanel topPanel = new JPanel();
    private JButton smiley = new JButton("");
    ImageIcon smileyImageIcon = null;


    // Name-constants for the game properties
    public int rows = 0;
    public int columns = 0;
    public Container cp;
    // Name-constants for UI control (sizes, colors and fonts)
    public static final int CELL_SIZE = 60;  // Cell width and height, in pixels
    //    public static final int CANVAS_WIDTH = CELL_SIZE * COLS; // Game board width/height
//    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
    public static final Color BGCOLOR_NOT_REVEALED = Color.GREEN;
    public static final Color FGCOLOR_NOT_REVEALED = Color.RED;    // flag
    public static final Color BGCOLOR_REVEALED = Color.DARK_GRAY;
    public static final Color FGCOLOR_REVEALED = Color.LIGHT_GRAY; // number of mines
    public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

    // Buttons for user interaction
    JButton btnCells[][];

    // Number of mines in this game. Can vary to control the difficulty level.
    int numMines;
    // Location of mines. True if mine is present on this cell.
    boolean mines[][];

    // User can right-click to plant/remove a flag to mark a suspicious cell
    boolean flags[][];

    // Number of cells revealed
    int numRevealed;

    // Constructor to set up all the UI and game components
    public TestMineSweeper() {
        // Reusable input variable
        String input;
        boolean exitFlag = false;
        // make a dynamic game modifier
        do {
            input = JOptionPane.showInputDialog(
                    null,
                    "How many rows would you want your mine sweeper have? (enter an integer value > 1)",
                    "Message",
                    JOptionPane.QUESTION_MESSAGE
            );
            // Some basic regex to detect digits and only degits since Java doesn't have a type safe parsing
            if (input == null) {
                System.exit(0);
                return;
            }
            if (input.trim().matches("\\d+")) {
                rows = Integer.parseInt(input.trim());
                if (rows <= 1) {
                    exitFlag = true;
                    continue;
                }
                exitFlag = false;
            } else {
                exitFlag = true;
            }
        } while (exitFlag);

        // reset the value of the flag for reuse
        exitFlag = false;
        do {
            input = JOptionPane.showInputDialog(
                    null,
                    "How many columns would you want your mine sweeper have? (enter an integer value > 1)",
                    "Message",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (input == null) {
                System.exit(0);
                return;
            }
            if (input.trim().matches("\\d+")) {
                columns = Integer.parseInt(input.trim());
                if (columns <= 1) {
                    exitFlag = true;
                    continue;
                }
                exitFlag = false;
            } else {
                exitFlag = true;
            }
        } while (exitFlag);

        exitFlag = false;
        do {
            input = JOptionPane.showInputDialog(
                    null,
                    "How many mines do you want to have? (enter an integer value > 0 and <= " + ((columns * rows) - 1) + ")",
                    "Message",
                    JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                System.exit(0);
                return;
            }
            if (input.trim().matches("\\d+")) {
                numMines = Integer.parseInt(input.trim());
                if (numMines < 0 || numMines > ((columns * rows) - 1)) {
                    exitFlag = true;
                    continue;
                }
                exitFlag = false;
            } else {
                exitFlag = true;
            }
        } while (exitFlag);

        btnCells = new JButton[rows][columns];
        mines = new boolean[rows][columns];
        flags = new boolean[rows][columns];

        screen = new JFrame();

        //button to put in topPanel
        smiley.setPreferredSize(new Dimension(25, 25));
        smileyImageIcon = getScaledImage("mini_assignment/images/smiley.png");
        smiley.setIcon(smileyImageIcon);
        topPanel.add(smiley);
        smiley.addActionListener(this);
        smiley.addMouseListener(this);


        contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        buttonPanel.setLayout(new GridLayout(rows, columns, 10, 10));

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                btnCells[row][col] = new JButton();  // Allocate each JButton of the array
                buttonPanel.add(btnCells[row][col]);          // add to content-pane in GridLayout

                // Add MouseEvent listener to process the left/right mouse-click
                // ... [TODO 4]
                // btnCells[row][col].addMouseListener(listener);
            }
        }
        contentPanel.add(buttonPanel);

        screen.add(topPanel, BorderLayout.NORTH);
        screen.add(contentPanel, BorderLayout.SOUTH);

        //cp.setPreferredSize(new Dimension(CELL_SIZE * columns, CELL_SIZE * rows));
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
        screen.setTitle("TestFrame");
        screen.setVisible(true);   // show it
//        screen.setResizable(false);
        screen.pack();
        // Initialize for a new game
        initGame();
    }

    // Initialize and re-initialize a new game
    private void initGame() {
        // Reset cells, mines, and flags
        CellMouseListener listener = new CellMouseListener();
        numRevealed = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Set all cells to un-revealed
                btnCells[row][col].setEnabled(true);  // enable button
                btnCells[row][col].setForeground(FGCOLOR_NOT_REVEALED);
                btnCells[row][col].setBackground(Color.white);
                btnCells[row][col].setFont(FONT_NUMBERS);
                btnCells[row][col].addMouseListener(listener);
                btnCells[row][col].setText("");       // display blank
                mines[row][col] = false;   // clear all the mines
                flags[row][col] = false;   // clear all the flags
            }
        }

        Random rand = new Random();
        // Set the number of mines and the mines' location
        for (int i = 0; i < numMines; i++) {
            int row = -1;
            int col = -1;
            do {
                row = rand.nextInt(rows);
                col = rand.nextInt(columns);
            } while (mines[row][col]);
            mines[row][col] = true;
        }
    }

    public static void main(String[] args) {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("./Last-Surprise.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            //woops
        }

        //[TODO 1]
        TestMineSweeper ms = new TestMineSweeper();
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


    // [TODO 2]
    private class CellMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            // Determine the (row, col) of the JButton that triggered the event
            int rowSelected = -1;
            int colSelected = -1;

            // Get the source object that fired the Event
            JButton source = (JButton) e.getSource();

            // refactored your code here to make it more neater
            // didn't knew this was a thing in programming until I looked it up like even JS has a similar implementation
            // https://stackoverflow.com/questions/886955/how-do-i-break-out-of-nested-loops-in-java
            rowloop:
            for (int row = 0; row < rows; ++row) {
                for (int col = 0; col < columns; ++col) {
                    if (source == btnCells[row][col]) {
                        rowSelected = row;
                        colSelected = col;
                        // break both inner/outer loops
                        break rowloop;
                    }
                }
            }

            // Left-click to reveal a cell; Right-click to plant/remove the flag.
            if (e.getButton() == MouseEvent.BUTTON1) {  // Left-button clicked
                // [TODO 5] If you hit a mine, game over
                // Otherwise, reveal the cell and display the number of surrounding mines
                if (mines[rowSelected][colSelected]) {
                    try {
                        InputStream in = new FileInputStream(new File("./boom.wav"));
//                        AudioStream a = new AudioStream(in);
//                        AudioPlayer.player.start(a);
                    } catch (Exception ex) {
                        //woops
                    }
                    for (int row = 0; row < rows; ++row) {
                        for (int col = 0; col < columns; ++col) {
                            if (mines[row][col]) {
                                btnCells[row][col].setForeground(FGCOLOR_REVEALED);
                                btnCells[row][col].setBackground(BGCOLOR_REVEALED);
                                btnCells[row][col].setText("B");
                            }
                            btnCells[row][col].removeMouseListener(this);
                            btnCells[row][col].setEnabled(false);
                        }// end of inner for loop
                    }// end of for loop
                    JOptionPane.showMessageDialog(
                            null,
                            "Game Over!",
                            "Game Over",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    int surroundingMineNum = calculateMineNumber(rowSelected, colSelected);
                    if (surroundingMineNum == 0) {
                        // A recursive method to find all empty spots
                        revealBlanks(rowSelected, colSelected);
                    } else {
                        btnCells[rowSelected][colSelected].setForeground(FGCOLOR_REVEALED);
                        btnCells[rowSelected][colSelected].setBackground(BGCOLOR_REVEALED);
                        btnCells[rowSelected][colSelected].setText(String.valueOf(surroundingMineNum));
                        btnCells[rowSelected][colSelected].removeMouseListener(this);
                        btnCells[rowSelected][colSelected].setEnabled(false);
                        numRevealed++;
                    }
                }

            } else if (e.getButton() == MouseEvent.BUTTON3) { // right-button clicked
                // [TODO 6] If the location is flagged, remove the flag
                // Otherwise, plant a flag.
                if (flags[rowSelected][colSelected]) {
                    btnCells[rowSelected][colSelected].setText("");
                    flags[rowSelected][colSelected] = false;
                } else {
                    btnCells[rowSelected][colSelected].setText("^");
                    flags[rowSelected][colSelected] = true;
                }

            }
            // [TODO 7] Check if the player has won, after revealing this cell
            if (numRevealed == (rows * columns) - numMines) {
                JOptionPane.showMessageDialog(
                        null,
                        "Congratulations!, you've finished the game",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                for (int row = 0; row < rows; ++row) {
                    for (int col = 0; col < columns; ++col) {
                        btnCells[row][col].removeMouseListener(this);
                        btnCells[row][col].setEnabled(false);
                    }// end of inner for loop
                }// end of for loop
            }
        }

        /**
         * *
         * recursive method to open all the blank location of the mines the
         * algorithm is called flood filled
         *
         * @param row
         * @param col
         */
        private void revealBlanks(int row, int col) {
            if (row < 0 || row >= rows || col < 0 || col >= columns) {
                return;
            }
            if (btnCells[row][col].getBackground() == BGCOLOR_REVEALED || btnCells[row][col].getForeground() == FGCOLOR_REVEALED) {
                return;
            }
            numRevealed++;
            btnCells[row][col].setForeground(FGCOLOR_REVEALED);
            btnCells[row][col].setBackground(BGCOLOR_REVEALED);
            btnCells[row][col].removeMouseListener(this);
            btnCells[row][col].setEnabled(false);
            int surroundingMineNum = calculateMineNumber(row, col);
            if (surroundingMineNum > 0) {
                btnCells[row][col].setText(String.valueOf(surroundingMineNum));
                return;
            }
            // I apparently cannot do a for-loop to a recursion for the flood fill algorithm
            // Stackoverflow link : https://stackoverflow.com/questions/14077521/recursive-floodfill-overflowing
            // And since I have only so little terms I wrote it out so YAY! :)
            // Max 8 terms for 2D array
            revealBlanks(row - 1, col - 1);
            revealBlanks(row - 1, col);
            revealBlanks(row - 1, col + 1);
            revealBlanks(row + 1, col - 1);
            revealBlanks(row + 1, col);
            revealBlanks(row + 1, col + 1);
            revealBlanks(row, col - 1);
            revealBlanks(row, col + 1);
        }

        // this is calculate the surrounding mine number
        private int calculateMineNumber(int row, int col) {
            int sum = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow = row + i;
                    int newCol = col + j;
                    if (newRow < rows && newRow >= 0) {
                        if (newCol >= 0 && newCol < columns) {
                            boolean initialCondFlag = (j == 0 && i == 0);
                            if (!initialCondFlag && mines[row + i][col + j]) {
                                ++sum;
                            }
                        }
                    }
                }
            }
            return sum;
        }
    }

    //Click on smiley button to reset the game
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == smiley) {
            initGame();
        }
    }
    public ImageIcon getScaledImage(String imageString) {
        ImageIcon imageIcon = new ImageIcon(imageString);
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);
        return imageIcon;
    }
}

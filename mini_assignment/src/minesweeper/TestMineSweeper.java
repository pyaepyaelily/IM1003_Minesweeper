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
public final class TestMineSweeper implements ActionListener, MouseListener {
    //From another minesweeper project
    private JFrame screen = null;
    private JPanel contentPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JPanel topPanel = new JPanel();
    private JButton smiley = new JButton("");
    ImageIcon smileyImageIcon = null;
    ImageIcon boomImageIcon = null;
    ImageIcon flagImageIcon = null;

    private GameDifficulty gameDifficulty = new GameDifficulty(Level.BEGINNER);


    // Name-constants for the game properties
//    public int rows = 10;
//    public int columns = 10;
//    public int numMines = 10;

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

    // Location of mines. True if mine is present on this cell.
    boolean mines[][];

    // User can right-click to plant/remove a flag to mark a suspicious cell
    boolean flags[][];

    // Number of cells revealed
    int numRevealed;

    // Constructor to set up all the UI and game components
    public TestMineSweeper() {
        final int rows = gameDifficulty.getRow();
        final int columns = gameDifficulty.getColumns();
        final int mineCount = gameDifficulty.getMineCount();

        btnCells = new JButton[rows][columns];
        mines = new boolean[rows][columns];
        flags = new boolean[rows][columns];

        screen = new JFrame();

//        screen.setIconImage(boomImageIcon.getImage());
        screen.setIconImage(mineSweeperImages("boom").getImage());

        screen.setJMenuBar(createMenuBar());
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
        screen.setTitle("TestFrame");
        screen.setVisible(true);   // show it
//        screen.setResizable(false);


        //button to put in topPanel
        smiley.setPreferredSize(new Dimension(25, 25));
        smiley.setIcon(mineSweeperImages("smiley"));

        topPanel.add(smiley);
        smiley.addActionListener(this);
        smiley.addMouseListener(this);


//        contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
//        contentPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        buttonPanel.setLayout(new GridLayout(rows, columns, 1, 1));

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                btnCells[row][col] = new JButton();  // Allocate each JButton of the array
                btnCells[row][col].setSize(new Dimension(30, 30));
                buttonPanel.add(btnCells[row][col]);          // add to content-pane in GridLayout
            }
        }

        contentPanel.add(buttonPanel);

        screen.add(topPanel, BorderLayout.CENTER);
        screen.add(contentPanel, BorderLayout.SOUTH);
        screen.pack();

        // Initialize for a new game
        initGame(rows, columns, mineCount);
    }

    // Initialize and re-initialize a new game
    private void initGame(int rows, int columns, int numMines) {
        // Reset cells, mines, and flags

        CellMouseListener listener = new CellMouseListener();
        numRevealed = 0;

        System.out.println(rows);
        System.out.println(columns);
        System.out.println(numMines);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Set all cells to un-revealed
//                btnCells[row][col] = new JButton();  // Allocate each JButton of the array
//                buttonPanel.add(btnCells[row][col]);


                btnCells[row][col].setEnabled(true);  // enable button
                btnCells[row][col].setForeground(FGCOLOR_NOT_REVEALED);
                btnCells[row][col].setBackground(Color.white);
                btnCells[row][col].setFont(FONT_NUMBERS);
                btnCells[row][col].addMouseListener(listener);
                btnCells[row][col].setText("");       // display blank
                mines[row][col] = false;   // clear all the mines
                flags[row][col] = false;   // clear all the flags
                btnCells[row][col].setIcon(null);
                btnCells[row][col].setSize(new Dimension(30, 30));
            }
        }

//        contentPanel.add(buttonPanel);

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

    public ImageIcon mineSweeperImages(String choice) {
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
        }
        return null;
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

    public JMenuBar createMenuBar() {

        JMenuBar mBar = new JMenuBar();
        JMenu game = new JMenu("Game");

        JMenu help = new JMenu("Help");

        final JMenuItem miNew = new JMenuItem("New (rows:10, columns:10, mines:10)");
        final JMenuItem miBeg = new JMenuItem("Beginner (rows:10, columns:10, mines:10)");
        final JMenuItem miInter = new JMenuItem("Intermediate (rows:20, columns:20, mines:20)");
        final JMenuItem miExp = new JMenuItem("Expert (rows:30, columns:30, mines:30)");
        final JMenuItem miExit = new JMenuItem("Exit");

        final JMenuItem about = new JMenuItem("About MineSweeper....");

        game.add(miNew);
        game.add(miBeg);
        game.add(miInter);
        game.add(miExp);
        game.add(miExit);

        help.add(about);

        ActionListener MENULSTNR = ae -> {
            if (miNew == ae.getSource()) {
                gameDifficulty = new GameDifficulty(Level.BEGINNER);
                initGame(gameDifficulty.getRow(), gameDifficulty.getColumns(), gameDifficulty.getMineCount());
            }
            if (miBeg == ae.getSource()) {
                gameDifficulty = new GameDifficulty(Level.BEGINNER);
                initGame(gameDifficulty.getRow(), gameDifficulty.getColumns(), gameDifficulty.getMineCount());

            }
            if (miInter == ae.getSource()) {
                gameDifficulty = new GameDifficulty(Level.MEDIUM);
                initGame(gameDifficulty.getRow(), gameDifficulty.getColumns(), gameDifficulty.getMineCount());

            }
            if (miExp == ae.getSource()) {
                gameDifficulty = new GameDifficulty(Level.HIGH);
                initGame(gameDifficulty.getRow(), gameDifficulty.getColumns(), gameDifficulty.getMineCount());

            }
            if (miExit == ae.getSource()) {
                if (screen != null) {
                    screen.dispose();
                }
                System.exit(0);

            }

            if (about == ae.getSource()) {
                System.out.println(" IM1003");
            }
        };

        miNew.addActionListener(MENULSTNR);
        miBeg.addActionListener(MENULSTNR);
        miInter.addActionListener(MENULSTNR);
        miExp.addActionListener(MENULSTNR);
        miExit.addActionListener(MENULSTNR);
        about.addActionListener(MENULSTNR);
        mBar.add(game);
        mBar.add(help);
        return mBar;
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

            int rows = gameDifficulty.getRow();
            int columns = gameDifficulty.getColumns();
            int numMines = gameDifficulty.getMineCount();

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
                                btnCells[row][col].setIcon(mineSweeperImages("boom"));
//                                btnCells[row][col].setText("Boom");
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
                    btnCells[rowSelected][colSelected].setIcon(null);
//                    btnCells[rowSelected][colSelected].setText("");
                    flags[rowSelected][colSelected] = false;
                } else {
                    btnCells[rowSelected][colSelected].setIcon(mineSweeperImages("flag"));
//                    btnCells[rowSelected][colSelected].setText("flag");
                    flags[rowSelected][colSelected] = true;
                }

            }
            // [TODO 7] Check if the player has won, after revealing this cell
            if (numRevealed == (rows * columns) - numMines) {
                JOptionPane.showMessageDialog(
                        null,
                        "Congratulations, You won!!",
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
            int rows = gameDifficulty.getRow();
            int columns = gameDifficulty.getColumns();

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
            int rows = gameDifficulty.getRow();
            int columns = gameDifficulty.getColumns();

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
            int rows = gameDifficulty.getRow();
            int columns = gameDifficulty.getColumns();
            int numMines = gameDifficulty.getMineCount();
            initGame(rows, columns, numMines);
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

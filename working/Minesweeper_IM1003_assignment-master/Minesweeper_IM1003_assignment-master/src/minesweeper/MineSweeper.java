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
public final class MineSweeper extends JFrame {

    // Name-constants for the game properties
//    public static final int ROWS = 10;      // number of cells
//    public static final int COLS = 10;
    public int rows = 0;
    public int columns = 0;
    public Container cp;
    public ResumeData resumeData;
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
    public MineSweeper() {

        resumeData = loadResumeData();
        if (resumeData != null) {
            int confirmation = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to play again",
                    "Continue",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirmation == JOptionPane.YES_OPTION) {
                resumeGame();
                setClosingEvent();
                deleteResumeData();
                return;
            }
            deleteResumeData();
        }
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

        cp = this.getContentPane();           // JFrame's content-pane
        cp.setLayout(new GridLayout(rows, columns, 2, 2)); // in 10x10 GridLayout

        // Allocate a common instance of listener as the MouseEvent listener
        // for all the JButtons
        // ... [TODO 3]
        // CellMouseListener listener = new CellMouseListener();
        // Construct dynamic JButtons and add to the content-pane
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                btnCells[row][col] = new JButton();  // Allocate each JButton of the array
                cp.add(btnCells[row][col]);          // add to content-pane in GridLayout

                // Add MouseEvent listener to process the left/right mouse-click
                // ... [TODO 4] 
                // btnCells[row][col].addMouseListener(listener);
            }
        }

        // Set the size of the content-pane and pack all the components
        //  under this container.
        cp.setPreferredSize(new Dimension(CELL_SIZE * columns, CELL_SIZE * rows));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
        setTitle("Mineswepper");
        setVisible(true);   // show it

        // Add on close listener
        setClosingEvent();

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
                btnCells[row][col].setBackground(BGCOLOR_NOT_REVEALED);
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

    /**
     * *
     * Overload method to re-initialize the game with new difficulty
     *
     * @param newRow
     * @param newCol
     * @param newNumMines
     */
    private void initGame(int newRow, int newCol, int newNumMines) {
        rows = newRow;
        columns = newCol;
        numMines = newNumMines;

        btnCells = new JButton[rows][columns];
        mines = new boolean[rows][columns];
        flags = new boolean[rows][columns];

        cp = this.getContentPane();
        cp.removeAll();
        cp.setLayout(new GridLayout(rows, columns, 2, 2));

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                btnCells[row][col] = new JButton();
                cp.add(btnCells[row][col]);
            }
        }

        cp.setPreferredSize(new Dimension(CELL_SIZE * columns, CELL_SIZE * rows));
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Mineswepper");
        setVisible(true);
        initGame();
    }

    /**
     * Use to load the previous game data
     *
     * @return ResumeData
     */
    public final ResumeData loadResumeData() {
        try {
            ObjectInputStream in;
            ResumeData loadedData;
            try (FileInputStream fis = new FileInputStream("./GameData.ser")) {
                in = new ObjectInputStream(fis);
                loadedData = (ResumeData) in.readObject();
                in.close();
            }
            return loadedData;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Woops");
        }

        return null;
    }

    /**
     * to save the current game data
     */
    public void saveResumeData() {
        try {
            ObjectOutputStream out;
            try (FileOutputStream fos = new FileOutputStream(new File("./GameData.ser"))) {
                out = new ObjectOutputStream(fos);
                out.writeObject(resumeData);
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Woops");
        }
    }

    /**
     * this is to remove the savedData
     */
    public final void deleteResumeData() {
        File file = new File("./GameData.ser");
        boolean removed;
        do {
            removed = file.delete();
        } while (!removed);
    }

    /**
     * This is to load the layout of the previous game
     */
    public final void resumeGame() {
        rows = resumeData.getRows();
        columns = resumeData.getCols();

        btnCells = new JButton[rows][columns];
        mines = resumeData.getMines();
        flags = resumeData.getFlags();
        numMines = resumeData.getNumMine();
        numRevealed = resumeData.getNumRevealed();

        cp = this.getContentPane();           // JFrame's content-pane
        cp.setLayout(new GridLayout(rows, columns, 2, 2)); // in 10x10 GridLayout

        CellMouseListener listener = new CellMouseListener();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                btnCells[row][col] = new JButton();
                cp.add(btnCells[row][col]);
                btnCells[row][col].setFont(FONT_NUMBERS);
                if (resumeData.getBtnOpened()[row][col]) {
                    btnCells[row][col].setEnabled(false);  // enable button
                    btnCells[row][col].setForeground(FGCOLOR_REVEALED);
                    btnCells[row][col].setBackground(BGCOLOR_REVEALED);
                } else {
                    btnCells[row][col].addMouseListener(listener);
                    btnCells[row][col].setEnabled(true);  // enable button
                    btnCells[row][col].setForeground(FGCOLOR_NOT_REVEALED);
                    btnCells[row][col].setBackground(BGCOLOR_NOT_REVEALED);
                }
                btnCells[row][col].setText(resumeData.getBtnText()[row][col]);
            }
        }

        // Set the size of the content-pane and pack all the components
        //  under this container.
        cp.setPreferredSize(new Dimension(CELL_SIZE * columns, CELL_SIZE * rows));
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
        setTitle("Mineswepper");
        setVisible(true);   // show it
    }

    public void setClosingEvent() {
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                int confirmation = JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to save game?",
                        "Save Game",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirmation == JOptionPane.YES_OPTION) {
                    resumeData = new ResumeData();
                    resumeData.setCols(columns);
                    resumeData.setRows(rows);
                    resumeData.setMines(mines);
                    resumeData.setNumMine(numMines);
                    resumeData.setFlags(flags);
                    resumeData.setNumRevealed(numRevealed);

                    String[][] btnText = new String[rows][columns];
                    boolean[][] btnOpened = new boolean[rows][columns];
                    for (int row = 0; row < rows; row++) {
                        for (int col = 0; col < columns; col++) {
                            JButton currBtn = btnCells[row][col];
                            if (currBtn.getBackground() == BGCOLOR_NOT_REVEALED && currBtn.getForeground() == FGCOLOR_NOT_REVEALED) {
                                btnOpened[row][col] = false;
                            } else if (currBtn.getBackground() == BGCOLOR_REVEALED && currBtn.getForeground() == FGCOLOR_REVEALED) {
                                btnOpened[row][col] = true;
                            }
                            btnText[row][col] = currBtn.getText();
                        }
                    }
                    resumeData.setBtnOpened(btnOpened);
                    resumeData.setBtnText(btnText);

                    saveResumeData();
                }
            }
        });
    }

    // The entry main() method
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
        MineSweeper ms = new MineSweeper();
    }

    //ask the user if they want to replay the game with the same difficulty or the same
    public void askContinue() {
        int confirmation = JOptionPane.showConfirmDialog(
                null,
                "Do you want to play again",
                "Continue",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            confirmation = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to change the difficulty?",
                    "Too difficult?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (confirmation == JOptionPane.NO_OPTION) {
                initGame();
                return;
            } else if (confirmation == JOptionPane.CANCEL_OPTION) {
                System.exit(0);
            } else if (confirmation == JOptionPane.YES_OPTION) {
                String input;
                int newRow = -1;
                int newCol = -1;
                int newNumMines = -1;
                boolean exitFlag = false;
                // make a dynamic game modifier 
                do {
                    input = JOptionPane.showInputDialog(
                            null,
                            "How many rows would you want your mine sweeper have? (enter an integer value > 1)",
                            "Message",
                            JOptionPane.QUESTION_MESSAGE
                    );
                    // Some basic regex to detect digits and only deguts since Java doesn't have a type safe parsing
                    if (input == null) {
                        System.exit(0);
                        return;
                    }
                    if (input.trim().matches("\\d+")) {
                        newRow = Integer.parseInt(input.trim());
                        if (newRow < 1) {
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
                        newCol = Integer.parseInt(input.trim());
                        if (newCol < 1) {
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
                            "How many mines do you want to have? (enter an integer value > 0 and <= " + ((newCol * newRow) - 1) + ")",
                            "Message",
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (input == null) {
                        System.exit(0);
                        return;
                    }
                    if (input.trim().matches("\\d+")) {
                        newNumMines = Integer.parseInt(input.trim());
                        if (newNumMines < 0 || newNumMines > ((newCol * newRow) - 1)) {
                            exitFlag = true;
                            continue;
                        }
                        exitFlag = false;
                    } else {
                        exitFlag = true;
                    }
                } while (exitFlag);
                initGame(newRow, newCol, newNumMines);
                return;
            }
        }
        System.exit(0);
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
                    askContinue();
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
                askContinue();
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
}

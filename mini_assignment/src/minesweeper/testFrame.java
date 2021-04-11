package minesweeper;

import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;  // Use AWT's Event handlers
import java.util.Random;  // Use to generate a random int for mines position
import javax.swing.*;     // Use Swing's Containers and Components


/**
 * The Mine Sweeper Game. Left-click to reveal a cell. Right-click to
 * plant/remove a flag for marking a suspected mine. You win if all the cells
 * not containing mines are revealed. You lose if you reveal a cell containing a
 * mine.
 */
@SuppressWarnings("serial")
public final class testFrame implements MouseListener, ActionListener {

    //From another minesweeper project
    private JFrame screen = null;
    private JPanel midPanel = new JPanel();
    private JPanel topPanel = new JPanel();

    private JButton smiley = new JButton("");


    // Name-constants for the game properties
    public int rows = 5; //0 to get user's input
    public int columns = 5; //0 to get user's input

//    Container cp = this.getContentPane();

    // Name-constants for UI control (sizes, colors and fonts)
    public static final int CELL_SIZE = 60;  // Cell width and height, in pixels
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
    public testFrame() {
        // Reusable input variable
//        String input;
//        boolean exitFlag = false;
//        // make a dynamic game modifier
//        do {
//            input = JOptionPane.showInputDialog(
//                    null,
//                    "How many rows would you want your mine sweeper have? (enter an integer value > 1)",
//                    "Message",
//                    JOptionPane.QUESTION_MESSAGE
//            );
//            // Some basic regex to detect digits and only degits since Java doesn't have a type safe parsing
//            if (input == null) {
//                System.exit(0);
//                return;
//            }
//            if (input.trim().matches("\\d+")) {
//                rows = Integer.parseInt(input.trim());
//                if (rows <= 1) {
//                    exitFlag = true;
//                    continue;
//                }
//                exitFlag = false;
//            } else {
//                exitFlag = true;
//            }
//        } while (exitFlag);
//
//        // reset the value of the flag for reuse
//        exitFlag = false;
//        do {
//            input = JOptionPane.showInputDialog(
//                    null,
//                    "How many columns would you want your mine sweeper have? (enter an integer value > 1)",
//                    "Message",
//                    JOptionPane.QUESTION_MESSAGE
//            );
//            if (input == null) {
//                System.exit(0);
//                return;
//            }
//            if (input.trim().matches("\\d+")) {
//                columns = Integer.parseInt(input.trim());
//                if (columns <= 1) {
//                    exitFlag = true;
//                    continue;
//                }
//                exitFlag = false;
//            } else {
//                exitFlag = true;
//            }
//        } while (exitFlag);
//
//        exitFlag = false;
//        do {
//            input = JOptionPane.showInputDialog(
//                    null,
//                    "How many mines do you want to have? (enter an integer value > 0 and <= " + ((columns * rows) - 1) + ")",
//                    "Message",
//                    JOptionPane.QUESTION_MESSAGE);
//            if (input == null) {
//                System.exit(0);
//                return;
//            }
//            if (input.trim().matches("\\d+")) {
//                numMines = Integer.parseInt(input.trim());
//                if (numMines < 0 || numMines > ((columns * rows) - 1)) {
//                    exitFlag = true;
//                    continue;
//                }
//                exitFlag = false;
//            } else {
//                exitFlag = true;
//            }
//        } while (exitFlag);
//
        btnCells = new JButton[5][5];
        mines = new boolean[5][5];
//        flags = new boolean[rows][columns];


        screen = new JFrame();
        //topPanel.setLayout(new BorderLayout());
        GridLayout gLayout = new GridLayout(5, 5, 2, 2);
        midPanel.setLayout(gLayout);

        //button to put in topPanel
        JButton button = new JButton("smiley icon here");
        topPanel.add(button, BorderLayout.PAGE_START);

        //Content Pane
//        Container cp = midPanel.getContentPane();           // JFrame's content-pane
//        cp.setLayout(new GridLayout(5, 5, 2, 2)); // in 10x10 GridLayout


        JPanel contentPane = new JPanel();
        contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        contentPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 5, 10, 10));

        CellMouseListener listener = new CellMouseListener();
        numRevealed = 0;


        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                btnCells[row][col] = new JButton();  // Allocate each JButton of the array
                btnCells[row][col].setEnabled(true);  // enable button
                btnCells[row][col].setForeground(FGCOLOR_NOT_REVEALED);
                btnCells[row][col].setBackground(BGCOLOR_NOT_REVEALED);
                btnCells[row][col].setFont(FONT_NUMBERS);
                btnCells[row][col].addMouseListener(listener);
                btnCells[row][col].setText("");       // display blank
                mines[row][col] = false;   // clear all the mines
//                flags[row][col] = false;   // clear all the flags
                buttonPanel.add(btnCells[row][col]);          // add to content-pane in GridLayout

            }
        }
        contentPane.add(buttonPanel);
        screen.add(topPanel, BorderLayout.NORTH);
        screen.add(contentPane, BorderLayout.SOUTH);

        //cp.setPreferredSize(new Dimension(CELL_SIZE * columns, CELL_SIZE * rows));
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
        screen.setTitle("TestFrame");
        screen.setVisible(true);   // show it
        screen.pack();
        // Initialize for a new game
        initGame();
    }

    //     Initialize and re-initialize a new game
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


    // The entry main() method
    public static void main(String[] args) {
        //[TODO 1]
        testFrame ms = new testFrame();
    }

    //ask the user if they want to replay the game with the same difficulty or the same
//    public void askContinue() {
//        int confirmation = JOptionPane.showConfirmDialog(
//                null,
//                "Do you want to play again",
//                "Continue",
//                JOptionPane.YES_NO_OPTION,
//                JOptionPane.QUESTION_MESSAGE
//        );
//
//        if (confirmation == JOptionPane.YES_OPTION) {
//            confirmation = JOptionPane.showConfirmDialog(
//                    null,
//                    "Do you want to change the difficulty?",
//                    "Too difficult?",
//                    JOptionPane.YES_NO_OPTION,
//                    JOptionPane.QUESTION_MESSAGE
//            );
//            if (confirmation == JOptionPane.NO_OPTION) {
//                initGame();
//                return;
//            } else if (confirmation == JOptionPane.CANCEL_OPTION) {
//                System.exit(0);
//            } else if (confirmation == JOptionPane.YES_OPTION) {
//                String input;
//                int newRow = -1;
//                int newCol = -1;
//                int newNumMines = -1;
//                boolean exitFlag = false;
//                // make a dynamic game modifier
//                do {
//                    input = JOptionPane.showInputDialog(
//                            null,
//                            "How many rows would you want your mine sweeper have? (enter an integer value > 1)",
//                            "Message",
//                            JOptionPane.QUESTION_MESSAGE
//                    );
//                    // Some basic regex to detect digits and only deguts since Java doesn't have a type safe parsing
//                    if (input == null) {
//                        System.exit(0);
//                        return;
//                    }
//                    if (input.trim().matches("\\d+")) {
//                        newRow = Integer.parseInt(input.trim());
//                        if (newRow < 1) {
//                            exitFlag = true;
//                            continue;
//                        }
//                        exitFlag = false;
//                    } else {
//                        exitFlag = true;
//                    }
//                } while (exitFlag);
//
//                // reset the value of the flag for reuse
//                exitFlag = false;
//                do {
//                    input = JOptionPane.showInputDialog(
//                            null,
//                            "How many columns would you want your mine sweeper have? (enter an integer value > 1)",
//                            "Message",
//                            JOptionPane.QUESTION_MESSAGE
//                    );
//                    if (input == null) {
//                        System.exit(0);
//                        return;
//                    }
//                    if (input.trim().matches("\\d+")) {
//                        newCol = Integer.parseInt(input.trim());
//                        if (newCol < 1) {
//                            exitFlag = true;
//                            continue;
//                        }
//                        exitFlag = false;
//                    } else {
//                        exitFlag = true;
//                    }
//                } while (exitFlag);
//
//                exitFlag = false;
//                do {
//                    input = JOptionPane.showInputDialog(
//                            null,
//                            "How many mines do you want to have? (enter an integer value > 0 and <= " + ((newCol * newRow) - 1) + ")",
//                            "Message",
//                            JOptionPane.QUESTION_MESSAGE
//                    );
//                    if (input == null) {
//                        System.exit(0);
//                        return;
//                    }
//                    if (input.trim().matches("\\d+")) {
//                        newNumMines = Integer.parseInt(input.trim());
//                        if (newNumMines < 0 || newNumMines > ((newCol * newRow) - 1)) {
//                            exitFlag = true;
//                            continue;
//                        }
//                        exitFlag = false;
//                    } else {
//                        exitFlag = true;
//                    }
//                } while (exitFlag);
////                initGame(newRow, newCol, newNumMines);
//                return;
//            }
//        }
//        System.exit(0);
//    }

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

    @Override
    public void actionPerformed(ActionEvent e) {

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
//                    askContinue();
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
//                askContinue();
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
package minesweeper;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;


/**
 * The Mine Sweeper Game. Left-click to reveal a cell. Right-click to
 * plant/remove a flag for marking a suspected mine. You win if all the cells
 * not containing mines are revealed. You lose if you reveal a cell containing a
 * mine.
 */
@SuppressWarnings("serial")
public final class TestMineSweeper implements ActionListener, MouseListener {
    private JFrame screen = null;
    private JPanel contentPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JPanel topPanel = new JPanel();
    private JButton restartButton = new JButton("");


    private GameDifficulty gameDifficulty = new GameDifficulty(Level.BEGINNER);
    private MineSweeperImage mineSweeperImages = new MineSweeperImage();

    ImageIcon smileyImageIcon = mineSweeperImages.getImage("smiley");
    ImageIcon boomImageIcon = mineSweeperImages.getImage("boom");
    ImageIcon flagImageIcon = mineSweeperImages.getImage("flag");
    ImageIcon num1ImageIcon = mineSweeperImages.getImage("num1");
    ImageIcon num2ImageIcon = mineSweeperImages.getImage("num2");
    ImageIcon num3ImageIcon = mineSweeperImages.getImage("num3");
    ImageIcon deadImageIcon = mineSweeperImages.getImage("dead");


    // Name-constants for UI control (sizes, colors and fonts)
    public static final int CELL_SIZE = 60;  // Cell width and height, in pixels
    public static final Color FGCOLOR_NOT_REVEALED = Color.RED;    // flag
    public static final Color BGCOLOR_REVEALED = Color.DARK_GRAY;
    public static final Color FGCOLOR_REVEALED = Color.LIGHT_GRAY; // number of mines
    public static final Color BUTTON_REVEALED = new Color(200, 200, 250);
    public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 15);

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

        System.out.println(rows);
        System.out.println(columns);
        System.out.println(mineCount);

        screen = new JFrame();
        screen.setIconImage(boomImageIcon.getImage());
        screen.setJMenuBar(createMenuBar());
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
        screen.setTitle("TestFrame");
        screen.setVisible(true);   // show it
        screen.setPreferredSize(new Dimension(600, 600));
        screen.setResizable(false);


        //button to put in topPanel
        restartButton.setPreferredSize(new Dimension(25, 25));
        restartButton.setIcon(smileyImageIcon);
        restartButton.addActionListener(this);
        restartButton.addMouseListener(this);
        topPanel.add(restartButton, BorderLayout.CENTER);
        topPanel.setPreferredSize(new Dimension(35, 35));

        //For buttons
        contentPanel.setBackground(Color.BLUE);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        contentPanel.setPreferredSize(new Dimension(rows * rows, 500));
        buttonPanel.setLayout(new GridLayout(rows, columns));
        contentPanel.add(buttonPanel);

        screen.add(topPanel, BorderLayout.NORTH);
        screen.add(contentPanel, BorderLayout.SOUTH);
        screen.pack();

        // Initialize for a new game
        initGame(rows, columns, mineCount);
    }

    private void clearGame() {

        var rows = gameDifficulty.getRow();
        var columns = gameDifficulty.getColumns();

        CellMouseListener listener = new CellMouseListener();
        numRevealed = 0;
        restartButton.setIcon(smileyImageIcon);
        if (topPanel.getBackground() == Color.black) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    // Set all cells to un-revealed
                    btnCells[row][col].setEnabled(true);  // enable button
                    btnCells[row][col].setForeground(FGCOLOR_NOT_REVEALED);
                    btnCells[row][col].setBackground(Color.black);
                    btnCells[row][col].setFont(FONT_NUMBERS);
                    btnCells[row][col].addMouseListener(listener);
                    btnCells[row][col].setText("");       // display blank
                    mines[row][col] = false;   // clear all the mines
                    flags[row][col] = false;   // clear all the flags
                    btnCells[row][col].setIcon(null);
                }
            }
        } else {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    // Set all cells to un-revealed
//                btnCells[row][col] = new JButton();  // Allocate each JButton of the array
                    btnCells[row][col].setEnabled(true);  // enable button
                    btnCells[row][col].setForeground(FGCOLOR_NOT_REVEALED);
                    btnCells[row][col].setBackground(Color.white);
                    btnCells[row][col].setFont(FONT_NUMBERS);
                    btnCells[row][col].addMouseListener(listener);
                    btnCells[row][col].setText("");       // display blank
                    mines[row][col] = false;   // clear all the mines
                    flags[row][col] = false;   // clear all the flags
                    btnCells[row][col].setIcon(null);
                }
            }
        }
    }

    // Initialize and re-initialize a new game
    private void initGame(int rows, int columns, int numMines) {
        btnCells = new JButton[rows][columns];
        mines = new boolean[rows][columns];
        flags = new boolean[rows][columns];

        contentPanel.remove(buttonPanel);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(rows, columns));
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                btnCells[row][col] = new JButton();  // Allocate each JButton of the array
                buttonPanel.add(btnCells[row][col]);          // add to content-pane in GridLayout
            }
        }
        contentPanel.add(buttonPanel);
        screen.pack();

        CellMouseListener listener = new CellMouseListener();
        if (topPanel.getBackground() == Color.black) {              // Dark theme
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    btnCells[row][col].setEnabled(true);  // enable button
                    btnCells[row][col].setForeground(FGCOLOR_NOT_REVEALED);
                    btnCells[row][col].setBackground(Color.black);
                    btnCells[row][col].setFont(FONT_NUMBERS);
                    btnCells[row][col].addMouseListener(listener);
                    btnCells[row][col].setText("");       // display blank
                    mines[row][col] = false;   // clear all the mines
                    flags[row][col] = false;   // clear all the flags
                    btnCells[row][col].setIcon(null);
                }
            }
        } else if (topPanel.getBackground() == Color.blue) {        // Blue theme
            for (int row = 0; row < rows; row++) {
                Color myBlueColor = new Color(0, 105, 148);        // custom color constructor for blue theme
                for (int col = 0; col < columns; col++) {
                    btnCells[row][col].setEnabled(true);  // enable button
                    btnCells[row][col].setForeground(FGCOLOR_NOT_REVEALED);
                    btnCells[row][col].setBackground(myBlueColor);
                    btnCells[row][col].setFont(FONT_NUMBERS);
                    btnCells[row][col].addMouseListener(listener);
                    btnCells[row][col].setText("");       // display blank
                    mines[row][col] = false;   // clear all the mines
                    flags[row][col] = false;   // clear all the flags
                    btnCells[row][col].setIcon(null);
                }
            }
        } else if (topPanel.getBackground() == Color.green) {       // Green theme
            for (int row = 0; row < rows; row++) {
                Color myGreenColor = new Color(1, 152, 117);       // custom color constructor for green theme
                for (int col = 0; col < columns; col++) {
                    btnCells[row][col].setEnabled(true);  // enable button
                    btnCells[row][col].setForeground(FGCOLOR_NOT_REVEALED);
                    btnCells[row][col].setBackground(myGreenColor);
                    btnCells[row][col].setFont(FONT_NUMBERS);
                    btnCells[row][col].addMouseListener(listener);
                    btnCells[row][col].setText("");       // display blank
                    mines[row][col] = false;   // clear all the mines
                    flags[row][col] = false;   // clear all the flags
                    btnCells[row][col].setIcon(null);
                }
            }
        } else if (topPanel.getBackground() == Color.magenta) {     // Pink theme
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    btnCells[row][col].setEnabled(true);  // enable button
                    btnCells[row][col].setForeground(FGCOLOR_NOT_REVEALED);
                    btnCells[row][col].setBackground(Color.pink);
                    btnCells[row][col].setFont(FONT_NUMBERS);
                    btnCells[row][col].addMouseListener(listener);
                    btnCells[row][col].setText("");       // display blank
                    mines[row][col] = false;   // clear all the mines
                    flags[row][col] = false;   // clear all the flags
                    btnCells[row][col].setIcon(null);
                }
            }
        } else {
            for (int row = 0; row < rows; row++) {              // Light theme
                for (int col = 0; col < columns; col++) {
                    btnCells[row][col].setEnabled(true);  // enable button
                    btnCells[row][col].setForeground(FGCOLOR_NOT_REVEALED);
                    btnCells[row][col].setBackground(Color.white);
                    btnCells[row][col].setFont(FONT_NUMBERS);
                    btnCells[row][col].addMouseListener(listener);
                    btnCells[row][col].setText("");       // display blank
                    mines[row][col] = false;   // clear all the mines
                    flags[row][col] = false;   // clear all the flags
                    btnCells[row][col].setIcon(null);
                }
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
            //Print out mine position to check
            System.out.println(row + "," + col);
        }

    }


    public static void main(String[] args) {
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
        JMenu theme = new JMenu("Themes");
        JMenu help = new JMenu("Help");


        final JMenuItem miNew = new JMenuItem("New (rows:9, columns:9, mines:10)");
        final JMenuItem miBeg = new JMenuItem("Beginner (rows:9, columns:9, mines:10)");
        final JMenuItem miInter = new JMenuItem("Intermediate (rows:16, columns:16, mines:40)");
        final JMenuItem miExp = new JMenuItem("Expert (rows:16, columns:30, mines:99)");
        final JMenuItem miExit = new JMenuItem("Exit");
        final JMenuItem themeLight = new JMenuItem("Light Theme");
        final JMenuItem themeDark = new JMenuItem("Dark Theme");
        final JMenuItem themeBlue = new JMenuItem("Blue Theme");
        final JMenuItem themeGreen = new JMenuItem("Green Theme");
        final JMenuItem themePink = new JMenuItem("Pink Theme");
        final JMenuItem about = new JMenuItem("About MineSweeper....");
        final JMenuItem instr = new JMenuItem("Instructions");
        final JMenuItem tips = new JMenuItem("Tips");

        game.add(miNew);
        game.add(miBeg);
        game.add(miInter);
        game.add(miExp);
        game.add(miExit);
        theme.add(themeLight);
        theme.add(themeDark);
        theme.add(themeBlue);
        theme.add(themeGreen);
        theme.add(themePink);
        help.add(about);
        help.add(instr);
        help.add(tips);

        ActionListener MENULSTNR = ae -> {
            if (miNew == ae.getSource()) {      // Start a new game
                clearGame();
                screen.setPreferredSize(new Dimension(600, 600));
                screen.setResizable(false);
                gameDifficulty = new GameDifficulty(Level.BEGINNER);
                initGame(gameDifficulty.getRow(), gameDifficulty.getColumns(), gameDifficulty.getMineCount());
            }
            if (miBeg == ae.getSource()) {      // Start a new game with beginner difficulty
                screen.setPreferredSize(new Dimension(600, 600));
                screen.setResizable(false);
                clearGame();
                gameDifficulty = new GameDifficulty(Level.BEGINNER);
                initGame(gameDifficulty.getRow(), gameDifficulty.getColumns(), gameDifficulty.getMineCount());

            }
            if (miInter == ae.getSource()) {    // Start a new game with intermediate difficulty
                clearGame();
                screen.setPreferredSize(new Dimension(800, 700));
                screen.setResizable(false);
                gameDifficulty = new GameDifficulty(Level.MEDIUM);
                initGame(gameDifficulty.getRow(), gameDifficulty.getColumns(), gameDifficulty.getMineCount());

            }
            if (miExp == ae.getSource()) {      // Start a new game with expert difficulty
                clearGame();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                screen.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
                gameDifficulty = new GameDifficulty(Level.HIGH);
                initGame(gameDifficulty.getRow(), gameDifficulty.getColumns(), gameDifficulty.getMineCount());

            }

            if (miExit == ae.getSource()) {     // Close entire window
                if (screen != null) {
                    screen.dispose();
                }
                System.exit(0);

            }
            
            // Various theme settings 
            if (themeLight == ae.getSource()) {
                JOptionPane.showMessageDialog(
                        null,
                        "You will restart the game if you change the theme",
                        "Change theme",
                        JOptionPane.WARNING_MESSAGE
                );
                String changeTheme = "light";
                askRestartTheme(changeTheme);
                System.out.println("Light theme");
            }
            
            if (themeDark == ae.getSource()) {

                JOptionPane.showMessageDialog(
                        null,
                        "You will restart the game if you change the theme",
                        "Change theme",
                        JOptionPane.WARNING_MESSAGE
                );
                String changeTheme = "black";
                askRestartTheme(changeTheme);
                System.out.println("Dark theme");
            }

            if (themeBlue == ae.getSource()) {
                JOptionPane.showMessageDialog(
                        null,
                        "You will restart the game if you change the theme",
                        "Change theme",
                        JOptionPane.WARNING_MESSAGE
                );
                String changeTheme = "blue";
                askRestartTheme(changeTheme);
                System.out.println("Blue theme");
            }

            if (themeGreen == ae.getSource()) {
                JOptionPane.showMessageDialog(
                        null,
                        "You will restart the game if you change the theme",
                        "Change theme",
                        JOptionPane.WARNING_MESSAGE
                );
                String changeTheme = "green";
                askRestartTheme(changeTheme);
                System.out.println("Green theme");
            }

            if (themePink == ae.getSource()) {
                JOptionPane.showMessageDialog(
                        null,
                        "You will restart the game if you change the theme",
                        "Change theme",
                        JOptionPane.WARNING_MESSAGE
                );
                String changeTheme = "pink";
                askRestartTheme(changeTheme);
                System.out.println("Pink theme");
            }

            if (about == ae.getSource()) {
                JOptionPane.showMessageDialog(
                        null,
                        "This is a mini-assignment using Java",
                        "About this",
                        JOptionPane.PLAIN_MESSAGE
                );
            }

            if (instr == ae.getSource()) {
                JOptionPane.showMessageDialog(null, "Instructions:\r\n" + "\r\n"
                                + "· You are presented with a board of squares. Some squares contain mines (bombs), others don't. \r\n"
                                + "· Your goal is to clear the board of squares without triggering any bombs. \r\n"
                                + "· To open a square, point your cursor at the square and click on it. \r\n"
                                + "· If you manage to click all the squares (without clicking on any bombs), you win.  \r\n"
                                + "· If you click on any single square containing a bomb, you lose. So be careful! \r\n" + "\r\n"
                                + "· Have Fun!",
                        "Instructions", JOptionPane.INFORMATION_MESSAGE
                );
            }
            if (tips == ae.getSource()) {
                JOptionPane.showMessageDialog(null, "Tips: \r\n" + "\r\n"
                                + "· Try opening the four corners of the board first when you start. \r\n"
                                + "· Next, try opening any square in the middle. \r\n"
                                + "· Mark all the mines that are obvious using flags. For example: eight 1's surrounding an unopened square. \r\n" + "\r\n"
                                + " · Good Luck!",
                        "Tips", JOptionPane.PLAIN_MESSAGE
                );
            }

        };

        miNew.addActionListener(MENULSTNR);
        miBeg.addActionListener(MENULSTNR);
        miInter.addActionListener(MENULSTNR);
        miExp.addActionListener(MENULSTNR);
        miExit.addActionListener(MENULSTNR);
        themeLight.addActionListener(MENULSTNR);
        themeDark.addActionListener(MENULSTNR);
        themeBlue.addActionListener(MENULSTNR);
        themeGreen.addActionListener(MENULSTNR);
        themePink.addActionListener(MENULSTNR);
        about.addActionListener(MENULSTNR);
        instr.addActionListener(MENULSTNR);
        tips.addActionListener(MENULSTNR);
        mBar.add(game);
        mBar.add(theme);
        mBar.add(help);
        return mBar;
    }

    public void askRestartTheme(String changeTheme) {
        int confirmation = JOptionPane.showConfirmDialog(
                null,
                "Do you want to change the theme?",
                "Change Theme",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (confirmation == JOptionPane.NO_OPTION) {
            return;
        } else {
            if (changeTheme.equals("black")) {
                topPanel.setBackground(Color.black);          // Dark theme
                int rows = gameDifficulty.getRow();
                int columns = gameDifficulty.getColumns();
                int mineCount = gameDifficulty.getMineCount();
                initGame(rows, columns, mineCount);
            } else if (changeTheme.equals("blue")) {
                topPanel.setBackground(Color.blue);            // Blue theme

                int rows = gameDifficulty.getRow();
                int columns = gameDifficulty.getColumns();
                int mineCount = gameDifficulty.getMineCount();
                initGame(rows, columns, mineCount);
            } else if (changeTheme.equals("green")) {
                topPanel.setBackground(Color.green);           // Green theme
                int rows = gameDifficulty.getRow();
                int columns = gameDifficulty.getColumns();
                int mineCount = gameDifficulty.getMineCount();
                initGame(rows, columns, mineCount);
            } else if (changeTheme.equals("pink")) {
                topPanel.setBackground(Color.magenta);         // Pink theme
                int rows = gameDifficulty.getRow();
                int columns = gameDifficulty.getColumns();
                int mineCount = gameDifficulty.getMineCount();
                initGame(rows, columns, mineCount);
            } else {
                topPanel.setBackground(Color.white);          // Light theme
                int rows = gameDifficulty.getRow();
                int columns = gameDifficulty.getColumns();
                int mineCount = gameDifficulty.getMineCount();
                initGame(rows, columns, mineCount);
            }
        }
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
                        System.out.println("click");
                        // break both inner/outer loops
                        break rowloop;
                    }
                }
            }

            // Left-click to reveal a cell; Right-click to plant/remove the flag.
            if (e.getButton() == MouseEvent.BUTTON1) {  // Left-button clicked
                // [TODO 5] If you hit a mine, game over
                // Otherwise, reveal the cell and display the number of surrounding mines (recursion) 

                //If you select the cell that contains mine, you lose
                if (mines[rowSelected][colSelected]) {
                    try {   // audio for clicking on a mine
                        File boomFile = new File("Audio./boom.wav"); 
                        AudioInputStream audioIn = AudioSystem.getAudioInputStream(boomFile);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioIn);
                        clip.start();
                    } catch (Exception ex) {
                        //woops
                    }
                    
                    for (int row = 0; row < rows; ++row) {
                        for (int col = 0; col < columns; ++col) {
                            if (mines[row][col]) {
                                btnCells[row][col].setForeground(FGCOLOR_REVEALED);
                                btnCells[row][col].setBackground(BGCOLOR_REVEALED);
                                btnCells[row][col].setIcon(boomImageIcon);
                            }
                            restartButton.setIcon(deadImageIcon);
                            flags[row][col] = false;
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
                    //If no mines around, reveal
                    btnCells[rowSelected][colSelected].setIcon(null);
                    if (surroundingMineNum == 0) {
                        // A recursive method to find all empty spots
                        revealBlanks(rowSelected, colSelected);
                    } else {
                        btnCells[rowSelected][colSelected].setForeground(FGCOLOR_REVEALED);
                        btnCells[rowSelected][colSelected].setBackground(BGCOLOR_REVEALED);
                        btnCells[rowSelected][colSelected].setText(String.valueOf(surroundingMineNum));
                        System.out.println("Below is the surroundMineNum");
                        System.out.println(surroundingMineNum);
                        btnCells[rowSelected][colSelected].removeMouseListener(this);
                        btnCells[rowSelected][colSelected].setEnabled(false);
                        numRevealed++;
                    }
                }

            } else if (e.getButton() == MouseEvent.BUTTON3) { // right-button clicked
                // [TODO 6] If the location is flagged, remove the flag
                // Otherwise, plant a flag.
                //Remove flag
                if (flags[rowSelected][colSelected]) {
                    System.out.println("remove flag");
                    btnCells[rowSelected][colSelected].setIcon(null);
                    flags[rowSelected][colSelected] = false;
                } else { //Add flag
                    btnCells[rowSelected][colSelected].setIcon(flagImageIcon);
                    flags[rowSelected][colSelected] = true;
                }

            }
            //if you finish clicking on all the numbers, eg got 9 rows x 9 cols = 81 - 10 (mineCounts) = 71
            //so meaning you reveal all 71 empty cells so you won
            if (numRevealed == (rows * columns) - numMines) {
                JOptionPane.showMessageDialog(
                        null,
                        "Congratulations, You won!!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                //Don't let user click anymore coz he won already
                for (int row = 0; row < rows; ++row) {
                    for (int col = 0; col < columns; ++col) {
                        btnCells[row][col].removeMouseListener(this);
                        btnCells[row][col].setEnabled(false);
                    }
                }
            }
        }

        /**
         * *
         * recursive method to open all the blank location of the mines
         * the algorithm is called flood filled
         *
         * @param row
         * @param col
         */
        // Stackoverflow link : https://stackoverflow.com/questions/14077521/recursive-floodfill-overflowing
        private void revealBlanks(int row, int col) {
            int rows = gameDifficulty.getRow();
            int columns = gameDifficulty.getColumns();

            if (row < 0 || row >= rows || col < 0 || col >= columns) {
                return;
            }
            if (btnCells[row][col].getBackground() == BGCOLOR_REVEALED || btnCells[row][col].getForeground() == FGCOLOR_REVEALED) {
                btnCells[row][col].setIcon(null);
                return;
            }
            numRevealed++;
            btnCells[row][col].setIcon(null);
            btnCells[row][col].setForeground(FGCOLOR_REVEALED);
            btnCells[row][col].setBackground(BGCOLOR_REVEALED);
            btnCells[row][col].removeMouseListener(this);
            btnCells[row][col].setEnabled(false);
            int surroundingMineNum = calculateMineNumber(row, col);
            if (surroundingMineNum > 0) {
                btnCells[row][col].setText(String.valueOf(surroundingMineNum));
                return;
            }
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

        // this is to calculate the surrounding mine number //still need to read
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
        if (ae.getSource() == restartButton) {
            int rows = gameDifficulty.getRow();
            int columns = gameDifficulty.getColumns();
            int numMines = gameDifficulty.getMineCount();
            clearGame();
            initGame(rows, columns, numMines);
        }
    }
}

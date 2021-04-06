
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
 * Author: Karthick
 * tutorialflow.com
 */
class InitScreen implements ActionListener, MouseListener {

    private JFrame screen = null;
    private JButton smiley = new JButton("");
    private JPanel composite = new JPanel();
    private JPanel topPanel = new JPanel();

    ImageIcon smileyImageIcon = null;
    ImageIcon tImageIcon = null;
    ImageIcon pitImageIcon = null;
    ImageIcon lossImageIcon = null;
    ImageIcon cryImageIcon = null;
    ImageIcon oneImageIcon = null;
    ImageIcon twoImageIcon = null;
    ImageIcon threeImageIcon = null;
    ImageIcon fourImageIcon = null;
    ImageIcon fiveImageIcon = null;

    public InitScreen() {
        screen = new JFrame("MineSweeper by Karthick, TutorialFlow.com");
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setVisible(true);
        screen.setResizable(false);
        screen.setJMenuBar(createMenuBar());
        loadMinesweeperImages();

        composite.setLayout(new BorderLayout());
        smiley.setPreferredSize(new Dimension(25, 25));
        smiley.setIcon(smileyImageIcon);
        topPanel.add(smiley);
        composite.add(topPanel, BorderLayout.NORTH);
        smiley.addActionListener(this);
        smiley.addMouseListener(this);
        arrangeButtons();
        screen.add(composite);
        screen.pack();
    }

    public void loadMinesweeperImages() {
        smileyImageIcon = getScaledImage("C:\\Users\\Pyae Pyae\\Downloads\\images\\smiley.png");
        tImageIcon = getScaledImage("C:\\Users\\Pyae Pyae\\Downloads\\images\\t.png");
        pitImageIcon = getScaledImage("C:\\Users\\Pyae Pyae\\Downloads\\images\\pit.png");
        lossImageIcon = getScaledImage("C:\\Users\\Pyae Pyae\\Downloads\\images\\loss.png");
        cryImageIcon = getScaledImage("C:\\Users\\Pyae Pyae\\Downloads\\images\\cry.png");
        oneImageIcon = getScaledImage("C:\\Users\\Pyae Pyae\\Downloads\\images\\1.png");
        twoImageIcon = getScaledImage("C:\\Users\\Pyae Pyae\\Downloads\\images\\2.png");
        threeImageIcon = getScaledImage("C:\\Users\\Pyae Pyae\\Downloads\\images\\3.png");
        fourImageIcon = getScaledImage("C:\\Users\\Pyae Pyae\\Downloads\\images\\4.png");
        fiveImageIcon = getScaledImage("C:\\Users\\Pyae Pyae\\Downloads\\images\\5.png");
    }

    public JMenuBar createMenuBar() {

        JMenuBar mBar = new JMenuBar();
        JMenu game = new JMenu("Game");

        JMenu help = new JMenu("Help");

        final JMenuItem miNew = new JMenuItem("New");
        final JMenuItem miBeg = new JMenuItem("Beginner");
        final JMenuItem miInter = new JMenuItem("Intermediate");
        final JMenuItem miExp = new JMenuItem("Expert");
        final JMenuItem miExit = new JMenuItem("Exit");

        final JMenuItem about = new JMenuItem("About MineSweeper....");

        game.add(miNew);
        game.add(miBeg);
        game.add(miInter);
        game.add(miExp);
        game.add(miExit);

        help.add(about);

        ActionListener MENULSTNR = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (miNew == ae.getSource()) {
                    btnwdth = 10;
                    btnhgt = 10;
                    mines = 10;
                    reset();
                }
                if (miBeg == ae.getSource()) {
                    btnwdth = 12;
                    btnhgt = 12;
                    mines = 12;
                    reset();

                }
                if (miInter == ae.getSource()) {
                    btnwdth = 15;
                    btnhgt = 15;
                    mines = 50;
                    reset();

                }
                if (miExp == ae.getSource()) {
                    btnwdth = 24;
                    btnhgt = 30;
                    mines = 80;
                    reset();
                }
                if (miExit == ae.getSource()) {
                    if (screen != null) {
                        screen.dispose();
                    }
                    System.exit(0);

                }

                if (about == ae.getSource()) {
                    System.out.println(" Karthick");
                }
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

    private int btnwdth = 10;

    private int btnhgt = 10;

    private int mines = 8;

    int mineArray[][];

    JButton button[][];
    JPanel minespan = null;

    public void arrangeButtons() {
        mineArray = new int[btnwdth][btnhgt];
        button = new JButton[btnwdth][btnhgt];
        boolean starting = true;
        if (minespan != null) {
            composite.remove(minespan);
            minespan = null;
            starting = false;

        }
        minespan = new JPanel();
        minespan.setLayout(new GridLayout(btnwdth, btnhgt));

        for (int i = 0; i < btnwdth; i++) {
            for (int j = 0; j < btnhgt; j++) {
                mineArray[i][j] = 0;
                button[i][j] = new JButton("");
                button[i][j].setBackground(Color.WHITE);
                button[i][j].setPreferredSize(new Dimension(16, 16));
                button[i][j].addActionListener(this);
                button[i][j].addMouseListener(this);
                minespan.add(button[i][j]);
            }
        }

        minespan.setVisible(true);
        composite.add(minespan, BorderLayout.CENTER);
        if (starting) {
            minesFormat(button);
        }
        screen.pack();
    }

    public void reset() {
        smiley.setIcon(smileyImageIcon);
        // btnwdth+=1;
        arrangeButtons();
        for (int i = 0; i < btnwdth; i++) {
            for (int j = 0; j < btnhgt; j++) {
                mineArray[i][j] = 0;
                button[i][j].addActionListener(this);
                button[i][j].addMouseListener(this);
                button[i][j].setText("");
                button[i][j].setBackground(Color.WHITE);
                //button[i][j].setIcon(tImageIcon);
            }
        }
        minesFormat(button);
        System.out.println("");
        System.out.println("");
    }

    public void minesFormat(JButton button[][]) {
        int mine[] = getRndmNos(btnwdth, btnhgt, mines);
        int count = 1;
        for (int i = 0; i < btnwdth; i++) {
            for (int j = 0; j < btnhgt; j++)

            {

                for (int k = 0; k < mine.length && mine[k] != 0; k++) {
                    if (count == mine[k]) {
                        mineArray[i][j] = 9;
                    }
                }
                count++;
            }
        }

        int boxcount = 0;
        for (int i = 0; i < btnwdth; i++) {
            for (int j = 0; j < btnhgt; j++) {
                boxcount = 0;

                if (mineArray[i][j] != 9) {
                    if (i > 0 && j > 0) {
                        if (mineArray[i - 1][j - 1] == 9)
                            boxcount++;
                    }

                    if (i > 0) {
                        if (mineArray[i - 1][j] == 9)
                            boxcount++;
                    }

                    if (i > 0 && j < btnhgt - 1) {
                        if (mineArray[i - 1][j + 1] == 9)
                            boxcount++;
                    }

                    if (i < btnwdth - 1 && j > 0) {
                        if (mineArray[i + 1][j - 1] == 9)
                            boxcount++;
                    }
                    if (i < btnwdth - 1) {
                        if (mineArray[i + 1][j] == 9)
                            boxcount++;
                    }

                    if (i < btnwdth - 1 && j < btnhgt - 1) {
                        if (mineArray[i + 1][j + 1] == 9)
                            boxcount++;
                    }

                    if (j > 0) {
                        if (mineArray[i][j - 1] == 9)
                            boxcount++;
                    }
                    if (j < btnhgt - 1) {
                        if (mineArray[i][j + 1] == 9)
                            boxcount++;
                    }
                    mineArray[i][j] = boxcount;
                }
            }
        }

        for (int i = 0; i < btnwdth; i++) {
            for (int j = 0; j < btnhgt; j++) {
                System.out.print(" " + mineArray[i][j]);
            }
            System.out.println("");
        }

    }

    public int[] getRndmNos(int btnwdth, int btnhgt, int mines) {
        Random rand = new Random();
        int rndmines[] = new int[btnwdth * btnhgt];
        boolean in = false;
        int count = 0;
        while (count < mines) {
            int rndno = (int) ((btnwdth * btnhgt) * (rand.nextDouble())) + 1;
            in = false;
            for (int i = 0; i < count; i++) {
                if (rndmines[i] == rndno) {
                    in = true;
                    break;
                }
            }
            if (!in) {
                rndmines[count++] = rndno;
            }
        }
        return rndmines;
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == smiley) {
            reset();
        } else {
            for (int i = 0; i < btnwdth; i++)
                for (int j = 0; j < btnhgt; j++) {
                    if (button[i][j] == ae.getSource()) {
                        if (mineArray[i][j] == 9) {
                            for (int k = 0; k < btnwdth; k++) {
                                for (int l = 0; l < btnhgt; l++) {

                                    if (mineArray[k][l] == 9) {
                                        button[k][l].setIcon(pitImageIcon);
                                    }

                                    button[k][l].removeActionListener(this);
                                    button[k][l].removeMouseListener(this);

                                }
                            }

                        }
                        if (mineArray[i][j] == 1) {
                            button[i][j].setIcon(oneImageIcon);
                        }
                        if (mineArray[i][j] == 2) {
                            button[i][j].setIcon(twoImageIcon);
                        }
                        if (mineArray[i][j] == 3) {
                            button[i][j].setIcon(threeImageIcon);
                        }
                        if (mineArray[i][j] == 4) {
                            button[i][j].setIcon(fourImageIcon);
                        }
                        if (mineArray[i][j] == 5) {
                            button[i][j].setIcon(fiveImageIcon);
                        }
                        if (mineArray[i][j] == 0) {
                            findAllEmpty(i, j);
                        }
                    }
                }
        }
    }

    public void findAllEmpty(int boxX, int boxY) {
        int arrX[] = new int[(btnwdth) * (btnhgt)];
        int arrY[] = new int[(btnwdth) * (btnhgt)];
        int cntEmpty = 0;
        for (int i = 0; i < ((btnwdth) * (btnhgt)); i++) {
            arrX[i] = -1;
            arrY[i] = -1;
        }
        arrX[cntEmpty] = boxX;
        arrY[cntEmpty] = boxY;
        cntEmpty++;

        for (int i = 0; i < cntEmpty; i++) {
            if (arrX[i] > 0) {
                int xxX = arrX[i] - 1;
                int yyY = arrY[i];
                if (mineArray[xxX][yyY] == 0) {
                    if (!findIn(arrX, arrY, cntEmpty, xxX, yyY)) {
                        arrX[cntEmpty] = xxX;
                        arrY[cntEmpty] = yyY;
                        cntEmpty++;
                    }
                }
            }

            if (arrX[i] < (btnwdth - 1)) {
                int xxX = arrX[i] + 1;
                int yyY = arrY[i];
                if (mineArray[xxX][yyY] == 0) {
                    if (!findIn(arrX, arrY, cntEmpty, xxX, yyY)) {
                        arrX[cntEmpty] = xxX;
                        arrY[cntEmpty] = yyY;
                        cntEmpty++;
                    }
                }
            }

            if (arrY[i] > 0) {
                int xxX = arrX[i];
                int yyY = arrY[i] - 1;
                if (mineArray[xxX][yyY] == 0) {
                    if (!findIn(arrX, arrY, cntEmpty, xxX, yyY)) {
                        arrX[cntEmpty] = xxX;
                        arrY[cntEmpty] = yyY;
                        cntEmpty++;
                    }
                }
            }

            if (arrY[i] < (btnhgt - 1)) {
                int xxX = arrX[i];
                int yyY = arrY[i] + 1;
                if (mineArray[xxX][yyY] == 0) {
                    if (!findIn(arrX, arrY, cntEmpty, xxX, yyY)) {
                        arrX[cntEmpty] = xxX;
                        arrY[cntEmpty] = yyY;
                        cntEmpty++;
                    }
                }
            }
        }

        for (int k = 0; k < cntEmpty; k++) {
            button[arrX[k]][arrY[k]].setBackground(new Color(200, 200, 250));
        }

    }

    public boolean findIn(int[] arrX, int[] arrY, int cntEmpty, int xxX, int yyY) {
        int j = 0;
        for (j = 0; j < cntEmpty; j++) {
            if ((arrX[j] == (xxX)) && (arrY[j] == (yyY))) {
                return true;
            }
        }
        return false;
    }

    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void mousePressed(MouseEvent me) {
        for (int i = 0; i < btnwdth; i++)
            for (int j = 0; j < btnhgt; j++) {
                if (button[i][j] == me.getSource()) {
                    smiley.setIcon(cryImageIcon);
                }
            }

        if (me.getSource() == smiley) {
            smiley.setIcon(cryImageIcon);
        }
    }

    public void mouseReleased(MouseEvent me) {
        // TODO Auto-generated method stub
        if (me.getSource() == smiley) {
            smiley.setIcon(smileyImageIcon);
        }
        for (int i = 0; i < btnwdth; i++)
            for (int j = 0; j < btnhgt; j++) {
                if (button[i][j] == me.getSource()) {
                    if (mineArray[i][j] == 9) {
                        smiley.setIcon(lossImageIcon);
                    } else {
                        smiley.setIcon(smileyImageIcon);
                    }
                }

            }

    }

    public ImageIcon getScaledImage(String imageString) {
        ImageIcon imageIcon = new ImageIcon(imageString);
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);
        return imageIcon;
    }


    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }
}
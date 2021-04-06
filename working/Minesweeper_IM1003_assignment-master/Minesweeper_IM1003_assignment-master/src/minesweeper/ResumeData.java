/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.io.Serializable;

/**
 *
 * @author lolsp
 */
public class ResumeData implements Serializable{
    private boolean[][] mines;
    private boolean[][] flags;
    private boolean[][] btnOpened;
    private String[][] btnText;
    private int rows;
    private int cols;
    private int numMine;
    private int numRevealed;

    public ResumeData() {
    }

    public int getNumRevealed() {
        return numRevealed;
    }

    public void setNumRevealed(int numRevealed) {
        this.numRevealed = numRevealed;
    }

    public int getNumMine() {
        return numMine;
    }

    public void setNumMine(int numMine) {
        this.numMine = numMine;
    }

    public boolean[][] getMines() {
        return mines;
    }

    public void setMines(boolean[][] mines) {
        this.mines = mines;
    }

    public boolean[][] getFlags() {
        return flags;
    }

    public void setFlags(boolean[][] flags) {
        this.flags = flags;
    }

    public boolean[][] getBtnOpened() {
        return btnOpened;
    }

    public void setBtnOpened(boolean[][] btnOpened) {
        this.btnOpened = btnOpened;
    }

    public String[][] getBtnText() {
        return btnText;
    }

    public void setBtnText(String[][] btnText) {
        this.btnText = btnText;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int row) {
        this.rows = row;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int col) {
        this.cols = col;
    }
    
}

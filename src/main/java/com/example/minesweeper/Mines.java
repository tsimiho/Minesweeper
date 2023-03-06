package com.example.minesweeper;

import eu.hansolo.toolbox.tuples.Triplet;

import java.util.Random;
import java.util.*;

public class Mines {
    static public String[][] board;
    static public int totalToReveal;
    static public int tries;
    static public int activeMineCounter;
    static int winLose = -1;
    static private int height, width, numMines, numberOfFlags, hypermine, h, w;
    static private FileController fc = new FileController();
    private Random rand = new Random(); // Generate random numbers

    private ArrayList<Triplet> mineCoordinates = new ArrayList<Triplet>();

    public Mines(int height, int width, int numMines, int hypermine) {

        this.height = height;
        this.width = width;
        this.numberOfFlags = 0;
        this.tries = 0;
        this.numMines = numMines;
        this.hypermine = hypermine;
        this.activeMineCounter = numMines;

        totalToReveal = (height * width) - this.numMines;
        board = new String[height][width]; // Create game's board with nulls initialized
        for (int i = 0; i < height; i++) // Initialize all slots in the board
            for (int j = 0; j < width; j++)
                board[i][j] = "NF."; // Nothing ON slot, False none is revealed yet and . as default
        while (numMines != 0) {
            h = rand.nextInt(height - 1); // Generate random number for height
            w = rand.nextInt(width - 1); // Generate random number for width
            if (board[h][w].charAt(2) != 'M') { // Make sure place wasn't used before
                if (numMines == 1 && hypermine == 1) {
                    board[h][w] = board[h][w].substring(0, 2) + "H";
                    Triplet t = new Triplet(w, h, 1);
                    mineCoordinates.add(t);
                } else {
                    board[h][w] = board[h][w].substring(0, 2) + "M"; // Mark a place containing a mine with M
                    Triplet t = new Triplet(w, h, 0);
                    mineCoordinates.add(t);
                }
                numMines--;
            }
        }
        fc.WriteMines(mineCoordinates);
        for (int i = 0; i < height; i++) // Initialize slot values
            for (int j = 0; j < width; j++)
                board[i][j] = board[i][j].substring(0, 2) + get(i, j);
    }

    static public void setShowAll() { // Set all slots to be revealed when game is over (win\lose)
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (!board[i][j].contains("T")) {
                    board[i][j] = board[i][j].substring(0, 1) + "T" + board[i][j].substring(2, 3);
                }
    }

    static public boolean isDone() {
        if (totalToReveal != 0)
            return false;
        setShowAll();
        return true; // True if no more slots to reveal (won the game)
    }

    static private void checkPlace(int i, int j) { // Is it a legal move? (borders)
        if (i >= height || j >= width || i < 0 || j < 0)
            throw new IllegalArgumentException("Illegal move on board!");
    }

    static private boolean inBoard(int i, int j) { // Is move legal? (make move or not)
        if (i >= height || j >= width || i < 0 || j < 0)
            return false;
        return true;
    }

    static public boolean open(int i, int j, Boolean clicked) {
        checkPlace(i, j);
        if (clicked) {
            tries++;
        }
        if (board[i][j].charAt(1) == 'T') // Already revealed
            return true;
        if (board[i][j].charAt(0) == 'D')
            numberOfFlags--;
        if (board[i][j].charAt(2) == 'M' || board[i][j].charAt(2) == 'H') { // Slot contains a mine (user lost)
            board[i][j] = board[i][j].substring(0, 1) + "T" + "B"; // B for boom
            totalToReveal = 0; // Finish game
            isDone();
            winLose = 0;
//            recordResult(winLose);
            return true;
        }
        if (board[i][j].charAt(2) != 'E' && board[i][j].charAt(2) != 'M' && board[i][j].charAt(2) != 'B') {
            board[i][j] = board[i][j].substring(0, 1) + "T" + board[i][j].substring(2, 3);
            totalToReveal--;
            if (isDone()) {
                winLose = 1;
//                recordResult(winLose);
            }
            return true;
        }
        if (board[i][j].charAt(2) == 'E') {
            board[i][j] = board[i][j].substring(0, 1) + "T" + board[i][j].substring(2, 3);
            totalToReveal--;
            for (int r = -1; r < 2; r++) {
                for (int c = -1; c < 2; c++) {
                    if (!(r == 0 && c == 0) && inBoard(i + r, j + c)) {
                        if (isDone()) {
                            winLose = 1;
//                            recordResult(winLose);
                        }
                        open(i + r, j + c, false);
                    }
                }
            }
            return true;
        }
        return false;
    }

    static public void toggleFlag(int x, int y) {
        checkPlace(x, y);
        if (board[x][y].contains("T"))
            return;
        if (board[x][y].contains("N") && numberOfFlags < activeMineCounter) {
            if (board[x][y].charAt(2) == 'H' && tries <= 4) {
                board[x][y] = "NTH";
                for (int j = 0; j < height; j++) {
                    if (!board[x][j].contains("T") && j != y) {
                        if (board[x][j].charAt(2) == 'M') {
                            board[x][j] = board[x][j].substring(0, 1) + "TI";
                            activeMineCounter--;
                        } else if (j != y) {
                            open(x, j, false);
                        }

                    }
                }
                for (int j = 0; j < width; j++) {
                    if (!board[j][y].contains("T") && j != x) {
                        if (board[j][y].charAt(2) == 'M') {
                            board[j][y] = board[j][y].substring(0, 1) + "TI";
                        } else if (j != x) {
                            open(j, y, false);
                        }

                    }
                }
            } else {
                board[x][y] = "D" + board[x][y].substring(1, 3); // Overwrite it with flag mark (D)
                numberOfFlags++;
            }
        } else if (board[x][y].contains("D")) {
            board[x][y] = "N" + board[x][y].substring(1, 3); // Remove flag mark
            numberOfFlags--;
        }
    }

    static public int getNumberOfFlags() {
        return numberOfFlags;
    }

    static public void recordResult(int result, int time) { // result: 1 for win and 0 for loss
        fc.WriteResult(numMines, tries, time, result);
    }

    public int getCol() {
        return width;
    }

    public int getRow() {
        return height;
    }

    public String get(int i, int j) {
        checkPlace(i, j);
        Integer countMines = 0; // To use the object's toString method
        if (board[i][j].contains("F")) { // Slot not revealed
            if (board[i][j].contains("D")) // Slot has flag
                return "F"; // F for flag
            else if (board[i][j].contains("?")) // Slot is marked as question mark (unsure of slot's content)
                return "?";
        } // Remaining slots are revealed
        if (board[i][j].contains("M"))
            return "M";
        else if (board[i][j].contains("H")) {
            return "H";
        } else {
            for (int r = -1; r < 2; r++) {
                for (int c = -1; c < 2; c++) {
                    if (!(r == 0 && c == 0) && inBoard(i + r, j + c) && board[i + r][j + c].contains("M")) {
                        countMines++;
                        if (countMines > 8 || countMines < 0)
                            throw new IllegalArgumentException("Something went wrong - mines count is illegal");
                    }
                }
            }
        }
        if (countMines == 0)
            return "E";
        else
            return countMines.toString();
    }

    @Override
    public String toString() {
        String s = "";
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (board[r][c].charAt(1) == 'T')
                    s += board[r][c].substring(2, 3);
                else { // Meaning slot isn't revealed
                    if (board[r][c].contains("D") || board[r][c].contains("?"))
                        s += get(r, c);
                    else
                        s += "X";
                }
                if (c == width - 1) // New row
                    s += '\n';
            }
        }
        return s;
    }
}
package com.example.minesweeper.game.logic;

public enum Difficulty {
    // Minimum value for row and col is 3
    EASY(9, 9, 10), MEDIUM(12, 9, 15), HARD(15, 9, 20);
    private int row, col, mineProb;

    private Difficulty(int row, int col, int mineProb) {
        this.row = row;
        this.col = col;
        this.mineProb = mineProb;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getMineProb() { return mineProb; }
}

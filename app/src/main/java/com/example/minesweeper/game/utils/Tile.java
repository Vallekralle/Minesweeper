package com.example.minesweeper.game.utils;

public class Tile {
    private int x, y, number;
    private boolean isBomb;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;

        isBomb = false;
    }

    public void activateBomb() {
        isBomb = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

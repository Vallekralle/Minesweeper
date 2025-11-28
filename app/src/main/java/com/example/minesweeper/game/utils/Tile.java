package com.example.minesweeper.game.utils;

import android.graphics.RectF;

public class Tile {
    private int x, y, size, number;
    private boolean isBomb, hasFlag;

    public Tile(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;

        isBomb = false;
        hasFlag = false;
    }

    public void activateBomb() {
        isBomb = true;
    }

    public void switchFlag() {
        hasFlag = !hasFlag;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public RectF getRect() {
        return new RectF(x, y, x + size, y + size);
    }

    public boolean isBomb() {
        return isBomb;
    }

    public boolean hasFlag() {
        return hasFlag;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

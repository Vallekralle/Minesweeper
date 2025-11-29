package com.example.minesweeper.game.utils;

import android.graphics.RectF;

public class Tile {
    public static final float TEXT_SIZE = 60f;

    private final Index index;
    private final int x, y, size;
    private int number;
    private boolean isMine, hasFlag, isRevealed;

    public Tile(int x, int y, int size, Index index) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.index = index;

        isMine = hasFlag = isRevealed = false;
    }

    public void activateMine() {
        isMine = true;
    }

    public void reveal() {
        isRevealed = true;
    }

    public void switchFlag() {
        hasFlag = !hasFlag;
    }

    /**
    * GETTER
    * */

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getNumber() {
        return number;
    }

    public Index getIndex() {
        return index;
    }

    public RectF getRect() {
        return new RectF(x, y, x + size, y + size);
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean hasFlag() {
        return hasFlag;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    /**
    * SETTER
    * */

    public void setNumber(int number) {
        this.number = number;
    }
}

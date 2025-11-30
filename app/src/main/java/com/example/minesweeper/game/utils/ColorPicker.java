package com.example.minesweeper.game.utils;

import android.graphics.Color;

public enum ColorPicker {
    BLUE_1(Color.argb(255, 25, 130, 196)),
    GREEN_2(Color.argb(255, 138, 201, 38)),
    YELLOW_3(Color.argb(255, 255, 202, 58)),
    LIGHT_ORANGE_4(Color.argb(255, 255, 174, 67)),
    ORANGE_5(Color.argb(255, 255, 146, 76)),
    RED_6(Color.argb(255, 255, 89, 94)),
    VIOLET_7(Color.argb(255, 181, 83, 121)),
    PURPLE_8(Color.argb(255, 106, 76, 147));

    private int color;

    private ColorPicker(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}

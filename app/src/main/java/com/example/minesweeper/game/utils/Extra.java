package com.example.minesweeper.game.utils;

public enum Extra {
    DIFFICULTY("Difficulty");
    private String tag;

    private Extra(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}

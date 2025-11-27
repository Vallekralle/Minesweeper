package com.example.minesweeper.game.ui;

import com.example.minesweeper.R;

public enum ImageLoader {
    BACKGROUND_TILE(R.drawable.minesweeper_background_tile),
    COVER_TILE(R.drawable.minesweeper_cover_tile),
    FLAG_ACTIVATED(R.drawable.minesweeper_flag_activated),
    FLAG_DEACTIVATED(R.drawable.minesweeper_flag_deactivated),
    SMILEY_GOOD(R.drawable.minesweeper_smiley_good);

    private int id;

    private ImageLoader(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

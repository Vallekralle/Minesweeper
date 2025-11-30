package com.example.minesweeper.game.ui;

import com.example.minesweeper.R;

public enum ImageLoader {
    BACKGROUND_TILE(R.drawable.minesweeper_background_tile),
    COVER_TILE(R.drawable.minesweeper_cover_tile),
    MINE_TILE(R.drawable.minesweeper_mine),
    FLAG_ACTIVATED(R.drawable.minesweeper_flag_activated),
    FLAG_DEACTIVATED(R.drawable.minesweeper_flag_deactivated),
    FLAG_TILE(R.drawable.minesweeper_flag_tile),
    SMILEY_GOOD(R.drawable.minesweeper_smiley_good),
    SMILEY_BAD(R.drawable.minesweeper_smiley_bad);

    private int id;

    private ImageLoader(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

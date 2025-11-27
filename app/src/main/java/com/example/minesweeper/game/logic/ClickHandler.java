package com.example.minesweeper.game.logic;

import android.view.MotionEvent;

import com.example.minesweeper.game.ui.Field;
import com.example.minesweeper.game.utils.Tile;

import java.util.Objects;
import java.util.Random;

public class ClickHandler {
    private Field field;
    private Random random;
    private Tile[][] tileField;

    private int mineProb, squareSize;
    private boolean bombsPlaced;

    public ClickHandler(Field field, int mineProb) {
        this.field = field;
        this.mineProb = mineProb;

        init();
    }

    private void init() {
        tileField = field.getTileField();
        squareSize = field.getSquareSize();
        random = new Random();
        bombsPlaced = false;
    }

    public void handleEvent(MotionEvent event) {
        Tile clickedTile = searchClickedTile((int) event.getX(), (int) event.getY());

        if(!bombsPlaced && clickedTile != null) {
            placeMines(clickedTile);
            calculateTileNumbers();
        }
    }

    private Tile searchClickedTile(int clickX, int clickY) {
        for(Tile[] tileCol : tileField) {
            for(Tile tile : tileCol) {
                if(tile.getX() <= clickX && tile.getX() + squareSize >= clickX
                        && tile.getY() <= clickY && tile.getY() + squareSize >= clickY) {
                    return tile;
                }
            }
        }
        return null;
    }

    private void placeMines(Tile clickedTile) {
        Objects.requireNonNull(tileField);
        Objects.requireNonNull(random);

        for(Tile[] tileCol : tileField) {
            for(Tile tile : tileCol) {
                int prob = random.nextInt(101);
                if(tile != clickedTile && prob <= mineProb) {
                    tile.activateBomb();
                }
            }
        }

        bombsPlaced = true;
    }

    private void calculateTileNumbers() {
        for(int row = 0; row < tileField.length; row++) {
            for(int col = 0; col < tileField[row].length; col++) {
                tileField[row][col].setNumber(countNeighbors(row, col));
            }
        }
    }

    private int countNeighbors(int row, int col) {
        // TODO: iterate through all neighbors and check if they are a bomb
        return 0;
    }
}

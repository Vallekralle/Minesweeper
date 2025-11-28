package com.example.minesweeper.game.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.minesweeper.game.main.Game;
import com.example.minesweeper.game.ui.Field;
import com.example.minesweeper.game.ui.ImageLoader;
import com.example.minesweeper.game.utils.Tile;

import java.util.Objects;
import java.util.Random;

public class TouchHandler {
    private Bitmap flagActivatedBitmap, flagDeactivatedBitmap;
    private Game game;
    private Field field;
    private Random random;
    private Tile[][] tileField;

    private int mineProb, squareSize, mineCount;
    private boolean minesPlaced, placeFlagMode;

    public TouchHandler(Game game, Field field, int mineProb) {
        this.game = game;
        this.field = field;
        this.mineProb = mineProb;

        init();
    }

    private void init() {
        flagActivatedBitmap = BitmapFactory.decodeResource(
                game.getContext().getResources(), ImageLoader.FLAG_ACTIVATED.getId()
        );
        flagDeactivatedBitmap = BitmapFactory.decodeResource(
                game.getContext().getResources(), ImageLoader.FLAG_DEACTIVATED.getId()
        );

        random = new Random();
        tileField = field.getTileField();
        squareSize = field.getSquareSize();
        minesPlaced = false;
        placeFlagMode = false;
        mineCount = 0;
    }

    /**
    * EVENTS
    * */

    public boolean handleFieldEvent(View view, MotionEvent event) {
        if(event.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }

        Tile clickedTile = searchClickedTile((int) event.getX(), (int) event.getY());

        if(clickedTile == null) {
            return false;
        }

        if(!minesPlaced) {
            placeMines(clickedTile);
            calculateTileNumbers();
        } else if(placeFlagMode) {
            checkFlagState(clickedTile);
        }
        // TODO: Reveal the tile info
        return true;
    }

    public boolean handleFlagEvent(View view, MotionEvent event) {
        if(event.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        if(!minesPlaced) {
            Toast.makeText(view.getContext(), "Reveal at least one tile!", Toast.LENGTH_SHORT).show();
            return false;
        }
        changeFlagMode(view);
        return true;
    }

    public boolean handleSmileyEvent(View view, MotionEvent event) {
        if(event.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        return true;
    }

    /**
    * HELP METHODS
    *  */

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

    /**
    * MINES
    * */

    private void placeMines(Tile clickedTile) {
        Objects.requireNonNull(tileField);
        Objects.requireNonNull(random);

        for(Tile[] tileCol : tileField) {
            for(Tile tile : tileCol) {
                int prob = random.nextInt(101);
                if(tile != clickedTile && prob <= mineProb) {
                    tile.activateBomb();
                    mineCount++;
                }
            }
        }

        minesPlaced = true;
    }

    /**
    * TILE-NUMBERS
    * */

    private void calculateTileNumbers() {
        for(int row = 0; row < tileField.length; row++) {
            for(int col = 0; col < tileField[row].length; col++) {
                tileField[row][col].setNumber(countNeighbors(row, col));
            }
        }
    }

    private int countNeighbors(int tileRow, int tileCol) {
        int count = 0;

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                // Calculating the index for the neighbors
                int rowInd = tileRow + i - 1;
                int colInd = tileCol + j - 1;

                // Preventing IndexOutOfBounceException
                if(rowInd < 0 || rowInd >= tileField.length || colInd < 0 || colInd >= tileField[i].length) {
                    continue;
                }

                // Preventing counting own tile
                if(rowInd == tileRow && colInd == tileCol) {
                    continue;
                }

                // Increment count after checking for bomb
                if(tileField[rowInd][colInd].isBomb()) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
    * FLAGS
    * */

    private void changeFlagMode(View view) {
        placeFlagMode = !placeFlagMode;

        if(placeFlagMode) {
            game.replaceImg((ImageButton) view, ImageLoader.FLAG_ACTIVATED);
        } else {
            game.replaceImg((ImageButton) view, ImageLoader.FLAG_DEACTIVATED);
        }
    }

    private void checkFlagState(Tile tile) {
        if (tile.hasFlag()) {
            field.drawImg(flagActivatedBitmap, tile.getRect());
        } else {
            field.drawImg(flagDeactivatedBitmap, tile.getRect());
        }

        tile.switchFlag();
    }
}

package com.example.minesweeper.game.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.minesweeper.game.main.Game;
import com.example.minesweeper.game.ui.Field;
import com.example.minesweeper.game.ui.ImageLoader;
import com.example.minesweeper.game.utils.Index;
import com.example.minesweeper.game.utils.Tile;

import java.util.Objects;
import java.util.Random;

public class TouchHandler {
    private Bitmap coverTileBitmap, flagDeactivatedBitmap, backgroundTileBitmap, mineBitmap;
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
        coverTileBitmap = BitmapFactory.decodeResource(
                game.getContext().getResources(), ImageLoader.COVER_TILE.getId()
        );
        flagDeactivatedBitmap = BitmapFactory.decodeResource(
                game.getContext().getResources(), ImageLoader.FLAG_DEACTIVATED.getId()
        );
        backgroundTileBitmap = BitmapFactory.decodeResource(
                game.getContext().getResources(), ImageLoader.BACKGROUND_TILE.getId()
        );
        mineBitmap = BitmapFactory.decodeResource(
                game.getContext().getResources(), ImageLoader.MINE_TILE.getId()
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

        if(clickedTile == null || clickedTile.isRevealed()) {
            return false;
        }

        if(!minesPlaced) {
            placeMines(clickedTile);
            calculateTileNumbers();
        } else if(placeFlagMode) {
            checkFlagState(clickedTile);
            return true;
        }
        revealTile(clickedTile);
        checkGameState(clickedTile);
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
                    tile.activateMine();
                    mineCount++;
                }
            }
        }

        minesPlaced = true;
    }

    /**
    * TILE NUMBERS
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
                if(tileField[rowInd][colInd].isMine()) {
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
            field.drawImg(coverTileBitmap, tile.getRect());
        } else {
            field.drawImg(flagDeactivatedBitmap, tile.getRect());
        }

        tile.switchFlag();
    }

    /**
    * REVEALING TILE
    * */

    private void revealTile(Tile tile) {
        if(tile.isRevealed()) {
            return;
        }
        tile.reveal();

        if(tile.isMine()) {
            field.drawImg(mineBitmap, tile.getRect());
        } else {
            field.drawImg(backgroundTileBitmap, tile.getRect());

            int num = tile.getNumber();

            if(num != 0) {
                placeNumber(tile, num);
            } else {
                revealEmptyNeighbors(tile);
            }
        }
    }

    private void placeNumber(Tile tile, int num) {
        Paint paint = new Paint();
        paint.setColor(selectColor(num));
        paint.setTextSize(Tile.TEXT_SIZE);
        paint.measureText(String.valueOf(num));

        field.drawCenterNumber(tile, paint);
    }

    private int selectColor(int num) {
        int color = Color.argb(255, 0, 0, 0);

        // TODO: Change colors and use enum for that
        color = switch (num) {
            case 1 -> Color.argb(255, 255, 0, 0);
            case 2 -> Color.argb(255, 0, 255, 0);
            case 3 -> Color.argb(255, 0, 0, 255);
            case 4 -> Color.argb(255, 0, 50, 0);
            case 5 -> Color.argb(255, 100, 0, 0);
            case 6 -> Color.argb(255, 0, 100, 0);
            case 7 -> Color.argb(255, 0, 0, 100);
            case 8 -> Color.argb(255, 50, 50, 50);
            default -> color;
        };

        return color;
    }

    private void revealEmptyNeighbors(Tile tile) {
        /*
         * TODO: Refactor with countNeighbors()-method
         * */

        Index tileInd = tile.getIndex();
        int row = tileInd.row();
        int col = tileInd.col();

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                // Calculating the index for the neighbors
                int rowInd = row + i - 1;
                int colInd = col + j - 1;

                // Preventing IndexOutOfBounceException
                if(rowInd < 0 || rowInd >= tileField.length || colInd < 0 || colInd >= tileField[i].length) {
                    continue;
                }

                // Preventing counting own tile
                if(rowInd == row && colInd == col) {
                    continue;
                }

                // Reveal all neighbor tiles
                Tile neighborTile = tileField[rowInd][colInd];
                revealTile(neighborTile);
            }
        }
    }
    
    /**
    * GAME END
    * */
    
    private void checkGameState(Tile tile) {
        if(tile.isMine()) {
            // TODO: Implement an end screen and change smiley face
            Toast.makeText(game.getContext(), "You have lost!", Toast.LENGTH_SHORT).show();
        }
        // TODO: Check if the player won
    }
}

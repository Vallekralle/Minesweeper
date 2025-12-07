package com.example.minesweeper.game.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.minesweeper.game.logic.Difficulty;
import com.example.minesweeper.game.utils.Index;
import com.example.minesweeper.game.utils.Tile;

public class Field {
    public static final int PADDING = 16;

    private final Context context;
    private final Canvas canvas;
    private Bitmap bitmap;

    private final int winWidth, winHeight, row, col;
    private int squareSize, startPosX, startPosY;
    private Tile[][] tileField;

    public Field(Context context, Canvas canvas, Difficulty diff) {
        this.context = context;
        this.canvas = canvas;
        this.winWidth = canvas.getWidth();
        this.winHeight = canvas.getHeight();
        this.row = diff.getRow();
        this.col = diff.getCol();

        initBitmap();
        calculateSquareSize();
        createFields();
    }

    private void initBitmap() {
        bitmap = BitmapFactory.decodeResource(
                context.getResources(), ImageLoader.COVER_TILE.getId()
        );
    }

    private void calculateSquareSize() {
        int colSize = (winWidth - PADDING * 2) / col;
        int rowSize = (winHeight - PADDING * 2) / row;

        // Calculation to center on the vertical or horizontal axis
        if(colSize <= rowSize) {
            squareSize = colSize;
            startPosX = PADDING;
            startPosY = Math.abs((winHeight - squareSize * row) / 2);
        } else {
            squareSize = rowSize;
            startPosY = PADDING;
            startPosX = Math.abs((winWidth - squareSize * col) / 2);
        }
    }

    private void createFields() {
        tileField = new Tile[row][col];
        drawCoverField();
    }

    private void drawCoverField() {
        int y = startPosY;

        for(int i = 0; i < row; i++) {
            int x = startPosX;

            for(int j = 0; j < col; j++) {
                tileField[i][j] = new Tile(x, y, squareSize, new Index(i, j));
                drawImg(bitmap, tileField[i][j].getRect());
                x += squareSize;
            }
            y += squareSize;
        }
    }

    /**
    * CANVAS DRAWING
    * */

    public void drawImg(Bitmap bitmap, RectF dst) {
        canvas.drawBitmap(bitmap, null, dst, null);
    }

    public void drawCenterNumber(Tile tile, Paint paint) {
        String num = String.valueOf(tile.getNumber());
        Rect bounds = new Rect();
        paint.getTextBounds(num, 0, num.length(), bounds);

        int x = tile.getX() + squareSize / 2 - bounds.width() / 2;
        int y = tile.getY() + squareSize / 2 + bounds.height() / 2;

        canvas.drawText(String.valueOf(tile.getNumber()), x, y, paint);
    }

    /**
    * GETTER
    * */

    public Tile[][] getTileField() {
        return tileField;
    }

    public int getSquareSize() {
        return squareSize;
    }
}

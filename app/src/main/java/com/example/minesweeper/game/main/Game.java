package com.example.minesweeper.game.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minesweeper.R;
import com.example.minesweeper.game.logic.ClickHandler;
import com.example.minesweeper.game.logic.Clock;
import com.example.minesweeper.game.logic.Difficulty;
import com.example.minesweeper.game.ui.Field;

import java.util.Objects;

public class Game {
    private Difficulty diff;
    private Context context;

    private Bitmap bitmap;
    private Canvas canvas;
    private ImageView imgViewField;
    private TextView textClock;

    private Field field;
    private Clock clock;
    private ClickHandler clickHandler;

    public Game(ImageView imgViewField, Difficulty diff, Context context) {
        this.diff = diff;
        this.context = context;
        Objects.requireNonNull(diff);
        Objects.requireNonNull(context);

        // Initialize image view and start thread to configure the canvas
        this.imgViewField = imgViewField;
        this.imgViewField.post(this::configureCanvas);
    }

    private void configureCanvas() {
        bitmap = Bitmap.createBitmap(
                imgViewField.getWidth(),
                imgViewField.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        Objects.requireNonNull(bitmap);
        imgViewField.setImageBitmap(bitmap);

        canvas = new Canvas(bitmap);
        Objects.requireNonNull(canvas);

        start();
    }

    private void start() {
        textClock = ((Activity)context).findViewById(R.id.textClock);

        field = new Field(context, canvas, diff);
        clock = new Clock(context, textClock);

        // Handle click events on the image view
        Objects.requireNonNull(field);
        clickHandler = new ClickHandler(field, diff.getMineProb());
        this.imgViewField.setOnTouchListener(this::onTouch);

        /*
        * Starting a new thread for the clock, which can be
        * interrupted without disturbing other threads.
        * */
        Objects.requireNonNull(clock);
        new Thread(clock).start();
    }

    public boolean onTouch(View view, MotionEvent event) {
        Objects.requireNonNull(clickHandler);
        clickHandler.handleEvent(event);
        return true;
    }
}

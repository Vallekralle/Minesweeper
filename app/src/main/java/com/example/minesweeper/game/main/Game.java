package com.example.minesweeper.game.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minesweeper.R;
import com.example.minesweeper.game.logic.TouchHandler;
import com.example.minesweeper.game.logic.Clock;
import com.example.minesweeper.game.logic.Difficulty;
import com.example.minesweeper.game.ui.Field;
import com.example.minesweeper.game.ui.ImageLoader;

import java.util.Objects;

public class Game {
    private Difficulty diff;
    private Context context;

    private Bitmap bitmap;
    private Canvas canvas;
    private ImageView imgViewField;
    private TextView textClock;
    private ImageButton imgBtnFlag, imgBtnSmiley;

    private Field field;
    private Clock clock;
    private TouchHandler touchHandler;

    public Game(Context context, Difficulty diff) {
        this.diff = diff;
        this.context = context;
        Objects.requireNonNull(diff);
        Objects.requireNonNull(context);

        // Initialize views and start thread to configure the canvas
        initViews();
        imgViewField.post(this::configureCanvas);
    }

    private void initViews() {
        imgViewField = ((Activity) context).findViewById(R.id.imgViewField);
        imgBtnFlag = ((Activity) context).findViewById(R.id.imgBtnFlag);
        imgBtnSmiley = ((Activity) context).findViewById(R.id.imgBtnSmiley);
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

        // Handle touch events on the image view
        handleTouchEvents();

        /*
        * Starting a new thread for the clock, which can be
        * interrupted without disturbing other threads.
        * */
        Objects.requireNonNull(clock);
        new Thread(clock).start();
    }

    private void handleTouchEvents() {
        Objects.requireNonNull(field);
        touchHandler = new TouchHandler(this, field, diff.getMineProb());

        // Handle field interactions
        imgViewField.setOnTouchListener(touchHandler::handleFieldEvent);

        // Handle flag interactions
        imgBtnFlag.setOnTouchListener(touchHandler::handleFlagEvent);

        // Handle smiley interactions
        imgBtnSmiley.setOnTouchListener(touchHandler::handleSmileyEvent);
    }

    public void replaceImg(ImageButton imgBtn, ImageLoader newImg) {
        imgBtn.setImageResource(newImg.getId());
    }
}

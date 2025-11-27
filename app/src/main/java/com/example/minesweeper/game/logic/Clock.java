package com.example.minesweeper.game.logic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import java.util.Objects;

public class Clock implements Runnable {
    private Context context;
    private TextView text;

    private int seconds, minutes, remainingTimeInSec;
    private boolean isRed;

    public Clock(Context context, TextView text) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(text);
        this.context = context;
        this.text = text;
        isRed = false;

        remainingTimeInSec = 300;
        calculateRemainingTime();
    }

    private void calculateRemainingTime() {
        seconds = remainingTimeInSec % 60;
        minutes = (remainingTimeInSec - seconds) / 60;
        /*
        * The context is used to update the ui of the clock,
        * no other thread will be using this resource.
        * */
        ((Activity) context).runOnUiThread(this::display);
    }

    private void display() {
        // This code only runs on the ui thread
        text.setText(
                String.format("%s:%s", appendZero(minutes), appendZero(seconds))
        );
    }

    private String appendZero(int number) {
        if(number < 10) {
            return String.format("0%s", number);
        }
        return String.valueOf(number);
    }

    private void changeColor() {
        if(!isRed && remainingTimeInSec <= 60) {
            ((Activity) context).runOnUiThread(() -> text.setTextColor(Color.RED));
            isRed = true;
        }
    }

    @Override
    public void run() {
        while(remainingTimeInSec >= 0) {
            try {
                calculateRemainingTime();
                changeColor();
                Thread.sleep(1000);
                remainingTimeInSec--;
            } catch(InterruptedException e) {
                Log.e(
                        "CLOCK_THREAD",
                        String.format("Der Thread der Uhr wurde unterbrochen: %s", e.getMessage())
                );
            }
        }
    }
}

package com.example.minesweeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minesweeper.R;
import com.example.minesweeper.game.logic.Difficulty;
import com.example.minesweeper.game.main.Game;
import com.example.minesweeper.game.utils.Extra;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    private Game game;
    private Difficulty diff;

    private Bundle bundle;

    private Button btnBackGame;
    private ImageView imgViewField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        // Init the back button
        btnBackGame = findViewById(R.id.btnBackGame);
        btnBackGame.setOnClickListener(view -> {
            returnToMenu();
        });

        // Saving info about the display
        imgViewField = findViewById(R.id.imgViewField);
        Objects.requireNonNull(imgViewField);

        // Getting extras form the last Activity, which started this one
        bundle = getIntent().getExtras();
        Objects.requireNonNull(bundle);

        startGame();
    }

    private void returnToMenu() {
        Intent menu = new Intent(GameActivity.this, MainActivity.class);
        startActivity(menu);
        finish();
    }

    private void startGame() {
        String value = bundle.getString(Extra.DIFFICULTY.getTag());
        Objects.requireNonNull(value);
        selectDiff(value);
        
        game = new Game(this, diff);
    }

    private void selectDiff(String value) {
        if(value.equals(getString(R.string.easy))) {
            diff = Difficulty.EASY;
        }
        else if (value.equals(getString(R.string.medium))) {
            diff = Difficulty.MEDIUM;
        }
        else if (value.equals(getString(R.string.hard))) {
            diff = Difficulty.HARD;
        }
    }
}
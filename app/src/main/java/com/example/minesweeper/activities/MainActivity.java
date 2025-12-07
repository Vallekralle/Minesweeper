package com.example.minesweeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minesweeper.R;
import com.example.minesweeper.game.utils.Extra;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Button btnPlay;
    private RadioGroup radioGroupDiff;
    private Intent gameActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);
        radioGroupDiff = findViewById(R.id.radioGroupDiff);

        btnPlay.setOnClickListener(view -> {
            startGameActivity();
        });
    }

    private void startGameActivity() {
        gameActivity = new Intent(MainActivity.this, GameActivity.class);
        gameActivity.putExtra(Extra.DIFFICULTY.getTag(), extractDiffInfo());
        startActivity(gameActivity);
        finish();
    }

    private String extractDiffInfo() {
        RadioButton checkedBtn = findViewById(radioGroupDiff.getCheckedRadioButtonId());
        return checkedBtn.getText().toString();
    }
}
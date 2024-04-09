package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Retrieve score from intent
        int score = getIntent().getIntExtra("SCORE", 0);

        // Display score
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("Your Score: " + score);

        // Check if score is 10 out of 10
        TextView messageTextView = findViewById(R.id.messageTextView);
        if (score >= 8) {
            messageTextView.setText("Congratulations! You did an excellent job!");
        } else if (score >= 5) {
            messageTextView.setText(" Keep up!..  good work!");
        } else {
            messageTextView.setText(" Keep practicing and try again!");
        }
    }
}
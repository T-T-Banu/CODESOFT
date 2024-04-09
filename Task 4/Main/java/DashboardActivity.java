package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        CardView cardView = findViewById(R.id.webDevelopment);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace NewActivity with the name of your new activity
                Intent intent = new Intent(DashboardActivity.this, WebActivity.class);
                startActivity(intent);
            }
        });

        CardView cardViewApp = findViewById(R.id.appDevelopment);
        cardViewApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace NewActivity with the name of your new activity
                Intent intent = new Intent(DashboardActivity.this, AppActivity.class);
                startActivity(intent);
            }
        });
    }
}
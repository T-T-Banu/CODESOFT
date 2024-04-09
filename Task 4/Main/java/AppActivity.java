package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AppActivity extends AppCompatActivity {

    private TextView questionTextView, scoreTextView;
    private RadioGroup optionsRadioGroup;
    private RadioButton option1RadioButton, option2RadioButton, option3RadioButton, option4RadioButton;
    private Button nextButton;

    private String[] questions = {"1.Which programming language is primarily used for Android app development in Android Studio?",
            "2. What is the name of the layout file where you define the UI elements of an Android app?",
            "3. Which file is used to specify the permissions required by an Android app?",
            "4. What is the purpose of an Intent in Android development?",
            "5. Which component is responsible for managing the app's user interface and interaction with the user?",
            "6. Which command is used to compile and run an Android app in Android Studio?",
            "7.Which database solution is commonly used for local data storage in Android apps?",
            "8.What is the purpose of Gradle in Android Studio?",
            "9.What tool is used for debugging Android apps in Android Studio",
            "10.Which folder contains the Java source code files of an Android app in Android Studio?"};
    private String[][] options = {{"Java", "Python", "C++", "Swift"},
            {" activity_main.xml", " layout.xml", "ui.xml", "main.xml"},
            {"AndroidManifest.xml", "permissions.xml", " app_permissions.xml", "AndroidPermissions.xml"},
            {"To define the user interface of an activity", "To pass data between activities or communicate with system components", "To manage the application lifecycle", "To handle user input events"},
            {"Activity", "Fragment", "services", "BroadcastReceiver"},
            {"build", "run", "compile", "debug"},
            {"MYSQL", "MangoDB", "SQLite", "PostgreSQL"},
            {"To design UI layouts", "To manage project dependencies and build automation", " To debug code", " To write unit tests"},
            {"Debugger", "Logcat", "Profiler", "Inspector"},
            {"res", "assests", "libs", "src"}};
    private int[] answers = {0, 0, 0, 2, 0, 1, 2, 1, 1, 3};
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        questionTextView = findViewById(R.id.questionTextView);
        //scoreTextView = findViewById(R.id.scoreTextView);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        option1RadioButton = findViewById(R.id.option1RadioButton);
        option2RadioButton = findViewById(R.id.option2RadioButton);
        option3RadioButton = findViewById(R.id.option3RadioButton);
        option4RadioButton = findViewById(R.id.option4RadioButton);
        nextButton = findViewById(R.id.nextButton);

        loadQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(); // this line to update score before loading next question or submitting
                if (currentQuestionIndex < questions.length - 1) {
                    loadNextQuestion();
                } else {
                    submitQuiz();
                }
            }


        });
    }

    private void loadQuestion() {
        questionTextView.setText(questions[currentQuestionIndex]);
        option1RadioButton.setText(options[currentQuestionIndex][0]);
        option2RadioButton.setText(options[currentQuestionIndex][1]);
        option3RadioButton.setText(options[currentQuestionIndex][2]);
        option4RadioButton.setText(options[currentQuestionIndex][3]);
    }

    private void loadNextQuestion() {
        currentQuestionIndex++;
        loadQuestion();
        optionsRadioGroup.clearCheck();
        updateButtonText();
    }

    private void updateButtonText() {
        if (currentQuestionIndex == questions.length - 1) {
            nextButton.setText("Submit");
        } else {
            nextButton.setText("Next");
        }
    }


    private void submitQuiz() {
        // Navigate to ScoreActivity and pass the score
        Intent intent = new Intent(AppActivity.this, ScoreActivity.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
        finish(); //  going back to it from ScoreActivity
    }

    private void checkAnswer() {
        int selectedOptionId = optionsRadioGroup.getCheckedRadioButtonId();
        if (selectedOptionId != -1) {
            RadioButton selectedOption = findViewById(selectedOptionId);
            int selectedAnswerIndex = optionsRadioGroup.indexOfChild(selectedOption);
            if (selectedAnswerIndex == answers[currentQuestionIndex]) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                score++;
            } else {
                Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select an option!", Toast.LENGTH_SHORT).show();
        }
    }
}

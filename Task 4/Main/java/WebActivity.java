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

public class WebActivity extends AppCompatActivity {
    private TextView questionTextView, scoreTextView;
    private RadioGroup optionsRadioGroup;
    private RadioButton option1RadioButton, option2RadioButton, option3RadioButton, option4RadioButton;
    private Button nextButton;

    private String[] questions = {"1.What does HTML stand for?",
            "2. What does AJAX stand for?",
            "3. What does CSS stand for?",
            "4. Which CSS property is used to control the spacing between individual letters?",
            "5. Which of the following is NOT a valid HTTP status code?",
            "6. Which of the following is NOT a valid method for creating JavaScript arrays?",
            "7.What does the \"src\" attribute in an HTML <img> tag define?",
            "8.What does the CSS property \"display: none;\" do?",
            "9.Which of the following is NOT a valid way to define a color in CSS?",
            "10. What is the purpose of the \"viewport\" meta tag in HTML?"};
    private String[][] options = {{"Hyper Text Markup Language", "Hyperlinks and Text Markup Language", "Hyper Transfer Markup Language", "Home Tool Markup Language"},
            {"Asynchronous JavaScript and XML", "Asynchronous JSON and XML", "Asynchronous JavaScript and XHTML", "Asynchronous JSON and XHTML"},
            {"Computer Style Sheets", "Creative Style Sheets", "Cascading Style Sheets", "Colorful Style Sheets"},
            {"letter-spacing", "word-spacing", "  line-height", "text-spacing"},
            {"200 OK", "404 Not Found", "500 Internal Server Error", "303 Redirect"},
            {"var arr = []", "var arr = new Array()", "var arr = {1, 2, 3}", "var arr = [1, 2, 3]"},
            {"The size of the image", "The type of image", "The source of the image", "The style of the image"},
            {"It makes the element invisible but still takes up space", "It removes the element from the document flow entirely", "It collapses the element vertically", " It changes the display mode to inline-block"},
            {"rgb(255, 0, 0)", "#ff0000", "color: red;", "hsl(0, 100%, 50%)"},
            {"To specify the character encoding of the document", "To control the appearance and behavior of the browser's viewport", "To define the structure of the document", "To specify the location of external resources"}};
    private int[] answers = {0, 0, 2, 0, 3, 2, 2, 1, 2, 1};
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

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
                checkAnswer(); // Add this line to update score before loading next question or submitting
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
        Intent intent = new Intent(WebActivity.this, ScoreActivity.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
        finish(); // Optional: Finish current activity to prevent going back to it from ScoreActivity
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
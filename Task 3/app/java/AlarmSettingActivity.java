package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class AlarmSettingActivity extends AppCompatActivity {
    TimePicker timePicker;
    Button setAlarmButton;
    Uri selectedRingtoneUri;
    Button chooseToneButton;
    TextView selectedToneTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        timePicker = findViewById(R.id.timePicker);
        setAlarmButton = findViewById(R.id.setAlarmButton);
        chooseToneButton = findViewById(R.id.chooseToneButton);
        selectedToneTextView = findViewById(R.id.selectedToneTextView);

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected hour and minute from TimePicker
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                setAlarm(timePicker);

                // Create a Calendar object and set it to the selected time
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                // Format the time
                String selectedTime = String.format("%02d:%02d", hour, minute);

                // Store the selected time in SharedPreferences
                SharedPreferences preferences = getSharedPreferences("AlarmPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("alarm1_time", selectedTime);
                editor.putBoolean("alarm1_enabled", true); // Assuming the alarm is enabled by default
                // Increment the number of alarms
                int numAlarms = preferences.getInt("num_alarms", 0);
                editor.putInt("num_alarms", numAlarms + 1);
                editor.apply();

                // Navigate back to AlarmManagement activity
                Intent intent = new Intent(AlarmSettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                ;
            }
        });

        chooseToneButton.setOnClickListener(v -> openRingtonePicker());
    }

    private void setAlarm(TimePicker timePicker) {
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        // Create a Calendar object and set it to the selected time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Format the time
        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

        // Store the alarm details in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("AlarmPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Get the current number of alarms
        int numAlarms = preferences.getInt("num_alarms", 0);

        // Increment the number of alarms
        numAlarms++;

        // Store the new alarm details
        editor.putString("alarm" + numAlarms + "_time", selectedTime);
        editor.putBoolean("alarm" + numAlarms + "_enabled", true); // Assuming the alarm is enabled by default
        editor.putString("alarm" + numAlarms + "_ringtone_uri", selectedRingtoneUri.toString());
        editor.putInt("num_alarms", numAlarms);
        editor.apply();

        // Schedule the alarm using AlarmManager
        AlarmHelper.setAlarm(this, calendar, selectedRingtoneUri);

        // Notify the user that the alarm is set
        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show();
    }

    private void openRingtonePicker() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, selectedRingtoneUri);
        startActivityForResult(intent, 0);
    }

    private void storeAlarmTime(int hour, int minute) {
        SharedPreferences preferences = getSharedPreferences("AlarmPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("alarm_hour", hour);
        editor.putInt("alarm_minute", minute);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (pickedUri != null) {
                selectedRingtoneUri = pickedUri;
                Ringtone ringtone = RingtoneManager.getRingtone(this, selectedRingtoneUri);
                if (ringtone != null) {
                    String ringtoneTitle = ringtone.getTitle(this);
                    selectedToneTextView.setText("Tone: " + ringtoneTitle);
                } else {
                    selectedToneTextView.setText("Tone: Unknown");
                }
            } else {
                selectedRingtoneUri = null;
                selectedToneTextView.setText("Tone: None");
            }
        }
    }
}
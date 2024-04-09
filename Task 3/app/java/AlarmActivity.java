package com.example.demo;



import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AlarmActivity extends Activity {
    private TextView alarm_state;
    private Button alarm_off;
    private Button alarm_dismiss;
    private AlarmManager alarm_manager;
    private PendingIntent pending_intent;
    private Intent my_intent;
    private long sound_select;
    private Uri selectedRingtoneUri;

    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // Initialize views
        alarm_state = findViewById(R.id.alarm_state);
        alarm_off = findViewById(R.id.alarm_off);
        alarm_dismiss = findViewById(R.id.alarm_dismiss);
        ImageView gifImageView = findViewById(R.id.gifImageView);

        // Initialize AlarmManager and PendingIntent
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        my_intent = new Intent(this, AlarmReceiver.class);
        pending_intent = PendingIntent.getBroadcast(this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Start ringing the alarm
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);
        ringtone.play();

        Glide.with(this)
                .load(R.raw.alarm)
                .into(gifImageView);




        // Set onClick listener for the stop button
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display alarm off message
                alarm_state.setText("Alarm Off!");

                // Stop the ringing tone
                if (ringtone != null && ringtone.isPlaying()) {
                    ringtone.stop();
                }

                // Cancel the alarm
                alarm_manager.cancel(pending_intent);

                // Finish the activity
                finish();
            }
        });


        // Set onClick listener for the dismiss button
        alarm_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display alarm dismiss message
                alarm_state.setText("Alarm Dismissed!");

                // Stop the ringing tone
                if (ringtone != null && ringtone.isPlaying()) {
                    ringtone.stop();
                }

                // Cancel the alarm
                alarm_manager.cancel(pending_intent);

                // Finish the activity
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop and release the ringtone when the activity is destroyed
        if (ringtone != null) {
            ringtone.stop();
            ringtone = null;
        }
    }
}

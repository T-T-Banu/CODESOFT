package com.example.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        // Retrieve the alarm time and selected ringtone URI from the intent
        String alarmTime = intent.getStringExtra("alarmTime");
        String ringtoneUriString = intent.getStringExtra("ringtoneUri");
        Uri ringtoneUri = Uri.parse(ringtoneUriString);

        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);

        // Get the current time
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String current = sdf.format(currentTime.getTime());

        // Check if the current time matches the alarm time and the alarm is enabled
        if (alarmTime.equals(current)) {
            // Play the ringtone
            playRingtone(context, ringtoneUri);
        }
    }

    private void playRingtone(Context context, Uri ringtoneUri) {
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
        if (ringtone != null) {
            ringtone.play();
        }
    }

}
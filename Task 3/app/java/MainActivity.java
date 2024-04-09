package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int EDIT_ALARM_REQUEST_CODE = 1001;

    private TextView currentTimeTextView, currentDateTextView;
    private AlarmAdapter adapter;
    private ListView alarmListView;

    private BroadcastReceiver timeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTimeAndDate();
        }


    };
    private Handler handler = new Handler();
    private Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimeAndDate();
            handler.postDelayed(this, 1000); // Update every second
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTimeTextView = findViewById(R.id.currentTimeTextView);
        currentDateTextView = findViewById(R.id.currentDateTextView);
        alarmListView = findViewById(R.id.alarmListView);

        // Set current time and date
        updateTimeAndDate();

        // Populate alarm list view
        populateAlarmListView();

        // Set click listener for setAlarmButton
        findViewById(R.id.setAlarmButton).setOnClickListener(v -> openAlarmSettingActivity());

        // Set long click listener for alarmListView items to delete alarms
        alarmListView.setOnItemLongClickListener((parent, view, position, id) -> {

            return true; // Consume the long click event
        });

        // Set click listener for alarmListView items to edit alarms
        alarmListView.setOnItemClickListener((parent, view, position, id) -> {
            AlarmItem alarmItem = adapter.getItem(position);
            if (alarmItem != null) {
                Intent editIntent = new Intent(MainActivity.this, AlarmSettingActivity.class);
                editIntent.putExtra("position", position);
                editIntent.putExtra("time", alarmItem.getTime());
                editIntent.putExtra("ringtoneUri", alarmItem.getRingtoneUri().toString());
                startActivityForResult(editIntent, EDIT_ALARM_REQUEST_CODE);
            }
        });

        // Start the time update runnable
        handler.post(updateTimeRunnable);

        // Register broadcast receiver for time changes
        IntentFilter timeChangedFilter = new IntentFilter();
        timeChangedFilter.addAction(Intent.ACTION_TIME_CHANGED);
        timeChangedFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(timeChangedReceiver, timeChangedFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receiver when activity is destroyed
        unregisterReceiver(timeChangedReceiver);
        handler.removeCallbacks(updateTimeRunnable);
    }
    public void setAlarm(Context context, long alarmTimeMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
    }




    private void updateTimeAndDate() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        String currentDate = dateFormat.format(new Date());

        currentTimeTextView.setText(" Time: " + currentTime);
        currentDateTextView.setText(" Date: " + currentDate);
    }

    private void openAlarmSettingActivity() {
        startActivity(new Intent(MainActivity.this, AlarmSettingActivity.class));
    }

    private void populateAlarmListView() {
        List<AlarmItem> alarms = getAlarms();
        adapter = new AlarmAdapter(this, alarms);
        alarmListView.setAdapter(adapter);

        Uri ringtoneUri = getSelectedRingtoneUri();
        adapter.setRingtone(ringtoneUri);
    }

    private Uri getSelectedRingtoneUri() {
        SharedPreferences preferences = getSharedPreferences("AlarmPrefs", MODE_PRIVATE);
        String defaultRingtoneUriString = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
        String selectedRingtoneUriString = preferences.getString("selected_ringtone_uri", defaultRingtoneUriString);
        return Uri.parse(selectedRingtoneUriString);
    }

    private List<AlarmItem> getAlarms() {
        List<AlarmItem> alarms = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("AlarmPrefs", MODE_PRIVATE);
        int numAlarms = preferences.getInt("num_alarms", 0);

        for (int i = 1; i <= numAlarms; i++) {
            String alarmKeyTime = "alarm" + i + "_time";
            String alarmKeyEnabled = "alarm" + i + "_enabled";
            String alarmKeyRingtoneUri = "alarm" + i + "_ringtone_uri";

            String alarmTime = preferences.getString(alarmKeyTime, null);
            boolean alarmEnabled = preferences.getBoolean(alarmKeyEnabled, false);
            String ringtoneUriString = preferences.getString(alarmKeyRingtoneUri, null);
            Uri ringtoneUri = (ringtoneUriString != null) ? Uri.parse(ringtoneUriString) : null;

            if (alarmTime != null) {
                alarms.add(new AlarmItem(alarmTime, alarmEnabled, ringtoneUri));
            }
        }

        return alarms;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ALARM_REQUEST_CODE && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            String time = data.getStringExtra("time");
            String ringtoneUriString = data.getStringExtra("ringtoneUri");

            if (position != -1) {
                SharedPreferences preferences = getSharedPreferences("AlarmPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("alarm" + (position + 1) + "_time", time);
                editor.putString("alarm" + (position + 1) + "_ringtone_uri", ringtoneUriString);
                editor.apply();

                populateAlarmListView();
            }
        }
    }
}
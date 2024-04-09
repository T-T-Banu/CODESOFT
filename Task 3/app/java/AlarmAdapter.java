package com.example.demo;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
public class AlarmAdapter extends ArrayAdapter<AlarmItem> {
    private OnAlarmToggleListener onAlarmToggleListener;
    private Ringtone currentRingtone;
    private Context context;
    private List<AlarmItem> alarms;

    public AlarmAdapter(Context context, List<AlarmItem> alarms) {
        super(context, 0, alarms);
        this.context = context;
        this.alarms = alarms;
    }

    public interface OnAlarmToggleListener {
        void onToggle(int position, boolean isChecked);
    }

    public void setOnAlarmToggleListener(OnAlarmToggleListener listener) {
        this.onAlarmToggleListener = listener;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item_alarm, parent, false);
        }

        AlarmItem currentAlarm = alarms.get(position);

        TextView timeTextView = listItem.findViewById(R.id.timeTextView);
        timeTextView.setText(currentAlarm.getTime());

        // Toggle button for enabling/disabling the alarm
        ToggleButton toggleButton = listItem.findViewById(R.id.toggleButton);
        toggleButton.setChecked(currentAlarm.isEnabled());
        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentAlarm.setEnabled(isChecked);
            // Update the alarm status in SharedPreferences or your data storage
            // You can call a method in MainActivity to handle this update
        });

        return listItem;
    }


    public void setRingtone(Uri ringtoneUri) {
        if (ringtoneUri != null) {
            currentRingtone = RingtoneManager.getRingtone(getContext(), ringtoneUri);
        }
    }
}
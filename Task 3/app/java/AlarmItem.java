package com.example.demo;

import android.net.Uri;

public class AlarmItem {
    private String time;
    private boolean enabled;
    private Uri ringtoneUri;


    public AlarmItem(String time, boolean enabled, Uri ringtoneUri) {
        this.time = time;
        this.enabled = enabled;
        this.ringtoneUri = ringtoneUri;
    }

    public String getTime() {
        return time;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Uri getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(Uri ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }
}


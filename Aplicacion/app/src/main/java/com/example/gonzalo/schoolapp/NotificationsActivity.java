package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Gonzalo on 17/02/2015.
 */
public class NotificationsActivity extends Activity {

    public void onCreate (Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        TextView textView = new TextView(this);
        textView.setText("Mis notificaciones");
        setContentView(textView);
    }
}
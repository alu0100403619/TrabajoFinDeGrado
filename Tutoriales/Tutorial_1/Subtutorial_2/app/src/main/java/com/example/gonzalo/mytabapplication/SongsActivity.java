package com.example.gonzalo.mytabapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SongsActivity extends Activity {
    public void onCreate (Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);

        TextView textView = new TextView(this);
        textView.setText("This is the Songs tab");
        setContentView(textView);
    }
}

package com.example.gonzalo.schoolapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Gonzalo on 18/03/2015.
 */
public class FatherActivity extends ListActivity {

    public void onCreate (Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        TextView textView = new TextView(this);
        textView.setText("This is the Fathers tab");
        setContentView(textView);
    }
}

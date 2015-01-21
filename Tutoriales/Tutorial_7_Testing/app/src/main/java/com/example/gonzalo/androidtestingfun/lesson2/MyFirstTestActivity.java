package com.example.gonzalo.androidtestingfun.lesson2;

import android.app.Activity;
import android.os.Bundle;

import com.example.gonzalo.androidtestingfun.R;

/**
 * Created by Gonzalo on 20/01/2015.
 *
 * Activity with a TextView that contains a String label.
 */
public class MyFirstTestActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_first_test);
    }
}

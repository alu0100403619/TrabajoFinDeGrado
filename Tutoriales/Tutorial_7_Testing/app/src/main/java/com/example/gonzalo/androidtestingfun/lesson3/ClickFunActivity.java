package com.example.gonzalo.androidtestingfun.lesson3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gonzalo.androidtestingfun.R;

/**
 * Created by Gonzalo on 20/01/2015.
 *
 * Activity which shows a "click me" button. When the button is clicked, a TextView is shown below
 * the button
 */
public class ClickFunActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_fun);

        final TextView infoTextView = (TextView) findViewById(R.id.info_text_view);
        final Button clickMeButton = (Button) findViewById(R.id.launch_next_activity_button);
        clickMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoTextView.setVisibility(View.VISIBLE);
                infoTextView.setText(getString(R.string.info_text));
            }
        });
    }
}

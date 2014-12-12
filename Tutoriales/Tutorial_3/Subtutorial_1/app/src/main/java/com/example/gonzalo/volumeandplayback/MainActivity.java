package com.example.gonzalo.volumeandplayback;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private Button modeBtn;
    private Button increaseBtn;
    private Button decreaseBtn;
    private RadioButton normal;
    private RadioButton silent;
    private RadioGroup ringGroup;
    private TextView status;
    private AudioManager myAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        normal = (RadioButton) findViewById(R.id.radioNormal);
        silent = (RadioButton) findViewById(R.id.radioSilent);
        status = (TextView) findViewById(R.id.text);
        ringGroup = (RadioGroup) findViewById(R.id.radioRinger);

        modeBtn = (Button) findViewById(R.id.mode);
        modeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = ringGroup.getCheckedRadioButtonId();

                if(selectedId == silent.getId()) {
                    silentEnable(view);
                } else if(selectedId == normal.getId()) {
                    normalEnable(view);
                } else {
                    vibrateEnable(view);
                }
            }
        });

        increaseBtn = (Button) findViewById(R.id.increase);
        increaseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                Toast.makeText(getApplicationContext(), "increase volume",
                        Toast.LENGTH_SHORT).show();
            }
        });
        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        decreaseBtn = (Button) findViewById(R.id.decrease);
        decreaseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // decrease the volume and show the ui
                myAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                Toast.makeText(getApplicationContext(), "decrease volume",
                        Toast.LENGTH_SHORT).show();
            }
        });
        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    }

        public void vibrateEnable(View view){
        // set the ring mode to vibrate
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        status.setText("Current Status: Vibrate Mode");
    }

    public void normalEnable(View view){
        // set the ring mode to loud
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        status.setText("Current Status: Ring Mode");
    }

    public void silentEnable(View view){
        // set the ring mode to silent
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        status.setText("Current Status: Silent Mode");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

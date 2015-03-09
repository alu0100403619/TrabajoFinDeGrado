package com.example.gonzalo.firebasestorage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;


public class DinoRegisterActivity extends ActionBarActivity {

    EditText nameEditText, hightEditText, appearEditText, disappearEditText, lengthEditText,
            orderEditText, weightEditText, scoreEditText;
    Firebase rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dino_register);
        Firebase.setAndroidContext(this);

        rootRef = new Firebase("https://dinosaurios.firebaseio.com/");

        nameEditText = (EditText) findViewById(R.id.name_editText);
        hightEditText = (EditText) findViewById(R.id.hight_editText);
        appearEditText = (EditText) findViewById(R.id.appear_editText);
        disappearEditText = (EditText) findViewById(R.id.disappear_editText);
        lengthEditText = (EditText) findViewById(R.id.length_editText);
        orderEditText = (EditText) findViewById(R.id.order_editText);
        weightEditText = (EditText) findViewById(R.id.weight_editText);
        scoreEditText = (EditText) findViewById(R.id.score_editText);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dino_register, menu);
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
        if (id == R.id.action_logout) {
            rootRef.unauth();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void back (View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void register (View view) {

        Firebase dinoRef = rootRef.child("dinosaurios");
        Firebase scoreRef = rootRef.child("scores");

        String name = String.valueOf(nameEditText.getText());
        double hight = Double.valueOf(String.valueOf(hightEditText.getText()));
        Long appear = Long.valueOf(String.valueOf(appearEditText.getText()));
        Long disappear = Long.valueOf(String.valueOf(disappearEditText.getText()));
        double length = Double.valueOf(String.valueOf(lengthEditText.getText()));
        String order = String.valueOf(orderEditText.getText());
        double weight = Double.valueOf(String.valueOf(weightEditText.getText()));
        double score = Double.valueOf(String.valueOf(scoreEditText.getText()));

        Map<String, Object> dinosaurMap = new HashMap<String, Object>();
        Firebase dinno = dinoRef.child(name);
        dinosaurMap.put(getString(R.string.hight), hight);
        dinosaurMap.put(getString(R.string.appear), appear);
        dinosaurMap.put(getString(R.string.disappear), disappear);
        dinosaurMap.put(getString(R.string.length), length);
        dinosaurMap.put(getString(R.string.order), order);
        dinosaurMap.put(getString(R.string.weight), weight);
        dinno.updateChildren(dinosaurMap);

        Map<String, Object> scoresMap = new HashMap<String, Object>();
        scoresMap.put(name, score);
        scoreRef.updateChildren(scoresMap);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

}

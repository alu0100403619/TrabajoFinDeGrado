package com.example.gonzalo.sharingsimpledata;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void sendTextData (View view) {
        Intent sendIntent = new Intent(this, MainActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putString ("text", "Esto es mi texto a enviar");
        sendIntent.putExtras(bundle);
        startActivity(sendIntent);
    }

    public void sendBinaryData (View view) {
        Intent sharedIntent = new Intent(this, MainActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putInt ("image", R.drawable.jacksally);
        sharedIntent.putExtras(bundle);
        startActivity(sharedIntent);
    }

    public void sendMultipliPieces (View view) {
        Intent multipleIntent = new Intent(this, MainActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putString ("text", "Esto es mi texto a enviar");
        bundle.putInt ("image", R.drawable.jacksally);
        multipleIntent.putExtras(bundle);
        startActivity(multipleIntent);
    }
}

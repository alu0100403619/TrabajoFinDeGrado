package com.example.gonzalo.savingdata;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.TextView;

import com.example.sharedpreferences.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends Activity {

    TextView name ;
    TextView phone;
    TextView email;
    TextView street;
    TextView place;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public static final String Street = "streetKey";
    public static final String Place = "placeKey";
    public static final String filename = "myFile";
    SharedPreferences sharedpreferences;
    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (TextView) findViewById(R.id.editTextName);
        phone = (TextView) findViewById(R.id.editTextPhone);
        email = (TextView) findViewById(R.id.editTextStreet);
        street = (TextView) findViewById(R.id.editTextEmail);
        place = (TextView) findViewById(R.id.editTextCity);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(Name)) {
            name.setText(sharedpreferences.getString(Name, ""));
        }
        if (sharedpreferences.contains(Phone)) {
            phone.setText(sharedpreferences.getString(Phone, ""));
        }
        if (sharedpreferences.contains(Email)) {
            email.setText(sharedpreferences.getString(Email, ""));
        }
        if (sharedpreferences.contains(Street)) {
            street.setText(sharedpreferences.getString(Street, ""));
        }
        if (sharedpreferences.contains(Place)) {
            place.setText(sharedpreferences.getString(Place,""));
        }
    }

    public void run(View view){
        String n  = name.getText().toString();
        String ph  = phone.getText().toString();
        String e  = email.getText().toString();
        String s  = street.getText().toString();
        String p  = place.getText().toString();
        Editor editor = sharedpreferences.edit();
        editor.putString(Name, n);
        editor.putString(Phone, ph);
        editor.putString(Email, e);
        editor.putString(Street, s);
        editor.putString(Place, p);

        editor.commit();

        //Guardar Archivo
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(n.getBytes());
            outputStream.write(ph.getBytes());
            outputStream.write(e.getBytes());
            outputStream.write(s.getBytes());
            outputStream.write(p.getBytes());
            outputStream.close();
            //Borrar un Archivo en memoria externa
            //filenameile.delete();
            //Borrar un Archivo en memoria interna
            //myContext.deleteFile(fileName);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(filename, null, context.getCacheDir());
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        return file;
    }

    //Para saber si se puede escribir en la memoria externa
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    //Para saber si se puede leer de la memoria externa
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
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

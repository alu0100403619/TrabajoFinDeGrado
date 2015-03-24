package com.example.gonzalo.schoolapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.firebase.client.Firebase;

public class AlumnoTabActivity extends TabActivity {

    String mail, clase;
    Firebase aluRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_tab);
        Firebase.setAndroidContext(this);
        aluRef = new Firebase (getString(R.string.aluRef));

        //Obtenemos el E-mail
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        clase = getIntent().getExtras().getString(getString(R.string.bbdd_class));

        //Añadiendo las Tabs
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //Alumno Tab
        intent = new Intent().setClass(this, AlumnoActivity.class);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        intent.putExtra(getString(R.string.bbdd_class), clase);
        spec = tabHost.newTabSpec("alumnos").setIndicator(getString(R.string.alumnos)).setContent(intent);
        tabHost.addTab(spec);

        //Teachers Tab
        intent = new Intent().setClass(this, TeachersActivity.class);
        intent.putExtra(getString(R.string.bbdd_class), clase);
        spec = tabHost.newTabSpec("profesores").setIndicator(getString(R.string.teachers)).setContent(intent);
        tabHost.addTab(spec);

        //Notificaciones Tab
        intent = new Intent().setClass(this, NotificationsActivity.class);
        spec = tabHost.newTabSpec("notificaciones").setIndicator(getString(R.string.notificaciones)).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alumno_tab, menu);
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

    public void launchCirculares(View view) {
        Intent intent = new Intent(this, CircularesActivity.class);
        startActivity(intent);
    }
}

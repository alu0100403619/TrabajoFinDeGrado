package com.example.gonzalo.schoolapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class AlumnoTabActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_tab);
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, AlumnoActivity.class);
        spec = tabHost.newTabSpec("alumnos").setIndicator(getString(R.string.alumnos)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, TeachersActivity.class);
        spec = tabHost.newTabSpec("profesores").setIndicator(getString(R.string.teachers)).setContent(intent);
        tabHost.addTab(spec);

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
}

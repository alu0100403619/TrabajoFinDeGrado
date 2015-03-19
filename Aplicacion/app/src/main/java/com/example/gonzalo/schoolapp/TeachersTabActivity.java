package com.example.gonzalo.schoolapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.firebase.client.Firebase;


public class TeachersTabActivity extends TabActivity {

    String mail, clase;
    Firebase teachersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_tab);
        Firebase.setAndroidContext(this);
        teachersRef = new Firebase (getString(R.string.profeRef));

        //Obtener mail
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));

        //Añadiendo las Tabs
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //Tab Profesores
        intent = new Intent().setClass(this, ExpandableTeachersActivity.class);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        spec = tabHost.newTabSpec("Profesores").setIndicator(getString(R.string._profes)).setContent(intent);
        tabHost.addTab(spec);

        //Tab Padres
        /*intent = new Intent().setClass(this, ExpandableFathersActivity.class);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        spec = tabHost.newTabSpec("Padres").setIndicator(getString(R.string._padres)).setContent(intent);
        tabHost.addTab(spec);*/

        //Tab Alumnos
        /*intent = new Intent().setClass(this, ExpandableAlumnosActivity.class);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        spec = tabHost.newTabSpec("alumnos").setIndicator(getString(R.string._alumnos)).setContent(intent);
        tabHost.addTab(spec);*/

        //Tab Notificaciones
        intent = new Intent().setClass(this, NotificationsActivity.class);
        spec = tabHost.newTabSpec("notificaciones").setIndicator(getString(R.string.notificaciones)).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teachers_tab, menu);
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

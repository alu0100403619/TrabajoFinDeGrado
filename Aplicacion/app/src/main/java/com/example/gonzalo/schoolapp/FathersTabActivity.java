package com.example.gonzalo.schoolapp;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.firebase.client.Firebase;

import java.util.ArrayList;


public class FathersTabActivity extends TabActivity {

    String mail, school;
    ArrayList<String> clases;
    Firebase fathersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fathers_tab);
        Firebase.setAndroidContext(this);
        fathersRef = new Firebase (getString(R.string.profeRef));
        clases = new ArrayList<>();

        //Obtener mail, colegio y las clases
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        school = getIntent().getExtras().getString(getString(R.string.bbdd_center));
        clases = getIntent().getExtras().getStringArrayList(getString(R.string.bbdd_teacher_class));

        //Añadiendo las Tabs
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //Tab Padres
        intent = new Intent().setClass(this, ExpandableFahtersActivity.class);
        intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
        intent.putExtra(getString(R.string.bbdd_center), school);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        spec = tabHost.newTabSpec("Padres").setIndicator(getString(R.string._padres)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ExpandableTeachersActivity.class);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
        intent.putExtra(getString(R.string.bbdd_center), school);
        spec = tabHost.newTabSpec("Profesores").setIndicator(getString(R.string._profes)).setContent(intent);
        tabHost.addTab(spec);

        //Tab Notificaciones
        intent = new Intent().setClass(this, NotificationsActivity.class);
        spec = tabHost.newTabSpec("notificaciones").setIndicator(getString(R.string.notificaciones)).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fathers_tab, menu);
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

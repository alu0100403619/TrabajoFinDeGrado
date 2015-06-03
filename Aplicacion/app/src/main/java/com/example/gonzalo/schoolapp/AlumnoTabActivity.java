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

    String mail, clase, school, myName, myRol;
    Firebase aluRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_tab);
        Firebase.setAndroidContext(this);
        getActionBar().setTitle(getString(R.string.ActionBar_alumno));
        aluRef = new Firebase (getString(R.string.aluRef));

        //Obtenemos el E-mail
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        clase = getIntent().getExtras().getString(getString(R.string.bbdd_class));
        school = getIntent().getExtras().getString(getString(R.string.bbdd_center));
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));

        //Añadiendo las Tabs
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //Alumno Tab
        intent = new Intent().setClass(this, AlumnoActivity.class);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        intent.putExtra(getString(R.string.bbdd_class), clase);
        intent.putExtra(getString(R.string.bbdd_center), school);
        intent.putExtra(getString(R.string.myName), myName);
        intent.putExtra(getString(R.string.myRol), myRol);
        spec = tabHost.newTabSpec(getString(R.string.tab_alumnos))
                .setIndicator(getString(R.string.tab_alumnos)).setContent(intent);
        tabHost.addTab(spec);

        //Teachers Tab
        intent = new Intent().setClass(this, TeachersActivity.class);
        intent.putExtra(getString(R.string.bbdd_class), clase);
        intent.putExtra(getString(R.string.bbdd_center), school);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        intent.putExtra(getString(R.string.myName), myName);
        intent.putExtra(getString(R.string.myRol), myRol);
        spec = tabHost.newTabSpec(getString(R.string.tab_alumnos_teachers))
                .setIndicator(getString(R.string.tab_alumnos_teachers)).setContent(intent);
        tabHost.addTab(spec);

        //Notificaciones Tab
        intent = new Intent().setClass(this, NotificationsActivity.class);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        intent.putExtra(getString(R.string.myName), myName);
        intent.putExtra(getString(R.string.myRol), myRol);
        spec = tabHost.newTabSpec(getString(R.string.tab_notifications))
                .setIndicator(getString(R.string.tab_notifications)).setContent(intent);
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

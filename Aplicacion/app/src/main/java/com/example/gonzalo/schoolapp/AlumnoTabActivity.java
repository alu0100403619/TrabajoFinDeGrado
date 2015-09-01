package com.example.gonzalo.schoolapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.example.gonzalo.schoolapp.utilities.Utilities;
import com.firebase.client.Firebase;

import java.util.Calendar;

public class AlumnoTabActivity extends TabActivity {

    String mail, clase, school, myName, myRol, myDNI;
    Firebase aluRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_tab);
        Firebase.setAndroidContext(this);
        getActionBar().setTitle(getString(R.string.ActionBar_alumno));
        aluRef = new Firebase (getString(R.string.studentRef));

        //Obtenemos el E-mail
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        clase = getIntent().getExtras().getString(getString(R.string.bbdd_class));
        school = getIntent().getExtras().getString(getString(R.string.bbdd_center));
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));
        myDNI = getIntent().getExtras().getString(getString(R.string.myDNI));

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
        intent.putExtra(getString(R.string.myDNI), myDNI);
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
        intent.putExtra(getString(R.string.myDNI), myDNI);
        intent.putExtra(getString(R.string.myRol), myRol);
        spec = tabHost.newTabSpec(getString(R.string.tab_alumnos_teachers))
                .setIndicator(getString(R.string.tab_alumnos_teachers)).setContent(intent);
        tabHost.addTab(spec);

        //Notificaciones Tab
        intent = new Intent().setClass(this, NotificationsActivity.class);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        intent.putExtra(getString(R.string.myName), myName);
        intent.putExtra(getString(R.string.myDNI), myDNI);
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
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }
        else if (id == R.id.action_modify) {
            Intent intent = new Intent(this, MyDataActivity.class);
            intent.putExtra(getString(R.string.bbdd_mail), mail);
            intent.putExtra(getString(R.string.bbdd_rol), myRol);
            intent.putExtra(getString(R.string.bbdd_dni), myDNI);
            startActivity(intent);
        }
        else if (id == R.id.action_logout) {
            Firebase rootref = new Firebase (getString(R.string.rootRef));
            Intent intent = new Intent(this, LoginActivity.class);
            rootref.unauth();
            startActivity(intent);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchCirculares(View view) {
        Intent intent = new Intent(this, CircularesActivity.class);
        startActivity(intent);
    }

    public void launchDates (View view) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("title", getString(R.string.meeting_school));
        startActivity(intent);
    }
}

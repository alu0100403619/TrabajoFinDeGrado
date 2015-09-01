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

import java.util.ArrayList;


public class TeachersTabActivity extends TabActivity {

    String mail, school, myName, myRol, myDNI;
    ArrayList<String> clases;
    Firebase teachersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_tab);
        getActionBar().setTitle(getString(R.string.ActionBar_teacher));
        Firebase.setAndroidContext(this);
        teachersRef = new Firebase (getString(R.string.teacherRef));
        clases = new ArrayList<>();

        //Obtener mail, colegio y las clases
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        school = getIntent().getExtras().getString(getString(R.string.bbdd_center));
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));
        myDNI = getIntent().getExtras().getString(getString(R.string.myDNI));
        clases = getIntent().getExtras().getStringArrayList(getString(R.string.bbdd_teacher_class));

        //Añadiendo las Tabs
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //Tab Profesores
        intent = new Intent().setClass(this, ExpandableTeachersActivity.class);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
        intent.putExtra(getString(R.string.bbdd_center), school);
        intent.putExtra(getString(R.string.myName), myName);
        intent.putExtra(getString(R.string.myRol), myRol);
        intent.putExtra(getString(R.string.myDNI), myDNI);
        spec = tabHost.newTabSpec(getString(R.string.tab_teachers))
                .setIndicator(getString(R.string.tab_teachers)).setContent(intent);
        tabHost.addTab(spec);

        //Tab Padres
        intent = new Intent().setClass(this, ExpandableFahtersActivity.class);
        intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
        intent.putExtra(getString(R.string.bbdd_center), school);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        intent.putExtra(getString(R.string.myName), myName);
        intent.putExtra(getString(R.string.myRol), myRol);
        intent.putExtra(getString(R.string.myDNI), myDNI);
        spec = tabHost.newTabSpec(getString(R.string.tab_fathers))
                .setIndicator(getString(R.string.tab_fathers)).setContent(intent);
        tabHost.addTab(spec);

        //Tab Alumnos
        intent = new Intent().setClass(this, ExpandableAlumnosActivity.class);
        intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
        intent.putExtra(getString(R.string.bbdd_center), school);
        intent.putExtra(getString(R.string.myName), myName);
        intent.putExtra(getString(R.string.myRol), myRol);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        intent.putExtra(getString(R.string.myDNI), myDNI);
        spec = tabHost.newTabSpec(getString(R.string.tab_teachers_alumnos))
                .setIndicator(getString(R.string.tab_teachers_alumnos)).setContent(intent);
        tabHost.addTab(spec);

        //Tab Notificaciones
        intent = new Intent().setClass(this, NotificationsActivity.class);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        intent.putExtra(getString(R.string.myName), myName);
        intent.putExtra(getString(R.string.myRol), myRol);
        intent.putExtra(getString(R.string.myDNI), myDNI);
        spec = tabHost.newTabSpec(getString(R.string.tab_notifications))
                .setIndicator(getString(R.string.tab_notifications)).setContent(intent);
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
            Log.i("TTA", "Logout");
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
        intent.putExtra(getString(R.string.myName), myName);
        intent.putExtra(getString(R.string.myRol), myRol);
        intent.putExtra(getString(R.string.myDNI), myDNI);
        intent.putExtra(getString(R.string.bbdd_center), school);
        intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
        startActivity(intent);
    }

    public void launchDates (View view) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("title", getString(R.string.meeting_school));
        startActivity(intent);
    }
}

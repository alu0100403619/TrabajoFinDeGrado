package com.example.gonzalo.schoolapp;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toolbar;

import com.example.gonzalo.schoolapp.utilities.Utilities;
import com.firebase.client.Firebase;

import java.util.ArrayList;


public class FathersTabActivity extends TabActivity {

    String mail, myName, myRol, myDNI;
    ArrayList<String> clases, schools;
    Firebase fathersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fathers_tab);
        getActionBar().setTitle(getString(R.string.ActionBar_father));
        Firebase.setAndroidContext(this);
        fathersRef = new Firebase (getString(R.string.teacherRef));
        clases = new ArrayList<>();
        schools = new ArrayList<>();

        //Obtener mail, colegio y las clases
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        schools = getIntent().getExtras().getStringArrayList(getString(R.string.bbdd_center));
        clases = getIntent().getExtras().getStringArrayList(getString(R.string.bbdd_teacher_class));
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));
        myDNI = getIntent().getExtras().getString(getString(R.string.myDNI));

        //Add Tabs
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent = null;

        //Tab Padres
        if (schools.size() == 1) {
            intent = new Intent().setClass(this, ExpandableFahtersActivity.class);
            intent.putExtra(getString(R.string.bbdd_center), schools.get(0));
        }
        else {
            //TODO A probar
            //intent = new Intent().setClass(this, FatherExpandableFathersActivity.class);
            //intent.putExtra(getString(R.string.bbdd_center), schools);
        }//else
        intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        intent.putExtra(getString(R.string.myName), myName);
        intent.putExtra(getString(R.string.myRol), myRol);
        intent.putExtra(getString(R.string.myDNI), myDNI);
        spec = tabHost.newTabSpec(getString(R.string.tab_fathers))
                .setIndicator(getString(R.string.tab_fathers)).setContent(intent);
        tabHost.addTab(spec);

        //Tab Profes
        if (schools.size() == 1) {
            intent = new Intent().setClass(this, ExpandableTeachersActivity.class);
            intent.putExtra(getString(R.string.bbdd_center), schools.get(0));
        }
        else {
            //TODO A probar
            //intent = new Intent().setClass(this, TeacherExpandableTeachersActivity.class);
            //intent.putExtra(getString(R.string.bbdd_center), schools);
        }
        intent.putExtra(getString(R.string.bbdd_mail), mail);
        intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
        intent.putExtra(getString(R.string.myName), myName);
        intent.putExtra(getString(R.string.myRol), myRol);
        intent.putExtra(getString(R.string.myDNI), myDNI);
        spec = tabHost.newTabSpec(getString(R.string.tab_teachers))
                .setIndicator(getString(R.string.tab_teachers)).setContent(intent);
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
        intent.putExtra(getString(R.string.myName), myName);
        intent.putExtra(getString(R.string.myRol), myRol);
        intent.putExtra(getString(R.string.myDNI), myDNI);
        intent.putExtra(getString(R.string.bbdd_center), schools);
        intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
        startActivity(intent);
    }

}//class

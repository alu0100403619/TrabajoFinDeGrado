package com.example.gonzalo.schoolapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExpandableTeachersActivity extends ActionBarActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ArrayList<String> clases;
    Firebase teachersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_teachers);
        getSupportActionBar().hide();
        Firebase.setAndroidContext(this);

        teachersRef = new Firebase (getString(R.string.profeRef));
        clases = new ArrayList<>();

        //Obtenemos las clases
        clases = getIntent().getExtras().getStringArrayList(getString(R.string.bbdd_teacher_class));

        //Obtener el elemento xml
        expListView = (ExpandableListView) findViewById(R.id.expListView);

        //Preparar los datos
        prepareListData();
    }

    public void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        for (String clase : clases) {
            
        }//for
    }//function

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expandable_teachers, menu);
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

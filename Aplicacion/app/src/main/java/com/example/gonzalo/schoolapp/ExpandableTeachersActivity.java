package com.example.gonzalo.schoolapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExpandableTeachersActivity extends ActionBarActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String mail;
    Firebase teachersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_teachers);
        getSupportActionBar().hide();
        Firebase.setAndroidContext(this);

        teachersRef = new Firebase (getString(R.string.profeRef));

        //Obtenemos el E-mail
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));

        //Obtener el elemento xml
        expListView = (ExpandableListView) findViewById(R.id.expListView);

        //Preparar los datos
        prepareListData();
    }

    public void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        Query currentTeacher = teachersRef.orderByChild(getString(R.string.bbdd_mail)).equalTo(mail);
        currentTeacher.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Map<String, Object> clasesMap = (Map<String, Object>) values.get(getString(R.string.bbdd_teacher_class));
                for (String key : clasesMap.keySet()) {
                    listDataHeader.add(clasesMap.get(key).toString());
                }//for key
                for (int i = 0; i < listDataHeader.size(); i++) {
                    Log.i("ExpanTeachersActivity", "Clase: " + listDataHeader.get(i));
                    Query teachers = teachersRef
                            .orderByChild(getString(R.string.bbdd_teacher_class))
                            .equalTo(listDataHeader.get(i));
                    teachers.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Map<String, Object> teacherMap = (Map<String, Object>) dataSnapshot
                                    .getValue();
                            Log.i("ExpanTeachersActivity", "teachersMap: " + teacherMap);
                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });
                }//for
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

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

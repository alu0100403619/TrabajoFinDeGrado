package com.example.gonzalo.schoolapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import java.util.Set;


public class ExpandableTeachersActivity extends ActionBarActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader, auxList;
    HashMap<String, List<String>> listDataChild;
    ArrayList<String> clases;
    Firebase teachersRef;
    int index = 0;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_teachers);
        getSupportActionBar().hide();
        Firebase.setAndroidContext(this);

        teachersRef = new Firebase (getString(R.string.profeRef));
        clases = new ArrayList<>();
        auxList = new ArrayList<>();

        //Obtenemos las clases
        clases = getIntent().getExtras().getStringArrayList(getString(R.string.bbdd_teacher_class));

        //Obtenemos el mail
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));

        //Obtener el elemento xml
        expListView = (ExpandableListView) findViewById(R.id.expListView);

        //Preparar los datos
        prepareListData();

        //Adaptador
        listAdapter = new ExpandableListAdapter (this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    //TODO Arreglar
    public void prepareListData() {
        //Adding Header Data
        listDataHeader = new ArrayList<>(clases);
        listDataChild = new HashMap<String, List<String>>();
        //Adding Child Data
        for (int i = 0; i < listDataHeader.size(); i++) {
            index = i-1;
            auxList.clear();
            Query teachersQuery = teachersRef.orderByChild(getString(R.string.bbdd_teacher_class));
            teachersQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                    Map<String, Object> classMap = (Map<String, Object>) values.get(getString(R.string.bbdd_teacher_class));
                    Set<String> keys = classMap.keySet();
                    String clase = listDataHeader.get(index);
                    for (String key : keys) {
                        if (clase.equals(classMap.get(key))) {
                            String name = values.get(getString(R.string.bbdd_name)) + " " +
                                    values.get(getString(R.string.bbdd_lastname));
                            String email = values.get(getString(R.string.bbdd_mail)).toString();
                            if ((!auxList.contains(name)) && (!email.equals(mail))) {
                                auxList.add(name);
                            }//if
                        }//if
                    }//for
                    listDataChild.put(listDataHeader.get(index), auxList);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(FirebaseError firebaseError) {}
            });//teachersQuery
            if (listDataChild.get(index) != null) {
                index++;
            }//if
        }//for
        //Quitar los header que esten vacios
        for (int i = 0; i < listDataHeader.size(); i++) {
            if ((listDataChild.get(listDataHeader.get(i)) == null) && ((i+1) < listDataHeader.size())) {
                listDataHeader.remove(i+1);
            }//if
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

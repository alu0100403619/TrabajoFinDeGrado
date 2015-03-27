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


public class ExpandableFahtersActivity extends ActionBarActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader, auxList;
    HashMap<String, List<String>> listDataChild;
    ArrayList<String> clases;
    Firebase fathersRef;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_fahters);
        getSupportActionBar().hide();
        Firebase.setAndroidContext(this);

        fathersRef = new Firebase (getString(R.string.madreRef));
        clases = new ArrayList<>();
        auxList = new ArrayList<>();

        //Obtenemos las clases
        clases = getIntent().getExtras().getStringArrayList(getString(R.string.bbdd_teacher_class));

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
            Query fathersQuery = fathersRef.orderByChild(getString(R.string.bbdd_children));
            fathersQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map<String, Object> child;
                    Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                    Map<String, Object> children = (Map<String, Object>)
                            values.get(getString(R.string.bbdd_children));
                    Set<String> keys = children.keySet();
                    for (String key: keys) {
                        child = (Map<String, Object>) children.get(key);
                        String classRoom = child.get(getString(R.string.bbdd_class)).toString();
                        if (classRoom.equals(listDataHeader.get(index))){
                            String name = values.get(getString(R.string.bbdd_name)) + " " +
                                    values.get(getString(R.string.bbdd_lastname));
                            if (!auxList.contains(name)) {
                                auxList.add(name);
                            }//if auxList
                        }//if clasroom
                    }//for key
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
            });//fathersQuery
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
        getMenuInflater().inflate(R.menu.menu_expandable_fahters, menu);
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

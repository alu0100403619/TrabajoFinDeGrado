package com.example.gonzalo.schoolapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.example.gonzalo.schoolapp.ExpandableListAdapter.ExpandableListAdapterFather;
import com.example.gonzalo.schoolapp.clases.Father;
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


public class FatherExpandableFathersActivity extends ActionBarActivity {

    ExpandableListAdapterFather listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader, auxList;
    HashMap<String, List<String>> listDataChild;
    ArrayList<String> clases, schools;
    String mail, myName, myRol;
    Firebase fathersRef;
    ArrayList<Father> fathers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_father_expandable_father);
        getSupportActionBar().hide();
        Firebase.setAndroidContext(this);

        fathersRef = new Firebase (getString(R.string.padreRef));
        clases = new ArrayList<>();
        schools = new ArrayList<>();
        fathers = new ArrayList<>();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //Obtenemos las clases
        clases = getIntent().getExtras().getStringArrayList(getString(R.string.bbdd_teacher_class));

        //Obtenemos el colegio
        schools = getIntent().getExtras().getStringArrayList(getString(R.string.bbdd_center));

        //Obtenemos el mail
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));

        //Obtener el elemento xml
        expListView = (ExpandableListView) findViewById(R.id.expListView);

        //Obtenemos los profes del centro
        getFathers();

        Log.i("FEFA", "clases: "+clases+", coles: "+schools+", mail: "+mail);
    }

    public void getFathers() {
        Query fathersQuery = fathersRef.orderByChild(getString(R.string.bbdd_center));
        fathersQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> child;
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Map<String, Object> childrens = (Map<String, Object>) values.get(getString(R.string.bbdd_children));
                Set<String> keys = childrens.keySet();
                for (String key: keys) {
                    child = (Map<String, Object>) childrens.get(key);
                    if (schools.contains(child.get(getString(R.string.bbdd_center)))) {
                        Father father = new Father (values);
                        //Si el array no lo contiene, mete al padre
                        if ((!fathers.contains(father)) && (!father.getMail().equals(mail))) {
                            fathers.add(father);
                        }//if
                    }//if
                }//for keys
                //Preparar los datos
                prepareListData();
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
    }

    public void prepareListData() {
        Log.i("FEFA", "fathers: "+fathers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_father_expandable_father, menu);
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

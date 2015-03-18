package com.example.gonzalo.schoolapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Gonzalo on 17/02/2015.
 */
public class TeachersActivity extends ListActivity {

    List<String> teachers;
    String clase;
    Firebase teacherRef;

    public void onCreate (Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Firebase.setAndroidContext(this);

        teacherRef = new Firebase(getString(R.string.profeRef));
        teachers = new ArrayList<>();

        //Obtenemos la clase
        clase = getIntent().getExtras().getString(getString(R.string.bbdd_class));

        preparingData();
    }

    public void preparingData(){
        //Obtener los profesores que dan esa clase
        Query getClassmates = teacherRef.orderByChild(getString(R.string.bbdd_class)).equalTo(clase);
        getClassmates.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                String name = values.get(getString(R.string.bbdd_name)) + " "+
                        values.get(getString(R.string.bbdd_lastname));
                teachers.add(name);

                //Seteamos el ArrayAdapter
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeachersActivity.this,
                        R.layout.list_item_layout,
                        teachers);
                setListAdapter(adapter);
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
}
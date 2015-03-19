package com.example.gonzalo.schoolapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
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
    String mail/*, clase//*/;
    Firebase teacherRef, aluRef;

    public void onCreate (Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Firebase.setAndroidContext(this);

        teacherRef = new Firebase(getString(R.string.profeRef));
        aluRef = new Firebase(getString(R.string.aluRef));
        teachers = new ArrayList<>();

        //Obtenemos la clase
        //clase = getIntent().getExtras().getString(getString(R.string.bbdd_class));
        //Obtenemos el Mail
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));

        getClase();
        //preparingData();
    }

    //TODO Arreglar que no entra en la llamada en onChildAdded
    public void preparingData(String clase){
        //Obtener los profesores que dan esa clase
        Query getClassmates = teacherRef.orderByChild(getString(R.string.bbdd_teacher_class)).equalTo(clase);
        Log.i("TeachersActivity", "Tras Query");
        getClassmates.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("TeachersActivity", "onChildAdded");
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Log.i("TeachersActivity", "Values: " + values);
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

    public void getClase() {
        Query getClass = aluRef.orderByChild(getString(R.string.bbdd_mail)).equalTo(mail);
        getClass.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                String clase = values.get(getString(R.string.bbdd_class)).toString();
                preparingData(clase);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });//addchild
    }//function
}
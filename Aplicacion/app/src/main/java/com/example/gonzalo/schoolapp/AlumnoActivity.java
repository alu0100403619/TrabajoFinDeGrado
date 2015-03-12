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
public class AlumnoActivity extends ListActivity {

    //ListView compañeros;
    List<String> alus;
    String mail, clase;
    Firebase aluRef;

    public void onCreate (Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Firebase.setAndroidContext(this);
        aluRef = new Firebase (getString(R.string.aluRef));
        alus = new ArrayList<>();
        clase = "";

        //Obtenemos el Mail
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));

        //Obtenemos la clase del Usuario
        Query currentUser = aluRef.orderByChild(getString(R.string.bbdd_mail)).equalTo(mail);
        currentUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> me = (Map<String, Object>) dataSnapshot.getValue();
                clase = me.get(getString(R.string.bbdd_class)).toString();
                preparingData();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });//currentUser
    }

    public void preparingData(){

        //Obtener los alumnos de la misma clase
        Query getClassmates = aluRef.orderByChild(getString(R.string.bbdd_class)).equalTo(clase);
        getClassmates.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //TODO no meter al propio usuario
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Log.i("AlumnoActivity", "MAP------------->"+values);
                alus.add(values.get(getString(R.string.bbdd_name)).toString());
                Log.i("AlumnoActivity", "ALUS------------->"+alus);

                //Seteamos el ArrayAdapter
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AlumnoActivity.this,
                        R.layout.list_item_layout,
                        alus);
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
    }//function
}//class
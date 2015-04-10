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

    List<String> alus;
    String mail, clase, school;
    Firebase aluRef;

    public void onCreate (Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Firebase.setAndroidContext(this);
        aluRef = new Firebase (getString(R.string.aluRef));
        alus = new ArrayList<>();
        clase = "";

        //Obtenemos el Mail y la Clase
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        clase = getIntent().getExtras().getString(getString(R.string.bbdd_class));
        school = getIntent().getExtras().getString(getString(R.string.bbdd_center));

        preparingData();
    }

    public void preparingData(){
        //Obtener los alumnos de la misma clase
        Query getClassmates = aluRef.orderByChild(getString(R.string.bbdd_center)).equalTo(school);
        getClassmates.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                String name = values.get(getString(R.string.bbdd_name)) + " "+
                        values.get(getString(R.string.bbdd_lastname));
                String email = values.get(getString(R.string.bbdd_mail)).toString();
                if ((!email.equals(mail)) && (values.get(getString(R.string.bbdd_class)).equals(clase))) {
                    alus.add(name);
                }//if

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
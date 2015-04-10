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
import java.util.Set;

/**
 * Created by Gonzalo on 17/02/2015.
 */
public class TeachersActivity extends ListActivity {

    List<String> classTeachers;
    String clase, school;
    Firebase teacherRef;
    ArrayList<Teacher> teachers;

    public void onCreate (Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Firebase.setAndroidContext(this);

        teacherRef = new Firebase(getString(R.string.profeRef));
        classTeachers = new ArrayList<>();
        teachers = new ArrayList<>();

        //Obtenemos la Clase
        clase = getIntent().getExtras().getString(getString(R.string.bbdd_class));
        school = getIntent().getExtras().getString(getString(R.string.bbdd_center));

        preparingData();
    }

    public void preparingData(){
        //Obtener los profesores que dan esa clase
        //Query getClassmates = teacherRef.orderByChild(getString(R.string.bbdd_teacher_class));
        Query getClassTeachers = teacherRef.orderByChild(getString(R.string.bbdd_center)).equalTo(school);
        getClassTeachers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Log.i("TeachersActivity", "Values: "+values);
                Teacher teacher = new Teacher(values);
                String name = values.get(getString(R.string.bbdd_name)) + " "+
                        values.get(getString(R.string.bbdd_lastname));
                if ((teacher.getClassRooms().contains(clase)) && (!classTeachers.contains(name))) {
                    classTeachers.add(name);
                }//if
                //Seteamos el ArrayAdapter
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeachersActivity.this,
                        R.layout.list_item_layout,
                        classTeachers);
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
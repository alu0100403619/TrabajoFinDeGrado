package com.example.gonzalo.schoolapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.clases.Teacher;
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

    List<Teacher> classTeachers;
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

        //Listener para click largo
        this.getListView().setLongClickable(true);
        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TeachersActivity.this, DataActivity.class);
                intent.putExtra(getString(R.string.person), classTeachers.get(position));
                intent.putExtra(getString(R.string.rol), classTeachers.get(position).getRol());
                startActivity(intent);
                /*Toast.makeText(TeachersActivity.this, "LongClickData: " + classTeachers.get(position),
                        Toast.LENGTH_SHORT).show();//*/
                return true;
            }
        });

        //Listener para click normal
        this.getListView().setClickable(true);
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO Mandar a ChatActivity
                Toast.makeText(TeachersActivity.this, "ClickData: " + classTeachers.get(position),
                        Toast.LENGTH_SHORT).show();
            }
        });
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
                if ((teacher.getClassRooms().contains(clase)) && (!classTeachers.contains(teacher))) {
                    classTeachers.add(teacher);
                }//if
                //Seteamos el ArrayAdapter
                ArrayAdapter<Teacher> adapter = new ArrayAdapter<Teacher>(TeachersActivity.this,
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
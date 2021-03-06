package com.example.gonzalo.schoolapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.gonzalo.schoolapp.clases.Teacher;
import com.example.gonzalo.schoolapp.utilities.Utilities;
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
    String clase, school, myName, mail, myRol, myDNI;
    Firebase teacherRef;
    ArrayList<Teacher> teachers;

    public void onCreate (Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Firebase.setAndroidContext(this);

        teacherRef = new Firebase(getString(R.string.teacherRef));
        classTeachers = new ArrayList<>();
        teachers = new ArrayList<>();

        //Obtenemos la Clase
        clase = getIntent().getExtras().getString(getString(R.string.bbdd_class));
        school = getIntent().getExtras().getString(getString(R.string.bbdd_center));
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));
        myDNI = getIntent().getExtras().getString(getString(R.string.myDNI));

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
                return true;
            }
        });

        //Listener para click normal
        this.getListView().setClickable(true);
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = classTeachers.get(position).getName() + " " +
                        classTeachers.get(position).getLastname();
                Intent intent = new Intent(TeachersActivity.this, ChatActivity.class);
                intent.putExtra(getString(R.string.name), name);
                intent.putExtra(getString(R.string.mail), mail);
                intent.putExtra(getString(R.string.bbdd_dni_remitter), classTeachers.get(position).getDNI());
                intent.putExtra(getString(R.string.myName), myName);
                intent.putExtra(getString(R.string.myRol), myRol);
                intent.putExtra(getString(R.string.myDNI), myDNI);
                startActivity(intent);
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

    public void launchCirculares(View view) {
        Intent intent = new Intent(this, CircularesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Firebase rootref = new Firebase (getString(R.string.rootRef));
        Intent intent = new Intent(this, LoginActivity.class);
        rootref.unauth();
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(getString(R.string.classTeachers), (ArrayList<? extends Parcelable>) classTeachers);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        classTeachers = savedInstanceState.getParcelableArrayList(getString(R.string.classTeachers));
        ArrayAdapter<Teacher> adapter = new ArrayAdapter<Teacher>(this,
                R.layout.list_item_layout, classTeachers);
        setListAdapter(adapter);
    }
}
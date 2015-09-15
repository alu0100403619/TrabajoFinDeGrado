package com.example.gonzalo.schoolapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.gonzalo.schoolapp.clases.Student;
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
public class StudentActivity extends ListActivity {

    List<Student> alus;
    String mail, clase, school, myName, myRol, myDNI;
    Firebase aluRef;

    public void onCreate (Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Firebase.setAndroidContext(this);
        aluRef = new Firebase (getString(R.string.studentRef));
        alus = new ArrayList<>();
        clase = "";

        //Obtenemos el Mail y la Clase
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        clase = getIntent().getExtras().getString(getString(R.string.bbdd_class));
        school = getIntent().getExtras().getString(getString(R.string.bbdd_center));
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));
        myDNI = getIntent().getExtras().getString(getString(R.string.myDNI));

        preparingData();

        //Listener para click largo
        this.getListView().setLongClickable(true);
        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StudentActivity.this, DataActivity.class);
                intent.putExtra(getString(R.string.person), alus.get(position));
                intent.putExtra(getString(R.string.rol), alus.get(position).getRol());
                startActivity(intent);
                return true;
            }
        });

        //Listener para click normal
        this.getListView().setClickable(true);
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = alus.get(position).getName() + " " +
                        alus.get(position).getLastname();
                Intent intent = new Intent(StudentActivity.this, ChatActivity.class);
                intent.putExtra(getString(R.string.name), name);
                intent.putExtra(getString(R.string.mail), mail);
                intent.putExtra(getString(R.string.bbdd_dni_remitter), alus.get(position).getDNI());
                intent.putExtra(getString(R.string.myName), myName);
                intent.putExtra(getString(R.string.myDNI), myDNI);
                intent.putExtra(getString(R.string.myRol), myRol);
                intent.putExtra(getString(R.string.bbdd_dni_remitter), alus.get(position).getDNI());
                startActivity(intent);
            }
        });
    }

    public void preparingData(){
        //Obtener los Student de la misma clase
        Query getClassmates = aluRef.orderByChild(getString(R.string.bbdd_center)).equalTo(school);
        getClassmates.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Student alu = new Student(values);
                String dni = values.get(getString(R.string.bbdd_dni)).toString();
                if ((values.get(getString(R.string.bbdd_class)).equals(clase)) && (!alu.getMail().isEmpty()) &&
                        (!dni.equals(myDNI))) {
                    alus.add(alu);
                }//if

                //Seteamos el ArrayAdapter
                ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(StudentActivity.this,
                        R.layout.list_item_layout, alus);
                setListAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }//function

    @Override
    public void onBackPressed(){
        Log.i("StudentActivity", "Back Pressed");
        Firebase rootref = new Firebase (getString(R.string.rootRef));
        Intent intent = new Intent(this, LoginActivity.class);
        rootref.unauth();
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(getString(R.string.students), (ArrayList<? extends Parcelable>) alus);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        alus = savedInstanceState.getParcelableArrayList(getString(R.string.students));
        ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(this,
                R.layout.list_item_layout, alus);
        setListAdapter(adapter);
    }
}//class
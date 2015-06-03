package com.example.gonzalo.schoolapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.Adapters.NotifyAdapter;
import com.example.gonzalo.schoolapp.clases.Alumno;
import com.example.gonzalo.schoolapp.clases.Message;
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

    List<Alumno> alus;
    String mail, clase, school, myName, myRol;
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
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));

        preparingData();

        //Listener para click largo
        this.getListView().setLongClickable(true);
        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AlumnoActivity.this, DataActivity.class);
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
                Intent intent = new Intent(AlumnoActivity.this, Chat2Activity.class);
                intent.putExtra(getString(R.string.name), name);
                intent.putExtra(getString(R.string.mail), mail);
                intent.putExtra(getString(R.string.mail_remitter), alus.get(position).getMail());
                intent.putExtra(getString(R.string.myName), myName);
                intent.putExtra(getString(R.string.myRol), myRol);
                startActivity(intent);
            }
        });
    }

    public void preparingData(){
        //Obtener los alumnos de la misma clase
        Query getClassmates = aluRef.orderByChild(getString(R.string.bbdd_center)).equalTo(school);
        getClassmates.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Alumno alu = new Alumno(values);
                String email = values.get(getString(R.string.bbdd_mail)).toString();
                if ((!email.equals(mail)) && (values.get(getString(R.string.bbdd_class)).equals(clase))) {
                    alus.add(alu);
                }//if

                //Seteamos el ArrayAdapter
                ArrayAdapter<Alumno> adapter = new ArrayAdapter<Alumno>(AlumnoActivity.this,
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

}//class
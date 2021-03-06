package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.gonzalo.schoolapp.ExpandableListAdapter.ExpandableListAdapterAlumno;
import com.example.gonzalo.schoolapp.clases.Student;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExpandableAlumnosActivity extends Activity {

    ExpandableListAdapterAlumno listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<Student>> listDataChild;
    ArrayList<String> clases;
    Firebase alumnosRef;
    String school, myName, mail, myRol, myDNI;
    ArrayList<Student> students;
    TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_alumnos);
        Firebase.setAndroidContext(this);

        alumnosRef = new Firebase (getString(R.string.studentRef));
        clases = new ArrayList<>();
        students = new ArrayList<>();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Student>>();

        //Obtenemos las clases
        clases = getIntent().getExtras().getStringArrayList(getString(R.string.bbdd_teacher_class));
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));

        //Obtenemos el colegio
        school = getIntent().getExtras().getString(getString(R.string.bbdd_center));
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));
        myDNI = getIntent().getExtras().getString(getString(R.string.myDNI));

        //Obtener el elemento xml
        messageTextView = (TextView) findViewById(R.id.message);
        if (myRol.equals(getString(R.string.teacher))) {
            messageTextView.setText(getString(R.string.teacher_select_course));
        } else {
            messageTextView.setText(getString(R.string.father_select_course));
        }
        expListView = (ExpandableListView) findViewById(R.id.expListView);

        //Listener Click Largo
        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (expListView.getPackedPositionType(id) == expListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = expListView.getPackedPositionGroup(id);
                    int childPosition = expListView.getPackedPositionChild(id);
                    Intent intent = new Intent(ExpandableAlumnosActivity.this, DataActivity.class);
                    intent.putExtra(getString(R.string.person),
                            listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                    intent.putExtra(getString(R.string.rol),listDataChild.get(listDataHeader.
                            get(groupPosition)).get(childPosition).getRol());
                    startActivity(intent);
                    return true;
                }//if
                return false;
            }
        });

        //Listener Click Normal
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String name = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getName() + " " +
                        listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getLastname();
                Intent intent = new Intent(ExpandableAlumnosActivity.this, ChatActivity.class);
                intent.putExtra(getString(R.string.name), name);
                intent.putExtra(getString(R.string.mail), mail);
                intent.putExtra(getString(R.string.bbdd_dni_remitter),
                        listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getDNI());
                intent.putExtra(getString(R.string.myName), myName);
                intent.putExtra(getString(R.string.myDNI), myDNI);
                intent.putExtra(getString(R.string.myRol), myRol);
                startActivity(intent);
                return true;
            }
        });

        //Preparar los datos
        getAlumnos();
    }

    public void getAlumnos() {
        Query alusQuery = alumnosRef.orderByChild(getString(R.string.bbdd_center)).equalTo(school);
        alusQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Student student = new Student(values);
                //students.add(student);
                if (!student.getMail().isEmpty()) {
                    students.add(student);
                }
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
        });//teachersQuery
    }

    public void prepareListData() {
        for (int i = 0; i < clases.size(); i++) {
            List<Student> auxList = new ArrayList<>();
            //Adding Header Data
            if (!listDataChild.containsKey(clases.get(i))) {
                listDataHeader.add(clases.get(i));
            }//if
            for (Student student : students) {
                //Adding Child Data
                if ((student.getClassroom().equals(clases.get(i))) && (!auxList.contains(student))){
                    auxList.add(student);
                }//if
            }//for teacher
            listDataChild.put(listDataHeader.get(i), auxList);
        }//for clase

        //Adaptador
        listAdapter = new ExpandableListAdapterAlumno (this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }//function

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expandable_alumnos, menu);
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
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
        savedInstanceState.putStringArrayList(getString(R.string.listDataHeader), (ArrayList<String>) listDataHeader);
        savedInstanceState.putSerializable(getString(R.string.listDataChild), listDataChild);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        listDataHeader = savedInstanceState.getStringArrayList(getString(R.string.listDataHeader));
        listDataChild = (HashMap<String, List<Student>>) savedInstanceState.getSerializable(getString(R.string.listDataChild));
        listAdapter = new ExpandableListAdapterAlumno (this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

}

package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.ExpandableListAdapter.ExpandableListAdapterTeacher;
import com.example.gonzalo.schoolapp.clases.Teacher;
import com.example.gonzalo.schoolapp.utilities.Utilities;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExpandableTeachersActivity extends Activity {

    ExpandableListAdapterTeacher listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<Teacher>> listDataChild;
    ArrayList<String> clases;
    Firebase teachersRef;
    String mail, school, myName, myRol, myDNI;
    ArrayList<Teacher> teachers;
    TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_teachers);
        Firebase.setAndroidContext(this);

        teachersRef = new Firebase (getString(R.string.teacherRef));
        clases = new ArrayList<>();
        teachers = new ArrayList<>();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Teacher>>();

        //Obtenemos las clases
        clases = getIntent().getExtras().getStringArrayList(getString(R.string.bbdd_teacher_class));

        //Obtenemos el mail
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));
        myDNI = getIntent().getExtras().getString(getString(R.string.myDNI));

        //Obtenemos el colegio
        school = getIntent().getExtras().getString(getString(R.string.bbdd_center));

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
                    Intent intent = new Intent(ExpandableTeachersActivity.this, DataActivity.class);
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
                Intent intent = new Intent(ExpandableTeachersActivity.this, ChatActivity.class);
                intent.putExtra(getString(R.string.name), name);
                intent.putExtra(getString(R.string.mail), mail);
                intent.putExtra(getString(R.string.bbdd_dni_remitter),
                        listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getDNI());
                intent.putExtra(getString(R.string.myName), myName);
                intent.putExtra(getString(R.string.myRol), myRol);
                intent.putExtra(getString(R.string.myDNI), myDNI);
                startActivity(intent);
                return true;
            }
        });

        //Obtenemos los profes del centro
        getTeachers();
    }

    public void getTeachers () {
        Query teachersQuery = teachersRef.orderByChild(getString(R.string.bbdd_center)).equalTo(school);
        teachersQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Teacher teacher = new Teacher(values);
                if ((!teachers.contains(teacher)) && (!myDNI.equals(teacher.getDNI()))) {
                    teachers.add(teacher);
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
    }//function

    public void prepareListData() {
        for (int i = 0; i < clases.size(); i++) {
            List<Teacher> auxList = new ArrayList<>();
            //Adding Header Data
            if (!listDataChild.containsKey(clases.get(i))) {
                listDataHeader.add(clases.get(i));
            }//if
            for (Teacher teacher : teachers) {
                //Adding Child Data
                if ((teacher.getClassRooms().contains(clases.get(i))) && (!auxList.contains(teacher))){
                    auxList.add(teacher);
                }//if
            }//for teacher
            listDataChild.put(listDataHeader.get(i), auxList);
        }//for clase

        //Adaptador
        listAdapter = new ExpandableListAdapterTeacher (this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }//function*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expandable_teachers, menu);
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
        listDataChild = (HashMap<String, List<Teacher>>) savedInstanceState.getSerializable(getString(R.string.listDataChild));
        listAdapter = new ExpandableListAdapterTeacher(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }
}

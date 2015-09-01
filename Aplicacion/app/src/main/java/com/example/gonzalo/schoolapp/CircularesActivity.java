package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.gonzalo.schoolapp.clases.Alumno;
import com.example.gonzalo.schoolapp.clases.Date;
import com.example.gonzalo.schoolapp.clases.Father;
import com.example.gonzalo.schoolapp.clases.Message;
import com.example.gonzalo.schoolapp.database.MessageSQLHelper;
import com.example.gonzalo.schoolapp.utilities.Utilities;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class CircularesActivity extends Activity {

    String mySchool, myDNI, myName, myRol;
    ArrayList<String> myClasses, studentsSelected, fathersSelected;
    ArrayList<Alumno> cloudStudents;
    ArrayList<Father> cloudFathers;
    //Firebase fatherRef, studentRef;
    Firebase rootRef, messageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circulares);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Firebase.setAndroidContext(this);

        rootRef = new Firebase(getString(R.string.rootRef));
        messageRef = new Firebase(getString(R.string.messagesRef));
        myClasses = new ArrayList<>();
        studentsSelected = new ArrayList<>();
        fathersSelected = new ArrayList<>();
        cloudStudents = new ArrayList<>();
        cloudFathers = new ArrayList<>();

        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));
        myDNI = getIntent().getExtras().getString(getString(R.string.myDNI));
        mySchool = getIntent().getExtras().getString(getString(R.string.bbdd_center));
        myClasses = getIntent().getExtras().getStringArrayList(getString(R.string.bbdd_teacher_class));

        getCloudData();
    }

    public void getCloudData() {
        Query getData = rootRef.orderByChild(getString(R.string.bbdd_center));
        getData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String keyBBDD = dataSnapshot.getKey();
                if (keyBBDD.equals(getString(R.string._students))) {
                    Map<String, Object> studentsData = (Map<String, Object>) dataSnapshot.getValue();
                    Set<String> keyStudents = studentsData.keySet();
                    for (String key: keyStudents) {
                        Map<String, Object> studentValue = (Map<String, Object>)studentsData.get(key);
                        //studentValue = {telefono=632414874, nombre=Victor, centro=IES Buenavista, mail=alu01004@gmail.com, Apellido=Cejas Garcia, clase=1B, dni=72554476G}
                        String studentSchool = studentValue.get(getString(R.string.bbdd_center)).toString();
                        String studentClass = studentValue.get(getString(R.string.bbdd_class)).toString();
                        if ((studentSchool.equals(mySchool)) && (myClasses.contains(studentClass))) {
                            Alumno student = new Alumno(studentValue);
                            if (!cloudStudents.contains(student)) {
                                cloudStudents.add(student);
                            }//if !contains
                        }//if school and class
                    }//for key
                } else if (keyBBDD.equals(getString(R.string._fathers))) {
                    Map<String, Object> fathersData = (Map<String, Object>) dataSnapshot.getValue();
                    Set<String> keyFathers = fathersData.keySet();
                    for (String key: keyFathers) {
                        Map<String, Object> fatherValue = (Map<String, Object>)fathersData.get(key);
                        //Log.i("CAF", "fatherValue: "+fatherValue);
                        //String fatherSchool = fatherValue.get(getString(R.string.bbdd_center)).toString();
                        Father father = new Father(fatherValue);
                        //if (fatherSchool.equals(mySchool)) {
                        if (father.getSchools().contains(mySchool)) {
                            ArrayList <String> fatherClasses = father.getClassrooms();
                            if ((Utilities.containsSomethingString(myClasses, fatherClasses)) &&
                            (!cloudFathers.contains(father))) {
                                cloudFathers.add(father);
                            }//containsSomethingString
                        }//if school
                    }//for key
                }//if else key
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });//addChild...
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_circulares, menu);
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


    public void launchClassSelector (View view) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View selectorView = layoutInflater.inflate(R.layout.class_selector_dialog, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setView(selectorView);
        dialog.setCancelable(false);

        //Annadiendo las Tabs
        TabHost tabHost = (TabHost) selectorView.findViewById(R.id.tabhost);
        tabHost.setup();

        //Students Tab
        TabHost.TabSpec studentsTab = tabHost.newTabSpec(getString(R.string.tab_alumnos));
        studentsTab.setContent(R.id.studentsListView);
        studentsTab.setIndicator(getString(R.string.tab_alumnos));
        tabHost.addTab(studentsTab);

        //Fathers Tab
        TabHost.TabSpec fathersTab = tabHost.newTabSpec(getString(R.string.tab_fathers));
        fathersTab.setContent(R.id.fathersListView);
        fathersTab.setIndicator(getString(R.string.tab_fathers));
        tabHost.addTab(fathersTab);

        tabHost.setCurrentTab(0);

        //Students ListView
        studentsSelected.clear();
        ListView studentsListView = (ListView) selectorView.findViewById(R.id.studentsListView);
        ArrayAdapter<String> studentsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, myClasses);
        studentsListView.setAdapter(studentsAdapter);
        studentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("CA", "Students ItemSelected: "+myClasses.get(position));
                if (!studentsSelected.contains(myClasses.get(position))) {
                    studentsSelected.add(myClasses.get(position));
                } else {
                    studentsSelected.remove(myClasses.get(position));
                }
            }
        });

        //Fathers ListView
        fathersSelected.clear();
        ListView fathersListView = (ListView) selectorView.findViewById(R.id.fathersListView);
        ArrayAdapter<String> fathersAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, myClasses);
        fathersListView.setAdapter(fathersAdapter);
        fathersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("CA", "Fathers ItemSelected: " + myClasses.get(position));
                if (!fathersSelected.contains(myClasses.get(position))) {
                    fathersSelected.add(myClasses.get(position));
                } else {
                    fathersSelected.remove(myClasses.get(position));
                }
            }
        });

        //Dialog
        dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView contactsTextView = (TextView) findViewById(R.id.editTextContact);
                String classesSelected = getString(R.string.alumnos) + ": ";
                for (int i = 0; i < studentsSelected.size(); i++) {
                    if (i < (studentsSelected.size() - 1)) {
                        classesSelected += studentsSelected.get(i) + ", ";
                    } else {
                        classesSelected += studentsSelected.get(i);
                    }
                }
                classesSelected += "\n";
                classesSelected += getString(R.string.teachers) + ": ";
                for (int i = 0; i < fathersSelected.size(); i++) {
                    if (i < (fathersSelected.size() - 1)) {
                        classesSelected += fathersSelected.get(i) + ", ";
                    } else {
                        classesSelected += fathersSelected.get(i);
                    }
                }
                contactsTextView.setText(classesSelected);
            }
        }).setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void SendMessage (View view) {
        //BBDD Local, en el dispositivo
        MessageSQLHelper messageBBDD = new MessageSQLHelper(this);
        //Mensaje a mandar
        String messageToSend = ((EditText) findViewById(R.id.editText_message)).getText().toString().trim();
        //Obtenemos la fecha
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.get(calendar.DAY_OF_MONTH),
                calendar.get(calendar.MONTH) + 1, calendar.get(calendar.YEAR),
                calendar.getTime().getHours(), calendar.getTime().getMinutes());
        Map<String, Object> dateMap = new HashMap<>();
        dateMap.put(getString(R.string.bbdd_date_year), date.getYear());
        dateMap.put(getString(R.string.bbdd_date_month), date.getMonth());
        dateMap.put(getString(R.string.bbdd_date_day), date.getDay());
        dateMap.put(getString(R.string.bbdd_date_hour), date.getHour());
        dateMap.put(getString(R.string.bbdd_date_minutes), date.getMinutes());
        //Creamos el mensaje
        Message message = new Message(myDNI, myName, messageToSend, date, myRol);

        for (Alumno student: cloudStudents) {
            String studentDNI = student.getDNI();
            String studentSchool = student.getSchool();
            String studentClass = student.getClassroom();
            if ((mySchool.equals(studentSchool)) && (studentsSelected.contains(studentClass))) {
                Log.i("CircularesActivity", "Entro por students contains");
                //Add mensaje a la BBDD local
                String idConversation = messageBBDD.getIdConversation(studentDNI);
                if (idConversation != null) {
                    messageBBDD.addMessage(message, idConversation);
                } else {
                    messageBBDD.addConversation(myDNI, studentDNI);
                    idConversation = messageBBDD.getIdConversation(studentDNI);
                    messageBBDD.addMessage(message, idConversation);
                }//if null
                //Metemos los datos comunes en el mismo mapa
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put(getString(R.string.bbdd_addressee), studentDNI);//destinatario
                messageMap.put(getString(R.string.bbdd_dni_remitter), myDNI);//dni_remitente
                messageMap.put(getString(R.string.bbdd_date), dateMap);//fecha
                messageMap.put(getString(R.string.bbdd_message), message.getMessage());//mensaje
                messageMap.put(getString(R.string.bbdd_remitter), myName);//remitente
                messageMap.put(getString(R.string.bbdd_rol_remitter), myRol);//tol_remitente
                //Mandamos el mensaje a la nube
                String uuid = UUID.randomUUID().toString();
                messageRef.child(uuid).setValue(messageMap);
                Log.i("CircularesActivity", "Alu Mensage Enviado a "+student.getName()+" "+student.getLastname());
            }//contains*/
        }//for Alumno
        for (Father father: cloudFathers) {
            String fatherDNI = father.getDNI();
            if ((father.getSchools().contains(mySchool)) &&
                    (Utilities.containsSomethingString(fathersSelected, father.getClassrooms()))) {
                //Add mensaje a la BBDD local
                String idConversation = messageBBDD.getIdConversation(fatherDNI);
                if (idConversation != null) {
                    messageBBDD.addMessage(message, idConversation);
                } else {
                    messageBBDD.addConversation(myDNI, fatherDNI);
                    idConversation = messageBBDD.getIdConversation(fatherDNI);
                    messageBBDD.addMessage(message, idConversation);
                }//if null
                //Metemos los datos comunes en el mismo mapa
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put(getString(R.string.bbdd_addressee), fatherDNI);//destinatario
                messageMap.put(getString(R.string.bbdd_dni_remitter), myDNI);//dni_remitente
                messageMap.put(getString(R.string.bbdd_date), dateMap);//fecha
                messageMap.put(getString(R.string.bbdd_message), message.getMessage());//mensaje
                messageMap.put(getString(R.string.bbdd_remitter), myName);//remitente
                messageMap.put(getString(R.string.bbdd_rol_remitter), myRol);//tol_remitente
                //Mandamos el mensaje a la nube
                String uuid = UUID.randomUUID().toString();
                messageRef.child(uuid).setValue(messageMap);
                Log.i("CircularesActivity", "Padre Mensage Enviado a "+father.getName()+" "+father.getLastname());
            }//contains
        }//for Father
        Log.i("CircularesActivity", "Circular Enviada");
        finish();
    }//function

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void back(View view) {
        onBackPressed();
    }
}

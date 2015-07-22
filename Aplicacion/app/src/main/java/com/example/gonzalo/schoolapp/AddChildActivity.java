package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.clases.Alumno;
import com.example.gonzalo.schoolapp.utilities.Utilities;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class AddChildActivity extends Activity {

    Firebase childRef, schoolsRef;
    Spinner spinner;
    ArrayList<String> schools;
    AlertDialog dialog;
    ArrayAdapter<String> spinnerAdapter;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        Firebase.setAndroidContext(this);

        childRef = new Firebase (getString(R.string.studentRef));
        schoolsRef = new Firebase (getString(R.string.schoolsRef));
        spinner = (Spinner) findViewById(R.id.spinner_2);

        schools = getSchools();

        schools.add(getString(R.string.select_school));
        schools.add(getString(R.string.add_school));
        spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, schools);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = String.valueOf(spinner.getSelectedItem());
                if (selected.equals(getString(R.string.add_school))) {
                    launchPrompt();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_child, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addChild (View vew) {
        if (!haveEmptyFields()) {
            final EditText mailEditText = (EditText) findViewById(R.id.text_mail);
            String mail = mailEditText.getText().toString();
            Query existStudent = childRef.orderByChild(getString(R.string.bbdd_mail)).equalTo(mail);
            existStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Student not Exist
                    if (dataSnapshot.getValue() == null) {
                        EditText nameEditText = (EditText) findViewById(R.id.text_name);
                        EditText lastnameEditText = (EditText) findViewById(R.id.text_lastname);
                        EditText telephoneEditText = (EditText) findViewById(R.id.text_telephone);
                        EditText classroomEditText = (EditText) findViewById(R.id.text_course_group);
                        String school = spinner.getSelectedItem().toString();

                        Map<String, Object> studentMap = new HashMap<String, Object>();
                        studentMap.put(getString(R.string.bbdd_name), nameEditText.getText().toString());
                        studentMap.put(getString(R.string.bbdd_lastname), lastnameEditText.getText().toString());
                        studentMap.put(getString(R.string.bbdd_telephone), telephoneEditText.getText().toString());
                        studentMap.put(getString(R.string.bbdd_center), school);
                        studentMap.put(getString(R.string.bbdd_class), classroomEditText.getText().toString());
                        studentMap.put(getString(R.string.bbdd_mail), mailEditText.getText().toString());
                        Alumno student = new Alumno(studentMap);
                        returnChild(student, false, null);
                    }
                    //Student Exist
                    else {
                        Map<String, Object> dataSnapshotMap = (Map<String, Object>) dataSnapshot.getValue();
                        key = (dataSnapshotMap.keySet().toArray())[0].toString();
                        Map<String, Object> studentMap = (Map<String, Object>) dataSnapshotMap.get(key);
                        Alumno student = new Alumno(studentMap);
                        returnChild(student, true, key);
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {}
            });//existStudent
        }
    }

    public void returnChild (Alumno student, boolean exist, String childKey) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.bbdd_children), student);
        returnIntent.putExtra(getString(R.string.exist), exist);
        if (childKey != null) {
            returnIntent.putExtra(getString(R.string.key), childKey);
        }
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    public boolean haveEmptyFields() {
        EditText nameEditText = (EditText) findViewById(R.id.text_name);
        EditText lastnameEditText = (EditText) findViewById(R.id.text_lastname);
        EditText telephoneEditText = (EditText) findViewById(R.id.text_telephone);
        EditText classroomEditText = (EditText) findViewById(R.id.text_course_group);
        EditText mailEditText = (EditText) findViewById(R.id.text_mail);

        String name = nameEditText.getText().toString();
        String lastname = lastnameEditText.getText().toString();
        String telephone = telephoneEditText.getText().toString();
        String classroom = classroomEditText.getText().toString();
        String mail = mailEditText.getText().toString();
        String school = ((Spinner) findViewById(R.id.spinner_2)).getSelectedItem().toString();
        boolean haveEmptyFields = false;

        if (name.isEmpty()) {
            Log.i("RegStudAct", "Name Empty");
            nameEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        }
        if (lastname.isEmpty()) {
            Log.i("RegStudAct", "Lastame Empty");
            lastnameEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        }
        if ((!telephone.isEmpty()) && (!Utilities.isTelephone(telephone))) {
            Log.i("RegStudAct", "Is Not Telephone");
            telephoneEditText.setError(getString(R.string.telephone_format_error));
            haveEmptyFields = true;
        }
        if (classroom.isEmpty()) {
            Log.i("RegStudAct", "classroom Empty");
            classroomEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        } else if (!Utilities.isOneClass(classroom)) {
            Log.i("RegStudAct", "Is not One Class");
            classroomEditText.setError(getString(R.string.error_class_format));
            haveEmptyFields = true;
        }
        if (mail.isEmpty()) {
            Log.i("RegStudAct", "Mail Empty");
            mailEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        }
        else if (!Utilities.isMail(mail)) {
            Log.i("RegStudAct", "Is not Mail");
            mailEditText.setError(getString(R.string.mail_format_error));
            haveEmptyFields = true;
        }
        if ((school.isEmpty()) || (school.equals(getString(R.string.add_school))) ||
                (school.equals(getString(R.string.select_school)))) {
            Log.i("RegStudAct", "school Empty");
            Toast.makeText(this, getString(R.string.select_school), Toast.LENGTH_LONG).show();
            haveEmptyFields = true;
        }
        return haveEmptyFields;
    }

    public ArrayList<String> getSchools() {
        final ArrayList<String> tmp = new ArrayList<>();
        Query allSchools = schoolsRef;
        allSchools.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("RegisterActivity", "value: " + dataSnapshot.getValue());
                //String key = dataSnapshot.getKey();
                if (!tmp.contains(dataSnapshot.getValue().toString())) {
                    tmp.add(dataSnapshot.getValue().toString());
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });//query
        return tmp;
    }

    public void launchPrompt() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompt.xml to alertdialog builder
        alertDialogBuilder.setView(promptView);

        final EditText schoolEditText = (EditText) promptView.findViewById(R.id.schoolEditText);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.save),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                String school = schoolEditText.getText().toString();
                                if (school.isEmpty()) {
                                    schoolEditText.setError(getString(R.string.field_empty));
                                } else {
                                    String uuid = UUID.randomUUID().toString();
                                    schoolsRef.child(uuid).setValue(school);
                                    schools.add(school);
                                    spinner.setSelection(spinnerAdapter.getPosition(school));
                                }
                            }
                        })
                .setNegativeButton(getString(R.string.back),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void back (View view) {
        setResult(RESULT_CANCELED,null);
        finish();
    }

    public void launchHelp (View view) {
        createHelpDialog();
        dialog.show();
    }

    public void createHelpDialog() {
        //Constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Annadiendo Botones
        builder.setPositiveButton(R.string.help_ok_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });//builder.setpossitive...
        //Set titulo y mensaje
        builder.setMessage(R.string.course_group_help).setTitle(R.string.help_dialog_title);
        //Creando dialogo de alerta
        dialog = builder.create();
    }
}
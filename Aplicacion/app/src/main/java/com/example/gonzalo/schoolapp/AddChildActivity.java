package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.clases.Student;
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
    Spinner spinner, classSpinner;
    ArrayList<String> schools, schoolsKeys, classSchool;
    AlertDialog dialog;
    ArrayAdapter<String> spinnerAdapter, classAdapter;
    String key, schoolKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        Firebase.setAndroidContext(this);

        childRef = new Firebase (getString(R.string.studentRef));
        schoolsRef = new Firebase (getString(R.string.schoolsRef));
        spinner = (Spinner) findViewById(R.id.spinner_2);
        classSpinner = (Spinner) findViewById(R.id.classSpinner);

        schoolsKeys = new ArrayList<>();
        classSchool = new ArrayList<>();

        schools = getSchools();
        schools.add(getString(R.string.select_school));
        schoolsKeys.add(getString(R.string.invalid_key));
        schools.add(getString(R.string.add_school));
        schoolsKeys.add(getString(R.string.invalid_key));
        spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, schools);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = String.valueOf(spinner.getSelectedItem());
                schoolKey = schoolsKeys.get(position);
                if (selected.equals(getString(R.string.add_school))) {
                    launchPrompt();
                } else if ((!selected.equals(getString(R.string.add_school))) &&
                        (!selected.equals(getString(R.string.select_school)))) {
                    classSchool.clear();
                    classSchool = getClassrooms(schoolsKeys.get(position));
                    classSchool.add(0, getString(R.string.add_class));
                    classSchool.add(0, getString(R.string.select_class));
                    classAdapter = new ArrayAdapter<String>(AddChildActivity.this, android.R.layout.simple_spinner_item, classSchool);
                    classSpinner.setAdapter(classAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Set the classSpinner
        classSchool.add(getString(R.string.select_class));
        classSchool.add(getString(R.string.add_class));
        classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classSchool);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        classSpinner.setAdapter(classAdapter);
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = classSpinner.getSelectedItem().toString();
                if (selected.equals(getString(R.string.add_class))) {
                    launchClassPrompt();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }//function

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
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addChild (View vew) {
        if (!haveEmptyFields()) {
            final EditText mailEditText = (EditText) findViewById(R.id.text_mail);
            EditText letterNieEditText = (EditText) findViewById(R.id.letterNIE);
            EditText dniEditText = (EditText) findViewById(R.id.DNI);
            EditText letterDniEditText = (EditText) findViewById(R.id.letterDNI);

            String mail = mailEditText.getText().toString();
            String dniStudent = letterNieEditText.getText().toString() + dniEditText.getText().toString()
                    + letterDniEditText.getText().toString();
            Query existStudent = childRef.orderByChild(getString(R.string.bbdd_dni)).equalTo(dniStudent);
            existStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Student not Exist
                    if (dataSnapshot.getValue() == null) {
                        EditText nameEditText = (EditText) findViewById(R.id.text_name);
                        EditText lastnameEditText = (EditText) findViewById(R.id.text_lastname);
                        EditText telephoneEditText = (EditText) findViewById(R.id.text_telephone);
                        String classroom = classSpinner.getSelectedItem().toString();
                        String school = spinner.getSelectedItem().toString();
                        String dni = ((EditText) findViewById(R.id.letterNIE)).getText().toString()
                                + ((EditText) findViewById(R.id.DNI)).getText().toString()
                                + ((EditText) findViewById(R.id.letterDNI)).getText().toString();

                        Map<String, Object> studentMap = new HashMap<String, Object>();
                        studentMap.put(getString(R.string.bbdd_name), nameEditText.getText().toString());
                        studentMap.put(getString(R.string.bbdd_lastname), lastnameEditText.getText().toString());
                        studentMap.put(getString(R.string.bbdd_telephone), telephoneEditText.getText().toString());
                        studentMap.put(getString(R.string.bbdd_dni), dni);
                        studentMap.put(getString(R.string.bbdd_center), school);
                        studentMap.put(getString(R.string.bbdd_class), classroom);
                        studentMap.put(getString(R.string.bbdd_mail), mailEditText.getText().toString());
                        Student student = new Student(studentMap);
                        Log.i("AddChildActivity", "Student not exist");
                        returnChild(student, false, null);
                    }
                    //Student Exist
                    else {
                        Map<String, Object> dataSnapshotMap = (Map<String, Object>) dataSnapshot.getValue();
                        key = (dataSnapshotMap.keySet().toArray())[0].toString();
                        Map<String, Object> studentMap = (Map<String, Object>) dataSnapshotMap.get(key);
                        Student student = new Student(studentMap);
                        Log.i("AddChildActivity", "Student exist: " + student.getName());
                        returnChild(student, true, key);
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {}
            });//existStudent
        }
    }

    public void returnChild (Student student, boolean exist, String childKey) {
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
        EditText mailEditText = (EditText) findViewById(R.id.text_mail);
        EditText letterNieEditText = (EditText) findViewById(R.id.letterNIE);
        EditText dniEditText = (EditText) findViewById(R.id.DNI);
        EditText letterDniEditText = (EditText) findViewById(R.id.letterDNI);

        String name = nameEditText.getText().toString();
        String lastname = lastnameEditText.getText().toString();
        String telephone = telephoneEditText.getText().toString();
        String classroom = ((Spinner) findViewById(R.id.classSpinner)).getSelectedItem().toString();
        String mail = mailEditText.getText().toString();
        String school = ((Spinner) findViewById(R.id.spinner_2)).getSelectedItem().toString();
        String dni = letterNieEditText.getText().toString() + dniEditText.getText().toString()
                + letterDniEditText.getText().toString();
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
        if (dni.isEmpty()) {
            dniEditText.setError(getString(R.string.field_empty));
            letterDniEditText.setError(getString(R.string.field_empty));
            Toast.makeText(this, getString(R.string.field_empty), Toast.LENGTH_LONG).show();
            haveEmptyFields = true;
        } else if ((!Utilities.isDNI(dni)) && (!Utilities.isNIE(dni))) {
            dniEditText.setError(getString(R.string.dni_format_error));
            letterDniEditText.setError(getString(R.string.dni_format_error));
            Toast.makeText(this, getString(R.string.dni_format_error), Toast.LENGTH_LONG).show();
            haveEmptyFields = true;
        }
        if ((classroom.isEmpty()) || (classroom.equals(getString(R.string.add_class))) ||
                (classroom.equals(getString(R.string.select_class)))) {
            Log.i("RegStudAct", "classroom Empty");
            TextView classroomTextView = (TextView) findViewById(R.id.label_spinner_2);
            classroomTextView.setTextColor(getResources().getColor(R.color.Red));
            Toast.makeText(this, getString(R.string.select_class), Toast.LENGTH_LONG).show();
            haveEmptyFields = true;
        }
        if ((!mail.isEmpty()) && (!Utilities.isMail(mail))) {
            Log.i("RegStudAct", "Is not Mail");
            mailEditText.setError(getString(R.string.mail_format_error));
            haveEmptyFields = true;
        }
        if ((school.isEmpty()) || (school.equals(getString(R.string.add_school))) ||
                (school.equals(getString(R.string.select_school)))) {
            Log.i("RegStudAct", "school Empty");
            TextView schoolTextView = (TextView) findViewById(R.id.class_label);
            schoolTextView.setTextColor(getResources().getColor(R.color.Red));
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
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                String name = values.get(getString(R.string.bbdd_name)).toString();
                if (!schoolsKeys.contains(dataSnapshot.getKey().toString())) {
                    schoolsKeys.add(dataSnapshot.getKey().toString());
                }//if key
                if (!tmp.contains(name)) {
                    tmp.add(name);
                    Log.i("StudentRegisterActivity", "Annadida: " + name);
                }//if name
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

    public ArrayList<String> getClassrooms (String key) {
        final ArrayList<String> tmp = new ArrayList<>();
        Query allClassSchool = schoolsRef.child(key).child(getString(R.string.bbdd_teacher_class));
        allClassSchool.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String classroom = dataSnapshot.getValue().toString();
                if (!tmp.contains(classroom)) {
                    tmp.add(classroom);
                    Log.i("StudentRegisterActivity", "Clase Annadida: " + classroom);
                }//if name
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
                                    schoolsKeys.add(uuid);
                                    schoolKey = uuid;
                                    spinner.setSelection(spinnerAdapter.getPosition(school));
                                    //obtener clases de la escuela nueva annadida
                                    classSchool.clear();
                                    classSchool = getClassrooms(uuid);
                                    classSchool.add(0, getString(R.string.add_class));
                                    classSchool.add(0, getString(R.string.select_class));
                                    classAdapter = new ArrayAdapter<String>(AddChildActivity.this, android.R.layout.simple_spinner_item, classSchool);
                                    classSpinner.setAdapter(classAdapter);
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

    public void launchClassPrompt() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.classprompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompt.xml to alertdialog builder
        alertDialogBuilder.setView(promptView);

        final EditText classEditText = (EditText) promptView.findViewById(R.id.classEditText);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.save),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                String classroom = classEditText.getText().toString();
                                if (classroom.isEmpty()) {
                                    classEditText.setError(getString(R.string.field_empty));
                                } else if (!Utilities.isOneClass(classroom)) {
                                    classEditText.setError(getString(R.string.error_class_format));
                                } else {
                                    String uuid = UUID.randomUUID().toString();
                                    schoolsRef.child(schoolKey)
                                            .child(getString(R.string.bbdd_teacher_class))
                                            .child(uuid).setValue(classroom);

                                    classSchool.add(classroom);
                                    classSpinner.setSelection(classAdapter.getPosition(classroom));
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

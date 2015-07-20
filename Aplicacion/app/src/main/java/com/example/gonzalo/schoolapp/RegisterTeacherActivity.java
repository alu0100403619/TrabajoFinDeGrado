package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.utilities.Utilities;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class RegisterTeacherActivity extends Activity {

    Firebase teacherRef, rootRef, schoolsRef;
    AlertDialog dialog, alertDialog;
    Spinner spinner;
    ArrayList<String> schools, teachersClasses;
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);
        Firebase.setAndroidContext(this);

        teacherRef = new Firebase (getString(R.string.profeRef));
        rootRef = new Firebase (getString(R.string.rootRef));
        schoolsRef = new Firebase (getString(R.string.colesRef));
        spinner = (Spinner) findViewById(R.id.spinner_2);

        teachersClasses = new ArrayList<>();
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
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_teacher, menu);
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

    public boolean haveEmptyFields() {
        EditText nameEditText = (EditText) findViewById(R.id.text_name);
        EditText lastnameEditText = (EditText) findViewById(R.id.text_lastname);
        EditText telephoneEditText = (EditText) findViewById(R.id.text_telephone);
        EditText classroomEditText = (EditText) findViewById(R.id.text_course_group);
        EditText mailEditText = (EditText) findViewById(R.id.text_mail);
        EditText passwordEditText = (EditText) findViewById(R.id.text_password);

        String name = nameEditText.getText().toString();
        String lastname = lastnameEditText.getText().toString();
        String telephone = telephoneEditText.getText().toString();
        String classroom = classroomEditText.getText().toString();
        String mail = mailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
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
        if (telephone.isEmpty()) {
            Log.i("RegStudAct", "Telephone Empty");
            telephoneEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        } else if (!Utilities.isTelephone(telephone)) {
            Log.i("RegStudAct", "Is Not Telephone");
            telephoneEditText.setError(getString(R.string.telephone_format_error));
            haveEmptyFields = true;
        }
        if (classroom.isEmpty()) {
            Log.i("RegStudAct", "classroom Empty");
            classroomEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        } else if (!Utilities.areManyClasses(classroom)) {
            Log.i("RegStudAct", "Are not Many Class");
            classroomEditText.setError(getString(R.string.error_classes_format));
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
        if (password.isEmpty()) {
            Log.i("RegStudAct", "pass Empty");
            passwordEditText.setError(getString(R.string.field_empty));
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

    public void submit (View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if (!haveEmptyFields()) {
            showLoading();
            final String mail = ((EditText) findViewById(R.id.text_mail)).getText().toString();
            final String password = ((EditText) findViewById(R.id.text_password)).getText().toString();
            rootRef.createUser(mail, password, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    final String name = ((EditText) findViewById(R.id.text_name)).getText().toString();
                    String lastname = ((EditText) findViewById(R.id.text_lastname)).getText().toString();
                    String telephone = ((EditText) findViewById(R.id.text_telephone)).getText().toString();
                    String classroom = ((EditText) findViewById(R.id.text_course_group)).getText().toString();
                    final String school = ((Spinner) findViewById(R.id.spinner_2)).getSelectedItem().toString();
                    teachersClasses = separateTeacherClasses(classroom);

                    Map<String, Object> teacherMap = new HashMap<>();
                    Map<String, Object> schoolsMap = new HashMap<>();
                    teacherMap.put(getString(R.string.bbdd_name), name);
                    teacherMap.put(getString(R.string.bbdd_lastname), lastname);
                    teacherMap.put(getString(R.string.bbdd_telephone), telephone);
                    teacherMap.put(getString(R.string.bbdd_mail), mail);
                    teacherMap.put(getString(R.string.bbdd_center), school);
                    for (String clas: teachersClasses) {
                        String uuid = UUID.randomUUID().toString();
                        schoolsMap.put(uuid, clas);
                    }
                    teacherMap.put(getString(R.string.bbdd_teacher_class), schoolsMap);

                    String uuid = UUID.randomUUID().toString();
                    Log.i("RegStudAct", "Map: "+teacherMap);
                    teacherRef.child(uuid).setValue(teacherMap);

                    //Lanzando TeachersTabActivity
                    rootRef.authWithPassword(mail, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Intent intent = new Intent(RegisterTeacherActivity.this, TeachersTabActivity.class);
                            intent.putExtra(getString(R.string.bbdd_mail), mail);
                            intent.putExtra(getString(R.string.bbdd_teacher_class), teachersClasses);
                            intent.putExtra(getString(R.string.bbdd_center), school);
                            intent.putExtra(getString(R.string.myName), name);
                            intent.putExtra(getString(R.string.myRol), getString(R.string.rol_student));
                            alertDialog.dismiss();
                            startActivity(intent);
                            RegisterTeacherActivity.this.finish();
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Toast.makeText(RegisterTeacherActivity.this,
                                    getString(R.string.login_error)
                                            + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterTeacherActivity.this, LoginActivity.class);
                            alertDialog.dismiss();
                            startActivity(intent);
                            finish();
                        }
                    });
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    alertDialog.dismiss();
                    Toast.makeText(RegisterTeacherActivity.this, getString(R.string.login_error) + " " +
                            firebaseError, Toast.LENGTH_LONG).show();
                }
            });//rootRef
        }//if
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

    public void showLoading () {
        //loading
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View loadView = layoutInflater.inflate(R.layout.load, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set load.xml to alertdialog builder
        alertDialogBuilder.setView(loadView);

        ImageView imageViewRotator = (ImageView) loadView.findViewById(R.id.ImageViewRotator);
        Animation myRotation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_rotate_pencil);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        //start the animation
        imageViewRotator.startAnimation(myRotation);

        // show it
        alertDialog.show();
    }

    public ArrayList<String> separateTeacherClasses (String classes) {
        ArrayList<String> tmpArrayList = new ArrayList<>();

        String tmp = classes.replaceAll(" ", "");
        String[] tmpArray = tmp.split(",");
        for (String tmpClass: tmpArray) {
            if (!tmpArrayList.contains(tmpClass)) {
                tmpArrayList.add(tmpClass);
            }
        }
        return tmpArrayList;
    }
}

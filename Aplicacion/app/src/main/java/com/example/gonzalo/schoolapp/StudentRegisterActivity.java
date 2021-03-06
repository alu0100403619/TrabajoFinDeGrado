package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.utilities.Utilities;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class StudentRegisterActivity extends Activity {

    Firebase studentRef, rootRef, schoolsRef;
    AlertDialog dialog, alertDialog;
    Spinner spinner, classSpinner;
    ArrayList<String> schools, schoolsKeys, classSchool;
    ArrayAdapter<String> spinnerAdapter, classAdapter;
    String schoolKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);
        Firebase.setAndroidContext(this);

        studentRef = new Firebase (getString(R.string.studentRef));
        rootRef = new Firebase (getString(R.string.rootRef));
        schoolsRef = new Firebase (getString(R.string.schoolsRef));
        spinner = (Spinner) findViewById(R.id.spinner_2);
        classSpinner = (Spinner) findViewById(R.id.classSpinner);

        schoolsKeys = new ArrayList<>();
        classSchool = new ArrayList<>();

        //Set the spinner
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
                    classAdapter = new ArrayAdapter<String>(StudentRegisterActivity.this, android.R.layout.simple_spinner_item, classSchool);
                    classSpinner.setAdapter(classAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
        getMenuInflater().inflate(R.menu.menu_register_student, menu);
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
        EditText mailEditText = (EditText) findViewById(R.id.text_mail);
        EditText passwordEditText = (EditText) findViewById(R.id.text_password);
        EditText passwordRepeatedEditText = (EditText) findViewById(R.id.text_repeated_password);
        EditText letterNieEditText = (EditText) findViewById(R.id.letterNIE);
        EditText dniEditText = (EditText) findViewById(R.id.DNI);
        EditText letterDniEditText = (EditText) findViewById(R.id.letterDNI);

        String name = nameEditText.getText().toString();
        String lastname = lastnameEditText.getText().toString();
        String telephone = telephoneEditText.getText().toString();
        String classroom = ((Spinner) findViewById(R.id.classSpinner)).getSelectedItem().toString();
        String mail = mailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String repeatedPassword = passwordRepeatedEditText.getText().toString();
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
        if (telephone.isEmpty()) {
            Log.i("RegStudAct", "Telephone Empty");
            telephoneEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        } else if (!Utilities.isTelephone(telephone)) {
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
            TextView classroomTextView = (TextView) findViewById(R.id.class_label);
            classroomTextView.setTextColor(getResources().getColor(R.color.Red));
            Toast.makeText(this, getString(R.string.select_school), Toast.LENGTH_LONG).show();
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
        if (repeatedPassword.isEmpty()) {
            Log.i("RegStudAct", "pass Empty");
            passwordRepeatedEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        }
        if (!password.equals(repeatedPassword)) {
            Log.i("RegStudAct", "pass no Equal");
            passwordEditText.setError(getString(R.string.password_not_equals));
            passwordRepeatedEditText.setError(getString(R.string.password_not_equals));
            haveEmptyFields = true;
        }
        if ((school.isEmpty()) || (school.equals(getString(R.string.add_school))) ||
                (school.equals(getString(R.string.select_school)))) {
            Log.i("RegStudAct", "school Empty");
            TextView schoolTextView = (TextView) findViewById(R.id.label_spinner_2);
            schoolTextView.setTextColor(getResources().getColor(R.color.Red));
            Toast.makeText(this, getString(R.string.select_class), Toast.LENGTH_LONG).show();
            haveEmptyFields = true;
        }
        return haveEmptyFields;
    }

    public void submit (View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if (!haveEmptyFields()) {
            showLoading();
            final String mail = ((EditText) findViewById(R.id.text_mail)).getText().toString().trim();
            final String password = ((EditText) findViewById(R.id.text_password)).getText().toString();
            rootRef.createUser(mail, password, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    final String name = ((EditText) findViewById(R.id.text_name)).getText().toString();
                    String lastname = ((EditText) findViewById(R.id.text_lastname)).getText().toString();
                    String telephone = ((EditText) findViewById(R.id.text_telephone)).getText().toString();
                    final String classroom = ((Spinner) findViewById(R.id.classSpinner)).getSelectedItem().toString();
                    final String school = ((Spinner) findViewById(R.id.spinner_2)).getSelectedItem().toString();
                    final String dni = ((EditText) findViewById(R.id.letterNIE)).getText().toString()
                            + ((EditText) findViewById(R.id.DNI)).getText().toString()
                            + ((EditText) findViewById(R.id.letterDNI)).getText().toString();

                    Map<String, Object> aluMap = new HashMap<>();
                    aluMap.put(getString(R.string.bbdd_name), name);
                    aluMap.put(getString(R.string.bbdd_lastname), lastname);
                    aluMap.put(getString(R.string.bbdd_telephone), telephone);
                    aluMap.put(getString(R.string.bbdd_dni), dni);
                    aluMap.put(getString(R.string.bbdd_mail), mail);
                    aluMap.put(getString(R.string.bbdd_class), classroom);
                    aluMap.put(getString(R.string.bbdd_center), school);

                    String uuid = UUID.randomUUID().toString();
                    Log.i("RegStudAct", "Map: "+aluMap);
                    studentRef.child(uuid).setValue(aluMap);

                    //Lanzando StudentTabActivity
                    rootRef.authWithPassword(mail, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Intent intent = new Intent(StudentRegisterActivity.this, StudentTabActivity.class);
                            intent.putExtra(getString(R.string.bbdd_mail), mail);
                            intent.putExtra(getString(R.string.bbdd_class), classroom);
                            intent.putExtra(getString(R.string.bbdd_center), school);
                            intent.putExtra(getString(R.string.bbdd_name), name);
                            intent.putExtra(getString(R.string.bbdd_dni), dni);
                            intent.putExtra(getString(R.string.myRol), getString(R.string.rol_student));
                            alertDialog.dismiss();
                            startActivity(intent);
                            StudentRegisterActivity.this.finish();
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Toast.makeText(StudentRegisterActivity.this,
                                    getString(R.string.login_error)
                                            + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(StudentRegisterActivity.this, LoginActivity.class);
                            alertDialog.dismiss();
                            startActivity(intent);
                            finish();
                        }
                    });
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    alertDialog.dismiss();
                    Toast.makeText(StudentRegisterActivity.this, getString(R.string.login_error) + " " +
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
                                    Map<String, Object> schoolMap = new HashMap<String, Object>();
                                    schoolMap.put(getString(R.string.bbdd_name), school);
                                    schoolsRef.child(uuid).setValue(schoolMap);
                                    schools.add(school);
                                    schoolsKeys.add(uuid);
                                    schoolKey = uuid;
                                    spinner.setSelection(spinnerAdapter.getPosition(school));
                                    //obtener clases de la escuela nueva annadida
                                    classSchool.clear();
                                    classSchool = getClassrooms(uuid);
                                    classSchool.add(0, getString(R.string.add_class));
                                    classSchool.add(0, getString(R.string.select_class));
                                    classAdapter = new ArrayAdapter<String>(StudentRegisterActivity.this, android.R.layout.simple_spinner_item, classSchool);
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

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void back (View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        this.finish();
    }
}

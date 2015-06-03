package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//Hacer los campos required como en HTML
//http://www.donnfelker.com/android-validation-with-edittext/
//https://tausiq.wordpress.com/2013/01/19/android-input-field-validation/
//http://stackoverflow.com/questions/11535011/edittext-field-is-required-before-moving-on-to-another-activity

public class RegisterActivity extends Activity {

    Spinner spinner1;
    LinearLayout course_groupLL, aluDataLL;
    Firebase ref;
    AlertDialog dialog;
    //ImageButton addChildImageButton;
    EditText mailEditText, nameEditText, lastnameEditText, schoolEditText, telephoneEditText,
            passwordEditText, clasesEditText;
    String mail, name, lastname, school, telephone, password, clases, selected;
    //Alumno
    EditText aluNameEditText, aluLastnameEditText, aluCourseGroupEditText, aluCenterEditText,
            aluMailEditText, aluTelephoneEditText;
    String aluName, aluLastname, aluCourseGroup, aluCenter, aluMail, aluTelephone;
    LinearLayout otherAlumnoLL, childsLL;
    ArrayList<LinearLayout> otherChildrens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Firebase.setAndroidContext(this);

        otherChildrens = new ArrayList<>();

        spinner1 = (Spinner) findViewById(R.id.spinner_1);
        course_groupLL = (LinearLayout) findViewById(R.id.course_group);
        aluDataLL = (LinearLayout) findViewById(R.id.alumno_data);
        //addChildImageButton = (ImageButton) findViewById(R.id.add_child);
        childsLL = (LinearLayout) findViewById(R.id.childs);

        //Obtenemos los datos del XML
        mailEditText = (EditText) findViewById(R.id.text_mail);
        nameEditText = (EditText) findViewById(R.id.text_name);
        lastnameEditText = (EditText) findViewById(R.id.text_lastname);
        schoolEditText = (EditText) findViewById(R.id.text_college);
        telephoneEditText = (EditText) findViewById(R.id.text_telephone);
        passwordEditText = (EditText) findViewById(R.id.text_password);
        clasesEditText = (EditText) findViewById(R.id.text_course_group);
        //Alumno
        aluNameEditText = (EditText) findViewById(R.id.text_alu_name);
        aluMailEditText= (EditText) findViewById(R.id.text_alu_mail);
        aluLastnameEditText = (EditText) findViewById(R.id.text_alu_lastname);
        aluCourseGroupEditText = (EditText) findViewById(R.id.text_alu_course_group);
        aluCenterEditText = (EditText) findViewById(R.id.text_alu_center);
        aluTelephoneEditText = (EditText) findViewById(R.id.text_alu_telephone);
        otherAlumnoLL = (LinearLayout) findViewById(R.id.other_alumno);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = String.valueOf(spinner1.getSelectedItem());
                if (selected.equals(getString(R.string.alumno))) {
                    ref = new Firebase(getString(R.string.aluRef));
                    course_groupLL.setVisibility(View.VISIBLE);
                    aluDataLL.setVisibility(View.GONE);
                    childsLL.setVisibility(View.GONE);
                } else if (selected.equals(getString(R.string.teacher))) {
                    ref = new Firebase(getString(R.string.profeRef));
                    course_groupLL.setVisibility(View.VISIBLE);
                    aluDataLL.setVisibility(View.GONE);
                    childsLL.setVisibility(View.GONE);
                } else if (selected.equals(getString(R.string.father))) {
                    ref = new Firebase(getString(R.string.padreRef));
                    course_groupLL.setVisibility(View.GONE);
                    aluDataLL.setVisibility(View.VISIBLE);
                    childsLL.setVisibility(View.VISIBLE);
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
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void submit (View view) {

        if (!haveEmptyFields()) {
            final Firebase rootRef = new Firebase (getString(R.string.rootRef));
            rootRef.createUser(mail, password, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    sendToBbdd();
                    //login
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();

                    /*rootRef.authWithPassword(mail, password,
                            new Firebase.AuthResultHandler() {

                                @Override
                                public void onAuthenticated(AuthData authData) {
                                    //TODO Lanzar Actividad Principal Correcta
                                }//onAuthenticated

                                @Override
                                public void onAuthenticationError(FirebaseError firebaseError) {
                                    Toast.makeText(RegisterActivity.this,
                                            getString(R.string.login_error)
                                                    + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterActivity.this,
                                            LoginActivity.class);
                                    startActivity(intent);
                                    RegisterActivity.this.finish();
                                }//onAuthenticationError
                            });//authWithPassword*/
                }//onSuccess

                @Override
                public void onError(FirebaseError firebaseError) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_error) +
                            firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                }//onError
            });//createUser
        }//if !haveEmptyFields
    }//function

    //TODO Revisar
    public void sendToBbdd() {

        String selected = String.valueOf(spinner1.getSelectedItem());
        String uuid = UUID.randomUUID().toString();
        Firebase regRef = ref.child(uuid);

        //Metemos en un HashMap los datos
        Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put(getString(R.string.bbdd_mail), mail);
        infoMap.put(getString(R.string.bbdd_name), name);
        infoMap.put(getString(R.string.bbdd_lastname), lastname);
        infoMap.put(getString(R.string.bbdd_center), school);
        infoMap.put(getString(R.string.bbdd_telephone), telephone);

        if (selected.equals(getString(R.string.alumno))) {
            String clase = ((EditText) findViewById(R.id.text_course_group)).getText().toString();
            infoMap.put(getString(R.string.bbdd_class), clase);
        }
        else if (selected.equals(getString(R.string.teacher))) {
            String clase = ((EditText) findViewById(R.id.text_course_group)).getText().toString();
            clase.replaceAll(" ", "");
            String[] clases = clase.split(",");
            Map<String, Object> clasesMap = new HashMap<String, Object>();
            for (int i = 0; i < clases.length; i++) {
                uuid = UUID.randomUUID().toString();
                clasesMap.put(uuid, clases[i]);
            }//for
            infoMap.put(getString(R.string.bbdd_teacher_class), clasesMap);
        }
        else if (selected.equals(getString(R.string.father))) {
            //TODO Comprobar que el alumno existe en la BBDD si no, registrarlo
            aluName = aluNameEditText.getText().toString();
            aluLastname = aluLastnameEditText.getText().toString();
            aluCourseGroup = aluCourseGroupEditText.getText().toString();
            aluCenter = aluCenterEditText.getText().toString();
            aluMail = aluMailEditText.getText().toString();
            aluTelephone = aluTelephoneEditText.getText().toString();

            Map<String, Object> aluMap = new HashMap<String, Object>();
            Map<String, Object> infoAlu = new HashMap<String, Object>();
            infoAlu.put(getString(R.string.bbdd_name), aluName);
            infoAlu.put(getString(R.string.bbdd_lastname), aluLastname);
            infoAlu.put(getString(R.string.bbdd_center), aluCenter);
            infoAlu.put(getString(R.string.bbdd_class), aluCourseGroup);
            infoAlu.put(getString(R.string.bbdd_mail), aluMail);
            infoAlu.put(getString(R.string.bbdd_telephone), aluTelephone);
            uuid = UUID.randomUUID().toString();
            aluMap.put(uuid, infoAlu);

            infoMap.put(getString(R.string.bbdd_father_alumno),aluMap);
        }

        //Actualizamos la BBDD
        regRef.updateChildren(infoMap);
    }//function

    public void launchHelp (View view) {
        createDialog();
        dialog.show();
    }

    public void createDialog() {
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

    public boolean haveEmptyFields () {
        boolean result = false;
        mail = mailEditText.getText().toString();
        name = nameEditText.getText().toString();
        lastname = lastnameEditText.getText().toString();
        school = schoolEditText.getText().toString();
        telephone = telephoneEditText.getText().toString();
        password = passwordEditText.getText().toString();
        clases = clasesEditText.getText().toString();
        aluMail = aluMailEditText.getText().toString();

        if (mail.isEmpty()) {
            mailEditText.setError(getString(R.string.field_empty));
            result = true;
        }
        if (name.isEmpty()) {
            nameEditText.setError(getString(R.string.field_empty));
            result = true;
        }
        if (lastname.isEmpty()) {
            lastnameEditText.setError(getString(R.string.field_empty));
            result = true;
        }
        if (school.isEmpty()) {
            schoolEditText.setError(getString(R.string.field_empty));
            result = true;
        }
        if (telephone.isEmpty()) {
            telephoneEditText.setError(getString(R.string.field_empty));
            result = true;
        }
        if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.field_empty));
            result = true;
        }
        if (clases.isEmpty()) {
            clasesEditText.setError(getString(R.string.field_empty));
            result = true;
        }
        //si es un padre comprobar que los datos del hijo no estan vacios
        if (selected.equals(getString(R.string.father))) {
            aluName = aluNameEditText.getText().toString();
            aluLastname = aluLastnameEditText.getText().toString();
            aluCourseGroup = aluCourseGroupEditText.getText().toString();
            aluCenter = aluCenterEditText.getText().toString();
            aluTelephone = aluTelephoneEditText.getText().toString();

            if (aluName.isEmpty()) {
                aluNameEditText.setError(getString(R.string.field_empty));
                result = true;
            }
            if (aluLastname.isEmpty()) {
                aluLastnameEditText.setError(getString(R.string.field_empty));
                result = true;
            }
            if (aluCourseGroup.isEmpty()) {
                aluCourseGroupEditText.setError(getString(R.string.field_empty));
                result = true;
            }
            if (aluCenter.isEmpty()) {
                aluCenterEditText.setError(getString(R.string.field_empty));
                result = true;
            }
            if (aluMail.isEmpty()) {
                aluMailEditText.setError(getString(R.string.field_empty));
                result = true;
            }
            if (aluTelephone.isEmpty()) {
                aluTelephoneEditText.setError(getString(R.string.field_empty));
                result = true;
            }
            if (otherChildrens.size() > 0) {
                for (int i = 0; i < otherChildrens.size(); i++) {
                    LinearLayout otherChildLL = otherChildrens.get(i);
                    //Obtenemos los eltos XML
                    EditText otherAluNameET = (EditText) otherChildLL.getChildAt(2);
                    EditText otherAluLastnameET = (EditText) otherChildLL.getChildAt(3);
                    EditText otherAluTelephoneET = (EditText) otherChildLL.getChildAt(4);
                    EditText otherAluMailET = (EditText) otherChildLL.getChildAt(5);
                    EditText otherAluCenterET = (EditText) otherChildLL.getChildAt(6);
                    EditText otherAluCourseGroupET = (EditText) otherChildLL.getChildAt(7);

                    //Obtenemos los datos en los editText
                    String otherAluName = otherAluNameET.getText().toString();
                    String otherAluLastname = otherAluLastnameET.getText().toString();
                    String otherAluTelephone = otherAluTelephoneET.getText().toString();
                    String otherAluMail = otherAluMailET.getText().toString();
                    String otherAluCenter = otherAluCenterET.getText().toString();
                    String otherAluCourseGroup = otherAluCourseGroupET.getText().toString();

                    if (otherAluName.isEmpty()) {
                        otherAluNameET.setError(getString(R.string.field_empty));
                    }
                    if (otherAluLastname.isEmpty()) {
                        otherAluLastnameET.setError(getString(R.string.field_empty));
                    }
                    if (otherAluTelephone.isEmpty()) {
                        otherAluTelephoneET.setError(getString(R.string.field_empty));
                    }
                    if (otherAluMail.isEmpty()) {
                        otherAluMailET.setError(getString(R.string.field_empty));
                    }
                    if (otherAluCenter.isEmpty()) {
                        otherAluCenterET.setError(getString(R.string.field_empty));
                    }
                    if (otherAluCourseGroup.isEmpty()) {
                        otherAluCourseGroupET.setError(getString(R.string.field_empty));
                    }
                }//for
            }//if
        }
        return result;
    }

    public void addChild (View view) {

        String selected = String.valueOf(spinner1.getSelectedItem());
        if (selected.equals(getString(R.string.father))) {

            TextView emptyText = new TextView(this); //Child 0
            emptyText.setText(getString(R.string.empty));

            TextView infoText = new TextView(this); //Child 1
            infoText.setGravity(Gravity.CENTER_HORIZONTAL);
            infoText.setText(getString(R.string.other_alumno_data));

            EditText otherAluName = new EditText(this); //Child 2
            otherAluName.setHint(getString(R.string.alumno_name));
            otherAluName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            otherAluName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_action_person, 0, 0, 0);
            otherAluName.setLayoutParams(aluNameEditText.getLayoutParams());

            EditText otherAluLastname = new EditText(this); //Child 3
            otherAluLastname.setHint(getString(R.string.alumno_lastname));
            otherAluLastname.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            otherAluLastname.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_action_person, 0, 0, 0);
            otherAluLastname.setLayoutParams(aluLastnameEditText.getLayoutParams());

            EditText otherAluTelephone = new EditText(this); //Child 4
            otherAluTelephone.setHint(getString(R.string.alumno_telephone));
            otherAluTelephone.setInputType(InputType.TYPE_CLASS_PHONE);
            otherAluTelephone.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_action_phone, 0, 0, 0);
            otherAluTelephone.setLayoutParams(aluTelephoneEditText.getLayoutParams());

            EditText otherAluMail = new EditText(this); //Child 5
            otherAluMail.setHint(getString(R.string.alumno_mail));
            otherAluMail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            otherAluMail.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_action_email, 0, 0, 0);
            otherAluMail.setLayoutParams(aluMailEditText.getLayoutParams());

            EditText otherAluCenter = new EditText(this); //Child 6
            otherAluCenter.setHint(getString(R.string.alumno_center));
            otherAluCenter.setInputType(InputType.TYPE_CLASS_TEXT);
            otherAluCenter.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_action_university_filled, 0, 0, 0);
            otherAluCenter.setLayoutParams(aluCenterEditText.getLayoutParams());

            EditText otherAluCourseGroup = new EditText(this); //Child 7
            otherAluCourseGroup.setHint(getString(R.string.alu_course_group_hint));
            otherAluCourseGroup.setInputType(InputType.TYPE_CLASS_TEXT);
            otherAluCourseGroup.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_action_group, 0, 0, 0);
            otherAluCourseGroup.setLayoutParams(aluCourseGroupEditText.getLayoutParams());

            TextView emptyText2 = new TextView(this); //Child 8
            emptyText2.setText(getString(R.string.empty));

            LinearLayout newAluDAtaLL = new LinearLayout(this);
            newAluDAtaLL.setOrientation(LinearLayout.VERTICAL);
            newAluDAtaLL.setLayoutParams(aluDataLL.getLayoutParams());

            newAluDAtaLL.addView(emptyText);
            newAluDAtaLL.addView(infoText);
            newAluDAtaLL.addView(otherAluName);
            newAluDAtaLL.addView(otherAluLastname);
            newAluDAtaLL.addView(otherAluTelephone);
            newAluDAtaLL.addView(otherAluMail);
            newAluDAtaLL.addView(otherAluCenter);
            newAluDAtaLL.addView(otherAluCourseGroup);
            newAluDAtaLL.addView(emptyText2);

            otherChildrens.add(newAluDAtaLL);
            otherAlumnoLL.addView(newAluDAtaLL);
        }//if
    }

    public void removeAllOtherChilds (View view) {
        if (otherChildrens.size() > 0){
            otherChildrens.remove(otherChildrens.size() - 1);
            otherAlumnoLL.removeAllViews();
        }
    }

}//class

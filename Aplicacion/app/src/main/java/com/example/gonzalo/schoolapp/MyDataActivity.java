package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.clases.Alumno;
import com.example.gonzalo.schoolapp.clases.Father;
import com.example.gonzalo.schoolapp.clases.Teacher;
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
import java.util.Objects;
import java.util.UUID;


public class MyDataActivity extends Activity {

    String rol, myMail, key, schoolSelected, myDNI;
    Object[] childrensKeyArray;
    Firebase ref, schoolsRef, childRef;

    EditText nameEditText, lastnameEditText, mailEditText, telephoneEditText, dniEditText;
    LinearLayout childrenGroupLL;
    Spinner spinnerSchool, childSchoolSpinner;
    ArrayList<String> schools;
    ArrayAdapter<String> spinnerAdapter;
    ArrayList<Alumno> childrensArrayList;
    int numberOfChildrens = 0;

    //TODO Revisar Colegios
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        Firebase.setAndroidContext(this);
        schoolsRef = new Firebase(getString(R.string.schoolsRef));

        rol = getIntent().getExtras().getString(getString(R.string.bbdd_rol));
        myMail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        myDNI = getIntent().getExtras().getString(getString(R.string.bbdd_dni));

        nameEditText = (EditText) findViewById(R.id.name);
        lastnameEditText = (EditText) findViewById(R.id.lastname);
        mailEditText = (EditText) findViewById(R.id.mail);
        telephoneEditText = (EditText) findViewById(R.id.telephone);
        dniEditText = (EditText) findViewById(R.id.dni);

        //Spinner
        spinnerSchool = (Spinner) findViewById(R.id.spinnerSchool);
        schools = getSchools();
        schools.add(getString(R.string.select_school));
        spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, schools);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerSchool.setAdapter(spinnerAdapter);

        nameEditText.setEnabled(false);
        lastnameEditText.setEnabled(false);
        dniEditText.setEnabled(false);
        mailEditText.setEnabled(false);
        telephoneEditText.setEnabled(false);
        spinnerSchool.setEnabled(false);

        childrenGroupLL = (LinearLayout) findViewById(R.id.children_group);

        if (rol.equals(getString(R.string.rol_student))) {
            ref = new Firebase(getString(R.string.studentRef));
            childrenGroupLL.setVisibility(View.GONE);
        }
        else if (rol.equals(getString(R.string.rol_teacher))) {
            ref = new Firebase(getString(R.string.teacherRef));
            childrenGroupLL.setVisibility(View.GONE);
        }
        else if (rol.equals(getString(R.string.rol_father))) {
            ref = new Firebase(getString(R.string.fatherRef));
            childrenGroupLL.setVisibility(View.VISIBLE);
        }
        Query userData = ref.orderByChild(getString(R.string.bbdd_dni)).equalTo(myDNI);
        userData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> dataSnapshotValue = (Map<String, Object>) dataSnapshot.getValue();
                Object[] keyArray = dataSnapshotValue.keySet().toArray();
                key = keyArray[0].toString();
                Map<String, Object> values = (Map<String, Object>) dataSnapshotValue.get(key);
                if (rol.equals(getString(R.string.rol_student))) {
                    Alumno alumno = new Alumno(values);
                    setData(alumno);
                }
                else if (rol.equals(getString(R.string.rol_teacher))) {
                    Teacher teacher = new Teacher(values);
                    setData(teacher);
                }
                else if (rol.equals(getString(R.string.rol_father))) {
                    Father father = new Father(values);
                    childrensArrayList = father.getChildrens();
                    Map<String, Object> childrensMap = (Map<String, Object>) values.get(getString(R.string.bbdd_children));
                    childrensKeyArray = childrensMap.keySet().toArray();
                    setData(father);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });//Query addListener
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_data, menu);
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
        else if (id == R.id.action_add_school) {
            launchPrompt();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setData (Alumno alumno) {
        //***Obtenemos los TextViews
        EditText courseGroupEditText = (EditText) findViewById(R.id.course_group);
        courseGroupEditText.setEnabled(false);

        nameEditText.setText(alumno.getName());
        lastnameEditText.setText(alumno.getLastname());
        courseGroupEditText.setText(alumno.getClassroom());
        mailEditText.setText(alumno.getMail());
        telephoneEditText.setText(alumno.getTelephone());
        dniEditText.setText(alumno.getDNI());

        spinnerSchool.setSelection(spinnerAdapter.getPosition(alumno.getSchool()));
    }

    //TODO Revisar
    public void setData (Teacher teacher) {
        String classRooms = "";
        ArrayList<String> clas = teacher.getClassRooms();
        EditText classRoomsEditText = (EditText) findViewById(R.id.course_group);
        classRoomsEditText.setEnabled(false);
        TextView classRoomsTextViewLabel = (TextView) findViewById(R.id.course_group_label);
        classRoomsTextViewLabel.setText(getString(R.string.teacher_class_label));

        nameEditText.setText(teacher.getName());
        lastnameEditText.setText(teacher.getLastname());
        mailEditText.setText(teacher.getMail());
        telephoneEditText.setText(teacher.getTelephone());
        dniEditText.setText(teacher.getDNI());

        spinnerSchool.setSelection(spinnerAdapter.getPosition(teacher.getSchool()));

        for (int i = 0; i < clas.size(); i++) {
            classRooms += clas.get(i);
            if (i != (clas.size() - 1)) {
                classRooms += ", ";
            }//if
        }//for
        classRoomsEditText.setText(classRooms);
    }

    public void setData (Father father) {
        numberOfChildrens = father.getCountChildrens();
        LinearLayout schoolGroup = (LinearLayout) findViewById(R.id.school_group);
        LinearLayout courseGroupGroup = (LinearLayout) findViewById(R.id.course_group_group);
        LinearLayout childs = (LinearLayout) findViewById(R.id.childs);
        schoolGroup.setVisibility(View.GONE);
        courseGroupGroup.setVisibility(View.GONE);
        //childrensArrayList = father.getChildrens();

        nameEditText.setText(father.getName());
        lastnameEditText.setText(father.getLastname());
        mailEditText.setText(father.getMail());
        telephoneEditText.setText(father.getTelephone());
        dniEditText.setText(father.getDNI());

        for (int i = 0; i < childrensArrayList.size(); i++) {
            Alumno alumno = (Alumno) childrensArrayList.get(i);
            if (alumno != null) {
                //LinearLayout's
                LinearLayout childNameGroup = new LinearLayout(this);
                LinearLayout childLastnameGroup = new LinearLayout(this);
                LinearLayout childSchoolGroup = new LinearLayout(this);
                LinearLayout childCoursegroupGroup = new LinearLayout(this);

                //Orientation LinearLayout's
                childNameGroup.setOrientation(LinearLayout.HORIZONTAL);
                childLastnameGroup.setOrientation(LinearLayout.HORIZONTAL);
                childSchoolGroup.setOrientation(LinearLayout.HORIZONTAL);
                childCoursegroupGroup.setOrientation(LinearLayout.HORIZONTAL);

                //TextView's
                TextView emptySeparator = new TextView(this);
                TextView childNameLabel = new TextView(new ContextThemeWrapper(this, R.style.SonDataContactLabel), null, 0);
                TextView childLastnameLabel = new TextView(new ContextThemeWrapper(this, R.style.SonDataContactLabel), null, 0);
                TextView childSchoolLabel = new TextView(new ContextThemeWrapper(this, R.style.SonDataContactLabel), null, 0);
                TextView childCourdsegroupLabel = new TextView(new ContextThemeWrapper(this, R.style.SonDataContactLabel), null, 0);

                //SetText
                emptySeparator.setText(getString(R.string.empty));
                childNameLabel.setText(getString(R.string.name_label));
                childLastnameLabel.setText(getString(R.string.lastname_label));
                childSchoolLabel.setText(getString(R.string.college_label));
                childCourdsegroupLabel.setText(getString(R.string.course_group_label));

                //EditText's
                EditText childName = new EditText(new ContextThemeWrapper(this, R.style.myDataEditText), null, 0);
                EditText childLastname = new EditText(new ContextThemeWrapper(this, R.style.myDataEditText), null, 0);
                EditText childClassroom = new EditText(new ContextThemeWrapper(this, R.style.myDataEditText), null, 0);

                //Spinner
                childSchoolSpinner = new Spinner(this);
                childSchoolSpinner.setAdapter(spinnerAdapter);

                //Disable EditText
                childName.setEnabled(false);
                childLastname.setEnabled(false);
                childClassroom.setEnabled(false);
                childSchoolSpinner.setEnabled(false);

                //Set Text EditText
                childName.setText(alumno.getName());
                childLastname.setText(alumno.getLastname());
                childClassroom.setText(alumno.getClassroom());

                //Annadimos a los LinearLayout's
                childNameGroup.addView(childNameLabel);
                childNameGroup.addView(childName);
                childLastnameGroup.addView(childLastnameLabel);
                childLastnameGroup.addView(childLastname);
                childSchoolGroup.addView(childSchoolLabel);
                childSchoolGroup.addView(childSchoolSpinner);
                childCoursegroupGroup.addView(childCourdsegroupLabel);
                childCoursegroupGroup.addView(childClassroom);

                //**Annadimos los nuevos elementos
                if (i == 0) {
                    childs.addView(emptySeparator);
                }
                childs.addView(childNameGroup);
                childs.addView(childLastnameGroup);
                childs.addView(childSchoolGroup);
                childs.addView(childCoursegroupGroup);
                TextView otherEmptySeparator = new TextView(this);
                otherEmptySeparator.setText(getString(R.string.empty));
                childs.addView(otherEmptySeparator);
            }
        }
    }

    public void modify (View view) {
        Button modifyButton = (Button) findViewById(R.id.modify_button);
        String textButton = (String) modifyButton.getText();

        if (textButton.equals(getString(R.string.modify))) {
            nameEditText.setEnabled(true);
            lastnameEditText.setEnabled(true);
            telephoneEditText.setEnabled(true);
            if (rol.equals(getString(R.string.rol_student))) {
                spinnerSchool.setEnabled(true);
                EditText courseGroupEditText = (EditText) findViewById(R.id.course_group);
                courseGroupEditText.setEnabled(true);
            }
            if (rol.equals(getString(R.string.rol_teacher))) {
                spinnerSchool.setEnabled(true);
                EditText classRoomsEditText = (EditText) findViewById(R.id.course_group);
                classRoomsEditText.setEnabled(true);
            }
            if (rol.equals(getString(R.string.rol_father))) {
                LinearLayout childsGroupLL = (LinearLayout) findViewById(R.id.childs);
                for (int i = 0; i < childsGroupLL.getChildCount(); i++) {
                    int eltoEditText = i % getResources().getInteger(R.integer.children_fields);
                    if (eltoEditText != 0) {
                        LinearLayout childLL = (LinearLayout) childsGroupLL.getChildAt(i);
                        if (eltoEditText != getResources().getInteger(R.integer.children_spinner_field)) {
                            EditText childEditText = (EditText) childLL.getChildAt(1);
                            childEditText.setEnabled(true);
                        } else {
                            Spinner childSpinner = (Spinner) childLL.getChildAt(1);
                            childSpinner.setEnabled(true);
                        }//else
                    }//if
                }//for
            }
            modifyButton.setText(getString(R.string.save));
        }
        else if (textButton.equals(getString(R.string.save))) {
            if (isFormOK(rol)) {
                save();
            }
        }
    }

    public void launchChangePassword (View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra(getString(R.string.bbdd_mail), myMail);
        startActivity(intent);
        this.finish();
    }

    public ArrayList<String> getSchools() {
        Firebase schoolsRef = new Firebase(getString(R.string.schoolsRef));
        final ArrayList<String> tmp = new ArrayList<>();
        Query allSchools = schoolsRef;
        allSchools.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                String schoolName = values.get(getString(R.string.bbdd_name)).toString();
                if (!tmp.contains(schoolName)) {
                    tmp.add(schoolName);
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

    public void save() {
        if (rol.equals(getString(R.string.rol_student))) {
            EditText courseGroupEditText = (EditText) findViewById(R.id.course_group);
            Map<String, Object> aluMap = new HashMap<>();
            aluMap.put(getString(R.string.bbdd_name), nameEditText.getText().toString());
            aluMap.put(getString(R.string.bbdd_lastname), lastnameEditText.getText().toString());
            aluMap.put(getString(R.string.bbdd_dni), dniEditText.getText().toString());
            aluMap.put(getString(R.string.bbdd_mail), mailEditText.getText().toString());
            aluMap.put(getString(R.string.bbdd_telephone), telephoneEditText.getText().toString());
            aluMap.put(getString(R.string.bbdd_center), spinnerSchool.getSelectedItem().toString());
            aluMap.put(getString(R.string.bbdd_class), courseGroupEditText.getText().toString());

            Log.i("MyDataActivity", "Form OK, Guardando modificaciones de alumno...");
            ref.child(key).updateChildren(aluMap);
            this.finish();
        }
        else if (rol.equals(getString(R.string.rol_teacher))) {
            EditText classRoomsEditText = (EditText) findViewById(R.id.course_group);
            Map<String, Object> teacherMap = new HashMap<>();
            Map<String, Object> classMap = new HashMap<>();
            teacherMap.put(getString(R.string.bbdd_name), nameEditText.getText().toString());
            teacherMap.put(getString(R.string.bbdd_lastname), lastnameEditText.getText().toString());
            teacherMap.put(getString(R.string.bbdd_dni), dniEditText.getText().toString());
            teacherMap.put(getString(R.string.bbdd_mail), mailEditText.getText().toString());
            teacherMap.put(getString(R.string.bbdd_telephone), telephoneEditText.getText().toString());
            teacherMap.put(getString(R.string.bbdd_center), spinnerSchool.getSelectedItem().toString());
            //Clases
            String [] classrooms = (classRoomsEditText.getText().toString()).split(",");
            for (String classroom: classrooms){
                String uuid = UUID.randomUUID().toString();
                classMap.put(uuid, classroom);
            }
            teacherMap.put(getString(R.string.bbdd_teacher_class), classMap);

            Log.i("MyDataActivity", "Form OK, Guardando modificaciones de profesor...");
            ref.child(key).updateChildren(teacherMap);
            this.finish();
        }
        else if (rol.equals(getString(R.string.rol_father))) {
            int currentChild = 0, childElto;
            LinearLayout childLinearLayout;
            String name = "", lastname = "", school = "", courseGroup = "";
            Map<String, Object> fatherMap = new HashMap<>();
            Map<String, Object> infoChildMap = new HashMap<>();
            Map<String, Object> tempMap = new HashMap<>();
            fatherMap.put(getString(R.string.bbdd_name), nameEditText.getText().toString());
            fatherMap.put(getString(R.string.bbdd_lastname), lastnameEditText.getText().toString());
            fatherMap.put(getString(R.string.bbdd_dni), dniEditText.getText().toString());
            fatherMap.put(getString(R.string.bbdd_mail), mailEditText.getText().toString());
            fatherMap.put(getString(R.string.bbdd_telephone), telephoneEditText.getText().toString());
            //Get info Childs
            for (int i = 0; i < childrenGroupLL.getChildCount(); i++) {
                childElto = (i % getResources().getInteger(R.integer.children_fields));
                switch (childElto) {
                    case 1:
                        childLinearLayout = (LinearLayout) childrenGroupLL.getChildAt(i);
                        name = ((EditText) childLinearLayout.getChildAt(1)).getText().toString();
                        tempMap.put(getString(R.string.bbdd_name), name);
                        childRef.child(childrensKeyArray[currentChild].toString()).
                                child(getString(R.string.bbdd_name)).setValue(name);
                        break;
                    case 2:
                        childLinearLayout = (LinearLayout) childrenGroupLL.getChildAt(i);
                        lastname = ((EditText) childLinearLayout.getChildAt(1)).getText().toString();
                        tempMap.put(getString(R.string.bbdd_lastname), lastname);
                        childRef.child(childrensKeyArray[currentChild].toString()).
                                child(getString(R.string.bbdd_lastname)).setValue(lastname);
                        break;
                    case 3:
                        childLinearLayout = (LinearLayout) childrenGroupLL.getChildAt(i);
                        school = ((Spinner) childLinearLayout.getChildAt(1)).getSelectedItem().toString();
                        tempMap.put(getString(R.string.bbdd_center), school);
                        childRef.child(childrensKeyArray[currentChild].toString()).
                                child(getString(R.string.bbdd_center)).setValue(school);
                        break;
                    case 4:
                        childLinearLayout = (LinearLayout) childrenGroupLL.getChildAt(i);
                        courseGroup = ((EditText) childLinearLayout.getChildAt(1)).getText().toString();
                        tempMap.put(getString(R.string.bbdd_class), courseGroup);
                        childRef.child(childrensKeyArray[currentChild].toString()).
                                child(getString(R.string.bbdd_class)).setValue(courseGroup);
                        break;
                }//switch
                if ((i != 0) && ((i % getResources().getInteger(R.integer.children_fields)) == 0)) {
                    Map<String, Object> childMap = new HashMap<>(tempMap);
                    infoChildMap.put(childrensKeyArray[currentChild].toString(), childMap);
                    tempMap.clear();
                    currentChild++;
                }
            }//for
            fatherMap.put(getString(R.string.bbdd_children), infoChildMap);
            Log.i("MyDataActivity", "padre: " + fatherMap);
            Log.i("MyDataActivity", "Actualizando datos padre...");
            ref.child(key).updateChildren(fatherMap);
            this.finish();
        }//rol padre
    }

    public boolean isFormOK(String rol) {
        boolean formOk = true;
        String data = "";
        if (rol.equals(getString(R.string.rol_student))) {
            EditText courseGroupEditText = (EditText) findViewById(R.id.course_group);
            data = nameEditText.getText().toString();
            if (data.isEmpty()) {
                formOk = false;
                nameEditText.setError(getString(R.string.field_empty));
            }
            data = lastnameEditText.getText().toString();
            if (data.isEmpty()) {
                formOk = false;
                lastnameEditText.setError(getString(R.string.field_empty));
            }
            data = dniEditText.getText().toString();
            if ((!Utilities.isDNI(data)) && (!Utilities.isNIE(data))) {
                formOk = false;
                dniEditText.setError(getString(R.string.dni_format_error));
            }
            data = mailEditText.getText().toString();
            if (!Utilities.isMail(data)) {
                formOk = false;
                mailEditText.setError(getString(R.string.mail_format_error));
            }
            data = telephoneEditText.getText().toString();
            if (!Utilities.isTelephone(data)) {
                formOk = false;
                telephoneEditText.setError(getString(R.string.telephone_format_error));
            }
            data = courseGroupEditText.getText().toString();
            if (!Utilities.isOneClass(data)) {
                formOk = false;
                courseGroupEditText.setError(getString(R.string.error_class_format));
            }
        } else if (rol.equals(getString(R.string.rol_teacher))) {
            EditText courseGroupEditText = (EditText) findViewById(R.id.course_group);
            data = nameEditText.getText().toString();
            if (data.isEmpty()) {
                formOk = false;
                nameEditText.setError(getString(R.string.field_empty));
            }
            data = lastnameEditText.getText().toString();
            if (data.isEmpty()) {
                formOk = false;
                lastnameEditText.setError(getString(R.string.field_empty));
            }
            data = dniEditText.getText().toString();
            if ((!Utilities.isDNI(data)) && (!Utilities.isNIE(data))) {
                formOk = false;
                dniEditText.setError(getString(R.string.dni_format_error));
            }
            data = mailEditText.getText().toString();
            if (!Utilities.isMail(data)) {
                formOk = false;
                mailEditText.setError(getString(R.string.mail_format_error));
            }
            data = telephoneEditText.getText().toString();
            if (!Utilities.isTelephone(data)) {
                formOk = false;
                telephoneEditText.setError(getString(R.string.telephone_format_error));
            }
            data = courseGroupEditText.getText().toString();
            data = data.replaceAll(" ", "");
            if (!Utilities.areManyClasses(data)) {
                formOk = false;
                courseGroupEditText.setError(getString(R.string.error_classes_format));
            }
        } else if (rol.equals(getString(R.string.rol_father))) {
            data = nameEditText.getText().toString();
            if (data.isEmpty()) {
                formOk = false;
                nameEditText.setError(getString(R.string.field_empty));
            }
            data = lastnameEditText.getText().toString();
            if (data.isEmpty()) {
                formOk = false;
                lastnameEditText.setError(getString(R.string.field_empty));
            }
            data = dniEditText.getText().toString();
            if ((!Utilities.isDNI(data)) && (!Utilities.isNIE(data))) {
                formOk = false;
                dniEditText.setError(getString(R.string.dni_format_error));
            }
            data = mailEditText.getText().toString();
            if (!Utilities.isMail(data)) {
                formOk = false;
                mailEditText.setError(getString(R.string.mail_format_error));
            }
            data = telephoneEditText.getText().toString();
            if (!Utilities.isTelephone(data)) {
                formOk = false;
                telephoneEditText.setError(getString(R.string.telephone_format_error));
            }
            for (int i = 0; i < childrenGroupLL.getChildCount(); i++) {
                EditText chilEdtitText;
                LinearLayout childLinearLayout;
                int childElto = (i % getResources().getInteger(R.integer.children_fields));
                switch (childElto) {
                    case 1:
                    case 2:
                        childLinearLayout = (LinearLayout) childrenGroupLL.getChildAt(childElto);
                        chilEdtitText = (EditText) (childLinearLayout.getChildAt(1));
                        if (chilEdtitText.getText().toString().isEmpty()) {
                            formOk = false;
                            chilEdtitText.setError(getString(R.string.field_empty));
                        }//if
                        break;
                    case 3:
                        childLinearLayout = (LinearLayout) childrenGroupLL.getChildAt(childElto);
                        Spinner childSpinner = (Spinner) (childLinearLayout.getChildAt(1));
                        data = childSpinner.getSelectedItem().toString();
                        if ((!schools.contains(data)) || (data.equals(getString(R.string.select_school)))) {
                            formOk = false;
                            Toast.makeText(this, getString(R.string.error_select_school), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 4:
                        childLinearLayout = (LinearLayout) childrenGroupLL.getChildAt(childElto);
                        chilEdtitText = (EditText) (childLinearLayout.getChildAt(1));
                        data = chilEdtitText.getText().toString();
                        if (!Utilities.isOneClass(data)) {
                            formOk = false;
                            chilEdtitText.setError(getString(R.string.error_class_format));
                        }
                        break;
                }//switch*/
            }//for
        }//if else if rol
        return formOk;
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
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                schoolSelected = schoolEditText.getText().toString();
                                schools.add(schoolSelected);
                                String uuid = UUID.randomUUID().toString();
                                Map<String, Object> schoolMap = new HashMap<String, Object>();
                                schoolMap.put(uuid, schoolSelected);
                                schoolsRef.updateChildren(schoolMap);
                                if (!rol.equals(getString(R.string.rol_father))) {
                                    spinnerSchool.setSelection(spinnerAdapter.getPosition(schoolSelected));
                                } else {
                                    childSchoolSpinner.setSelection(spinnerAdapter.getPosition(schoolSelected));
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

    public void launchDeleteAccountDialog (View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.delete_account_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompt.xml to alertdialog builder
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle(getString(R.string.delete_account));

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.delete_account),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                EditText myPasswordEditText = (EditText) promptView.findViewById(R.id.password_field);
                                EditText myRepeatedPasswordEditText = (EditText) promptView.findViewById(R.id.password_field_repited);

                                String myPassword = myPasswordEditText.getText().toString();
                                String myRepeatedPassword = myRepeatedPasswordEditText.getText().toString();
                                
                                if (myPassword.equals(myRepeatedPassword)) {
                                    final Firebase rootRef = new Firebase(getString(R.string.rootRef));
                                    rootRef.removeUser(myMail, myPassword, new Firebase.ResultHandler() {
                                        @Override
                                        public void onSuccess() {
                                            //Eliminamos la informacion de los hijo
                                            /*if (rol.equals(getString(R.string.rol_father))) {
                                                for (Object childKey: childrensKeyArray){
                                                    String cKey = childKey.toString();
                                                    childRef.child(cKey).removeValue();
                                                }//for childKey
                                            }//*/
                                            ref.child(key).removeValue();
                                            Intent intent = new Intent(MyDataActivity.this, WelcomeActivity.class);
                                            rootRef.unauth();
                                            Toast.makeText(MyDataActivity.this,
                                                    getString(R.string.deleteAccountSucceeded),
                                                    Toast.LENGTH_LONG).show();
                                            startActivity(intent);
                                            MyDataActivity.this.finish();
                                        }

                                        @Override
                                        public void onError(FirebaseError firebaseError) {
                                            Toast.makeText(MyDataActivity.this,
                                                    getString(R.string.delete_account_error) + " " +
                                                            firebaseError, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    myPasswordEditText.setError(getString(R.string.password_not_equals));
                                    myRepeatedPasswordEditText.setError(getString(R.string.password_not_equals));
                                    Toast.makeText(MyDataActivity.this,
                                            getString(R.string.password_not_equals), Toast.LENGTH_LONG).show();
                                }//if password equal
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

}//class

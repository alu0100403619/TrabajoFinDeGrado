package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gonzalo.schoolapp.clases.Alumno;
import com.example.gonzalo.schoolapp.clases.Father;
import com.example.gonzalo.schoolapp.clases.Teacher;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;


public class MyDataActivity extends Activity {

    String rol, myMail, key;
    Firebase ref;

    EditText nameEditText, lastnameEditText, mailEditText, telephoneEditText;
    LinearLayout childrenGroupLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        Firebase.setAndroidContext(this);

        rol = getIntent().getExtras().getString(getString(R.string.bbdd_rol));
        myMail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));

        nameEditText = (EditText) findViewById(R.id.name);
        lastnameEditText = (EditText) findViewById(R.id.lastname);
        mailEditText = (EditText) findViewById(R.id.mail);
        telephoneEditText = (EditText) findViewById(R.id.telephone);

        nameEditText.setEnabled(false);
        lastnameEditText.setEnabled(false);
        mailEditText.setEnabled(false);
        telephoneEditText.setEnabled(false);

        childrenGroupLL = (LinearLayout) findViewById(R.id.children_group);

        if (rol.equals(getString(R.string.alumno))) {
            ref = new Firebase(getString(R.string.aluRef));
        }
        else if (rol.equals(getString(R.string.teacher))) {
            ref = new Firebase(getString(R.string.profeRef));
        }
        else if (rol.equals(getString(R.string.father))) {
            ref = new Firebase(getString(R.string.padreRef));
        }
        Query userData = ref.orderByChild(getString(R.string.bbdd_mail)).equalTo(myMail);
        userData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> dataSnapshotValue = (Map<String, Object>) dataSnapshot.getValue();
                Object[] keyArray = dataSnapshotValue.keySet().toArray();
                key = keyArray[0].toString();
                Map<String, Object> values = (Map<String, Object>) dataSnapshotValue.get(key);
                if (rol.equals(getString(R.string.alumno))) {
                    Alumno alumno = new Alumno(values);
                    setData(alumno);
                }
                else if (rol.equals(getString(R.string.teacher))) {
                    Teacher teacher = new Teacher(values);
                    setData(teacher);
                }
                else if (rol.equals(getString(R.string.father))) {
                    Father father = new Father(values);
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

        return super.onOptionsItemSelected(item);
    }

    public void setData (Alumno alumno) {
        //***Obtenemos los TextViews
        childrenGroupLL.setVisibility(View.GONE);
        TextView schoolTextView = (TextView) findViewById(R.id.school);
        EditText courseGroupEditText = (EditText) findViewById(R.id.course_group);
        courseGroupEditText.setEnabled(false);

        nameEditText.setText(" " + alumno.getName());
        lastnameEditText.setText(" " + alumno.getLastname());
        courseGroupEditText.setText(" " + alumno.getClassroom());
        mailEditText.setText(" " + alumno.getMail());
        telephoneEditText.setText(" " + alumno.getTelephone());

        //TODO Cambiar la modificacion de Colegio
        schoolTextView.setText(" " + alumno.getSchool());
    }

    public void setData (Teacher teacher) {
        String classRooms = " ";
        ArrayList<String> clas = teacher.getClassRooms();
        EditText classRoomsEditText = (EditText) findViewById(R.id.course_group);
        classRoomsEditText.setEnabled(false);
        TextView schoolTextView = (TextView) findViewById(R.id.school);
        TextView classRoomsTextViewLabel = (TextView) findViewById(R.id.course_group_label);
        classRoomsTextViewLabel.setText(getString(R.string.teacher_class_label));
        childrenGroupLL.setVisibility(View.GONE);

        nameEditText.setText(" " + teacher.getName());
        lastnameEditText.setText(" " + teacher.getLastname());
        mailEditText.setText(" " + teacher.getMail());
        telephoneEditText.setText(" " + teacher.getTelephone());
        //TODO Cambiar la modificacion de colegio
        schoolTextView.setText(" " + teacher.getSchool());

        for (int i = 0; i < clas.size(); i++) {
            classRooms += clas.get(i);
            if (i != (clas.size() - 1)) {
                classRooms += ", ";
            }//if
        }//for
        classRoomsEditText.setText(classRooms);
    }

    public void setData (Father father) {
        LinearLayout schoolGroup = (LinearLayout) findViewById(R.id.school_group);
        LinearLayout courseGroupGroup = (LinearLayout) findViewById(R.id.course_group_group);
        LinearLayout childs = (LinearLayout) findViewById(R.id.childs);
        schoolGroup.setVisibility(View.GONE);
        courseGroupGroup.setVisibility(View.GONE);
        ArrayList<Alumno> childrens = father.getChildrens();
        Log.i("DataActivity", "Hijos: " + childrens);

        nameEditText.setText(father.getName());
        lastnameEditText.setText(father.getLastname());
        mailEditText.setText(father.getMail());
        telephoneEditText.setText(father.getTelephone());

        for (int i = 0; i < childrens.size(); i++) {
            Alumno alumno = (Alumno) childrens.get(i);
            if (alumno != null) {
                TextView childNumber = new TextView(this);
                childNumber.setText(" ##" + getString(R.string.son_label) + " " + (i + 1) + "##");
                //TextView childName = new TextView(this);
                EditText childName = new EditText(this);
                childName.setEnabled(false);
                childName.setText(alumno.getName());
                //TextView childLastname = new TextView(this);
                EditText childLastname = new EditText(this);
                childLastname.setEnabled(false);
                childLastname.setText(alumno.getLastname());
                //TextView childSchool = new TextView(this);
                EditText childSchool = new EditText(this);
                childSchool.setEnabled(false);
                childSchool.setText(alumno.getSchool());
                //TextView childClassroom = new TextView(this);
                EditText childClassroom = new EditText(this);
                childClassroom.setEnabled(false);
                childClassroom.setText(alumno.getClassroom());

                //**Annadimos los nuevos elementos
                childs.addView(childNumber);
                childs.addView(childName);
                childs.addView(childLastname);
                childs.addView(childSchool);
                childs.addView(childClassroom);
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
            if (rol.equals(getString(R.string.alumno))) {
                EditText courseGroupEditText = (EditText) findViewById(R.id.course_group);
                courseGroupEditText.setEnabled(true);
            }
            //Todo Annadir modificacion de la escuels
            if (rol.equals(getString(R.string.teacher))) {
                EditText classRoomsEditText = (EditText) findViewById(R.id.course_group);
                classRoomsEditText.setEnabled(true);
            }
            if (rol.equals(getString(R.string.father))) {
                for (int i = 0; i < childrenGroupLL.getChildCount(); i++) {
                    LinearLayout childs = (LinearLayout) childrenGroupLL.getChildAt(i);
                    Log.i("MyDataActivity", "Nombre Hijo: "+childs.getChildAt(0));
                    Log.i("MyDataActivity", "Apellido Hijo: "+childs.getChildAt(1));
                    Log.i("MyDataActivity", "Escuela Hijo: "+childs.getChildAt(2));
                    Log.i("MyDataActivity", "Clase Hijo: "+childs.getChildAt(3));

                    /*EditText childName = ;
                    EditText childLastname = ;
                    EditText childSchool = ;
                    EditText childClassroom = ;

                    childName.setEnabled(false);
                    childLastname.setEnabled(false);
                    childSchool.setEnabled(false);
                    childClassroom.setEnabled(false);//*/
                }//for
            }
            modifyButton.setText(getString(R.string.save));
        }
        else if (textButton.equals(getString(R.string.save))) {
            //TODO Meter en un Mapa Map<String, String>
            //ref.child(key).updateChildren(values);
        }
    }

    public void launchChangePassword (View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra(getString(R.string.bbdd_mail), myMail);
        startActivity(intent);
        this.finish();
    }

}//class

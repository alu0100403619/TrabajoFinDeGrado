package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gonzalo.schoolapp.clases.Alumno;
import com.example.gonzalo.schoolapp.clases.Father;
import com.example.gonzalo.schoolapp.clases.Teacher;
import com.example.gonzalo.schoolapp.utilities.Utilities;

import java.util.ArrayList;


public class DataActivity extends Activity {

    private String rol;
    private TextView nameTextView;
    private TextView lastnameTextView;
    private TextView mailTextView;
    private TextView telephoneTextView;
    private LinearLayout childrenGroupLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        nameTextView = (TextView) findViewById(R.id.name);
        lastnameTextView = (TextView) findViewById(R.id.lastname);
        mailTextView = (TextView) findViewById(R.id.mail);
        telephoneTextView = (TextView) findViewById(R.id.telephone);
        childrenGroupLL = (LinearLayout) findViewById(R.id.children_group);

        rol = getIntent().getExtras().getString(getString(R.string.rol));
        switch (rol) {
            case "Alumno":
                Alumno alumno = getIntent().getExtras().getParcelable(getString(R.string.person));
                setData(alumno);
                break;
            case "Padre":
                Father father =  getIntent().getExtras().getParcelable(getString(R.string.person));
                setData(father);
                break;
            case "Profesor":
                Teacher teacher = getIntent().getExtras().getParcelable(getString(R.string.person));
                setData(teacher);
                break;
        }//switch*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data, menu);
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
        TextView courseGroupTextView = (TextView) findViewById(R.id.course_group);

        nameTextView.setText(" " + alumno.getName());
        lastnameTextView.setText(" " + alumno.getLastname());
        schoolTextView.setText(" " + alumno.getSchool());
        courseGroupTextView.setText(" " + alumno.getClassroom());
        mailTextView.setText(" " + alumno.getMail());
        telephoneTextView.setText(" " + alumno.getTelephone());
    }

    public void setData (Teacher teacher) {
        String classRooms = " ";
        ArrayList<String> clas = teacher.getClassRooms();
        TextView classRoomsTextView = (TextView) findViewById(R.id.course_group);
        TextView schoolTextView = (TextView) findViewById(R.id.school);
        TextView classRoomsTextViewLabel = (TextView) findViewById(R.id.course_group_label);
        classRoomsTextViewLabel.setText(getString(R.string.teacher_class_label));
        childrenGroupLL.setVisibility(View.GONE);

        nameTextView.setText(" " + teacher.getName());
        lastnameTextView.setText(" " + teacher.getLastname());
        mailTextView.setText(" " + teacher.getMail());
        telephoneTextView.setText(" " + teacher.getTelephone());
        schoolTextView.setText(" " + teacher.getSchool());

        for (int i = 0; i < clas.size(); i++) {
            classRooms += clas.get(i);
            if (i != (clas.size() - 1)) {
              classRooms += ", ";
            }//if
        }//for
        classRoomsTextView.setText(classRooms);
    }

    public void setData (Father father) {
        LinearLayout schoolGroup = (LinearLayout) findViewById(R.id.school_group);
        LinearLayout courseGroupGroup = (LinearLayout) findViewById(R.id.course_group_group);
        LinearLayout childs = (LinearLayout) findViewById(R.id.childs);
        schoolGroup.setVisibility(View.GONE);
        courseGroupGroup.setVisibility(View.GONE);
        ArrayList<Alumno> childrens = father.getChildrens();
        Log.i("DataActivity", "Hijos: "+childrens);

        nameTextView.setText(father.getName());
        lastnameTextView.setText(father.getLastname());
        mailTextView.setText(father.getMail());
        telephoneTextView.setText(father.getTelephone());

        for (int i = 0; i < childrens.size(); i++) {
            Alumno alumno = (Alumno) childrens.get(i);
            if (alumno != null) {
                TextView childNumber = new TextView(this);
                childNumber.setText(" ##" + getString(R.string.son_label) + " " + (i + 1) + "##");
                TextView childName = new TextView(this);
                //childName.setText(child.getName());
                childName.setText(alumno.getName());
                TextView childLastname = new TextView(this);
                //childLastname.setText(child.getLastname());
                childLastname.setText(alumno.getLastname());
                TextView childSchool = new TextView(this);
                //childSchool.setText(child.getSchool());
                childSchool.setText(alumno.getSchool());
                TextView childClassroom = new TextView(this);
                //childClassroom.setText(child.getClassroom());
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

}

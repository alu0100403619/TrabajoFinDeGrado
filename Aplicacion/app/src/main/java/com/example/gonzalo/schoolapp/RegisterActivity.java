package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.clases.Alumno;
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

import com.example.gonzalo.schoolapp.utilities.Utilities;

//Hacer los campos required como en HTML
//http://www.donnfelker.com/android-validation-with-edittext/
//https://tausiq.wordpress.com/2013/01/19/android-input-field-validation/
//http://stackoverflow.com/questions/11535011/edittext-field-is-required-before-moving-on-to-another-activity

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

    public void launchRegisterStudent(View view) {
        Intent intent = new Intent(this, RegisterStudentActivity.class);
        startActivity(intent);
        finish();
    }

    public void launchRegisterTeacher(View view) {
        Intent intent = new Intent(this, RegisterTeacherActivity.class);
        startActivity(intent);
        finish();
    }

    public void launchRegisterFather(View view) {
        Intent intent = new Intent(this, RegisterFatherActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        this.finish();
    }
}//class

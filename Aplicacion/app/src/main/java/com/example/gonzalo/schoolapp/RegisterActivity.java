package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchRegisterStudent(View view) {
        Intent intent = new Intent(this, StudentRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void launchRegisterTeacher(View view) {
        Intent intent = new Intent(this, TeacherRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void launchRegisterFather(View view) {
        Intent intent = new Intent(this, FatherRegisterActivity.class);
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

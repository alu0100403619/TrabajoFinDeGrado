package com.example.gonzalo.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {

    TextView usernameTextView, passwordTextView;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameTextView = (TextView) findViewById(R.id.usernameField);
        passwordTextView = (TextView) findViewById(R.id.passwordField);
        userType = "";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void setTypeUser (View view) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupUser);
        int userTypeInt = radioGroup.getCheckedRadioButtonId();
        switch (userTypeInt) {
            case R.id.radioButton_alumno :
                userType = getString(R.string.alumno);
                break;
            case R.id.radioButton_teacher :
                userType = getString(R.string.teacher);
                break;
            case R.id.radioButton_mother :
                userType = getString(R.string.mother);
                break;
        }//switch
    }

    public void login (View view) {
        String message = getString(R.string.prueba);
        CharSequence username = usernameTextView.getText();
        CharSequence password = passwordTextView.getText();
        message += " -- " + username + "::" + password + "::" + userType;
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        if ((!username.toString().isEmpty()) && (!password.toString().isEmpty())) {
            Intent intent = new Intent(this, AlumnoTabActivity.class);
            intent.putExtra(getString(R.string.rol), userType);
            intent.putExtra(getString(R.string.username_hint), username.toString());
            startActivity(intent);
        } //if
        else {
            message = "";
            if (username.toString().isEmpty()) { message += getString(R.string.username_empty) + "\n"; }
            if (password.toString().isEmpty()) { message += getString(R.string.password_empty) + "\n"; }
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    public void launchRegister (View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}

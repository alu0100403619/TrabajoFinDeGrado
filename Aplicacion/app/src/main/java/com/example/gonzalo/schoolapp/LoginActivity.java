package com.example.gonzalo.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class LoginActivity extends ActionBarActivity {

    EditText mailEditText, passwordEditText;
    String userType;
    Firebase rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        rootRef = new Firebase(getString(R.string.rootRef));

        mailEditText = (EditText) findViewById(R.id.mailField);
        passwordEditText = (EditText) findViewById(R.id.passwordField);
        userType = "";
        /*if (mailEditText.getText().toString().equals("")) {
            mailEditText.setError( "Mail is required!" );
        }
        if (passwordEditText.getText().length() == 0) {
            passwordEditText.setError("Password is required!");
        }//Funcionna Bien*/
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

        String mail = mailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        rootRef.authWithPassword(mail, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Intent intent = new Intent(LoginActivity.this, AlumnoTabActivity.class);
                //intent.putExtra(getString(R.string.rol), userType);
                //intent.putExtra(getString(R.string.username_hint), username.toString());
                startActivity(intent);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Toast.makeText(LoginActivity.this,
                        getString(R.string.login_error)
                                + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void launchRegister (View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        this.finish();
    }
}

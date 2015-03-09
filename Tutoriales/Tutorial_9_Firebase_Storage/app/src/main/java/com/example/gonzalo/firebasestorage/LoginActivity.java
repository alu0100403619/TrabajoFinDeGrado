package com.example.gonzalo.firebasestorage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class LoginActivity extends ActionBarActivity {

    EditText usermailText, passwordText;
    String usermail, password;
    Firebase rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);

        rootRef = new Firebase("https://dinosaurios.firebaseio.com/");

        usermailText = (EditText) findViewById(R.id.usermail);
        passwordText = (EditText) findViewById(R.id.password);

        rootRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    // user is logged in
                    launchMainActivity();
                } else {
                    // user is not logged in
                    String message = "You aren't register. Please, sign-in";
                    Toast.makeText(LoginActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });//rootRef
    }


    public void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void login(View view) {
        usermail = usermailText.getText().toString();
        password = passwordText.getText().toString();

        rootRef.authWithPassword(usermail, password,
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        // Authentication just completed successfully :)
                        launchMainActivity();
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError error) {
                        // Something went wrong :(
                        String message = "Login Failed: " + error.getMessage();
                        Toast.makeText(LoginActivity.this, message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void signin (View view) {

        usermail = usermailText.getText().toString();
        password = passwordText.getText().toString();

        /*String message = "Not Implemented Yet";
        Toast.makeText(LoginActivity.this, message,
                Toast.LENGTH_SHORT).show();//*/
        rootRef.createUser(usermail, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                String message = "Sign-in successfully";
                Toast.makeText(LoginActivity.this, message,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                String message = "Sign-in Error: " + firebaseError.getMessage();
                Toast.makeText(LoginActivity.this, message,
                        Toast.LENGTH_SHORT).show();
            }
        });//create User
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
}

package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.utilities.Utilities;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class ChangePasswordActivity extends Activity {

    String myMail;
    EditText mailEditText, oldPasswordEditText, newPasswordEditText, newRepeatedPasswordEditText;
    Firebase rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Firebase.setAndroidContext(this);
        rootRef = new Firebase(getString(R.string.rootRef));

        mailEditText = (EditText) findViewById(R.id.mailField);
        oldPasswordEditText = (EditText) findViewById(R.id.oldPasswordField);
        newPasswordEditText = (EditText) findViewById(R.id.newPasswordField);
        newRepeatedPasswordEditText = (EditText) findViewById(R.id.newRepeatedPasswordField);

        myMail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));

        mailEditText.setText(myMail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
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

    public void changePassword(View view) {
        String mailFieldText = mailEditText.getText().toString();
        String oldPasswordFieldText = oldPasswordEditText.getText().toString();
        String newPasswordFieldText = newPasswordEditText.getText().toString();
        String newRepeatedPasswordFieldText = newRepeatedPasswordEditText.getText().toString();

        if (mailFieldText.isEmpty()) {
            mailEditText.setError(getString(R.string.mail_empty));
        }
        if (oldPasswordFieldText.isEmpty()) {
            oldPasswordEditText.setError(getString(R.string.password_empty));
        }
        if (newPasswordFieldText.isEmpty()) {
            newPasswordEditText.setError(getString(R.string.password_empty));
        }
        if (newRepeatedPasswordFieldText.isEmpty()) {
            newRepeatedPasswordEditText.setError(getString(R.string.password_empty));
        }
        if (newPasswordFieldText.equals(newRepeatedPasswordFieldText)) {
            newPasswordEditText.setError(getString(R.string.password_not_equals));
            newRepeatedPasswordEditText.setError(getString(R.string.password_not_equals));
        }

        if ((!mailFieldText.isEmpty()) && (!oldPasswordFieldText.isEmpty()) &&
                (!newRepeatedPasswordFieldText.isEmpty()) && (!newPasswordFieldText.isEmpty())) {
            rootRef.changePassword(mailFieldText, oldPasswordFieldText, newPasswordFieldText, new Firebase.ResultHandler(){
                @Override
                public void onSuccess() {
                    Toast.makeText(ChangePasswordActivity.this, getString(R.string.correct_change_password_message),
                            Toast.LENGTH_SHORT).show();
                    ChangePasswordActivity.this.finish();
                }
                @Override
                public void onError(FirebaseError firebaseError) {
                    Toast.makeText(ChangePasswordActivity.this, getString(R.string.incorrect_change_password_message) +
                                    " " + firebaseError, Toast.LENGTH_SHORT).show();
                }
            });//change Password
        }
    }

    public void back (View view) {
        ChangePasswordActivity.this.finish();
    }
}

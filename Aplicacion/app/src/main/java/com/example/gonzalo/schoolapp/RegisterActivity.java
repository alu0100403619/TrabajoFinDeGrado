package com.example.gonzalo.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class RegisterActivity extends ActionBarActivity {

    Spinner spinner1;
    //Button submit;
    Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Firebase.setAndroidContext(this);

        spinner1 = (Spinner) findViewById(R.id.spinner_1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = String.valueOf(spinner1.getSelectedItem());
                if (selected.equals(getString(R.string.alumno))) {
                    ref = new Firebase (getString(R.string.aluRef));
                }
                else if (selected.equals(getString(R.string.teacher))) {
                    ref = new Firebase (getString(R.string.profeRef));
                }
                else if (selected.equals(getString(R.string.mother))) {
                    ref = new Firebase (getString(R.string.madreRef));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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

        final Firebase rootRef = new Firebase (getString(R.string.rootRef));

        final String mail = ((EditText) findViewById(R.id.text_mail)).getText().toString();
        final String password = ((EditText) findViewById(R.id.text_password)).getText().toString();

        rootRef.createUser(mail, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                sendToBbdd();
                //login
                rootRef.authWithPassword(mail, password,
                        new Firebase.AuthResultHandler() {

                            @Override
                            public void onAuthenticated(AuthData authData) {
                                //TODO lanzar actividad principal
                            }//onAuthenticated

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {
                                Toast.makeText(RegisterActivity.this,
                                        getString(R.string.login_error)
                                        + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent (RegisterActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);
                                RegisterActivity.this.finish();
                            }//onAuthenticationError
                        });//authWithPassword
            }//onSuccess

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(RegisterActivity.this, getString(R.string.register_error) +
                        firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }//onError
        });//createUser
    }//function

    public void sendToBbdd() {

        String uuid = UUID.randomUUID().toString();
        Firebase regRef = ref.child(uuid);

        //Obtenemos los datos del XML
        String rol = String.valueOf(spinner1.getSelectedItem());
        String mail = ((EditText) findViewById(R.id.text_mail)).getText().toString();
        String name = ((EditText) findViewById(R.id.text_name)).getText().toString();
        String lastname = ((EditText) findViewById(R.id.text_lastname)).getText().toString();
        String school = ((EditText) findViewById(R.id.text_college)).getText().toString();
        String telephone = ((EditText) findViewById(R.id.text_telephone)).getText().toString();

        //Metemos en un HashMap los datos
        Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put(getString(R.string.bbdd_mail), mail);
        infoMap.put(getString(R.string.bbdd_name), name);
        infoMap.put(getString(R.string.bbdd_lastname), lastname);
        infoMap.put(getString(R.string.bbdd_center), school);
        infoMap.put(getString(R.string.bbdd_telephone), telephone);
        infoMap.put(getString(R.string.bbdd_rol), rol);

        //Actualizamos la BBDD
        regRef.updateChildren(infoMap);
    }//function

}//class

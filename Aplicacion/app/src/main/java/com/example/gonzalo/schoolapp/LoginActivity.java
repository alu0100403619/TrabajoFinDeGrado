package com.example.gonzalo.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


public class LoginActivity extends ActionBarActivity {

    EditText mailEditText, passwordEditText;
    String userType, mail;
    Firebase rootRef;
    ArrayList<String> clases = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        rootRef = new Firebase(getString(R.string.rootRef));

        mailEditText = (EditText) findViewById(R.id.mailField);
        passwordEditText = (EditText) findViewById(R.id.passwordField);
        //TODO si auth != null lanzar la actividad correcta
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
        //TODO Boton de Salir
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String setTypeUser () {
        userType = "";
        mail = mailEditText.getText().toString();
        Query rolQuery = rootRef.child(getString(R.string._alumnos))
                .orderByChild(getString(R.string.bbdd_mail)).equalTo(mail);
        rolQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            //Alumno
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userType = dataSnapshot.getKey();
                //Si el usuario no es un alumno
                if (dataSnapshot.getValue() == null) {
                    //Padre
                    Query rolQuery2 = rootRef.child(getString(R.string._padres))
                            .orderByChild(getString(R.string.bbdd_mail)).equalTo(mail);
                    rolQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userType = dataSnapshot.getKey();
                            //Si el usuario no es un padre
                            if (dataSnapshot.getValue() == null) {
                                //Profesor
                                Query rolQuery3 = rootRef.child(getString(R.string._profes))
                                        .orderByChild(getString(R.string.bbdd_mail)).equalTo(mail);
                                rolQuery3.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        userType = dataSnapshot.getKey();
                                        //Si el usuario no es un profesor
                                        if (dataSnapshot.getValue() == null) {
                                            Toast.makeText(LoginActivity.this,
                                                    getString(R.string.login_error) + " UserType",
                                                    Toast.LENGTH_LONG).show();
                                        }//if no user
                                        else {
                                            //Profesor
                                            Map<String, Object> values3 = (Map<String, Object>) dataSnapshot.getValue();
                                            clases = getUserClass (values3);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {}
                                });
                            }//if no padre
                            else {
                                //Padre
                                Map<String, Object> values2 = (Map<String, Object>) dataSnapshot.getValue();
                                clases = getUserClass (values2);
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });
                }//if no alumno
                else {
                    //Alumno
                    Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                    clases = getUserClass (values);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
        return userType;
    }//function

    public void login (View view) {

        final String mail = mailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (mail.isEmpty()) {
            mailEditText.setError(getString(R.string.field_empty));
        }

        if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.field_empty));
        }

        if ((!mail.isEmpty()) && (!password.isEmpty())) {
            userType = setTypeUser();
            rootRef.authWithPassword(mail, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    if (userType.equals(getString(R.string._alumnos))) {
                        Intent intent = new Intent(LoginActivity.this, AlumnoTabActivity.class);
                        intent.putExtra(getString(R.string.bbdd_mail), mail);
                        intent.putExtra(getString(R.string.bbdd_class), clases.get(0));
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                    else if (userType.equals(getString(R.string._profes))) {
                        Intent intent = new Intent(LoginActivity.this, TeachersTabActivity.class);
                        intent.putExtra(getString(R.string.bbdd_mail), mail);
                        intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                    else if (userType.equals(getString(R.string._padres))) {
                        //Intent intent = new Intent(LoginActivity.this, FathersTabActivity.class);
                        //intent.putExtra(getString(R.string.bbdd_mail), mail);
                        //intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
                        //startActivity(intent);
                        //LoginActivity.this.finish();
                        Toast.makeText(LoginActivity.this,
                                getString(R.string.mother), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.login_error)
                                    + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });//rootRef
        }//if
    }//funciton

    public ArrayList<String> getUserClass (Map<String, Object> values) {
        ArrayList<String> clases = new ArrayList<>();
        Map<String, Object> data = null, tempMap = null;
        Set<String> keys = values.keySet();
        for (String key: keys) {
            data = (Map<String, Object>) values.get(key);
        }
        if (userType.equals(getString(R.string._alumnos))) {
            clases.add(data.get(getString(R.string.bbdd_class)).toString());
        }
        else if (userType.equals(getString(R.string._profes))) {
            tempMap = (Map<String, Object>) data.get(getString(R.string.bbdd_teacher_class));
            keys = tempMap.keySet();
            for (String key: keys) {
                clases.add(tempMap.get(key).toString());
            }//for
        }
        else if (userType.equals(getString(R.string._padres))) {
            tempMap = (Map<String, Object>) data.get(getString(R.string.bbdd_children));
            data.clear();
            keys = tempMap.keySet();
            for (String key: keys) {
                data = (Map<String, Object>) tempMap.get(key);
                clases.add(data.get(getString(R.string.bbdd_class)).toString());
            }//for
        }
        return clases;
    }

    public void launchRegister (View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        this.finish();
    }
}

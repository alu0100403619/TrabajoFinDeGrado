package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.utilities.Utilities;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


public class LoginActivity extends Activity {

    EditText mailEditText, passwordEditText;
    String userType, mail, myName, myRol, myDNI;
    Firebase rootRef;
    ArrayList<String> clases = new ArrayList<>();
    ArrayList<String> colegios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }
        else if (id == R.id.action_change_language) {
            Intent intent = new Intent(this, ChangeLanguageActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    public String setTypeUser () {
        userType = "";
        mail = mailEditText.getText().toString();
        Query rolQuery = rootRef.child(getString(R.string._students))
                .orderByChild(getString(R.string.bbdd_mail)).equalTo(mail);
        rolQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            //Alu
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userType = dataSnapshot.getKey();
                //Si el usuario no es un alumno
                if (dataSnapshot.getValue() == null) {
                    //Padre
                    Query rolQuery2 = rootRef.child(getString(R.string._fathers))
                            .orderByChild(getString(R.string.bbdd_mail)).equalTo(mail);
                    rolQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userType = dataSnapshot.getKey();
                            //Si el usuario no es un padre
                            if (dataSnapshot.getValue() == null) {
                                //Profesor
                                Query rolQuery3 = rootRef.child(getString(R.string._teachers))
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
                    //Alu
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

        if (mail.isEmpty()){
            mailEditText.setError(getString(R.string.field_empty));
        }
        else if (!Utilities.isMail(mail)) {
            mailEditText.setError(getString(R.string.mail_format_error));
        }

        if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.field_empty));
        }

        if ((!mail.isEmpty()) && (!password.isEmpty()) && (Utilities.isMail(mail))) {

            //Hide the keyboard
            mailEditText.clearFocus();
            passwordEditText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            //loading
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View loadView = layoutInflater.inflate(R.layout.load, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set load.xml to alertdialog builder
            alertDialogBuilder.setView(loadView);

            final ImageView imageViewRotator = (ImageView) loadView.findViewById(R.id.ImageViewRotator);
            final Animation myRotation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.animation_rotate_pencil);

            // create alert dialog
            final AlertDialog alertDialog = alertDialogBuilder.create();

            //start the animation
            imageViewRotator.startAnimation(myRotation);

            // show it
            alertDialog.show();

            userType = setTypeUser();
            rootRef.authWithPassword(mail, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    if (userType.equals(getString(R.string._students))) {
                        Intent intent = new Intent(LoginActivity.this, AlumnoTabActivity.class);
                        intent.putExtra(getString(R.string.bbdd_mail), mail);
                        intent.putExtra(getString(R.string.myName), myName);
                        intent.putExtra(getString(R.string.myRol), myRol);
                        intent.putExtra(getString(R.string.myDNI), myDNI);
                        intent.putExtra(getString(R.string.bbdd_class), clases.get(0));
                        //El alumno solo va a un colegio
                        intent.putExtra(getString(R.string.bbdd_center), colegios.get(0));
                        alertDialog.dismiss();
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                    else if (userType.equals(getString(R.string._teachers))) {
                        Intent intent = new Intent(LoginActivity.this, TeachersTabActivity.class);
                        intent.putExtra(getString(R.string.bbdd_mail), mail);
                        intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
                        intent.putExtra(getString(R.string.myDNI), myDNI);
                        intent.putExtra(getString(R.string.myName), myName);
                        intent.putExtra(getString(R.string.myRol), myRol);
                        //Suponemos que un profesor solo da clases en un colegio
                        intent.putExtra(getString(R.string.bbdd_center), colegios.get(0));
                        alertDialog.dismiss();
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                    else if (userType.equals(getString(R.string._fathers))) {
                        Intent intent = new Intent(LoginActivity.this, FathersTabActivity.class);
                        intent.putExtra(getString(R.string.bbdd_mail), mail);
                        intent.putExtra(getString(R.string.bbdd_teacher_class), clases);
                        intent.putExtra(getString(R.string.myDNI), myDNI);
                        intent.putExtra(getString(R.string.bbdd_center), colegios);
                        intent.putExtra(getString(R.string.myName), myName);
                        intent.putExtra(getString(R.string.myRol), myRol);
                        alertDialog.dismiss();
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    alertDialog.dismiss();
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.login_error)
                                    + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });//rootRef
        }//if
    }//funciton

    public ArrayList<String> getUserClass (Map<String, Object> values) {
        String clase, school;
        ArrayList<String> clases = new ArrayList<>();
        Map<String, Object> data = null, tempMap = null;
        Set<String> keys = values.keySet();
        for (String key: keys) {
            data = (Map<String, Object>) values.get(key);
        }
        myName = data.get(getString(R.string.bbdd_name)) + " " +
                data.get(getString(R.string.bbdd_lastname));
        myDNI = data.get(getString(R.string.bbdd_dni)).toString();
        if (userType.equals(getString(R.string._students))) {
            myRol = "Alumno";
            clases.add(data.get(getString(R.string.bbdd_class)).toString());
            colegios.add(data.get(getString(R.string.bbdd_center)).toString());
        }
        else if (userType.equals(getString(R.string._teachers))) {
            myRol = "Profesor";
            tempMap = (Map<String, Object>) data.get(getString(R.string.bbdd_teacher_class));
            keys = tempMap.keySet();
            for (String key: keys) {
                clase = tempMap.get(key).toString();
                if (!clases.contains(clase)) {
                    clases.add(clase);
                }//if
            }//for
            colegios.add(data.get(getString(R.string.bbdd_center)).toString());
        }
        else if (userType.equals(getString(R.string._fathers))) {
            myRol = "Padre";
            tempMap = (Map<String, Object>) data.get(getString(R.string.bbdd_children));
            data.clear();
            keys.clear();
            keys = tempMap.keySet();
            for (String key: keys) {
                data = (Map<String, Object>) tempMap.get(key);
                clase = data.get(getString(R.string.bbdd_class)).toString();
                school = data.get(getString(R.string.bbdd_center)).toString();
                if (!clases.contains(clase)) {
                    clases.add(clase);
                }
                if (!colegios.contains(school)) {
                    colegios.add(school);
                }
            }//for
        }
        return clases;
    }

    public void launchRegister (View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void resetPassword (View view) {
        mail = mailEditText.getText().toString();
        Log.i("LoginActivity", "Mail: "+mail);
        if (mail.isEmpty()) {
            mailEditText.setError(getString(R.string.field_empty));
        }//if
        else {
            rootRef.resetPassword(mail, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.forget_Password_message),
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.forget_Password_message_error),
                            Toast.LENGTH_LONG).show();
                }
            });//resetPassword
        }//else
    }
}
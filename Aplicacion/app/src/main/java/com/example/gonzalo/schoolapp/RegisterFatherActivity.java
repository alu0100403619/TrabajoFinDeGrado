package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.clases.Alumno;
import com.example.gonzalo.schoolapp.utilities.Utilities;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class RegisterFatherActivity extends Activity {

    ArrayList<Alumno> children;
    ArrayList<Boolean> childrenExist;//si es false annadir a la BD ademas de al padre
    ArrayList<String> childrenKey, schools, classes;
    Firebase rootRef, studentRef, fatherRef;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_father);
        Firebase.setAndroidContext(this);

        rootRef = new Firebase(getString(R.string.rootRef));
        studentRef = new Firebase(getString(R.string.studentRef));
        fatherRef = new Firebase(getString(R.string.fatherRef));

        children = new ArrayList<>();
        childrenExist = new ArrayList<>();
        childrenKey = new ArrayList<>();
        schools = new ArrayList<>();
        classes = new ArrayList<>();
        TextView numberChildrenTextView = (TextView) findViewById(R.id.numberChildren);
        numberChildrenTextView.setText(String.valueOf(children.size()));

        /*if (children.size() == 0) {
            launchRegisterChild();
        }//*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_father, menu);
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

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        final String mail = ((EditText) findViewById(R.id.text_mail)).getText().toString();
        final String password = ((EditText) findViewById(R.id.text_password)).getText().toString();

        if (!haveEmptyFields()) {
            showLoading();
            rootRef.createUser(mail, password, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    String uuid = null;

                    final String name = ((EditText) findViewById(R.id.text_name)).getText().toString();
                    String lastname = ((EditText) findViewById(R.id.text_lastname)).getText().toString();
                    String telephone = ((EditText) findViewById(R.id.text_telephone)).getText().toString();

                    Map<String, Object> fatherMap = new HashMap<String, Object>();
                    Map<String, Object> infoMap = new HashMap<String, Object>();
                    fatherMap.put(getString(R.string.bbdd_name), name);
                    fatherMap.put(getString(R.string.bbdd_lastname), lastname);
                    fatherMap.put(getString(R.string.bbdd_telephone), telephone);
                    fatherMap.put(getString(R.string.bbdd_mail), mail);

                    for (int i = 0; i < children.size(); i++) {
                        Map<String, Object> childMap = children.get(i).toMap();
                        Log.i("RFA", "ChildMap: " + childMap);
                        if (childrenExist.get(i).booleanValue() == false) {
                            uuid = UUID.randomUUID().toString();
                            studentRef.child(uuid).setValue(childMap);
                            childMap.remove(getString(R.string.bbdd_telephone));
                            childMap.remove(getString(R.string.bbdd_mail));
                            infoMap.put(uuid, childMap);
                        }
                        else {
                            childMap.remove(getString(R.string.bbdd_telephone));
                            childMap.remove(getString(R.string.bbdd_mail));
                            infoMap.put(childrenKey.get(0), childMap);
                            childrenKey.remove(0);
                        }
                    }
                    fatherMap.put(getString(R.string.bbdd_children), infoMap);
                    Log.i("RFA", "fatherMap: " + fatherMap);
                    uuid = UUID.randomUUID().toString();
                    fatherRef.child(uuid).setValue(fatherMap);

                    //Lanzar FathersTabActivity
                    rootRef.authWithPassword(mail, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Intent intent = new Intent(RegisterFatherActivity.this, FathersTabActivity.class);
                            intent.putExtra(getString(R.string.bbdd_mail), mail);
                            intent.putExtra(getString(R.string.bbdd_center), schools);
                            intent.putExtra(getString(R.string.bbdd_teacher_class), classes);
                            intent.putExtra(getString(R.string.myName), name);
                            intent.putExtra(getString(R.string.myRol), getString(R.string.rol_father));
                            alertDialog.dismiss();
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Toast.makeText(RegisterFatherActivity.this,
                                    getString(R.string.login_error)
                                            + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterFatherActivity.this, LoginActivity.class);
                            alertDialog.dismiss();
                            startActivity(intent);
                            finish();
                        }
                    });//auth

                    alertDialog.dismiss();
                    finish();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    alertDialog.dismiss();
                    Toast.makeText(RegisterFatherActivity.this, firebaseError.toString(),
                            Toast.LENGTH_LONG).show();
                }
            });//createUser
        }
    }

    public void launchRegisterChild (View view) {
        launchRegisterChild();
    }

    public void launchRegisterChild() {
        Intent intent = new Intent(this, AddChildActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String key;
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Alumno student = data.getExtras().getParcelable(getString(R.string.bbdd_children));
                boolean exist = data.getExtras().getBoolean(getString(R.string.exist));
                if (data.getExtras().containsKey(getString(R.string.key))) {
                    key = data.getExtras().getString(getString(R.string.key));
                    childrenKey.add(key);
                    Log.i("RFA", "key: " + key);
                }
                Log.i("RFA", "student: " + student);
                Log.i("RFA", "exist: " + exist);
                children.add(student);
                childrenExist.add(new Boolean(exist));
                if (!schools.contains(student.getSchool())) {
                    schools.add(student.getSchool());
                }
                if (!classes.contains(student.getClassroom())) {
                    classes.add(student.getClassroom());
                }
                ((TextView) findViewById(R.id.numberChildren)).setText(String.valueOf(children.size()));
            }
        }
    }

    public boolean haveEmptyFields () {
        EditText nameEditText = (EditText) findViewById(R.id.text_name);
        EditText lastnameEditText = (EditText) findViewById(R.id.text_lastname);
        EditText telephoneEditText = (EditText) findViewById(R.id.text_telephone);
        EditText mailEditText = (EditText) findViewById(R.id.text_mail);
        EditText passwordEditText = (EditText) findViewById(R.id.text_password);

        String name = nameEditText.getText().toString();
        String lastname = lastnameEditText.getText().toString();
        String telephone = telephoneEditText.getText().toString();
        String mail = mailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean haveEmptyFields = false;

        if (name.isEmpty()) {
            Log.i("RegFatAct", "Name Empty");
            nameEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        }
        if (lastname.isEmpty()) {
            Log.i("RegFatAct", "lastname Empty");
            lastnameEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        }
        if (telephone.isEmpty()) {
            Log.i("RegFatAct", "telephone Empty");
            telephoneEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        } else if (!Utilities.isTelephone(telephone))  {
            Log.i("RegFatAct", "is not Telephone");
            telephoneEditText.setError(getString(R.string.telephone_format_error));
            haveEmptyFields = true;
        }
        if (mail.isEmpty()) {
            Log.i("RegFatAct", "Mail Empty");
            mailEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        } else if (!Utilities.isMail(mail)) {
            Log.i("RegFatAct", "Is not Mail");
            mailEditText.setError(getString(R.string.mail_format_error));
            haveEmptyFields = true;
        }
        if (password.isEmpty()) {
            Log.i("RegFatAct", "Password Empty");
            passwordEditText.setError(getString(R.string.field_empty));
            haveEmptyFields = true;
        }
        if (children.size() == 0) {
            Log.i("RegFatAct", "Have not Childrens");
            ImageView asterisk1 = (ImageView) findViewById(R.id.asterisk1);
            asterisk1.setImageResource(R.drawable.ic_action_required_empty);
            Toast.makeText(this, getString(R.string.empty_children), Toast.LENGTH_LONG).show();
            haveEmptyFields = true;
        }

        return haveEmptyFields;
    }

    public void showLoading () {
        //loading
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View loadView = layoutInflater.inflate(R.layout.load, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set load.xml to alertdialog builder
        alertDialogBuilder.setView(loadView);

        ImageView imageViewRotator = (ImageView) loadView.findViewById(R.id.ImageViewRotator);
        Animation myRotation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_rotate_pencil);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        //start the animation
        imageViewRotator.startAnimation(myRotation);

        // show it
        alertDialog.show();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        this.finish();
    }
}

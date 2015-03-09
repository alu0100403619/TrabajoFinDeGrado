package com.example.gonzalo.firebasestorage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

//https://www.firebase.com/docs/android/guide/
public class MainActivity extends ActionBarActivity {

    Firebase dinoRef, scoreRef, rootRef;
    Spinner atributos;
    String atributo;
    ArrayList<String> dinosaurs;
    Spinner dinoSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        rootRef = new Firebase("https://dinosaurios.firebaseio.com");
        dinoRef = new Firebase("https://dinosaurios.firebaseio.com/dinosaurios");
        scoreRef = new Firebase("https://dinosaurios.firebaseio.com/scores");
        atributos = (Spinner) findViewById(R.id.atributos);
        dinoSpinner = (Spinner) findViewById(R.id.dinoSpinner);
        atributo = String.valueOf(atributos.getSelectedItem());

        //dinosaurs = new ArrayList<String>();
        dinosaurs = getDinosaurs();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dinosaurs);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dinoSpinner.setAdapter(spinnerAdapter);
        //dinoSpinner.setSelection(0);

        /*ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(MainActivity.this, String.valueOf(dataSnapshot.getValue()),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(MainActivity.this, firebaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });//*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_logout) {
            rootRef.unauth();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void orderBy (View view) {
        //query a la BBDD
        Query queryRef/* = ref.orderByChild(atributo)*/;
        //Obtenemos el atributo por el que vamos a ordenar
        atributo = String.valueOf(atributos.getSelectedItem());
        if (atributo == getString(R.string.name)) {
            queryRef =  dinoRef.orderByKey();
        }//if
        else{
            queryRef = dinoRef.orderByChild(atributo);
        }//else
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Toast.makeText(MainActivity.this, dataSnapshot.getKey()/*String.valueOf(values)*/,
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    public void orderByScores (View view) {
        //query a la BBDD
        Query queryRef = scoreRef.orderByValue();
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(MainActivity.this, dataSnapshot.getKey(),
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });//queryRef
    }

    public void twoMostHighest (View view) {
        //query a la BBDD
        Query queryRef = dinoRef.orderByChild(getString(R.string.hight)).limitToLast(2);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(MainActivity.this, dataSnapshot.getKey(),
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });//queryRef
    }

    public void twoMostlight (View view) {
        //query a la BBDD
        Query queryRef = dinoRef.orderByChild(getString(R.string.weight))
                .limitToFirst(2);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(MainActivity.this, dataSnapshot.getKey(),
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });//queryRef
    }

    public void between1And9 (View view) {
        //query a la BBDD
        Query queryRef = dinoRef.orderByChild(getString(R.string.length))
                .startAt(1).endAt(9);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(MainActivity.this, dataSnapshot.getKey(),
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });//queryRef
    }

    public ArrayList<String> getDinosaurs () {
        final ArrayList<String> tmp = new ArrayList<String>();
        Query queryRef = dinoRef.orderByKey();
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!tmp.contains(dataSnapshot.getKey())) {
                    tmp.add(dataSnapshot.getKey());
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });//queryRef
        return tmp;
    }

    public void launchDinoRegister (View view) {
        Intent intent = new Intent(this, DinoRegisterActivity.class);
        startActivity(intent);
        this.finish();
    }

    //ERROOOOOOOOOOOOOOOOOOOOOOOOOOOOOORRRRRRRRRRRRR
    public void moreShortThan (View view) {
        final String dinosaurName = dinoSpinner.getSelectedItem().toString();

        dinoRef.child(dinosaurName).child(getString(R.string.hight))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataDinoSnapshot) {
                        Long dinoHight = dataDinoSnapshot.getValue(Long.class);
                        Query queryRef = dinoRef.orderByChild("height").endAt(dinoHight)
                                .limitToLast(2);
                        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot querySnapshot) {
                                if (querySnapshot.getChildrenCount() == 2) {
                                    // Data is ordered by increasing height, so we want the first
                                    // entry
                                    DataSnapshot dinosaur = querySnapshot.getChildren().iterator()
                                            .next();
                                    String message = "The dinosaur just shorter than the" +
                                            dinosaurName + " is " + dinosaur.getKey();
                                    Toast.makeText(MainActivity.this, message,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    String message = "The" + dinosaurName + "is the shortest dino";
                                    Toast.makeText(MainActivity.this, message,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }

                        });//queryRef
                    }//onDataChange

                    @Override
                    //DinoRef
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });//dinoRef*/
    }

}//class

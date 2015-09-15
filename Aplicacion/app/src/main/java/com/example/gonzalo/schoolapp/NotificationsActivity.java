package com.example.gonzalo.schoolapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.gonzalo.schoolapp.Adapters.NotifyAdapter;
import com.example.gonzalo.schoolapp.clases.Message;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Gonzalo on 17/02/2015.
 */
public class NotificationsActivity extends ListActivity {

    String mail, myName, myRol, myDNI;
    Firebase messageRef;
    ArrayList<Message> messagesList;
    ArrayList<String> messagesListView, dnisList;
    ArrayList<Integer> numberMessages;
    ArrayList<String> rolRemitterMessages;
    NotifyAdapter notifyAdapter;

    public void onCreate (Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Firebase.setAndroidContext(this);
        messageRef = new Firebase(getString(R.string.messagesRef));
        messagesList = new ArrayList<>(); //Todos los mensajes
        messagesListView = new ArrayList<>(); //Nombres de los que nos mandan mensajes
        rolRemitterMessages = new ArrayList<>(); //Roles de los que nos mandan mensajes
        dnisList = new ArrayList<>(); //DNIs de los que nos mandan mensajes
        numberMessages = new ArrayList<>(); //Numero de Mensajes del remitente, su pos coincide con MessageListView

        //Obtenemos el Mail
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));
        myDNI = getIntent().getExtras().getString(getString(R.string.myDNI));

        //Preparamos los datos
        preparingData();

        //Listener click Normal
        this.getListView().setClickable(true);
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = 0;
                String itemDni = dnisList.get(position);
                ArrayList<Message> messages = new ArrayList<Message>();

                while (i < messagesList.size()) {
                    Message message = messagesList.get(i);
                    if ((itemDni.equals(message.getDniRemitter())) && (!messages.contains(message))) {
                        messages.add(message);
                        messagesList.remove(message);
                        i--;
                    }//if
                    i++;
                }//while

                Intent intent = new Intent(NotificationsActivity.this, ChatActivity.class);
                //Lanzar Actividad
                intent.putExtra(getString(R.string.bbdd_message), messages);
                intent.putExtra(getString(R.string.name), messagesListView.get(position));
                intent.putExtra(getString(R.string.mail), mail);
                intent.putExtra(getString(R.string.bbdd_dni_remitter), messages.get(0).getDniRemitter());//ERROR
                intent.putExtra(getString(R.string.myName), myName);
                intent.putExtra(getString(R.string.myRol), myRol);
                intent.putExtra(getString(R.string.myDNI), myDNI);

                //Borrar al hacer click
                int pos = dnisList.indexOf(messages.get(0).getDniRemitter());
//              if ((pos != -1) || (pos >= 0)) {
                dnisList.remove(pos);
                messagesListView.remove(pos);
                numberMessages.remove(pos);
                rolRemitterMessages.remove(pos);
                notifyAdapter.notifyDataSetChanged();
                startActivityForResult(intent, 1);
                Log.i("NotificationsActivity", "Eliminar al hacer click");
            }//public void onItemClick
        });//setOnITemClickListener
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String mailRemitter = data.getStringExtra(getString(R.string.bbdd_mail_remitter));
                int pos = dnisList.indexOf(mailRemitter);
                if (pos != -1) {
                    dnisList.remove(pos);
                    messagesListView.remove(pos);
                    numberMessages.remove(pos);
                    rolRemitterMessages.remove(pos);
                    notifyAdapter.notifyDataSetChanged();
                    Log.i("NotificationsActivity", "Eliminar al Volver del chat con "+mailRemitter);
                }
            }
        }
    }

    public void preparingData() {
        //Obtener los Mensajes
        Query getMessages = messageRef.orderByChild(getString(R.string.bbdd_addressee)).equalTo(myDNI);
        getMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Message message = new Message(values);
                messagesList.add(message);
                if (!dnisList.contains(message.getDniRemitter())) {
                    dnisList.add(message.getDniRemitter());
                    messagesListView.add(message.getRemitter());
                    numberMessages.add(1);
                    rolRemitterMessages.add((String) values.get(getString(R.string.bbdd_rol_remitter)));
                } else {
                    int pos = dnisList.indexOf(message.getDniRemitter());
                    int number = numberMessages.get(pos) + 1;
                    numberMessages.remove(pos);
                    numberMessages.add(pos, number);
                    //El rol ya esta en el ArrayList
                }

                //Borrar el mensaje de la BBDD
                messageRef.child(dataSnapshot.getKey()).removeValue();

                //Seteamos el ArrayAdapter
                notifyAdapter = new NotifyAdapter(NotificationsActivity.this, messagesListView,
                        numberMessages, rolRemitterMessages);
                setListAdapter(notifyAdapter);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });//getMessages
    }

    @Override
    public void onBackPressed(){
        Log.i("NotificationsActivity", "Back Pressed");
        Firebase rootref = new Firebase (getString(R.string.rootRef));
        Intent intent = new Intent(this, LoginActivity.class);
        rootref.unauth();
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        //messagesList
        savedInstanceState.putParcelableArrayList(getString(R.string.messagesList), messagesList);
        //messagesListView
        savedInstanceState.putStringArrayList(getString(R.string.messagesListView), messagesListView);
        //rolRemitterMessages
        savedInstanceState.putStringArrayList(getString(R.string.rolRemitterMessages), rolRemitterMessages);
        //dnisList
        savedInstanceState.putStringArrayList(getString(R.string.dnisList), dnisList);
        //numberMessages
        savedInstanceState.putIntegerArrayList(getString(R.string.numberMessages), numberMessages);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        messagesList = savedInstanceState.getParcelableArrayList(getString(R.string.messagesList));
        messagesListView = savedInstanceState.getStringArrayList(getString(R.string.messagesListView));
        rolRemitterMessages = savedInstanceState.getStringArrayList(getString(R.string.rolRemitterMessages));
        dnisList = savedInstanceState.getStringArrayList(getString(R.string.dnisList));
        numberMessages = savedInstanceState.getIntegerArrayList(getString(R.string.numberMessages));
        notifyAdapter = new NotifyAdapter(NotificationsActivity.this, messagesListView,
                numberMessages, rolRemitterMessages);
        setListAdapter(notifyAdapter);
    }

}//class
package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonzalo.schoolapp.Adapters.NotifyAdapter;
import com.example.gonzalo.schoolapp.clases.Alumno;
import com.example.gonzalo.schoolapp.clases.Message;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Gonzalo on 17/02/2015.
 */
public class NotificationsActivity extends ListActivity {

    String mail, myName, myRol;
    Firebase messageRef;
    ArrayList<Message> messagesList;
    ArrayList<String> messagesListView, mailsList;
    ArrayList<Integer> numberMessages;
    ArrayList<String> rolRemitterMessages;
    NotifyAdapter notifyAdapter;

    public void onCreate (Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Firebase.setAndroidContext(this);
        messageRef = new Firebase(getString(R.string.mensajesRef));
        messagesList = new ArrayList<>(); //Todos los mensajes
        messagesListView = new ArrayList<>(); //Nombres de los que nos mandan mensajes
        rolRemitterMessages = new ArrayList<>(); //Roles de los que nos mandan mensajes
        mailsList = new ArrayList<>(); //Mails de los que nos mandan mensajes
        numberMessages = new ArrayList<>(); //Numero de Mensajes del remitente, su pos coincide con MessageListView

        //Obtenemos el Mail
        mail = getIntent().getExtras().getString(getString(R.string.bbdd_mail));
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));

        //Preparamos los datos
        preparingData();

        //Listener Click Largo

        //Listener click Normal
        this.getListView().setClickable(true);
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemMail = mailsList.get(position);
                ArrayList<Message> messages = new ArrayList<Message>();
                //TODO error en mi tablet pero no en mi movil (A veces)
                //java.util.ConcurrentModificationException
                for (Message message : messagesList) {
                    if ((itemMail.equals(message.getMailRemitter())) && (!messages.contains(message))) {
                        messages.add(message);
                        messagesList.remove(message);
                    }//if
                }//for

                //Lanzar Actividad
                //Intent intent = new Intent(NotificationsActivity.this, ChatActivity.class);
                Intent intent = new Intent(NotificationsActivity.this, Chat2Activity.class);
                intent.putExtra(getString(R.string.bbdd_message), messages);
                intent.putExtra(getString(R.string.name), messagesListView.get(position));
                intent.putExtra(getString(R.string.mail), mail);
                intent.putExtra(getString(R.string.mail_remitter), messages.get(0).getMailRemitter());
                intent.putExtra(getString(R.string.myName), myName);
                intent.putExtra(getString(R.string.myRol), myRol);

                //Borrar al hacer click
                //notifyAdapter.remove(position);
                int pos = mailsList.indexOf(messages.get(0).getMailRemitter());
                mailsList.remove(pos);
                messagesListView.remove(pos);
                numberMessages.remove(pos);
                rolRemitterMessages.remove(pos);
                notifyAdapter.notifyDataSetChanged();

                startActivity(intent);

            }//public void onItemClick
        });//setOnITemClickListener
    }

    public void preparingData() {
        //Obtener los Mensajes
        Query getMessages = messageRef.orderByChild(getString(R.string.bbdd_addressee)).equalTo(mail);
        getMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Message message = new Message(values);
                messagesList.add(message);
                if (!mailsList.contains(message.getMailRemitter())) {
                    mailsList.add(message.getMailRemitter());
                    messagesListView.add(message.getRemitter());
                    numberMessages.add(1);
                    rolRemitterMessages.add((String) values.get(getString(R.string.bbdd_rol_remitter)));
                }
                else {
                    int pos = mailsList.indexOf(message.getMailRemitter());
                    int number = numberMessages.get(pos) + 1;
                    numberMessages.add(pos, number);
                    //El rol ya esta en el ArrayLis
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
}//class
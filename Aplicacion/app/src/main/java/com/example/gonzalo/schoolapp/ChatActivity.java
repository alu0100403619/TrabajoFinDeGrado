package com.example.gonzalo.schoolapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.gonzalo.schoolapp.Adapters.ChatAdapter;
import com.example.gonzalo.schoolapp.clases.Date;
import com.example.gonzalo.schoolapp.clases.Message;
import com.example.gonzalo.schoolapp.database.MessageSQLHelper;
import com.example.gonzalo.schoolapp.utilities.Utilities;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import junit.runner.Version;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Gonzalo on 29/05/2015.
 *
 *
 */
public class ChatActivity extends ListActivity {

    String chatName, mail, myName, dniRemitter, myRol, myDNI, idConversation;
    ArrayList<Message> messages, tempMessages;
    MessageSQLHelper messageBBDD;
    Firebase messageRef;
    ChatAdapter chatAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Firebase.setAndroidContext(this);
        messageRef = new Firebase(getString(R.string.messagesRef));

        messages = new ArrayList<>(); //Mensajes
        tempMessages = new ArrayList<>(); //Mensajes Temporales
        messageBBDD = new MessageSQLHelper(this); //BBDD

        //Obtener el Nombre
        chatName = getIntent().getExtras().getString(getString(R.string.name));

        //Obtener el Nombre
        myName = getIntent().getExtras().getString(getString(R.string.myName));
        myRol = getIntent().getExtras().getString(getString(R.string.myRol));
        myDNI = getIntent().getExtras().getString(getString(R.string.myDNI));

        //Obtener mi e-mail
        mail = getIntent().getExtras().getString(getString(R.string.mail));
        dniRemitter = getIntent().getExtras().getString(getString(R.string.bbdd_dni_remitter));
        idConversation = messageBBDD.getIdConversation(myDNI, dniRemitter);
        Log.i("ChatActivity", "idConversation: " + idConversation);

        //Cargar Mensajes Previos Almacenados en la memoria de la BBDD
        if (idConversation != null) {
            messages.addAll(messageBBDD.getAllMessagesConversation(idConversation));
        } else {
            messageBBDD.addConversation(myDNI, dniRemitter);
            idConversation = messageBBDD.getIdConversation(myDNI, dniRemitter);
        }

        //Cambiar el titulo de la ActionBar
        getActionBar().setTitle(chatName);

        //obtener los mensaje que se le pasan si vienen de NotificationsActivity
        if (getIntent().getExtras().containsKey(getString(R.string.bbdd_message))) {
            ArrayList<Message> messagesToNotifications = getIntent().getExtras().
                    getParcelableArrayList(getString(R.string.bbdd_message));
            for (Message message: messagesToNotifications) {
                messageBBDD.addMessage(message, idConversation);
            }
            messages.addAll(messagesToNotifications);
        }//if

        //Obtener Mensajes de la Nube
        getCloudMessages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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
        else if (id == R.id.action_empty_chat) {
            if (idConversation != null) {
                messageBBDD.deleteAllMessageConversation(idConversation);
                messageBBDD.deleteConversation(idConversation);
            }
            messages.clear();
            chatAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    public void send (View view) {
        EditText messageInputEditText = (EditText) findViewById(R.id.messageInput);
        String messageInput = messageInputEditText.getText().toString();
        if (!messageInput.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            Date date = new Date(calendar.get(calendar.DAY_OF_MONTH),
                calendar.get(calendar.MONTH) + 1, calendar.get(calendar.YEAR),
                calendar.getTime().getHours(), calendar.getTime().getMinutes());
            Message message = new Message(myDNI, myName, messageInput, date, myRol);
            messages.add(message);

            //Mandar el mensaje a la BBDD mientras el name no sea System
            sendToDataBase(message);

            messageInputEditText.setText(getString(R.string.empty));

            //Hide the keyboard
            messageInputEditText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            //Refresh the view
            chatAdapter.notifyDataSetChanged();
        }//si messageinput no esta vacio te lo deja mandar
    }

    @Override
    public void onBackPressed(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.bbdd_dni_remitter), dniRemitter);
        setResult(RESULT_OK,returnIntent);
        super.onBackPressed();
    }

    public void sendToDataBase(Message message) {
        Map<String, Object> dateMap = new HashMap<String, Object>();
        dateMap.put(getString(R.string.bbdd_date_day), message.getDate().getDay());
        dateMap.put(getString(R.string.bbdd_date_month), message.getDate().getMonth());
        dateMap.put(getString(R.string.bbdd_date_year), message.getDate().getYear());
        dateMap.put(getString(R.string.bbdd_date_hour), message.getDate().getHour());
        dateMap.put(getString(R.string.bbdd_date_minutes), message.getDate().getMinutes());

        Map<String, Object> messageMap = new HashMap<String, Object>();
        messageMap.put(getString(R.string.bbdd_addressee), dniRemitter);
        messageMap.put(getString(R.string.bbdd_date), dateMap);
        messageMap.put(getString(R.string.bbdd_dni_remitter), myDNI);
        messageMap.put(getString(R.string.bbdd_message), message.getMessage());
        messageMap.put(getString(R.string.bbdd_remitter), myName);
        messageMap.put(getString(R.string.bbdd_rol_remitter), myRol);

        String uuid = UUID.randomUUID().toString();
        messageRef.child(uuid).updateChildren(messageMap);
        //mandar a la base de datos local
        messageBBDD.addMessage(message, idConversation);
    }

    public void getCloudMessages () {
        Query getMessages = messageRef.orderByChild(getString(R.string.bbdd_addressee)).equalTo(myDNI);
        getMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                Message message = new Message(values);
                if (dniRemitter.equals(message.getDniRemitter())) {
                    messages.add(message);
                    messageBBDD.addMessage(message, idConversation);
                }//if

                Collections.sort(messages);

                //Seteamos el Adapter
                //chatAdapter = new ChatAdapter(ChatActivity.this, messages);
                chatAdapter = new ChatAdapter(ChatActivity.this, messages, myDNI);
                setListAdapter(chatAdapter);

                //Borrar el mensaje de la BBDD
                messageRef.child(dataSnapshot.getKey()).removeValue();
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

        //Seteamos el Adapter
        chatAdapter = new ChatAdapter(ChatActivity.this, messages, myDNI);
        setListAdapter(chatAdapter);
    }
}

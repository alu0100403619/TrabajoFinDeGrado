package com.example.gonzalo.schoolapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.gonzalo.schoolapp.Adapters.ChatAdapter;
import com.example.gonzalo.schoolapp.clases.Date;
import com.example.gonzalo.schoolapp.clases.Message;
import com.example.gonzalo.schoolapp.database.MessageSQLHelper;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class ChatActivity extends ListActivity {

    String chatName, mail, myName, mailRemitter, idConversation;
    ArrayList<Message> messages, tempMessages;
    MessageSQLHelper messageBBDD;
    Firebase messageRef;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Firebase.setAndroidContext(this);
        messageRef = new Firebase(getString(R.string.mensajesRef));

        messages = new ArrayList<>();
        tempMessages = new ArrayList<>();
        messageBBDD = new MessageSQLHelper(this);

        //Obtener el Nombre
        chatName = getIntent().getExtras().getString(getString(R.string.name));

        //Obtener mi e-mail
        mail = getIntent().getExtras().getString(getString(R.string.mail));
        mailRemitter = getIntent().getExtras().getString(getString(R.string.mail_remitter));
        idConversation = messageBBDD.getIdConversation(mailRemitter);

        //Obtener el Nombre
        myName = getIntent().getExtras().getString(getString(R.string.myName));

        //Cargar Mensajes Previos Almacenados en la memoria de la BBDD
        if (idConversation != null) {
            messages.addAll(messageBBDD.getAllMessagesConversation(idConversation));
            messageBBDD.deleteAllMessageConversation(idConversation);
            messageBBDD.deleteConversation(idConversation);
        }

        //Obtener Mensajes del bundle
        tempMessages = getIntent().getExtras().getParcelableArrayList(getString(R.string.bbdd_message));
        messages.addAll(tempMessages);

        //Cambiar el titulo de la ActionBar
        getActionBar().setTitle(chatName);

        //Seteamos el Adapter
        //setListAdapter(new ChatAdapter(this, messages));
        chatAdapter = new ChatAdapter(this, messages);
        setListAdapter(chatAdapter);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void send (View view) {
        EditText messageInputEditText = (EditText) findViewById(R.id.messageInput);
        String messageInput = messageInputEditText.getText().toString();
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.get(calendar.DAY_OF_MONTH),
                calendar.get(calendar.MONTH) + 1, calendar.get(calendar.YEAR),
                calendar.getTime().getHours(), calendar.getTime().getMinutes());
        Message message = new Message(mail, myName, messageInput, date);
        messages.add(message);

        //Mandar el mensaje a la BBDD mientras el name no sea System
        sendToDataBase(message);

        //Refresh the view
        messageInputEditText.setText(getString(R.string.empty));
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy () {
        Log.i("ChatActivity", "OnDestroy");
        //MessageSQLHelper messageSQLHelper = new MessageSQLHelper(this);
        messageBBDD.addConversation(mail, mailRemitter);
        String newIdConversation = messageBBDD.getIdConversation(mailRemitter);
        for (Message message: messages) {
            messageBBDD.addMessage(message, newIdConversation);
        }
        super.onDestroy();
    }

    public void sendToDataBase(Message message) {
        Map<String, Object> dateMap = new HashMap<String, Object>();
        dateMap.put(getString(R.string.bbdd_date_day), message.getDate().getDay());
        dateMap.put(getString(R.string.bbdd_date_month), message.getDate().getMonth());
        dateMap.put(getString(R.string.bbdd_date_year), message.getDate().getYear());
        dateMap.put(getString(R.string.bbdd_date_hour), message.getDate().getHour());
        dateMap.put(getString(R.string.bbdd_date_minutes), message.getDate().getMinutes());

        Map<String, Object> messageMap = new HashMap<String, Object>();
        messageMap.put(getString(R.string.bbdd_addressee), mailRemitter);
        messageMap.put(getString(R.string.bbdd_date), dateMap);
        messageMap.put(getString(R.string.bbdd_mail_remitter), mail);
        messageMap.put(getString(R.string.bbdd_message), message.getMessage());
        messageMap.put(getString(R.string.bbdd_remitter), myName);

        String uuid = UUID.randomUUID().toString();
        messageRef.child(uuid).updateChildren(messageMap);
    }
}

package com.example.gonzalo.schoolapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gonzalo.schoolapp.R;
import com.example.gonzalo.schoolapp.clases.Message;

import java.util.ArrayList;

/**
 * Created by Gonzalo on 27/05/2015.
 */
public class ChatAdapter extends BaseAdapter {

    Context context;
    ArrayList<Message> messages;
    private static LayoutInflater inflater = null;
    String myDNI;

    public ChatAdapter () {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ChatAdapter (Activity chatActivity, ArrayList<Message> messages, String myDNI) {
        context = chatActivity;
        this.messages = messages;
        this.myDNI = myDNI;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TempMessage message = new TempMessage();
        View view;

        view = inflater.inflate(R.layout.list_chat_item, null);
        LinearLayout chatItem = (LinearLayout) view.findViewById(R.id.chatItem);
        Message msg = messages.get(position);

        message.message = (TextView) view.findViewById(R.id.message_send);
        message.date = (TextView) view.findViewById(R.id.date_message);
        message.message.setText(messages.get(position).getMessage());
        message.date.setText(messages.get(position).getDate().toString());

        if (myDNI.equals(msg.getDniRemitter())) {
            chatItem.setBackgroundResource(R.drawable.bubble_b);
        }
        else {
            chatItem.setBackgroundResource(R.drawable.bubble_a);
        }

        return view;
    }

    public class TempMessage {
        TextView message;
        TextView date;
    }
}

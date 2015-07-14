package com.example.gonzalo.schoolapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    public ChatAdapter () {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ChatAdapter (Activity chatActivity, ArrayList<Message> messages) {
        context = chatActivity;
        this.messages = messages;
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
        message.message = (TextView) view.findViewById(R.id.message_send);
        message.date = (TextView) view.findViewById(R.id.date_message);
        message.message.setText(messages.get(position).getMessage());
        message.date.setText(messages.get(position).getDate().toString());

        return view;
    }

    public class TempMessage {
        TextView message;
        TextView date;
    }
}

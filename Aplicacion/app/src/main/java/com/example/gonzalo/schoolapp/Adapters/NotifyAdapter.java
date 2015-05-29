package com.example.gonzalo.schoolapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gonzalo.schoolapp.NotificationsActivity;
import com.example.gonzalo.schoolapp.R;

import java.util.ArrayList;

/**
 * Created by Gonzalo on 26/05/2015.
 */
public class NotifyAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> names;
    ArrayList<Integer> numberMessages;
    private static LayoutInflater inflater = null;

    public NotifyAdapter (NotificationsActivity notificationsActivity,
                          ArrayList<String> messageListView, ArrayList<Integer> numberMessages) {
        context = notificationsActivity;
        this.names = messageListView;
        this.numberMessages = numberMessages;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.size();
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
        User user = new User();
        View view;

        view = inflater.inflate(R.layout.list_notify_layout, null);
        user.name = (TextView) view.findViewById(R.id.name_remitter);
        user.number = (TextView) view.findViewById(R.id.number_messages);
        user.name.setText(names.get(position));
        user.number.setText(numberMessages.get(position).toString());

        //Listeners
        /*view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });//*/

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });//*/

        return view;
    }

    public class User {
        TextView name;
        TextView number;
    }
}

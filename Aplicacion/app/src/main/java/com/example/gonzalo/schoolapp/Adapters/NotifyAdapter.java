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
    ArrayList<String> roles;
    ArrayList<Integer> numberMessages;
    private static LayoutInflater inflater = null;

    public NotifyAdapter (NotificationsActivity notificationsActivity,
                          ArrayList<String> messageListView, ArrayList<Integer> numberMessages,
                          ArrayList<String> roles) {
        context = notificationsActivity;
        this.names = messageListView;
        this.numberMessages = numberMessages;
        this.roles = roles;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void remove(int position) {
        names.remove(position);
        numberMessages.remove(position);
        roles.remove(position);
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
        TextView empty = (TextView) view.findViewById(R.id.empty);
        user.name.setText(names.get(position));
        user.number.setText(numberMessages.get(position).toString());

        //Segun el rol le aplicamos el estilo
        switch (roles.get(position)) {
            case "Alumno":
                user.name.setBackgroundResource(R.color.DarkSeaGreen);
                user.number.setBackgroundResource(R.color.DarkSeaGreen);
                empty.setBackgroundResource(R.color.DarkSeaGreen);
                //view.setBackgroundColor(R.color.LightGreen);
                break;
            case "Profesor" :
                user.name.setBackgroundResource(R.color.Salmon);
                user.number.setBackgroundResource(R.color.Salmon);
                empty.setBackgroundResource(R.color.Salmon);
                //view.setBackgroundColor(R.color.Salmon);
                break;
            case "Padre":
                user.name.setBackgroundResource(R.color.SkyBlue);
                user.number.setBackgroundResource(R.color.SkyBlue);
                empty.setBackgroundResource(R.color.SkyBlue);
                //view.setBackgroundColor(R.color.SkyBlue);
                break;
        }//switch


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

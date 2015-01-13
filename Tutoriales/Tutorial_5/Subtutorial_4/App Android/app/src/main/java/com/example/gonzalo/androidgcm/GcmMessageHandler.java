package com.example.gonzalo.androidgcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Gonzalo on 12/01/2015.
 * Esta clase define qué hacer con el mensaje recibido.
 * El mensaje recibido contiene en su parte de datos
 * "como veremos más adelante en el paso servidor GCM",
 * dos atributos del título y el mensaje, extraeremos el
 * valor del "título" del intent en los extras y mostrarlo en un Toast.
 */
public class GcmMessageHandler extends IntentService {

    private Handler handler;

    public GcmMessageHandler () {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String title,msg;

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // El parámetro intent getMessageType() debe ser el intent
        // que recives en tu BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        //Obtenemos y mostramos el título
        title = extras.getString("title");
        showToast(title);
        Log.i("GCM", "Title Received : (" + messageType +
                ") "+extras.getString("title"));
        //Obtenemos y mostramos el mensaje
        msg = extras.getString("message");
        showToast(msg);
        Log.i("GCM", "Message Received : (" + messageType +
                ") "+extras.getString("message"));
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void showToast(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
                        .show();
            }//run
        }); //post
    }
}//class

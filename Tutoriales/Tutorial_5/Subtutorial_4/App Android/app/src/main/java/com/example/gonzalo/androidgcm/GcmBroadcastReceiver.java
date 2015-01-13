package com.example.gonzalo.androidgcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Gonzalo on 12/01/2015.
 *
 * Esta es la clase que recibirá el mensaje GCM y lo pasará al GcmMessageHandler.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Explícitamente especificar que GcmMessageHandler se encargará de
        // manejar el intent.
        ComponentName comp = new ComponentName(context.getPackageName()
                , GcmMessageHandler.class.getName());

        // Inicia el servicio, manteniendo el dispositivo despierto mientras que está lanzando.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}

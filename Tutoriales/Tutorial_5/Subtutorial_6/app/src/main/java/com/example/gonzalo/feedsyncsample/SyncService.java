package com.example.gonzalo.feedsyncsample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/** Created by Gonzalo on 14/01/2015.
 *
 * Define a Service that returns an IBinder for the
 * sync adapter class, allowing the sync adapter framework to call
 * onPerformSync(). */

 public class SyncService extends Service {
    private static final String TAG = "SyncService";

    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();
    // Storage for an instance of the sync adapter
    private static SyncAdapter sSyncAdapter = null;

    /** Thread-safe constructor, creates static {@link SyncAdapter} instance. */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    /** Logging-only destructor. */
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }

    /** Return Binder handle for IPC communication with {@link SyncAdapter}.
     * <p>New sync requests will be sent directly to the SyncAdapter using this channel.
     * @param intent Calling intent
     * @return Binder handle for {@link SyncAdapter} */
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}

package com.askey.hahow.atm1;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;



public class HelloService extends IntentService {
    private static final String TAG = HelloService.class.getSimpleName();
    public static final String ACTION_HELLO_DONE = "actio_hello_done";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public HelloService() {
        super("HelloService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: "+intent.getStringExtra("NAME"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent done = new Intent();
        done.setAction(ACTION_HELLO_DONE);
        sendBroadcast(done);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }
//    Conflict with onHandleIntent
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TAG, "onStartCommand: ");
//        //return super.onStartCommand(intent, flags, startId);
////        try {
////            Thread.sleep(5000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//        return START_STICKY;
//    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

}

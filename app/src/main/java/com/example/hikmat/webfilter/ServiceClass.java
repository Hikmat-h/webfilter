package com.example.hikmat.webfilter;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ServiceClass extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(getApplicationContext(), "created!!!!",
           //     Toast.LENGTH_SHORT).show();
        int a = 5;
        Toast.makeText(getApplicationContext(), "go go go!!!!"+String.valueOf(a),
                Toast.LENGTH_LONG).show();
        toasts();
        callAsynchronousTask();
        //android.os.Debug.waitForDebugger();

        //runService();
    }

    public void toasts () {
        Toast.makeText(getApplicationContext(), "go go go!!!!",
                Toast.LENGTH_LONG).show();
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            Toast.makeText(getApplicationContext(), "this is again!!!!",
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000); //execute in every 50000 ms
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        toasts();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "stoppped!!!!",
                Toast.LENGTH_SHORT).show();
        //super.onDestroy();
    }
}

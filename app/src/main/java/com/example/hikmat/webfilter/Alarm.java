package com.example.hikmat.webfilter;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Hikmat on 12/2/17.
 */

public class Alarm extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Mail_manager send=new Mail_manager();
        send.Send();
        Toast.makeText(context.getApplicationContext(), "search history report is send to email provided", Toast.LENGTH_LONG);
    }
}

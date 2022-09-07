package com.example.hikmat.webfilter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    String email;
    String key;

    String FILENAME="list.txt";
    FileOutputStream user_list;
    static String path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final User_list execute=new User_list();

        Intent service = new Intent(getApplicationContext(), ServiceClass.class);
        startService(service);

        try {
            user_list=openFileOutput(FILENAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        path=getFilesDir().getAbsolutePath();

        Calendar calendar=Calendar.getInstance();         //time for sending mail
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 3);
        Intent intent= new Intent(getApplicationContext(), Alarm.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(), 11, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Password pass=new Password();
        final Time_manager time=new Time_manager();
        Intent i =new Intent(this, Password.class);
        startActivity(i);
        pass.allowed=false;                  //make it ask password again
        //Toast.makeText(getApplicationContext(), String.valueOf(pass.allowed), Toast.LENGTH_SHORT).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("login", 0);

        Button add_word=(Button) findViewById(R.id.add);
        final EditText new_word=(EditText) findViewById(R.id.new_word);
        Button set_time=(Button) findViewById(R.id.time_limit);
        final EditText time_hour=(EditText) findViewById(R.id.time_hour);
        final EditText time_min=(EditText) findViewById(R.id.time_min);
        time_hour.setText(String.valueOf(time.time_limit/60));
        time_min.setText(String.valueOf(time.time_limit%60));

        add_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=sharedPref.getString("email", null);
                key =  myRef.push().getKey();
                myRef.child(email.substring(0, email.indexOf("."))).child("list").
                child(key).setValue(new_word.getText().toString());
                try {
                    user_list.write((new_word.getText().toString()+ " ").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //path=getFilesDir().getAbsolutePath();
                //file =new File(path+"/"+FILENAME);
                Context context=getApplicationContext();
                execute.getlist(context);
                new_word.setText("");
                //Toast.makeText(getApplicationContext(), file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "word is added to black list database", Toast.LENGTH_LONG).show();
            }
        });

        ImageButton settings=(ImageButton) findViewById(R.id.settings);
        Button enable=(Button) findViewById(R.id.enable);
        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(getApplication(), Filter_service.class));
                startService(new Intent(getApplication(), Time_manager.class));
                Toast.makeText(getApplicationContext(), "protection is enabled and running in background", Toast.LENGTH_LONG).show();
            }

        });
        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.time_limit=(Integer.parseInt(time_hour.getText().toString())%24)*60+
                        (Integer.parseInt(time_min.getText().toString()))%60;
                Toast.makeText(getApplicationContext(), String.valueOf(time.time_limit)+" minutes set for using web", Toast.LENGTH_LONG).show();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });

    }
}
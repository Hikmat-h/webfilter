package com.example.hikmat.webfilter;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button save=(Button) findViewById(R.id.save);
        final EditText new_pwd=(EditText) findViewById(R.id.new_pwd);
        final EditText email=(EditText) findViewById(R.id.mail);
        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("login", 0);
        final SharedPreferences.Editor editor=sharedPref.edit();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(new_pwd.getText().toString().length()==4) {
                    editor.putString("password", new_pwd.getText().toString());
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "password is changed", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "password must be 4 number", Toast.LENGTH_SHORT).show();
                if(!email.getText().toString().isEmpty())
                {
                    editor.putString("email", email.getText().toString());
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "email is changed", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "enter email", Toast.LENGTH_SHORT).show();

            }
        });


    }
}

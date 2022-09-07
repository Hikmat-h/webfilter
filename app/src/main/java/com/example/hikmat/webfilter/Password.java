package com.example.hikmat.webfilter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Password extends AppCompatActivity {
    public boolean allowed=false;
    String passcode="1234";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("login", 0);

        Button submit = (Button) findViewById(R.id.submit);
        final EditText pass=(EditText) findViewById(R.id.passcode);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcode=sharedPref.getString("password", "1234");

                if(pass.getText().toString().contains(passcode))
                {
                    Toast.makeText(getApplicationContext(), "correct", Toast.LENGTH_SHORT).show();
                    allowed=true;
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("allowed", true);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "incorrect passcode", Toast.LENGTH_SHORT).show();
                    pass.setText("");
                    allowed=false;
                }
                //Toast.makeText(getApplicationContext(), String.valueOf(allowed), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }
}

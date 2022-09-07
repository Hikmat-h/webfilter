package com.example.hikmat.webfilter;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hikmat on 12/3/17.
 */

public class User_list extends Filter_service{
    String file_name="list.txt";
    String word=null;
    String list;
    FileInputStream user_list;
    static List<String> black_list = new ArrayList<String>();
    public void getlist(Context context)
    {
        try {
            user_list = context.openFileInput(file_name);
        }catch (FileNotFoundException e)
        {}

        try{

            if ( user_list != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(user_list);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while ( (word = bufferedReader.readLine()) != null ) {
                    //if(black_list.contains(word)==false)
                    list=word+" ";
                }

                user_list.close();
            }
            black_list= Arrays.asList(list.split("\\s+"));
            Filter_service list=new Filter_service();
            list.user_list.addAll(black_list);

        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
}

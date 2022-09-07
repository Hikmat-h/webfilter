package com.example.hikmat.webfilter;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Hikmat on 10/12/17.
 */

public class Filter_service extends AccessibilityService {
    String list;
    List<String> sites;      //for word matching in database
    List<String> sentence;   //for word matching in google
    List<String> search_history;
    String FILENAME="history.txt";
    FileOutputStream history;

    int eventType;
    String current_time;
    String previous_word=null;
    String eventText;
    static String path;
    static String entered_text_for_focused="";       //special saving of text for view accessibility focused
    public static List<String> user_list=new ArrayList<>();
    @Override
    public void onCreate()
    {
        try {
            history=openFileOutput(FILENAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        path=getFilesDir().getAbsolutePath();
        Toast.makeText(getApplicationContext(), "service is created", Toast.LENGTH_SHORT).show();
        try {
            InputStream is = getAssets().open("list.txt");
            int size = is.available();
            byte[] data1 = new byte[size];
            is.read(data1);
            list=new String(data1);
            sites = Arrays.asList(list.split("\\s+"));   //split them
            is.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "FileNotFoundException", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
        }
        search_history= new ArrayList<String>();
    }
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //Toast.makeText(getApplicationContext(), user_list.toString(), Toast.LENGTH_SHORT).show();
        eventType=event.getEventType();
        AccessibilityNodeInfo source = event.getSource();
        try {
            eventText=String.valueOf(source.getText());
        }
        catch (Exception e)
        {
        }
        sentence = Arrays.asList(eventText.split("\\s+"));   //get all words entered to the array list
        Bundle arguments = new Bundle();
        if(sentence.isEmpty()==false) {
            for (String s : sentence) {
                if (sites.contains(s.toLowerCase())) {
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, null);
                    source.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_TEXT.getId(), arguments);
                    Toast.makeText(getApplicationContext(), "Bad Content! Typed word: " + eventText + " occurs in " +
                            sites.indexOf(s.toLowerCase()) + "s index in the list", Toast.LENGTH_LONG).show();
                    performGlobalAction(GLOBAL_ACTION_BACK);
                    performGlobalAction(GLOBAL_ACTION_BACK);
                }
                if(user_list.contains(s.toLowerCase()))
                {
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "");
                    source.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_TEXT.getId(), arguments);
                    Toast.makeText(getApplicationContext(), "Bad content! Typed word: " + eventText + " occurs in " +
                            user_list.indexOf(s.toLowerCase()) + "s index in the black list", Toast.LENGTH_LONG).show();
                    performGlobalAction(GLOBAL_ACTION_BACK);
                    performGlobalAction(GLOBAL_ACTION_BACK);
                }
            }
        }
        if(eventType==AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED)
        {
            entered_text_for_focused=eventText;
        }
        if(eventType==AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED && event.getClassName().
                equals("android.webkit.WebView"))
        {
                if(eventText.equals(previous_word)==false && eventText.isEmpty()==false) {
                    current_time = DateFormat.getDateTimeInstance().format(new Date());
                    search_history.add(eventText + " -time: " + current_time);
                    try {
                        history.write((eventText+" -time: "+ current_time + "\n").getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    previous_word=eventText;
                }
                else
                if(eventText.equals("YouTube"))
                {
                    current_time = DateFormat.getDateTimeInstance().format(new Date());
                    search_history.add(entered_text_for_focused + " -"+eventText+" -time: " + current_time);
                    try {
                        history.write((entered_text_for_focused+" -"+eventText+" -time: "+ current_time + "\n").getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            Toast.makeText(getApplicationContext(), entered_text_for_focused, Toast.LENGTH_SHORT).show();
        }
        try {
            source.recycle();
        }
        catch (Exception e)
        {
        }
    }
    @Override
    public void onInterrupt() {
        Toast.makeText(getApplicationContext(), "interrupt occured", Toast.LENGTH_SHORT).show();
    }
}
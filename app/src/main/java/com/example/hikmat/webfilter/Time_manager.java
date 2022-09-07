package com.example.hikmat.webfilter;

import android.accessibilityservice.AccessibilityService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Hikmat on 10/30/17.
 */

public class Time_manager extends AccessibilityService{
    static int total_time;
    static int mozilla_time;
    static int chrome_time;
    public static int time_limit=30;
    public final static int REQUEST_CODE = 22;
    @Override
    public void onCreate()
    {

    }
    public void onAccessibilityEvent(AccessibilityEvent event) {
        getForegroundProcess();
    }
    public void getForegroundProcess() {

        String PackageName = "No package";

        long TimeInforground = 500;

        int minutes, seconds, hours;
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);

        long time = System.currentTimeMillis();
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 1511147523, time);

        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }

            try {
                PackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            } catch (Exception e)
            {
            }
            if(PackageName.contains("org.mozilla.firefox") || PackageName.contains("com.android.chrome")) {
                try {
                    TimeInforground = mySortedMap.get(mySortedMap.lastKey()).getTotalTimeInForeground();
                } catch (Exception e) {
                }
                minutes = (int) ((TimeInforground / (1000 * 60)) % 60);
                seconds = (int) (TimeInforground / 1000) % 60;
                hours = (int) ((TimeInforground / (1000 * 60 * 60)));
                Toast.makeText(getApplicationContext(), "Time is: " + hours + "h" + ":" + minutes + "m" +
                        seconds + "s", Toast.LENGTH_SHORT).show();
                if(PackageName.contains("org.mozilla.firefox"))
                    mozilla_time=hours*60+minutes+seconds/60;
                else
                    chrome_time=hours*60+minutes+seconds/60;
                total_time=mozilla_time+chrome_time;
                if(total_time>=time_limit)
                {
                    Toast.makeText(getApplicationContext(), "Time is up! Total used time: "+total_time+">="+time_limit, Toast.LENGTH_LONG).show();
                    performGlobalAction(GLOBAL_ACTION_HOME);
                }
                //Toast.makeText(getApplicationContext(), String.valueOf(pass.allowed), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onInterrupt() {
        Toast.makeText(getApplicationContext(), "interrupt occured", Toast.LENGTH_SHORT).show();
    }

}
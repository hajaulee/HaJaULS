package com.hajau.lockscreen;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by HaJaU on 13-12-17.
 */

public class DetectOtherApp extends Service {

    boolean googleCalled = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //Toast.makeText(this,"Service Started", Toast.LENGTH_LONG).show();
        Log.d("Service", "started hihi");
        final String str = "";

        Timer timer  =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int phonelaunched = 0,phoneclosed =0;
            int phonelaunches = 1;
            @Override
            public void run() {
                Log.d("Run", "Running");
                StringBuilder w = new StringBuilder("/Ngu:");


                final UsageStatsManager usageStatsManager=(UsageStatsManager)
                        DetectOtherApp.this.getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);

                final long currentTime = System.currentTimeMillis(); // Get current time in milliseconds

                final Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -2); // Set year to beginning of desired period.
                //final long beginTime = cal.getTimeInMillis(); // Get begin time in milliseconds
                final long beginTime = currentTime - 3000;
                final List<UsageStats> queryUsageStats=usageStatsManager
                        .queryUsageStats(UsageStatsManager.INTERVAL_YEARLY,
                        beginTime, currentTime);
                for (UsageStats a : queryUsageStats){
                    if(a.getLastTimeStamp() > beginTime)
                        w.append(new Date(a.getLastTimeStamp()).toString() + ":" + a.getPackageName() + "/");
                }
                if (!googleCalled && w.indexOf("com.google.android.tts") != -1){
                    Main_Activity main_activity = Main_Activity.instance;
                    if (main_activity != null){
                       main_activity.runOnUiThread(main_activity.runnable);
                    }
                    googleCalled = true;
                }else {
                    googleCalled = false;
                }
                Log.d("ahihi","Show" + w.toString());
            }
        },0,3000);

        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

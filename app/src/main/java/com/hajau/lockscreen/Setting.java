package com.hajau.lockscreen;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Thiết lập");
        setSupportActionBar(toolbar);

        Switch activeLock = (Switch) findViewById(R.id.activeLock);
        Main_Activity.wantToStopLock = Integer.parseInt(load(this));
        if(Main_Activity.wantToStopLock >= 4)
            activeLock.setChecked(false);
        activeLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((Switch)v).isChecked()){
                    Main_Activity.wantToStopLock = 0;
                }else{
                    Main_Activity.wantToStopLock = 4;
                }
                saveFile(Setting.this, "" + Main_Activity.wantToStopLock);
            }
        });
        //Switch hideIcon = (Switch) findViewById(R.id.hideIcon);
        TextView deviceAdmin = (TextView) findViewById(R.id.deviceAdmin);
        TextView readLaunchedLog = (TextView)findViewById(R.id.readLaunchedLog);
        final TextView timeText = (TextView) findViewById(R.id.time);
        timeText.setText(secondToText(Main_Activity.interval/1000));
        deviceAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName mDeviceAdmin = new ComponentName(Setting.this, DemoDeviceAdminReceiver.class);
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "EXPLANATION");
                startActivityForResult(intent, 0);

            }
        });
        readLaunchedLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }
        });
        SeekBar timeToReactive = (SeekBar) findViewById(R.id.timeToReactive);
        timeToReactive.setProgress(Main_Activity.interval/5000);
        timeToReactive.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int second = progress * 5;
                    if(second > 20) {
                        timeText.setText(secondToText(second));
                        Main_Activity.interval = second * 1000;
                    }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent i = new Intent(Setting.this, Main_Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Setting.this.startActivity(i);
                Setting.this.finish();
            }
        });

        //startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
//        this.startService(new Intent(this, DetectOtherApp.class));
       // analyzeLogcat();
    }
    public String secondToText(int second){
        String timeS = "";
        if(second > 60)
            timeS += (second/60) + " phút ";
        timeS += (second%60) + " giây";
        return timeS;
    }

    public boolean saveFile(Context context, String mytext){
        Log.i("Log", "SAVE");
        try {
            FileOutputStream fos = context.openFileOutput("active",Context.MODE_PRIVATE);
            Writer out = new OutputStreamWriter(fos);
            out.write(mytext);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String load(Context context){
        Log.i("Log", "FILE");
        try {
            FileInputStream fis = context.openFileInput("active");
            BufferedReader r = new BufferedReader(new InputStreamReader(fis));
            String line= r.readLine();
            r.close();
            return line;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Log", "FILE - false");
            return "4"; // Default value to set not active
        }
    }

    public void analyzeLogcat(){
        try
        {
            Process mLogcatProc = null;
            BufferedReader reader = null;
            mLogcatProc = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});

            reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));

            String line;
            final StringBuilder log = new StringBuilder();
            String separator = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null)
            {
                log.append(line);
                log.append(separator);
            }
            String w = log.toString();
            Toast.makeText(getApplicationContext(),w, Toast.LENGTH_LONG).show();
            Log.d("ngu", w);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

package com.diraj.battery;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class IndividualProcess extends ActionBarActivity  {

    public String packageName;
    public int userPid;
    public  String processName;
    public Drawable icon;
    public String version;
    public String wakelockType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_process);
       WLData Process = getIntent().getParcelableExtra("ProcessClicked");

        packageName = Process.getPackage();
        userPid = Process.getPID();
        processName = Process.getProcess();
        icon = getIcon(userPid);
        wakelockType = getWakeLockType(Process.getPID());

        ImageView iconHolder = (ImageView)findViewById(R.id.Icon);
        TextView processHolder = (TextView)findViewById(R.id.Process);
        TextView versionHolder = (TextView) findViewById(R.id.Version);
        TextView typeHolder = (TextView) findViewById(R.id.Type);

        if(icon != null)
            iconHolder.setImageDrawable(icon);

        processHolder.setText(processName);
        versionHolder.setText("Version: "+ version);
        typeHolder.setText(wakelockType);
        Toast.makeText(getApplicationContext(), timeinfo(Process.getPackage()), Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_individual_process, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public String timeinfo(String Package)
    {
        String Time = null;
        int runtime;
        long millis = SystemClock.uptimeMillis();
        int time=(int)millis/60;
        try {
            String t="dumpsys activity ";
            t=t+Package;
            String cmd[]=new String[]{"su","-c",t};
            //adb command to read the dumpsys power
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
            int count = 0;//to skip the first line of bufferreader
            String str; //to store bufferreader nextline
            while ((str = bufferedReader.readLine()) != null)
            {
                String [] deli1 = str.split(" ");
                String  a="ms";
                //split with the '_'
                for(int i=0;i<deli1.length;i++)
                {
                    if (a.equalsIgnoreCase(deli1[i]))
                    {
                        a=deli1[7].substring(1);
                        Time = a;
                        runtime = (int) Integer.parseInt(a);//process run time
                        int hr=runtime/216000;
                        int min=runtime%216000;
                        min=min/3600;
                        int sec=runtime%3600;
                        sec=sec/60;
                        int ns=runtime%60;
                        Time = String.valueOf(hr)+" hr "+String.valueOf(min)+" min "+String.valueOf(sec)+" sec "+String.valueOf(ns)+" ns ";
                        System.out.println("Time "+Time);
                    }
                }

            }

        }
        catch(Exception e)
        {
            Context context = getApplicationContext();
            CharSequence text = "Exception found";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        return Time;
    }
    private String getWakeLockType(int PID)
    {
        int pid;
        String wlType = null;
        try
        {
            //adb command to read the dumpsys power
            Process process = Runtime.getRuntime().exec(new String[]{ "su","-c","dumpsys power"});
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            int count=0;//to skip the first line of bufferreader
            String s; //to store bufferreader nextline

            while ((s =bufferedReader.readLine())!= null)
            {
                if (isPresent(s))
                {
                    if(count==0)
                        count++;
                    else
                    {
                        String intValue = s.replaceAll("[^0-9]", " "); //replace all alpa to space
                        intValue = intValue.replaceAll("\\s+", " ");//replace extra space

                        //split the string into words
                        String[] values = new String[4];
                        values = intValue.split(" ");

                        //uid and pid
                        pid=Integer.parseInt(values[2]);

                        if(pid==PID)
                        {
                            s = s.replaceAll("\\s+", " ");
                            String [] temp = s.split(" ");
                            wlType = temp[1];
                            System.out.println(temp[1]);

                        }

                    }

                }
            }

        }
        catch (Exception e)
        {
            Context context = getApplicationContext();
            CharSequence text = "Exception found";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        return wlType;
    }
    public Drawable getIcon(int PID)
    {
        Drawable Icon = null;
        ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        PackageManager pm = this.getPackageManager();

        List l = am.getRunningAppProcesses();//list of all rnning processes
        Iterator i = l.iterator();

        while(i.hasNext())
        {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
            try
            {
                if(info.pid == PID)
                {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    //Log.d("Process", "Id: "+ info.pid +" ProcessName: "+ info.processName +"  Label: "+c.toString());
                    Icon = pm.getApplicationIcon(info.processName);
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    version = pInfo.versionName;
                    //packageName = getApplicationContext().getPackageName();
                    //  appLabel = (String) pm.getApplicationLabel(info);
                }
            }
            catch(Exception e)
            {
                //Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return Icon;
    }
    private boolean isPresent(String s)
    {
        String [] deli1 = s.split("_");
        String [] deli2 = s.split(" ");
        String  a="WAKE";

        //split with the '_'
        for(int i=0;i<deli1.length;i++)
            if(a.equalsIgnoreCase(deli1[i]))
                return true;
        //split with ' '
        for(int i=0;i<deli2.length;i++)
            if(a.equalsIgnoreCase(deli2[i]))
                return true;

        return false;//does not contain wakelock
    }
}

package com.diraj.battery;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_process);
       WLData Process = getIntent().getParcelableExtra("ProcessClicked");

        packageName = Process.getPackage();
        userPid = Process.getPID();
        processName = Process.getProcess();
        icon = getIcon(userPid);

        ImageView iconHolder = (ImageView)findViewById(R.id.Icon);
        TextView processHolder = (TextView)findViewById(R.id.Process);
        TextView versionHolder = (TextView) findViewById(R.id.Version);

        if(icon != null)
            iconHolder.setImageDrawable(icon);

        processHolder.setText(processName);
        versionHolder.setText(version);
       //Toast.makeText(getApplicationContext(), Process.getProcess(), Toast.LENGTH_SHORT).show();
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
}

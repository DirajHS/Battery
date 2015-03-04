package com.diraj.battery;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MainActivity extends ActionBarActivity
{

    private static RecyclerView.Adapter adapter;
    private static RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<WLData> wakelocks;

    //Wakelock Details will be stored in below arrays which are changed dynamically in size
    public String[] packageName = new String[0];
    public int[] userPid=new int[0];
    public  String[] processName = new String[0];//list of user process package name
    public Drawable[] imageId =new Drawable[0];

    public void clicked(View view, int position)
    {
        Intent intent = new Intent(this, IndividualProcess.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.wakelock_recycler);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position)
            {
                clicked(view, position);
            }
        }));
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        wakelocks = new ArrayList<WLData>();

        getWakelock(); //Get and store wakelock data;

        int counterSize = userPid.length;

        for (int counter = 0; counter < counterSize; counter++) {
            wakelocks.add(new WLData(processName[counter], userPid[counter], packageName[counter], imageId[counter]));
        }

        adapter = new MyAdapter(wakelocks);
        recyclerView.setAdapter(adapter);
    }

    private void getWakelock()
    {
        int pid,uid;

        try
        {
            //adb command to read the dumpsys power
            Process process = Runtime.getRuntime().exec(new String[]{ "su","-c","dumpsys power"});
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

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
                        uid=Integer.parseInt(values[1]);

                        //system level application
                        if(uid<=10000);

                            //user level application
                        else if(pid!=0)
                            getAppInfo(pid);
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
    }

    //to check wakelock in dumpsys data
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

    //get the app name using pid
    private void getAppInfo(int pID)
    {
        ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        PackageManager pm = this.getPackageManager();

        List l = am.getRunningAppProcesses();//list of all rnning processes
        Iterator i = l.iterator();

        while(i.hasNext())
        {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
            try
            {
                if(info.pid == pID)
                {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    //Log.d("Process", "Id: "+ info.pid +" ProcessName: "+ info.processName +"  Label: "+c.toString());

                    userPid=intdynamic(userPid);
                    userPid[userPid.length-1] = pID;

                    processName=stringdynamic(processName);
                    processName[processName.length-1]=c.toString();

                    packageName=stringdynamic(packageName);
                    packageName[packageName.length-1]=info.processName;

                    Drawable[] newArray = new Drawable[imageId.length + 1];
                    for (int j = 0; j < imageId.length; j++)
                    {
                        newArray[j] = imageId[j];
                    }
                    imageId = newArray;
                    imageId[imageId.length - 1] = pm.getApplicationIcon(info.processName);
                    //packageName = getApplicationContext().getPackageName();
                    //  appLabel = (String) pm.getApplicationLabel(info);
                }
            }
            catch(Exception e)
            {
                //Log.d("Process", "Error>> :"+ e.toString());
            }
        }
    }

    private String[] stringdynamic(String[] array)
    {
        String[] newArray = new String[array.length+1];

        // we assume that the old array is full

        for (int j=0; j<array.length; j++)
        {
            newArray[j] = array[j];
        }
        return newArray;
    }

    private int[] intdynamic(int[] array)
    {
        int[] newArray = new int[array.length+1];

        // we assume that the old array is full

        for (int j=0; j<array.length; j++)
        {
            newArray[j] = array[j];
        }
        return newArray;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildPosition(childView));
            return true;
        }
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }
}

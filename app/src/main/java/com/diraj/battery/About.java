package com.diraj.battery;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by diraj on 3/23/15.
 */
public class About extends Activity
{
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.about);

        TextView textView = (TextView) findViewById(R.id.aboutText);
        textView.setText("Battery Optimizer is an application which detects those applications which are holding 'Wakelocks' but are not performing any " +
                "useful tasks. Such applications can drain the battery drastically. However, in this application, only user level process can be killed" +
                ", because killing system level applications can affect the Android Operating System.");


    }
}

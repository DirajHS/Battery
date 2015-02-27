package com.diraj.battery;

import android.graphics.drawable.Drawable;

/**
 * Created by diraj on 2/26/15.
 */
public class WLData
{
        String Process;
        int PID;
        String Package;
        Drawable Icon;

    public WLData(String Process, int PID, String Package, Drawable Icon)
    {
        this.Process = Process;
        this.PID = PID;
        this.Package = Package;
        this.Icon = Icon;
    }

    public String getPackage()
    {
        return this.Package;
    }
    public Drawable getIcon()
    {
        return this.Icon;
    }
    public String getProcess() { return this.Process; }

}

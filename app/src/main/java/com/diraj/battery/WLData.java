package com.diraj.battery;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by diraj on 2/26/15.
 */
public class WLData implements Parcelable {
    public String Process;
    public int PID;
    public String Package;
    public Drawable Icon;

    public WLData()
    {
        Process = "NO WAKEOCKS";
    }

    public WLData(String Process, int PID, String Package, Drawable Icon) {
        this.Process = Process;
        this.PID = PID;
        this.Package = Package;
        this.Icon = Icon;
    }

    public WLData(Parcel source)
    {
        ClassLoader cl = getClass().getClassLoader();
        Process = source.readString();
        PID = source.readInt();
        Package = source.readString();
        Icon = (Drawable)source.readValue(cl);
    }
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(Process);
        dest.writeInt(PID);
        dest.writeString(Package);
        //dest.writeValue(Icon);
    }

    public static final Creator<WLData> CREATOR = new Creator<WLData>() {
        @Override
        public WLData createFromParcel(Parcel source) {

            return new WLData(source);
        }

        @Override
        public WLData[] newArray(int size) {
            return new WLData[0];
        }
    };


    public String getPackage()
    {
        return this.Package;
    }
    public Drawable getIcon()
    {
        return this.Icon;
    }
    public String getProcess() { return this.Process; }
    public int getPID() { return this.PID; }

    public void setProcess(String Process) { this.Process = Process; }
    public void setPID(int PID) { this.PID = PID; }
    public void setPackage(String Package){ this.Package = Package; }
    public void setIcon(Drawable Icon) { this.Icon = Icon; }

}

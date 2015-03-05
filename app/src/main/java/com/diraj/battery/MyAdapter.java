package com.diraj.battery;

/**
 * Created by diraj on 2/26/15.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    private ArrayList<WLData> wakelockData;

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView processName;
        ImageView icon;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            this.processName = (TextView)itemView.findViewById(R.id.Process);
            this.icon = (ImageView)itemView.findViewById(R.id.icon);
        }
    }

    public MyAdapter(ArrayList<WLData> wakelock)
    {
        this.wakelockData = wakelock;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {
        TextView Package = holder.processName;
        ImageView icon = holder.icon;

        Package.setText(wakelockData.get(listPosition).getProcess());
        icon.setImageDrawable(wakelockData.get(listPosition).getIcon());
    }

    @Override
    public int getItemCount()
    {
        return wakelockData.size();
    }
}

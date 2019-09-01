package com.sgc.clock.ui.alarmClock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sgc.clock.R;
import com.sgc.clock.db.AlarmClockDataBaseHelper;
import com.sgc.clock.model.AlarmClock;

import java.util.ArrayList;

public class AlarmClockListAdapter extends RecyclerView.Adapter<AlarmClockListAdapter.TimeHolder> {

    ArrayList<AlarmClock> alarmClocks;
    LayoutInflater inflater;

    AlarmClockClickListener alarmClockClickListener;

    public interface AlarmClockClickListener{
        void alarmClockItemClick(int alarmClockId);
    }


    public AlarmClockListAdapter(Context context, ArrayList<AlarmClock> alarmClocks , AlarmClockClickListener alarmClockClickListener) {
        this.alarmClocks = alarmClocks;
        this.inflater = LayoutInflater.from(context);
        this.alarmClockClickListener =alarmClockClickListener;
    }

    @Override
    public TimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.alarm_item, parent, false);
        return new TimeHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final TimeHolder holder, final int position) {
        AlarmClock alarmClockItem = alarmClocks.get(position);

        holder.timeAlarm.setText(alarmClockItem.getAlarmClockTime());
        holder.nameAlarm.setText(alarmClockItem.getAlarmClockName());
        holder.dayOfWeek.setText(alarmClockItem.getAlarmClockDaysOfWeek());
        holder.activeAlarmClock.setChecked(alarmClockItem.getActive());

        holder.activeAlarmClock.setOnCheckedChangeListener((compoundButton, b) -> {
            alarmClockItem.setActive(b);
            AlarmClockDataBaseHelper.getInstance(inflater.getContext()).updateAlarmClockToDataBase(alarmClockItem);
        });

        holder.itemView.setOnClickListener(view->alarmClockClickListener.alarmClockItemClick(alarmClockItem.get_id()));
    }

    @Override
    public int getItemCount() {
        return alarmClocks.size();
    }

    class TimeHolder extends RecyclerView.ViewHolder {

        TextView timeAlarm;
        TextView nameAlarm;
        TextView dayOfWeek;
        SwitchCompat activeAlarmClock;

        public TimeHolder(@NonNull View itemView) {
            super(itemView);
            timeAlarm = itemView.findViewById(R.id.timeAlarm);
            nameAlarm = itemView.findViewById(R.id.nameAlarm);
            dayOfWeek = itemView.findViewById(R.id.dayOfWeek);
            activeAlarmClock = itemView.findViewById(R.id.activeAlarmClock);
        }


    }
}
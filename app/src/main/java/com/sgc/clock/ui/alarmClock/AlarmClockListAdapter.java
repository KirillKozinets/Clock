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

    public AlarmClockListAdapter(Context context, ArrayList<AlarmClock> alarmClocks) {
        this.alarmClocks = alarmClocks;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public TimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.alarm_item, parent, false);
        return new TimeHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final TimeHolder holder, final int position) {
        holder.timeAlarm.setText(alarmClocks.get(position).getAlarmClockTime());
        holder.nameAlarm.setText(alarmClocks.get(position).getAlarmClockName());
        holder.dayOfWeek.setText(alarmClocks.get(position).getAlarmClockDaysOfWeek());
        holder.activeAlarmClock.setChecked(alarmClocks.get(position).getActive());

        holder.activeAlarmClock.setOnCheckedChangeListener((compoundButton, b) -> {
            AlarmClock updateAlarmClock = alarmClocks.get(position);
            updateAlarmClock.setActive(b);
            AlarmClockDataBaseHelper.getInstance(inflater.getContext()).updateAlarmClockToDataBase(updateAlarmClock);
        });
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
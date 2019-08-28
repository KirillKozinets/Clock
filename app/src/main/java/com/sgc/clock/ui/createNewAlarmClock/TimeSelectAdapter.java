package com.sgc.clock.ui.createNewAlarmClock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sgc.clock.R;

public class TimeSelectAdapter extends RecyclerView.Adapter<TimeSelectAdapter.TimeHolder> {

    int max;
    int step;
    private LayoutInflater inflater;

    public TimeSelectAdapter(Context context, int max, int step) {
        this.max = max;
        this.step = step;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public TimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.time_select_item, parent, false);
        return new TimeHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final TimeHolder holder, final int position) {
        int positionInList = position % max;
        holder.textView.setText(String.valueOf(positionInList * step));
        holder.textView.setTextColor(Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class TimeHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public TimeHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textTime);

        }


    }
}
package com.sgc.clock.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sgc.clock.R;
import com.sgc.clock.adapter.TimeSelectAdapter;

public class RecyclerViewUtil {

    public static void installationRecyclerView(RecyclerView recyclerView, LinearSnapHelper snapHelper,
                                          RecyclerView.LayoutManager layoutManager, TimeSelectAdapter adapter,
                                          RecyclerView.OnScrollListener scrollListener, int position) {

        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.scrollToPosition(position);
        recyclerView.post(() -> toTargetPosition(layoutManager, snapHelper, recyclerView, position));
    }

    private static void toTargetPosition(RecyclerView.LayoutManager LayoutManager, LinearSnapHelper snap, RecyclerView recyclerView, int position) {
        View view = LayoutManager.findViewByPosition(position);
        if (view != null) {
            int[] snapDistance = snap.calculateDistanceToFinalSnap(LayoutManager, view);
            if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                recyclerView.scrollBy(snapDistance[0], snapDistance[1]);
            }
        }
    }

    @SuppressLint("ResourceType")
    public static TextView setSelectColor(TextView lastText, LinearSnapHelper snapHelper, RecyclerView.LayoutManager manager, Context context) {
        TextView centerView = (TextView) snapHelper.findSnapView(manager);
        if (lastText != null)
            lastText.setTextColor(Color.BLACK);
        int blueColor = Color.parseColor(context.getResources().getString(R.color.blue));
        (centerView).setTextColor(blueColor);
        (centerView).setTextColor(Color.parseColor("#2853e0"));
        return centerView;
    }
}

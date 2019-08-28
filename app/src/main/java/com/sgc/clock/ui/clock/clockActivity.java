package com.sgc.clock.ui.clock;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.sgc.clock.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class clockActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private viewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        ButterKnife.bind(this);

        pagerAdapter = new viewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

    }
}

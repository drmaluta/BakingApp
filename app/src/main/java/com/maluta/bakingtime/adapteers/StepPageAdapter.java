package com.maluta.bakingtime.adapteers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.maluta.bakingtime.R;
import com.maluta.bakingtime.StepFragment;
import com.maluta.bakingtime.model.Step;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by admin on 7/13/2018.
 */

public class StepPageAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Step> steps;
    private final String tabTitle;
    private Context mContext;

    public StepPageAdapter(FragmentManager fm, ArrayList<Step> steps, Context context) {
        super(fm);
        setSteps(steps);
        tabTitle = context.getResources().getString(R.string.step_page_label);
        mContext = context;
    }

    public void setSteps(@NonNull ArrayList<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return StepFragment.newInstance(
                steps.get(position).getDescription(),
                steps.get(position).getVideoURL(),
                steps.get(position).getThumbnailURL()
        );
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getResources().getString(R.string.first_step_page_label);
        }
        return String.format(Locale.US, tabTitle, position);
    }
}

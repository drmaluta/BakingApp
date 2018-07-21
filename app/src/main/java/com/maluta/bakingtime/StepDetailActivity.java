package com.maluta.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.maluta.bakingtime.adapteers.StepPageAdapter;
import com.maluta.bakingtime.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {

    @BindView(R.id.stepToolbar)
    Toolbar mToolBar;
    @BindView(R.id.recipe_step_viewpager)
    ViewPager recipeStepViewPager;
    @BindView(R.id.recipe_step_tablayout)
    TabLayout recipeStepTabLayout;

    private StepPageAdapter viewPagerAdapter;
    int stepId;

    private static final String STEPS_LIST = "recipeList";
    private static final String POSITION = "position";
    private static final String RECIPE_NAME = "recipe_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String recipeName = intent.getStringExtra(RECIPE_NAME);
        getSupportActionBar().setTitle(recipeName);
        if (savedInstanceState == null) {
            stepId = intent.getIntExtra(POSITION, -1);
        } else {
            stepId = savedInstanceState.getInt(POSITION);
        }
        Bundle bundle = intent.getExtras();
        ArrayList<Step> steps = bundle.getParcelableArrayList(STEPS_LIST);


        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPagerAdapter = new StepPageAdapter(fragmentManager, steps, this);
        recipeStepViewPager.setAdapter(viewPagerAdapter);
        setUpViewPagerListener();
        recipeStepTabLayout.setupWithViewPager(recipeStepViewPager);
        recipeStepTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        recipeStepTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        recipeStepViewPager.setCurrentItem(stepId);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, stepId);

    }

    private void setUpViewPagerListener() {
        recipeStepViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                stepId = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

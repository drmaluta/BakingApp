package com.maluta.bakingtime;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.maluta.bakingtime.model.Recipe;
import com.maluta.bakingtime.model.Step;
import com.maluta.bakingtime.utils.JsonUtils;
import com.maluta.bakingtime.widget.WidgetUpdateService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ALL")
public class DetailActivity extends AppCompatActivity implements RecipeDetailFragment.StepListener {
    @BindView(R.id.fragmentToolbar)
    Toolbar mToolBar;
    @BindView(R.id.heart_imageView)
    ImageView mFavorite;

    private Recipe mRecipe;
    private Step mStep;
    public static boolean mTwoPane;
    private boolean isLandscape;

    private static final String RECIPE = "recipe";
    private static final String STEP = "step";
    private static final String STEPS_LIST = "recipeList";
    private static final String POSITION = "position";
    private static final String RECIPE_NAME = "recipe_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.getParcelableExtra(RECIPE) != null) {
            mRecipe = intent.getParcelableExtra(RECIPE);
        } else {
            mRecipe = JsonUtils.recipeFromJson(this);
        }

        mTwoPane = false;
        getSupportActionBar().setTitle(mRecipe.getName());

        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WidgetUpdateService.startActionUpdateListView(getApplicationContext(), mRecipe);
            }
        });

        mTwoPane = findViewById(R.id.step_info_fragment_container) != null;

        FragmentManager fragmentManager = getSupportFragmentManager();




        if (savedInstanceState == null) {
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setRecipeData(mRecipe);
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentRecipe, recipeDetailFragment)
                    .commit();

            if (isLandscape && !mTwoPane) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentRecipe, recipeDetailFragment)
                        .commit();
            }

            if (mTwoPane) {
                mStep = mRecipe.getSteps().get(0);
                StepFragment stepFragment = StepFragment.newInstance(mStep.getDescription(), mStep.getVideoURL(), mStep.getThumbnailURL());
                fragmentManager.beginTransaction()
                        .add(R.id.step_info_fragment, stepFragment)
                        .commit();
            }
        } else {
            mRecipe = savedInstanceState.getParcelable(RECIPE);
            mStep = savedInstanceState.getParcelable(STEP);
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setRecipeData(mRecipe);
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentRecipe, recipeDetailFragment)
                    .commit();

            if (isLandscape && !mTwoPane) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentRecipe, recipeDetailFragment)
                        .commit();
            }

            if (mTwoPane) {
                StepFragment stepFragment = StepFragment.newInstance(mStep.getDescription(), mStep.getVideoURL(), mStep.getThumbnailURL());
                fragmentManager.beginTransaction()
                        .replace(R.id.step_info_fragment, stepFragment)
                        .commit();
            }
        }


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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE, mRecipe);
        outState.putParcelable(STEP, mStep);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStepClicked(ArrayList<Step> steps, int position) {
        if (mTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            mStep = steps.get(position);
            StepFragment stepFragment = StepFragment.newInstance(mStep.getDescription(), mStep.getVideoURL(), mStep.getThumbnailURL());
            fragmentManager.beginTransaction()
                    .replace(R.id.step_info_fragment, stepFragment)
                    .commit();
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(STEPS_LIST, steps);
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(RECIPE_NAME, mRecipe.getName());
            intent.putExtra(POSITION, position);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // checking the orientation of screen
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("ORIENTATION", " LANDSCAPE");
            // sets the boolean tracker used to adjust layout on orientation
            isLandscape = true;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("ORIENTATION", " PORTRAIT");
            // sets the boolean tracker used to adjust layout on orientation
            isLandscape = false;
        }
    }
}

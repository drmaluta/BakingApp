package com.maluta.bakingtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.maluta.bakingtime.adapteers.RecipeAdapter;
import com.maluta.bakingtime.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeFragment.RecipeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static boolean mTwoPane;

    @BindView(R.id.mainToolBar)
    Toolbar mToolBar;

    private static final String RECIPE_LIST = "recipeList";
    private static final String RECIPE= "recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTwoPane = findViewById(R.id.two_pane_layout) != null;
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, new RecipeFragment())
                    .commit();
        }
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE, recipe);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

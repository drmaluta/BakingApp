package com.maluta.bakingtime;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.maluta.bakingtime.adapteers.RecipeAdapter;
import com.maluta.bakingtime.model.Recipe;
import com.maluta.bakingtime.utils.ApiUtils;
import com.maluta.bakingtime.utils.RecipeClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 7/8/2018.
 */

@SuppressWarnings("ALL")
public class RecipeFragment extends Fragment implements RecipeAdapter.ItemClickListener {
    @BindView(R.id.recipes_rv)
    RecyclerView mRecipesRv;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> mRecipes = new ArrayList<>();;
    private static final String RECIPE_LIST = "recipeList";
    private RecipeListener recipeClickListener;



    public interface RecipeListener {
        void onRecipeClicked(Recipe recipe);
    }

    public RecipeFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            recipeClickListener = (RecipeListener) context;
        } catch (ClassCastException ec) {
            throw new ClassCastException(context.toString()
                    + " must implement RecipeListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        mRecipeAdapter = new RecipeAdapter(this);
        LinearLayoutManager mLinearLayoutManager;
        GridLayoutManager mGridLayoutManager;

        if (savedInstanceState != null) {
            ButterKnife.bind(this, view);
            mProgressBar.setVisibility(View.INVISIBLE);

            // retrieves recipe list from last run, so we don't need to call API again
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
            if (!MainActivity.mTwoPane || (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)) {
                mLinearLayoutManager = new LinearLayoutManager(view.getContext());
                mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecipesRv.setLayoutManager(mLinearLayoutManager);
            } else {
                mGridLayoutManager = new GridLayoutManager(view.getContext(), 2);
                mRecipesRv.setLayoutManager(mGridLayoutManager);
            }
            mRecipeAdapter.setData(mRecipes);
            mRecipesRv.setAdapter(mRecipeAdapter);
        } else {
            // sets up views to inflate
            ButterKnife.bind(this, view);
            mRecipes = new ArrayList<>();
            mProgressBar.setVisibility(View.VISIBLE);
            if (!MainActivity.mTwoPane || (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)) {
                mLinearLayoutManager = new LinearLayoutManager(view.getContext());
                mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecipesRv.setLayoutManager(mLinearLayoutManager);
            } else {
                mGridLayoutManager = new GridLayoutManager(view.getContext(), 2);
                mRecipesRv.setLayoutManager(mGridLayoutManager);
            }
            loadRecipes();
        }

        return view;
    }

    private void loadRecipes() {
        mProgressBar.setVisibility(View.VISIBLE);

        RecipeClient client = ApiUtils.getRecipeClient();
        Call<ArrayList<Recipe>> call = client.getRecipes();

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecipes = response.body();
                mRecipeAdapter.setData(mRecipes);
                mRecipesRv.setAdapter(mRecipeAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.e("Error", "Error in retrofit");
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity().getApplicationContext());
                dialog.setCancelable(false);
                dialog.setTitle(getString(R.string.connection_error_title));
                dialog.setMessage(getString(R.string.connection_error));
                dialog.setPositiveButton(getString(R.string.reload_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        loadRecipes();
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // saves recipe list so we don't need to call API again on orientation change
        outState.putParcelableArrayList(RECIPE_LIST, mRecipes);
    }

    @Override
    public void onClick(Recipe recipe) {
        recipeClickListener.onRecipeClicked(recipe);
    }
}

package com.maluta.bakingtime.utils;

import com.maluta.bakingtime.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by admin on 6/26/2018.
 */
@SuppressWarnings("DefaultFileTemplate")
public interface RecipeClient {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}

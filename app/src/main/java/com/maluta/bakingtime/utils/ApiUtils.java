package com.maluta.bakingtime.utils;

/**
 * Created by admin on 6/26/2018.
 */

@SuppressWarnings("DefaultFileTemplate")
public class ApiUtils {

    public static RecipeClient getRecipeClient() {
        return ApiClient.getClient().create(RecipeClient.class);
    }
}

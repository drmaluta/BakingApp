package com.maluta.bakingtime.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.maluta.bakingtime.model.Recipe;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by admin on 7/17/2018.
 */

public class JsonUtils {
    private static final String RECIPE = "recipe";

    public static void recipeToJson (Context context, Recipe recipe){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recipe);
        prefsEditor.putString(RECIPE, json);
        prefsEditor.apply();
    }

    public static Recipe recipeFromJson (Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(RECIPE, null);
        Type type = new TypeToken<Recipe>() {}.getType();
        return gson.fromJson(json, type);
    }
}

package com.maluta.bakingtime.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.maluta.bakingtime.R;
import com.maluta.bakingtime.model.Ingredient;
import com.maluta.bakingtime.model.Recipe;
import com.maluta.bakingtime.utils.JsonUtils;

import java.util.List;

/**
 * Created by admin on 7/17/2018.
 */

public class ListViewWigetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext()) ;
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<Ingredient> ingredients;
    private Recipe recipe;

    private static final String RECIPE = "recipe";

    public ListRemoteViewsFactory(Context context){
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipe = JsonUtils.recipeFromJson(mContext);
        ingredients = recipe.getIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_widget_list_item);
        Ingredient ingredient = ingredients.get(position);
        remoteViews.setTextViewText(R.id.widget_list_view_text_ingredient, ingredient.getIngredient());
        remoteViews.setTextViewText(R.id.widget_list_view_text_quantity, String.valueOf(ingredient.getQuantity()));
        remoteViews.setTextViewText(R.id.widget_list_view_text_measure, ingredient.getMeasure());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(RECIPE, recipe);
        remoteViews.setOnClickFillInIntent(R.id.widget_list_item_view, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

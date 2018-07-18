package com.maluta.bakingtime.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.maluta.bakingtime.model.Recipe;
import com.maluta.bakingtime.utils.JsonUtils;
import com.maluta.bakingtime.R;

/**
 * Created by admin on 7/17/2018.
 */

public class WidgetUpdateService extends IntentService {
    public static final String ACTION_UPDATE_LIST_VIEW = "update_widget_list";
    private static final String RECIPE = "recipe";

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent !=null){
            final String action = intent.getAction();
            if (ACTION_UPDATE_LIST_VIEW.equals(action)){
                Recipe recipe = intent.getParcelableExtra(RECIPE);
                handleActionUpdateListView(recipe);
            }
        }
    }

    private void handleActionUpdateListView(Recipe recipe) {
        if (recipe != null) {
            JsonUtils.recipeToJson(this, recipe);
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));

        IngredientsWidgetProvider.updateAppWidgets(this, appWidgetManager, appWidgetIds);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
    }

    public static void startActionUpdateListView (Context context, Recipe recipe) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_LIST_VIEW);
        intent.putExtra(RECIPE, recipe);
        context.startService(intent);
    }
}

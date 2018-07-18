package com.maluta.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.maluta.bakingtime.DetailActivity;
import com.maluta.bakingtime.R;
import com.maluta.bakingtime.model.Recipe;
import com.maluta.bakingtime.utils.JsonUtils;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {
    private static final String RECIPE = "recipe";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        Recipe recipe = JsonUtils.recipeFromJson(context);
        if (recipe != null) {
            widgetText = recipe.getName();
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent intentService = new Intent(context, ListViewWigetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intentService);

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(RECIPE, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (!widgetText.equals(context.getResources().getString(R.string.appwidget_text))) {
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        }

        Intent appIntent = new Intent(context, DetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);

        views.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetUpdateService.startActionUpdateListView(context,null);
    }

    public static void updateAppWidgets (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


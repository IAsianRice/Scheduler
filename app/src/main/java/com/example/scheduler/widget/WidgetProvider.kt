package com.example.scheduler.widget

// WidgetProvider.kt

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews


class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Perform operations when the widget is updated

        // Iterate through all widgets
        appWidgetIds.forEach { appWidgetId ->
            // Update the widget UI
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, com.example.scheduler.R.layout.widget_layout)

        // Update UI components or set click listeners if needed
        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        /*val intent = Intent(context, MyRemoteViewsFactory(context, items))
        views.setRemoteAdapter(com.example.scheduler.R.id.listView, intent)*/
        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
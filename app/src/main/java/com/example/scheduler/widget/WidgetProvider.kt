package com.example.scheduler.widget

// WidgetProvider.kt
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.RemoteViews
import com.example.scheduler.R
import com.example.scheduler.widget.remote_views_factory.MyRemoteViewsFactory

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
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        // Update UI components or set click listeners if needed
        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
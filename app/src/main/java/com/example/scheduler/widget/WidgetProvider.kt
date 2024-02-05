package com.example.scheduler.widget

// WidgetProvider.kt
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.scheduler.R

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

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
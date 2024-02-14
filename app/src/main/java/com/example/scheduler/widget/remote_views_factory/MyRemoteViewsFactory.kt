package com.example.scheduler.widget.remote_views_factory

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.scheduler.R

class MyRemoteViewsFactory(private val context: Context, private val items: Array<String>) :
    RemoteViewsService.RemoteViewsFactory {

    override fun onCreate() {
        // Initialize your data here
    }

    override fun onDataSetChanged() {
        // Update your data here if needed
    }

    override fun onDestroy() {
        // Cleanup resources if needed
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.list_item)
        views.setTextViewText(androidx.core.R.id.text, items[position])
        return views
    }

    override fun getLoadingView(): RemoteViews? {
        // Return a loading view if needed, can be null
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1 // Only one type of view in this example
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}
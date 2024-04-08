package com.example.scheduler.widget.remote_views_factory

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.scheduler.R

class MyRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        //val itemList = intent.getStringArrayListExtra("itemList") ?: listOf<String>()
        return MyRemoteViewsFactory(this) //, itemList)
    }
    class MyRemoteViewsFactory(private val context: Context) ://, private val items: List<String>) :
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
            return 4//items.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.list_item)
            //views.setTextViewText(R.id.itemTextView, items[position])
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
}
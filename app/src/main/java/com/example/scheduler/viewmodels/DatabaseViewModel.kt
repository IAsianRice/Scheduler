package com.example.scheduler.viewmodels

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduler.MainActivity
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.ActiveScheduleQuotaCrossRef
import com.example.scheduler.database.entities.QuotaItem
import com.example.scheduler.database.entities.QuotaTagCrossRef
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.database.entities.ScheduleTagCrossRef
import com.example.scheduler.database.entities.TagItem
import com.example.scheduler.services.SchedulerTrackerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class DatabaseViewModel : ViewModel() {

    private val _tagsListStateFlow = MutableStateFlow<List<TagItem>>(listOf())
    val tagsListStateFlow = _tagsListStateFlow.asStateFlow()

    private val _quotasListStateFlow = MutableStateFlow<List<QuotaItem>>(listOf())
    val quotasListStateFlow = _quotasListStateFlow.asStateFlow()

    private val _scheduleItemListStateFlow = MutableStateFlow<List<ScheduleItem>>(listOf())
    val scheduleItemListStateFlow = _scheduleItemListStateFlow.asStateFlow()

    private val _scheduleItemMapStateFlow = MutableStateFlow<Map<Long, ScheduleItem>>(mapOf())
    val scheduleItemMapStateFlow = _scheduleItemMapStateFlow.asStateFlow()

    private val _activeScheduleItemListStateFlow = MutableStateFlow<List<ActiveScheduleItem>>(listOf())
    val activeScheduleItemListStateFlow = _activeScheduleItemListStateFlow.asStateFlow()

    private val _activeScheduleQuotaCrossRefsListStateFlow = MutableStateFlow<List<ActiveScheduleQuotaCrossRef>>(listOf())
    val activeScheduleQuotaCrossRefsListStateFlow = _activeScheduleQuotaCrossRefsListStateFlow.asStateFlow()


    // Should be in service!
    private val _upcomingActiveScheduleItemsListStateFlow = MutableStateFlow<List<ActiveScheduleItem>>(listOf())
    val upcomingActiveScheduleItemsListStateFlow = _upcomingActiveScheduleItemsListStateFlow.asStateFlow()

    private val _currentSchedulesStateFlow = MutableStateFlow<List<ActiveScheduleItem>>(listOf())
    val currentSchedulesStateFlow = _currentSchedulesStateFlow.asStateFlow()



    // Scheduler Tracker Service Binding Operations
    private var serviceBound: Boolean = false
    private lateinit var myBinder: SchedulerTrackerService.SchedulerTrackerBinder
    fun bindService(context: Context) {
        val serviceIntent = Intent(context, SchedulerTrackerService::class.java)
        context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun unbindService(context: Context) {
        context.unbindService(serviceConnection)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
            Log.d("onServiceConnected", "Connected")
            myBinder = binder as SchedulerTrackerService.SchedulerTrackerBinder
            serviceBound = true

            viewModelScope.launch(Dispatchers.IO) {
                myBinder.getUpcomingActiveScheduleItemListStateFlow().collect {
                    Log.d("serviceConnection", "Updated getUpcomingActiveScheduleItemListStateFlow")
                    _upcomingActiveScheduleItemsListStateFlow.value = it
                }
            }
            viewModelScope.launch(Dispatchers.IO) {
                myBinder.getInProgressActiveScheduleItemListStateFlow().collect {
                    Log.d("serviceConnection", "Updated getInProgressActiveScheduleItemListStateFlow")
                    _currentSchedulesStateFlow.value = it
                }
            }
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            serviceBound = false
        }
    }

    // Initializations
    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateActiveScheduleQuotaCrossRef()
            updateScheduleItems()
            updateActiveScheduleItems()
            updateTagItems()
            updateQuotaItems()
            Log.d("DatabaseViewModel", "scheduleItemListStateFlow: ${scheduleItemListStateFlow.value.size}")
            Log.d("DatabaseViewModel", "scheduleItemMapStateFlow: ${scheduleItemMapStateFlow.value.size}")
            Log.d("DatabaseViewModel", "activeScheduleItemListStateFlow: ${activeScheduleItemListStateFlow.value.size}")
            Log.d("DatabaseViewModel", "upcomingActiveScheduleItemsListStateFlow: ${upcomingActiveScheduleItemsListStateFlow.value.size}")
            Log.d("DatabaseViewModel", "currentSchedulesStateFlow: ${currentSchedulesStateFlow.value.size}")
        }
    }

    // ActiveScheduleQuotaCrossRef Related Methods
    private suspend fun updateActiveScheduleQuotaCrossRef() {
        _activeScheduleQuotaCrossRefsListStateFlow.value = MainActivity.appDatabase.activeScheduleQuotaCrossRefDao().getAllActiveScheduleQuotaCrossRefs()
    }
    fun addActiveScheduleQuotaCrossRef(activeScheduleQuotaCrossRef: ActiveScheduleQuotaCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            MainActivity.appDatabase.activeScheduleQuotaCrossRefDao().insert(activeScheduleQuotaCrossRef)
            updateActiveScheduleQuotaCrossRef()
        }
    }
    fun updateActiveScheduleQuotaCrossRef(activeScheduleQuotaCrossRef: ActiveScheduleQuotaCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            MainActivity.appDatabase.activeScheduleQuotaCrossRefDao().update(activeScheduleQuotaCrossRef)
            updateActiveScheduleQuotaCrossRef()
        }
    }

    // TagItem Related Methods
    private suspend fun updateTagItems() {
        _tagsListStateFlow.value = MainActivity.appDatabase.tagItemDao().getAllTags()
    }
    fun addTagItem(tagItem: TagItem) {
        viewModelScope.launch(Dispatchers.IO) {
            MainActivity.appDatabase.tagItemDao().insert(tagItem)
            updateTagItems()
        }
    }

    // Quota Item Related Methods
    private suspend fun updateQuotaItems() {
        _quotasListStateFlow.value = MainActivity.appDatabase.quotaItemDao().getAllQuotas()
    }
    fun addQuotaItem(quotaItem: QuotaItem, tagsIdList: List<Long>?) {
        viewModelScope.launch(Dispatchers.IO) {
            val quotaId = MainActivity.appDatabase.quotaItemDao().insert(quotaItem)
            tagsIdList?.forEach { tagId ->
                val crossRef = QuotaTagCrossRef(quotaId, tagId)
                MainActivity.appDatabase.quotaTagCrossRefDao().insertQuotaTagCrossRef(crossRef)
            }
            updateQuotaItems()
        }
    }


    // ScheduleItem Related Methods
    private suspend fun updateScheduleItems() {
        _scheduleItemListStateFlow.value = MainActivity.appDatabase.scheduleItemDao().getAllSchedules()
        _scheduleItemMapStateFlow.value = _scheduleItemListStateFlow.value.associateBy {it.scheduleId}
    }
    fun addScheduleItem(scheduleItem: ScheduleItem, tagsIdList: List<Long>?) {
        viewModelScope.launch(Dispatchers.IO) {
            val scheduleId = MainActivity.appDatabase.scheduleItemDao().insert(scheduleItem)
            tagsIdList?.forEach { tagId ->
                val crossRef = ScheduleTagCrossRef(scheduleId, tagId)
                MainActivity.appDatabase.scheduleTagCrossRefDao().insertScheduleTagCrossRef(crossRef)
            }
            updateScheduleItems()
        }
    }
    fun updateScheduleItem(scheduleItem: ScheduleItem) {
        viewModelScope.launch(Dispatchers.IO) {
            MainActivity.appDatabase.scheduleItemDao().updateSchedule(scheduleItem)
            updateScheduleItems()
        }
    }

    // ActiveScheduleItem Related Methods
    private suspend fun updateActiveScheduleItems() {
        _activeScheduleItemListStateFlow.value = MainActivity.appDatabase.activeScheduleItemDao().getAllActiveSchedules()
        val cal = Calendar.getInstance()
        _currentSchedulesStateFlow.value = MainActivity.appDatabase.activeScheduleItemDao().getInProgressSchedules(cal.timeInMillis)
        _upcomingActiveScheduleItemsListStateFlow.value = MainActivity.appDatabase.activeScheduleItemDao().getNextUpcomingSchedules()
    }
    fun addActiveScheduleItem(activeScheduleItem: ActiveScheduleItem, quotaItems: Map<QuotaItem,Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            val activeScheduleId = MainActivity.appDatabase.activeScheduleItemDao().insert(activeScheduleItem)
            quotaItems.forEach { (quotaItem, amount) ->
                addActiveScheduleQuotaCrossRef(
                    ActiveScheduleQuotaCrossRef(
                        activeScheduleId = activeScheduleId,
                        quotaId = quotaItem.quotaId,
                        quotaNeeded = amount,
                        quotaFinished = 0
                    )
                )
            }
            updateActiveScheduleItems()
            myBinder.update()
        }
    }

    // ViewModel Related Functions
    override fun onCleared() {
        super.onCleared()

        Log.d("Viewmodel", "Cleared")
    }
}
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
import com.example.scheduler.database.entities.QuotaItem
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.database.entities.SchedulesWithTags
import com.example.scheduler.fragments.ScheduleDetailsFragment.Companion.printDateAndTime
import com.example.scheduler.services.SchedulerTrackerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class ScheduleViewModel() : ViewModel() {
    // Define ViewModel factory in a companion object
    private val selectedDay = Calendar.getInstance()
    private val currentDay = Calendar.getInstance()

    // Detailed View
    private val _schedulesWithTags = MutableStateFlow<SchedulesWithTags?>(null)
    val schedulesWithTags = _schedulesWithTags.asStateFlow()

    private val _selectedSchedulerItem = MutableStateFlow<ScheduleItem?>(null)
    val selectedSchedulerItem = _selectedSchedulerItem.asStateFlow()

    private val _selectedActiveSchedulerItems = MutableStateFlow<List<ActiveScheduleItem>?>(null)
    val selectedActiveSchedulerItems = _selectedActiveSchedulerItems.asStateFlow()

    // Daily Schedule View
    private val _selectedDayActiveScheduleItems = MutableStateFlow<List<ActiveScheduleItem>?>(null)
    val selectedDayActiveScheduleItems = _selectedDayActiveScheduleItems.asStateFlow()

    // Set Schedule View
    private val _selectedQuotas = MutableStateFlow<List<QuotaItem>>(listOf())
    val selectedQuotas = _selectedQuotas.asStateFlow()

    // Bind Service
    private var serviceBound: Boolean = false
    private lateinit var myBinder: SchedulerTrackerService.SchedulerTrackerBinder


    // Set Schedule Related Methods
    fun setSelectedQuotas(quotas: List<QuotaItem>){
        _selectedQuotas.value = quotas
    }


    fun bindService(context: Context) {
        val serviceIntent = Intent(context, SchedulerTrackerService::class.java)
        context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun unbindService(context: Context) {
        if (serviceBound) {
            context.unbindService(serviceConnection)
            serviceBound = false
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
            myBinder = binder as SchedulerTrackerService.SchedulerTrackerBinder
            serviceBound = true
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            serviceBound = false
        }
    }

    init {
        selectedDay.set(Calendar.HOUR_OF_DAY, 0)
        selectedDay.set(Calendar.MINUTE, 0)
        selectedDay.set(Calendar.SECOND, 0)
        selectedDay.set(Calendar.MILLISECOND, 0)
    }

    fun getSelectedDayCalendar(): Calendar {
        return selectedDay
    }
    fun setSelectedSchedulerItemState(scheduleItem: ScheduleItem) {
        _selectedSchedulerItem.value = scheduleItem
        viewModelScope.launch(Dispatchers.IO) {
            _selectedActiveSchedulerItems.value = MainActivity.appDatabase.activeScheduleItemDao().getSchedulesOfScheduleId(selectedSchedulerItem.value!!.scheduleId)
            _schedulesWithTags.value = MainActivity.appDatabase.scheduleItemDao().getSchedulesWithTags(scheduleItem.scheduleId)
        }
    }
    fun setSelectedDayInMillis(millis: Long) {
        selectedDay.timeInMillis = millis
        selectedDay.set(Calendar.HOUR_OF_DAY, 0)
        selectedDay.set(Calendar.MINUTE, 0)
        selectedDay.set(Calendar.SECOND, 0)
        selectedDay.set(Calendar.MILLISECOND, 0)
        getSchedulesForSelectedDay()

    }
    fun setSelectedDay(year: Int? = null, month: Int? = null, dayOfMonth: Int? = null) {
        if (year != null)
        {
            selectedDay.set(Calendar.YEAR, year)
        }
        if (month != null)
        {
            selectedDay.set(Calendar.MONTH, month)
        }
        if (dayOfMonth != null)
        {
            selectedDay.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        getSchedulesForSelectedDay()

    }
    fun addToSelectedDay(days: Int) {
        selectedDay.add(Calendar.DAY_OF_YEAR, days)
        getSchedulesForSelectedDay()
    }

    private fun getSchedulesForSelectedDay() {
        viewModelScope.launch(Dispatchers.IO) {
            var startCal = Calendar.getInstance()
            var endCal = Calendar.getInstance()
            startCal.timeInMillis = selectedDay.timeInMillis
            startCal.set(Calendar.HOUR_OF_DAY, 0)
            startCal.set(Calendar.MINUTE, 0)
            startCal.set(Calendar.SECOND, 0)
            startCal.set(Calendar.MILLISECOND, 0)
            endCal.timeInMillis = startCal.timeInMillis
            endCal.add(Calendar.HOUR, 24)

            Log.d("Time", "Set Time = ${printDateAndTime(selectedDay.time)}")
            Log.d("Time", "Start Time = ${printDateAndTime(startCal.time)}")
            Log.d("Time", "endCal Time = ${printDateAndTime(endCal.time)}")
            _selectedDayActiveScheduleItems.value = MainActivity.appDatabase.activeScheduleItemDao().getSchedulesWithinTimeFrame(startCal.timeInMillis, endCal.timeInMillis)
            Log.d("Time", "endCal Time = ${_selectedDayActiveScheduleItems.value!!.size}")
        }
    }




    fun selectedThisWeek(): Boolean {
        val selectedWeekStart = Calendar.getInstance()
        selectedWeekStart.timeInMillis = selectedDay.timeInMillis
        selectedWeekStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        val selectedWeekEnd = Calendar.getInstance()
        selectedWeekEnd.timeInMillis = selectedDay.timeInMillis
        selectedWeekEnd.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)

        return currentDay.after(selectedWeekStart) && currentDay.before(selectedWeekEnd) ||
                currentDay.timeInMillis == selectedWeekStart.timeInMillis ||
                currentDay.timeInMillis == selectedWeekEnd.timeInMillis
    }
    fun selectedAfterThisWeek(): Boolean {

        val selectedWeekEnd = Calendar.getInstance()
        selectedWeekEnd.timeInMillis = currentDay.timeInMillis
        selectedWeekEnd.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)

        return selectedDay.after(selectedWeekEnd)
    }
    fun selectedBeforeThisWeek(): Boolean {

        val selectedWeekStart = Calendar.getInstance()
        selectedWeekStart.timeInMillis = currentDay.timeInMillis
        selectedWeekStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        return selectedDay.before(selectedWeekStart)
    }

}
package com.example.scheduler.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduler.MainActivity
import com.example.scheduler.database.entities.ScheduleItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Stack

class DatabaseViewModel : ViewModel() {
    private val _ScheduleItemListStateFlow = MutableStateFlow<List<ScheduleItem>>(listOf())
    val scheduleItemListStateFlow = _ScheduleItemListStateFlow.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _ScheduleItemListStateFlow.value = MainActivity.appDatabase.scheduleItemDao().getAllSchedules()
        }
    }

    override fun onCleared() {
        super.onCleared()

        Log.d("Viewmodel", "Cleared")
    }
}
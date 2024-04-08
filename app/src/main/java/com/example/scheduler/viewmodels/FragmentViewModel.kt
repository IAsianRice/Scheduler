package com.example.scheduler.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Stack

sealed class FragmentState {
    object AddScheduleFragment : FragmentState()
    object TasksFragment : FragmentState()
    object ScheduleDetailsFragment : FragmentState()
    object LandingPageFragment : FragmentState()
    object ScheduleListFragment : FragmentState()
    object CalendarFragment : FragmentState()
    object WeeklyScheduleFragment : FragmentState()
    object SetScheduleFragment : FragmentState()
    object PictureInPictureFragment : FragmentState()
    object UnimplementedFragment : FragmentState()
    //object QuotaSelectFragment : FragmentState()
}

class FragmentViewModel : ViewModel() {
    private val _fragmentStateFlow = MutableStateFlow<FragmentState>(FragmentState.LandingPageFragment)
    val fragmentStateFlow = _fragmentStateFlow.asStateFlow()

    private val fragmentStack = Stack<FragmentState>()

    init {
        fragmentStack.push(FragmentState.LandingPageFragment)
    }

    fun setFragmentState(state: FragmentState) {
        _fragmentStateFlow.value = state
        fragmentStack.push(state)
    }

    fun back(): Boolean {
        fragmentStack.pop()
        if (fragmentStack.empty())
        {
            return false
        }
        val state = fragmentStack.peek()
        _fragmentStateFlow.value = state
        return true
    }

    fun skipBackOver(fragmentState: FragmentState): Boolean {
        while (fragmentStack.peek() == fragmentState)
        {
            fragmentStack.pop()
        }
        _fragmentStateFlow.value = fragmentStack.peek()
        return true
    }

    override fun onCleared() {
        super.onCleared()

        Log.d("Viewmodel", "Cleared")
    }
}
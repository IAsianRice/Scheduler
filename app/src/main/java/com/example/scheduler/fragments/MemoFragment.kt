package com.example.scheduler.fragments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.scheduler.MainActivity
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerHelperFunctions.Companion.scheduleListToDictionary
import com.example.scheduler.utility.SchedulerMockData.Companion.randomizeActiveScheduleItemList
import com.example.scheduler.viewmodels.ScheduleViewModel

class MemoFragment() : AppFragment {
    @Composable
    override fun Content() {
        val activeSchedules by MainActivity.scheduleViewModel.selectedDayActiveScheduleItems.collectAsState()
        val scheduleItems by MainActivity.databaseViewModel.scheduleItemListStateFlow.collectAsState()

        BaseContent(scheduleViewModel = MainActivity.scheduleViewModel,
            schedules = activeSchedules ?: listOf(),
            scheduleItems = scheduleListToDictionary(scheduleItems)
        )
    }

    companion object {
        @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
        @Composable
        fun BaseContent(
            scheduleViewModel: ScheduleViewModel = ScheduleViewModel(),
            schedules: List<ActiveScheduleItem> = randomizeActiveScheduleItemList(20),
            scheduleItems: Map<Long,ScheduleItem> = mapOf<Long,ScheduleItem>(),
            onDayChanged: () -> Unit = {},
            onSetScheduleClicked: () -> Unit = {}
        ) {

        }
    }
}

@Composable
@Preview(showBackground = true,)
fun MemoPreview() {

    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MemoFragment.BaseContent()
        }
    }
}

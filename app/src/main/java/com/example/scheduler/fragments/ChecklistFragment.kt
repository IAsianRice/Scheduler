package com.example.scheduler.fragments

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.scheduler.MainActivity
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme


class ChecklistFragment() : AppFragment {
    @Composable
    override fun Content() {
        val activeSchedules by MainActivity.scheduleViewModel.selectedDayActiveScheduleItems.collectAsState()
        val scheduleItems by MainActivity.databaseViewModel.scheduleItemListStateFlow.collectAsState()

        BaseContent()
    }

    companion object {

        @Composable
        fun BaseContent(
            schedule: ScheduleItem = ScheduleItem( title = "Schedule", description = "Description"),
        ) {

        }
    }
}

@Composable
@Preview(showBackground = true,)
fun ChecklistPreview() {

    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ChecklistFragment.BaseContent()
        }
    }
}

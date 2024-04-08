package com.example.scheduler.fragments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scheduler.MainActivity
import com.example.scheduler.composables.SchedulerDesignComposables.Companion.CardColumn
import com.example.scheduler.composables.SchedulerFieldComposables.Companion.EditableTextField
import com.example.scheduler.composables.SchedulerModalComposables.Companion.YesNoModalDialog
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.database.entities.TagItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerMockData.Companion.generateRandomTags
import com.example.scheduler.utility.SchedulerMockData.Companion.randomizeActiveScheduleItemList
import com.example.scheduler.viewmodels.FragmentState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ScheduleDetailsFragment() : AppFragment {
    @Composable
    override fun Content() {
        val selectedSchedulesWithTags by MainActivity.scheduleViewModel.schedulesWithTags.collectAsState()
        val selectedScheduleItem by MainActivity.scheduleViewModel.selectedSchedulerItem.collectAsState()
        val selectedActiveScheduleItems by MainActivity.scheduleViewModel.selectedActiveSchedulerItems.collectAsState()
        BaseContent(
            tagsList = selectedSchedulesWithTags?.tags ?: listOf(),
            scheduleItem = selectedScheduleItem,
            onScheduleItemEdited = {
                MainActivity.databaseViewModel.updateScheduleItem(it)
            },
            activeScheduleItems =  selectedActiveScheduleItems,
            onActiveScheduleItemClicked = {
                MainActivity.fragmentViewModel.setFragmentState(FragmentState.WeeklyScheduleFragment)
                MainActivity.scheduleViewModel.setSelectedDayInMillis(it.startTimeInMillis)
            })
    }

    companion object {
        fun printDateAndTime(date: Date) : String {
            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
            return sdf.format(date)
        }

        fun printDate(date: Date) : String {
            val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            return sdf.format(date)
        }

        @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
        @Composable
        fun BaseContent(
            tagsList: List<TagItem> = generateRandomTags(30),
            scheduleItem: ScheduleItem? = ScheduleItem(title = "Workout", description = "100 Pushups, wow"),
            onScheduleItemEdited: (ScheduleItem) -> Unit = {},
            activeScheduleItems: List<ActiveScheduleItem>? = randomizeActiveScheduleItemList(10),
            onActiveScheduleItemClicked: (ActiveScheduleItem) -> Unit = {},
        ) {
            Scaffold(
                modifier = Modifier,
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Schedule Details")},
                    )
                },
                content = { paddingValues ->
                    val calendar = Calendar.getInstance()
                    Column(modifier = Modifier.padding(paddingValues)) {
                        CardColumn (modifier = Modifier.fillMaxWidth()) {
                            EditableTextField(value = scheduleItem?.title ?: "[Title]",
                                onValueChange =
                                {onScheduleItemEdited(ScheduleItem(scheduleId = scheduleItem?.scheduleId ?: 0,
                                    title = it,
                                    description = scheduleItem?.description ?: ""))}
                            )
                        }

                        CardColumn(modifier = Modifier.fillMaxWidth()) {
                            EditableTextField(value = scheduleItem?.description ?: "[Description]",
                                onValueChange =
                                {
                                    onScheduleItemEdited(
                                        ScheduleItem(
                                            scheduleId = scheduleItem?.scheduleId ?: 0,
                                            title = scheduleItem?.title ?: "",
                                            description = it
                                        )
                                    )
                                }
                            )
                        }


                        CardColumn(modifier = Modifier.fillMaxWidth()) {
                            FlowRow {
                                for (item in tagsList) {
                                    Column(
                                        modifier = Modifier
                                            .border(
                                                BorderStroke(1.dp, Color.Black),
                                                CircleShape
                                            )
                                            .background(Color.Gray, CircleShape)
                                    ) {
                                        Text(modifier = Modifier.padding(10.dp), text = item.name)
                                    }
                                }
                            }
                        }

                        // UpComing Dates
                        CardColumn(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Upcoming Schedules")
                            LazyColumn(modifier = Modifier.height(200.dp))
                            {
                                items(activeScheduleItems ?: listOf()) {
                                    Column(modifier = Modifier
                                        .padding(5.dp)
                                        .border(
                                            BorderStroke(
                                                1.dp,
                                                MaterialTheme.colorScheme.primaryContainer
                                            )
                                        )
                                        .clickable {
                                            onActiveScheduleItemClicked(it)
                                        }
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(5.dp)
                                        ) {
                                            calendar.timeInMillis = it.startTimeInMillis
                                            Text(
                                                text = "Schedule Starting: ${
                                                    printDateAndTime(
                                                        calendar.time
                                                    )
                                                }"
                                            )
                                            calendar.timeInMillis =
                                                it.startTimeInMillis + it.durationInMillis
                                            Text(
                                                text = "Schedule Ends At: ${
                                                    printDateAndTime(
                                                        calendar.time
                                                    )
                                                }"
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        var showDialog by remember { mutableStateOf(false) }
                        Button(colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            onClick = { showDialog = true }) {
                            Text(text = "Delete")
                        }
                        YesNoModalDialog(showDialog,
                            onDismiss = {
                            showDialog = false
                        })
                    }
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true,)
fun ScheduleDetailsPreview() {

    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ScheduleDetailsFragment.BaseContent()
        }
    }
}

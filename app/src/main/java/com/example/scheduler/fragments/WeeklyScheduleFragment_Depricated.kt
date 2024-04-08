package com.example.scheduler.fragments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheduler.MainActivity
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerHelperFunctions.Companion.scheduleListToDictionary
import com.example.scheduler.utility.SchedulerMockData.Companion.randomizeActiveScheduleItemList
import com.example.scheduler.utility.SchedulerTimeFunctions.Companion.getDayOfWeekName
import com.example.scheduler.viewmodels.FragmentState
import com.example.scheduler.viewmodels.ScheduleViewModel
import kotlinx.coroutines.delay
import java.util.Calendar

class WeeklyScheduleFragment_Depricated() : AppFragment {
    @Composable
    override fun Content() {
        val activeSchedules by MainActivity.scheduleViewModel.selectedDayActiveScheduleItems.collectAsState()
        val scheduleItems by MainActivity.databaseViewModel.scheduleItemListStateFlow.collectAsState()

        BaseContent(scheduleViewModel = MainActivity.scheduleViewModel,
            schedules = activeSchedules ?: listOf(),
            scheduleItems = scheduleListToDictionary(scheduleItems),
            onSetScheduleClicked = {
                MainActivity.fragmentViewModel.setFragmentState(FragmentState.SetScheduleFragment)
            }
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
            // Ruler Markings
            val minDPSize = 15
            // List the hours
            val hours = (0..24).toList() // List of hours from 0 to 24
            val items = (1..7).toList()
            //
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            //
            val pagerState = rememberPagerState(
                initialPage = Int.MAX_VALUE/2 - (Int.MAX_VALUE/2 % items.size)
                        + (scheduleViewModel.getSelectedDayCalendar().get(Calendar.DAY_OF_WEEK) - 1),
                pageCount = { Int.MAX_VALUE }
            )
            
            var currentPage by remember { mutableIntStateOf(pagerState.currentPage) }

            var weekTitle by remember { mutableStateOf("This week's schedule") }
            //Log.d("WeeklyScheduleFragment", "Current Day: ${scheduleViewModel.currentDay.timeInMillis}")
            //Log.d("WeeklyScheduleFragment", "Selected Day: ${scheduleViewModel.selectedDay.timeInMillis}")

            // Keep track of pages to adapt to changes
            LaunchedEffect(pagerState.currentPage) {
                if (pagerState.currentPage > currentPage)
                {
                    scheduleViewModel.addToSelectedDay(1)
                }
                else if (pagerState.currentPage < currentPage)
                {
                    scheduleViewModel.addToSelectedDay(-1)
                }
                // we check if we have passed the week or before the week.
                if (scheduleViewModel.selectedAfterThisWeek())
                {
                    weekTitle = "Next Week's Schedule"
                }
                else if (scheduleViewModel.selectedBeforeThisWeek())
                {
                    weekTitle = "Last Week's Schedule"
                }
                else
                {
                    weekTitle = "This Week's Schedule"
                }
                currentPage = pagerState.currentPage
            }

            // Compose UI
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(
                        title = { Text(text = weekTitle) },
                        scrollBehavior = scrollBehavior,
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = onSetScheduleClicked
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                    }
                },
                content = {
                    val rememscrollstates = listOf(rememberScrollState(), rememberScrollState(), rememberScrollState(), rememberScrollState(), rememberScrollState(), rememberScrollState() ,rememberScrollState() ,rememberScrollState())


                    HorizontalPager(modifier = Modifier,
                        state = pagerState) { num ->
                        Box(modifier = Modifier
                            .padding(it)
                            .verticalScroll(rememscrollstates[num % items.size]))
                        {
                            // Shows the day of the week
                            Text(text = "Week Day: ${getDayOfWeekName((num % items.size) + 1)}")

                            // the schedule content
                            Box (modifier = Modifier
                                .offset(y = (200).dp)
                                .fillMaxWidth()){

                                // a timed function that updates the current time. (Move to viewmodel)
                                var cal by remember { mutableStateOf(Calendar.getInstance())  }
                                LaunchedEffect(true) {
                                    while (true) {
                                        // Wait for the specified interval
                                        delay(6000)
                                        cal = Calendar.getInstance()
                                    }
                                }

                                // If the selected day is today
                                if (cal.get(Calendar.DAY_OF_YEAR) ==
                                    scheduleViewModel.getSelectedDayCalendar().get(Calendar.DAY_OF_YEAR))
                                {
                                    // display a moving slider to show the time.
                                    Box(modifier = Modifier
                                        .background(Color(20,20,20,100))
                                        .height(((cal.get(Calendar.HOUR_OF_DAY) * minDPSize * 4) + ((cal.get(
                                            Calendar.MINUTE
                                        ) / 15) * minDPSize)).dp)
                                        .fillMaxWidth()
                                    )
                                }

                                // Display the times on the left side needs rework
                                for (hour in hours)
                                {
                                    TimelineHour(hour = hour, minDPSize= minDPSize)
                                }


                                // Display Schedule Blocks - needs rework
                                var columns = mutableMapOf<ActiveScheduleItem, Int>()
                                for ((index, schedule) in schedules.withIndex()) {

                                    // Pre Calculations for the placement of Schedule Blocks
                                    var calendarStart = Calendar.getInstance()
                                    var calendarEnd = Calendar.getInstance()

                                    calendarStart.timeInMillis = schedule.startTimeInMillis
                                    calendarEnd.timeInMillis =
                                        schedule.startTimeInMillis + schedule.durationInMillis

                                    var leftpad = 0
                                    var column = 0
                                    var placementNotFound = true
                                    while (placementNotFound) {
                                        placementNotFound = false
                                        for (i in 0..<index) {
                                            if (column == columns[schedules[i]]) {
                                                val scheduleStartTime = schedule.startTimeInMillis
                                                val scheduleEndTime =
                                                    schedule.startTimeInMillis + schedule.durationInMillis

                                                val existingScheduleStartTime =
                                                    schedules[i].startTimeInMillis
                                                val existingScheduleEndTime =
                                                    schedules[i].startTimeInMillis + schedules[i].durationInMillis

                                                val isOverlap =
                                                    ((scheduleStartTime <= existingScheduleEndTime && scheduleEndTime >= existingScheduleStartTime) ||
                                                            (existingScheduleStartTime <= scheduleEndTime && existingScheduleEndTime >= scheduleStartTime))

                                                if (isOverlap) {
                                                    leftpad += 100
                                                    column++
                                                    placementNotFound = true
                                                    break
                                                }
                                            }
                                        }
                                        if(!placementNotFound)
                                        {
                                            columns[schedule] = column
                                        }
                                    }

                                    // Schedule Block
                                    Box(modifier = Modifier
                                        .padding(
                                            top = ((calendarStart.get(Calendar.HOUR_OF_DAY) * minDPSize * 4) + ((calendarStart.get(
                                                Calendar.MINUTE
                                            ) / 15) * minDPSize)).dp,
                                            start = (100 + leftpad).dp
                                        )
                                        .background(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .height(
                                            (((calendarEnd.get(Calendar.HOUR_OF_DAY) - calendarStart.get(
                                                Calendar.HOUR_OF_DAY
                                            )) * minDPSize * 4) + ((calendarEnd.get(
                                                Calendar.MINUTE
                                            ) / 15) * minDPSize)).dp
                                        )
                                        .width(100.dp)) {
                                        Text(text = scheduleItems[schedule.scheduleID]?.title ?: "No Title")
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }

        @Composable
        fun TimelineHour(hour: Int, minDPSize: Int) {
            Column(
                modifier = Modifier.padding(top = (hour * minDPSize * 4).dp),
                verticalArrangement = Arrangement.Center
            ) {
                val minutes = listOf(0,15,30,45) // List of hours from 0 to 24
                Row(
                ) {
                    Text(
                        text = "$hour:00",
                        fontSize = 16.sp,
                    )
                    Column(
                        modifier = Modifier,
                    ) {
                        for (minute in minutes) {
                            Box(modifier = Modifier.height(minDPSize.dp)) {
                                Text(
                                    text = "$minute",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

    }

}

@Composable
@Preview(showBackground = true, widthDp = 320)
fun WeeklySchedule_DepricatedPreview() {

    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WeeklyScheduleFragment_Depricated.BaseContent()
        }
    }
}

package com.example.scheduler.fragments

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheduler.composables.SchedulerNavComposables
import com.example.scheduler.utility.SchedulerTimeFunctions
import com.example.scheduler.viewmodels.FragmentState
import com.example.scheduler.viewmodels.FragmentViewModel
import com.example.scheduler.viewmodels.ScheduleViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BaseContent(
    fragmentViewModel: FragmentViewModel = FragmentViewModel(),
    scheduleViewModel: ScheduleViewModel = ScheduleViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Get local density from composable
        val weekHeight = 30
        val topappbvrg = rememberTopAppBarState()
        var today = Calendar.getInstance()
        val contentScroll = rememberScrollState()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topappbvrg)
        val elevation by animateDpAsState(if (topappbvrg.overlappedFraction == 0.0f) 0.dp else 50.dp,
            label = ""
        )
        var time by remember {
            mutableStateOf("")
        }
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                Surface(shadowElevation = elevation) {
                    TopAppBar(
                        title = { Text(text = "Calender $time") },
                        scrollBehavior = scrollBehavior
                    )
                }
            },
            bottomBar = {
                SchedulerNavComposables.SchedulerNavBottomBar(fragmentViewModel)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        fragmentViewModel.setFragmentState(FragmentState.SetScheduleFragment)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            },
        ) {
            Row(modifier = Modifier
                .padding(it)
                .verticalScroll(contentScroll)
            ) {
                var weeks = SchedulerTimeFunctions.generateWeeks(2024)
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .height((weekHeight * weeks.size).dp)
                        .padding(top = 30.dp, bottom = 30.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    SchedulerTimeFunctions.Companion.Months.values().forEachIndexed { index, month ->
                        Box(modifier = Modifier.clickable {  }) {
                            Text(
                                text = month.name,
                                modifier = Modifier
                                    .vertical()
                                    .rotate(-90f)
                                    .padding(top = 12.5.dp, bottom = 12.5.dp)
                                    .width(100.dp)
                                    .background(CalendarFragment.monthColors[index]),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                FlowRow (
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary)){
                    var calender = Calendar.getInstance()
                    var weeksInYear = calender.weeksInWeekYear
                    calender.set(Calendar.DAY_OF_YEAR, 1)
                    calender.get(Calendar.DAY_OF_MONTH)
                    for (week in 1..weeksInYear+2) {
                        calender.set(Calendar.WEEK_OF_YEAR, week)
                        calender.get(Calendar.DAY_OF_MONTH)
                        for (day_of_week in 1..7) {
                            calender.set(Calendar.DAY_OF_WEEK, day_of_week)
                            var month = calender.get(Calendar.MONTH)
                            var day = calender.get(Calendar.DAY_OF_MONTH)
                            Text(
                                text = "${calender.get(Calendar.DAY_OF_MONTH)}",
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(weekHeight.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    .background(
                                        if (calender.after(today)) CalendarFragment.monthColors[calender.get(
                                            Calendar.MONTH
                                        )] else Color.Gray
                                    )
                                    .clickable {
                                        scheduleViewModel.setSelectedDay(
                                            month = month,
                                            dayOfMonth = day
                                        )
                                        //Log.d("Calendar", "Current Day: ${scheduleViewModel.currentDay.timeInMillis}")
                                        //Log.d("Calendar", "Selected Day: ${scheduleViewModel.selectedDay.timeInMillis}")
                                        fragmentViewModel.setFragmentState(FragmentState.WeeklyScheduleFragment)
                                    },
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
@Preview(showBackground = true, widthDp = 640)
fun YearGridExample() {
    var optionsList by remember { mutableStateOf(listOf<String>("Craf", "Pow", "Yow"))}
    var selectedOptionsList by remember { mutableStateOf(listOf<String>())}
    BaseContent()
}
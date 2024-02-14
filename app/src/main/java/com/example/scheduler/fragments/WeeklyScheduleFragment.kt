package com.example.scheduler.fragments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.viewmodels.FragmentViewModel
import kotlin.random.Random


fun generateTestData(): List<ActiveScheduleItem> {
    val testData = mutableListOf<ActiveScheduleItem>()

    repeat(10) {
        val scheduleID = Random.nextLong()
        val repeatFactor = Random.nextInt(1, 5) // Random repeat factor between 1 and 4
        val durationHour = Random.nextInt(24)
        val durationMinute = Random.nextInt(60)
        val durationSecond = Random.nextInt(60)
        val startingTimeHour = Random.nextInt(24)
        val startingTimeMinute = Random.nextInt(60)
        val dayOfWeek = Random.nextInt(1, 8) // Random day of the week between 1 and 7
        val dayOfMonth = Random.nextInt(1, 32) // Random day of the month between 1 and 31
        val month = Random.nextInt(1, 13) // Random month between 1 and 12
        val year = Random.nextInt(2022, 2025) // Random year between 2022 and 2024

        testData.add(
            ActiveScheduleItem(
                scheduleID = scheduleID,
                repeatFactor = repeatFactor,
                durationHour = durationHour,
                durationMinute = durationMinute,
                durationSecond = durationSecond,
                startingTimeHour = startingTimeHour,
                startingTimeMinute = startingTimeMinute,
                dayOfWeek = dayOfWeek,
                dayOfMonth = dayOfMonth,
                month = month,
                year = year
            )
        )
    }

    return testData
}
class WeeklyScheduleFragment() : AppFragment {
    @Composable
    override fun Content() {
        TODO("Not yet implemented")
    }

    companion object {
        @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
        @Composable
        fun BaseContent(
            schedules : List<ActiveScheduleItem> = generateTestData(),
        ) {
            val minDPSize = 15
            val hours = (0..24).toList() // List of hours from 0 to 24
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Weekly Schedule") },
                        scrollBehavior = scrollBehavior,
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                    }
                },
                content = {
                    val items = (1..7).toList()
                    val rememscrollstates = listOf(rememberScrollState(), rememberScrollState(), rememberScrollState(), rememberScrollState(), rememberScrollState(), rememberScrollState() ,rememberScrollState() ,rememberScrollState())
                    val pagerState = rememberPagerState( initialPage = Int.MAX_VALUE/2 - (Int.MAX_VALUE/2 % items.size), pageCount = { Int.MAX_VALUE })
                    HorizontalPager(modifier =Modifier,
                        state = pagerState) { num ->
                        Box(modifier = Modifier
                            .padding(it)
                            .verticalScroll(rememscrollstates[num%items.size]))
                        {
                            Text(text = "Week Day: ${num % items.size}")
                            for (hour in hours)
                            {
                                TimelineHour(hour = hour, minDPSize= minDPSize)
                            }
                            for (schedule in schedules) {
                                Box(modifier = Modifier
                                    .padding(top = ((schedule.startingTimeHour * minDPSize * 4) + ((schedule.startingTimeMinute/15) * minDPSize)).dp
                                        , start = 100.dp)
                                    .background(Color(50,50,50,50))
                                    .height(((schedule.durationHour * minDPSize * 4) + ((schedule.durationMinute/15) * minDPSize)).dp)) {
                                    Text(text = "${schedule.scheduleID}:${schedule.startingTimeHour}:${schedule.startingTimeMinute}\n" +
                                            "${schedule.durationHour}:${schedule.durationMinute}:${schedule.durationSecond}")
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
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
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
@Preview(showBackground = true,)
fun WeeklySchedulePreview() {

    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WeeklyScheduleFragment.BaseContent()
        }
    }
}

package com.example.scheduler.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.scheduler.composables.SchedulerNavComposables
import com.example.scheduler.composables.SchedulerPickerComposables
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.viewmodels.FragmentViewModel
import kotlin.math.min
import kotlin.random.Random

class MonthlyScheduleFragment() : AppFragment {
    @Composable
    override fun Content() {
        TODO("Not yet implemented")
    }

    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BaseContent(
            //schedules : List<ScheduleItem> = generateRandomScheduleItems(),
        ) {
            val minDPSize = 15
            val hours = (0..24).toList() // List of hours from 0 to 24
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Daily Schedule") },
                        scrollBehavior = scrollBehavior,
                    )
                }
                ,
                content = {
                    Box(modifier = Modifier.padding(it)
                        .verticalScroll(rememberScrollState()))
                    {
                        for (hour in hours)
                        {
                            TimelineHour(hour = hour, minDPSize= minDPSize)
                        }
                        /*for (schedule in schedules) {
                            Box(modifier = Modifier
                                .padding(top = ((schedule.startingTimeHour * minDPSize * 60) + (schedule.startingTimeMinute * minDPSize)).dp
                                    , start = 100.dp)
                                .background(Color(50,50,50,50))
                                .height(((schedule.durationHour * minDPSize * 60) + (schedule.durationMinute * minDPSize)).dp)) {
                                Text(text = "${schedule.title}:${schedule.startingTimeHour}:${schedule.startingTimeMinute}\n" +
                                        "${schedule.durationHour}:${schedule.durationMinute}:${schedule.durationSecond}")
                            }
                        }*/
                    }
                }
            )
        }

        @Composable
        fun TimelineHour(hour: Int, minDPSize: Int) {
            Column(
                modifier = Modifier.padding(top = (hour * minDPSize * 60).dp),
                verticalArrangement = Arrangement.Center
            ) {
                val minutes = (0..59).toList() // List of hours from 0 to 24
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
fun MonthlySchedulePreview() {

    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MonthlyScheduleFragment.BaseContent()
        }
    }
}

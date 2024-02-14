package com.example.scheduler.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.viewmodels.FragmentViewModel
import kotlin.random.Random

class ScheduleDetailsFragment() : AppFragment {
    @Composable
    override fun Content() {
        TODO("Not yet implemented")
    }

    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BaseContent(
           // schedules : List<ScheduleItem> = generateRandomScheduleItems(),
        ) {
            /*val hours = (0..24).toList() // List of hours from 0 to 24
            Lazy(
                modifier = Modifier.fillMaxWidth(),
                //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                itemsIndexed(hours) { index, hour ->
                    TimelineHour(hour = hour)
                    Box(
                    ) {
                        for (schedule in schedules) {
                            Box(modifier = Modifier.padding(top = ((schedule.startingTimeHour * 7 * 60) + (schedule.startingTimeMinute*7)).dp
                                , start = 100.dp)) {
                                Text(text = "${schedule.title}")
                            }
                        }
                    }
                }
            }*/
        }

        @Composable
        fun TimelineHour(hour: Int) {
            Column(
                modifier = Modifier,
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
                            Box(modifier = Modifier.height(7.dp)) {
                                Text(
                                    text = "$minute",
                                    fontSize = 6.sp
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

package com.example.scheduler.fragments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scheduler.MainActivity
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerMockData.Companion.randomizeActiveScheduleItemList
import java.lang.Math.PI
import java.lang.Math.cos
import java.lang.Math.sin

class PictureInPictureFragment2(): AppFragment {
    @Composable
    override fun Content() {
        val upcomingSchedules by MainActivity.databaseViewModel.upcomingActiveScheduleItemsListStateFlow!!.collectAsState()
        val currentActiveScheduleItems by MainActivity.databaseViewModel.currentSchedulesStateFlow!!.collectAsState()
        BaseContent(currentActiveScheduleItems ?: listOf(), upcomingSchedules ?: listOf())
    }
    companion object {
        private const val SCROLL_INTERVAL_MS = 2000L // 2 seconds
        @OptIn(ExperimentalFoundationApi::class)
        @Composable
        fun BaseContent(
            currentSchedules: List<ActiveScheduleItem> = randomizeActiveScheduleItemList(10),
            upcomingSchedules: List<ActiveScheduleItem> = randomizeActiveScheduleItemList(10),
            onActiveScheduleItemClicked: (ActiveScheduleItem) -> Unit = {},
        ) {
            ConcentricCircles()
            Clock()
        }
        @Composable
        fun ConcentricCircles() {
            Surface(color = Color.White) {
                Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta)
                    val radius = listOf(50f, 100f, 150f, 200f, 250f)

                    repeat(5) { index ->
                        drawCircle(
                            color = colors[index],
                            center = Offset(size.width / 2, size.height / 2),
                            radius = radius[index],
                            style = Stroke(35f)
                        )
                    }
                }
            }
        }
        @Composable
        fun Clock() {
            Surface(color = Color.White) {
                Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val radius = 150f
                    val clockHandLength = radius - 20f // Length of the clock hand

                    // Draw concentric circles
                    repeat(24) { index ->
                        val angle = index * (2 * PI / 24) // Calculate angle for each slice
                        val startX = centerX + cos(angle).toFloat() * radius
                        val startY = centerY + sin(angle).toFloat() * radius
                        val endX = centerX + cos(angle).toFloat() * (radius - 10f)
                        val endY = centerY + sin(angle).toFloat() * (radius - 10f)
                        drawLine(color = Color.Gray, start = Offset(startX, startY), end = Offset(endX, endY), strokeWidth = 5f)
                    }

                    // Draw clock hand
                    val currentHour = 10 // Example hour (0-23)
                    val hourAngle = currentHour * (2 * PI / 24) // Calculate angle for current hour
                    val handEndX = centerX + cos(hourAngle).toFloat() * clockHandLength
                    val handEndY = centerY + sin(hourAngle).toFloat() * clockHandLength
                    drawLine(color = Color.Red, start = Offset(centerX, centerY), end = Offset(handEndX, handEndY), strokeWidth = 8f)
                }
            }
        }


    }
}

@Composable
@Preview(showBackground = true,)
fun PictureInPictureFragment2Preview() {
    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PictureInPictureFragment2.BaseContent()
        }
    }
}
package com.example.scheduler.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.zIndex
import com.example.scheduler.MainActivity
import com.example.scheduler.composables.SchedulerAAGComposables.Companion.ShortList
import com.example.scheduler.composables.SchedulerNavComposables
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerHelperValues.Companion.MILLIS_IN_DAY
import com.example.scheduler.viewmodels.FragmentViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class LandingPageFragment() : AppFragment {
    // Overrides
    @Composable
    override fun Content() {
        val scheduleItemMap by MainActivity.databaseViewModel.scheduleItemMapStateFlow.collectAsState()
        val upcomingActiveScheduleItemsList by MainActivity.databaseViewModel.upcomingActiveScheduleItemsListStateFlow!!.collectAsState()
        BaseContent(
            MainActivity.fragmentViewModel,
            upcomingActiveScheduleItems = upcomingActiveScheduleItemsList,
            scheduleItemMap = scheduleItemMap
            )
    }

    // Static Functions
    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BaseContent(
            fragmentViewModel: FragmentViewModel = FragmentViewModel(),
            upcomingActiveScheduleItems: List<ActiveScheduleItem> = listOf(),
            scheduleItemMap: Map<Long, ScheduleItem> = mapOf(),

            ) {
            val configuration = LocalConfiguration.current
            val density = LocalDensity.current

            val screenWidthInDP = with(density) { configuration.screenWidthDp.dp }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                Scaffold(
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Scheduler") },
                            scrollBehavior = scrollBehavior,
                            actions = {
                                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                            }
                        )
                    },
                    bottomBar = {
                        SchedulerNavComposables.SchedulerNavBottomBar(fragmentViewModel)
                    }
                    ,
                    content = {
                        Box(modifier = Modifier.padding(it)) {
                            Column(modifier = Modifier
                                .fillMaxHeight()
                            ) {

                                // Top 3 Tasks
                                ShortList(showing = 3,
                                    listItem = {
                                        Box(modifier = Modifier.fillMaxWidth())
                                        {
                                            val cal = Calendar.getInstance()
                                            var percentage = 0.0f
                                            if(cal.timeInMillis > upcomingActiveScheduleItems[it].startTimeInMillis - MILLIS_IN_DAY)
                                            {
                                                val offset = upcomingActiveScheduleItems[it].startTimeInMillis - MILLIS_IN_DAY
                                                percentage = (cal.timeInMillis.toFloat() - offset )/ (upcomingActiveScheduleItems[it].startTimeInMillis.toFloat() - offset)
                                            }

                                            FillingProgressBar(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(100.dp),
                                                percentage = percentage,
                                                barHeight = 100.dp,
                                                backgroundColor = ListItemDefaults.containerColor,
                                                fillColor = Color(0xFF2E8B57)
                                            )
                                            ListItem(
                                                modifier = Modifier.fillMaxSize(),
                                                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                                headlineContent = { Text(text = scheduleItemMap[upcomingActiveScheduleItems[it].scheduleID]?.title ?: "No Title")},
                                                supportingContent = { Text(text = scheduleItemMap[upcomingActiveScheduleItems[it].scheduleID]?.description ?: "No Description")},
                                                trailingContent = { Text(text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(upcomingActiveScheduleItems[it].startTimeInMillis))}
                                            )
                                        }
                                    },
                                    listSize = upcomingActiveScheduleItems.size)
                                // 4 Dot system
                                //TaskTracker()
                            }
                        }
                    }
                )
            }
        }
        @Composable
        fun FillingProgressBar(
            modifier: Modifier = Modifier,
            percentage: Float,
            backgroundColor: Color = Color.LightGray,
            fillColor: Color = Color.Green,
            barHeight: Dp = 20.dp
        ) {
            androidx.compose.foundation.Canvas(modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                val width = size.width
                val height = barHeight.toPx()

                // Draw background
                drawRect(color = backgroundColor, size = Size(width, height))

                // Draw filling bar
                val fillingWidth = width * percentage
                drawRect(color = fillColor, size = Size(fillingWidth, height))
            }
        }
        @Composable
        fun Sidebar(width: Dp) {
            Box(modifier = Modifier
                .fillMaxHeight()
                .offset(x = width)
                .background(Color.Gray)
                .fillMaxWidth(0.5f) // Adjust the width of the sidebar as needed
                .zIndex(1f) // Set the z-index to make the sidebar overlap with the content
            ) {
                Column {
                    Text(text = "Add New Quota")
                    Text(text = "Add New Schedule")
                    Text(text = "View Schedules")
                    Text(text = "View Calender")
                    Text(text = "View Pain")
                }
            }
        }

        @Composable
        fun TaskTracker() {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Task Tracker",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val fontSize = 2.5.em
                        Column (modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Urgent",
                                fontSize= fontSize,
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color.Red, shape = CircleShape)
                            )
                            Text(
                                text = "1",
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Column (modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Warning",
                                fontSize= fontSize,
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color.Yellow, shape = CircleShape)
                            )
                            Text(
                                text = "2",
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Column (modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Upcoming",
                                fontSize= fontSize,
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color.Green, shape = CircleShape)
                            )
                            Text(
                                text = "5",
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Column (modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Completed",
                                fontSize= fontSize,
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color.Gray, shape = CircleShape)
                            )
                            Text(
                                text = "20",
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Show Preview
@Composable
@Preview(showBackground = true)
fun LandingPageFragmentPreview() {
    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LandingPageFragment.BaseContent()
            //LandingPageFragment.Sidebar(0.dp)
        }
    }
}


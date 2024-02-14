package com.example.scheduler.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.zIndex
import com.example.scheduler.MainActivity
import com.example.scheduler.composables.SchedulerNavComposables
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.viewmodels.FragmentViewModel

class LandingPageFragment() : AppFragment {
    // Overrides
    @Composable
    override fun Content() {
        BaseContent(MainActivity.fragmentViewModel)
    }

    // Static Functions
    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BaseContent(
            bottomNavAppBarFragmentViewModel: FragmentViewModel = FragmentViewModel()
        ) {
            val configuration = LocalConfiguration.current
            val density = LocalDensity.current

            val screenWidthInDP = with(density) { configuration.screenWidthDp.dp }
            var resizer by remember { mutableStateOf(-(screenWidthInDP/2)) }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                Scaffold(
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {

                                },
                                onDragEnd = {
                                    if (resizer < -(screenWidthInDP / 4)) {
                                        resizer = -(screenWidthInDP / 2)
                                    } else {
                                        resizer = 0.0f.dp
                                    }
                                }
                            ) { change, dragAmount ->
                                if (change.positionChange() != Offset.Zero) change.consume()
                                resizer += dragAmount.x.dp
                                if (resizer < -(screenWidthInDP / 2)) {
                                    resizer = -(screenWidthInDP / 2)
                                }
                                if (resizer > 0.0f.dp) {
                                    resizer = 0.0f.dp
                                }
                            }
                        },
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
                        SchedulerNavComposables.SchedulerNavBottomBar(bottomNavAppBarFragmentViewModel)
                    }
                    ,
                    content = {
                        Box(modifier = Modifier.padding(it)) {
                            Sidebar(resizer)
                            Column(modifier = Modifier
                                .fillMaxHeight()
                            ) {
                                // Top 3 Tasks
                                UpcomingTasks()
                                // 4 Dot system
                                TaskTracker()
                            }
                        }
                    }
                )
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
        fun UpcomingTasks() {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column (
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Upcoming Tasks")
                    LazyColumn(modifier = Modifier) {
                        items(3) { index ->
                            ListItem(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.tertiary,
                                        shape = RoundedCornerShape(16.dp)
                                    ),

                                headlineContent = {
                                    Text("hC")
                                },
                                overlineContent = {
                                    Text("oC")
                                },
                                supportingContent = {
                                    Text("sC")
                                },
                                leadingContent = {
                                    Text("lC")
                                },
                                trailingContent = {
                                    Text("tC")
                                }
                            )
                        }
                    }
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


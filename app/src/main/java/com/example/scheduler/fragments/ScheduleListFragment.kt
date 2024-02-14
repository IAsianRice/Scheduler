package com.example.scheduler.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import com.example.scheduler.MainActivity
import com.example.scheduler.composables.SchedulerNavComposables
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerMockData.Companion.generateRandomScheduleItems
import com.example.scheduler.viewmodels.FragmentViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ScheduleListFragment() : AppFragment {
    @Composable
    override fun Content() {
        val schedules by MainActivity.databaseViewModel.scheduleItemListStateFlow.collectAsState()
        BaseContent(
            MainActivity.fragmentViewModel,
            schedules
        )
    }
    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BaseContent(
            bottomNavAppBarFragmentViewModel: FragmentViewModel = FragmentViewModel(),
            scheduleItemList: List<ScheduleItem> = generateRandomScheduleItems(10)
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
                            title = { Text(text = "Schedule List") },
                            scrollBehavior = scrollBehavior,
                            actions = {
                                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                            }
                        )
                    },
                    bottomBar = {
                        SchedulerNavComposables.SchedulerNavBottomBar(bottomNavAppBarFragmentViewModel)
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
                        Box(modifier = Modifier.padding(it)) {
                            LazyColumn(modifier = Modifier) {
                                items(scheduleItemList) { item ->
                                    ListItem(
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.tertiary,
                                                shape = RoundedCornerShape(16.dp)
                                            ),

                                        headlineContent = {
                                            Text(item.description)
                                        },
                                        overlineContent = {
                                            Text(item.title)
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }

    }
}

// Show Preview
@Composable
@Preview(showBackground = true)
fun ScheduleListFragmentPreview() {
    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ScheduleListFragment.BaseContent()
            //LandingPageFragment.Sidebar(0.dp)
        }
    }
}

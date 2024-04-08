package com.example.scheduler.fragments

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scheduler.MainActivity
import com.example.scheduler.composables.SchedulerDesignComposables.Companion.HighlightedText
import com.example.scheduler.composables.SchedulerNavComposables
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerMockData.Companion.generateRandomScheduleItems
import com.example.scheduler.viewmodels.FragmentState
import com.example.scheduler.viewmodels.FragmentViewModel
import com.example.scheduler.viewmodels.ScheduleViewModel
class ScheduleListFragment() : AppFragment {
    @Composable
    override fun Content() {
        val schedules by MainActivity.databaseViewModel.scheduleItemListStateFlow.collectAsState()
        BaseContent(
            MainActivity.fragmentViewModel,
            MainActivity.scheduleViewModel,
            schedules
        )
    }
    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BaseContent(
            fragmentViewModel: FragmentViewModel = FragmentViewModel(),
            scheduleViewModel: ScheduleViewModel = ScheduleViewModel(),
            scheduleItemList: List<ScheduleItem> = generateRandomScheduleItems(10)
        ) {
            // Define the target sizes for the expanded and collapsed states
            var expandedSearchBar by remember { mutableStateOf(false) }
            val expandedSize = 75f
            val collapsedSize = 0f

            // States
            var search by remember { mutableStateOf("") }

            // Animate the size based on the current state
            val targetSize = if (expandedSearchBar) expandedSize else collapsedSize
            val size by animateFloatAsState(targetValue = targetSize, label = "Open Searchbar")
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
                            title = { Text(text = "Schedule List")},
                            scrollBehavior = scrollBehavior,
                            actions = {

                                Button(onClick = { expandedSearchBar = !expandedSearchBar }) {
                                    Icon(imageVector = Icons.Filled.Search,
                                        contentDescription = "Search")
                                }

                            }
                        )
                    },
                    bottomBar = {
                        SchedulerNavComposables.SchedulerNavBottomBar(fragmentViewModel)
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                fragmentViewModel.setFragmentState(FragmentState.AddScheduleFragment)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add"
                            )
                        }
                    },
                    content = {
                        Column(modifier = Modifier.padding(it)) {
                            SearchBar(
                                query = search,
                                onQueryChange = {query -> search = query},
                                onSearch = {},
                                active = true,
                                onActiveChange = {},
                                modifier = Modifier.height(size.dp)
                            ) {}
                            LazyColumn(modifier = Modifier) {
                                items(scheduleItemList) { item ->
                                    if (item.title.contains(search)) {
                                        ListItem(
                                            modifier = Modifier
                                                .clickable
                                                {
                                                    scheduleViewModel.setSelectedSchedulerItemState(item)
                                                    fragmentViewModel.setFragmentState(FragmentState.ScheduleDetailsFragment)
                                                },

                                            headlineContent = {
                                                HighlightedText(
                                                    text = item.description,
                                                    highlight = search,
                                                    textColor = MaterialTheme.colorScheme.onTertiary
                                                )
                                            },
                                            overlineContent = {
                                                HighlightedText(
                                                    text = item.title,
                                                    highlight = search,
                                                    textColor = MaterialTheme.colorScheme.onTertiary
                                                )
                                            },
                                            colors = ListItemDefaults.colors(
                                                containerColor = MaterialTheme.colorScheme.tertiary
                                            )
                                        )
                                    }
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

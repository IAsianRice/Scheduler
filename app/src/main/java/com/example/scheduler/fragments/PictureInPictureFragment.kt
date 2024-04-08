package com.example.scheduler.fragments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheduler.MainActivity
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerMockData.Companion.randomizeActiveScheduleItemList
import kotlinx.coroutines.delay
import java.util.Calendar

class PictureInPictureFragment(): AppFragment {
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
            Surface(color = MaterialTheme.colorScheme.background) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Current Schedules",
                        modifier = Modifier,
                        fontSize = 10.sp)
                    AutoScrollingHorizontalPager(modifier = Modifier, content = currentSchedules) {
                        val calendar = Calendar.getInstance()
                        Column(modifier = Modifier
                            .padding(5.dp)
                            .border(
                                BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                            .fillMaxWidth()
                            .clickable {
                                onActiveScheduleItemClicked(currentSchedules[it])
                            },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Column (modifier = Modifier
                                .padding(5.dp)
                            ) {
                                calendar.timeInMillis = currentSchedules[it].startTimeInMillis
                                Text(text = "Schedule Starting: ${
                                    ScheduleDetailsFragment.printDateAndTime(
                                        calendar.time
                                    )
                                }",
                                    modifier = Modifier,
                                    fontSize = 10.sp)
                            }
                        }
                    }
                    Text(text = "Upcoming Schedules",
                        modifier = Modifier,
                        fontSize = 10.sp)
                    AutoScrollingHorizontalPager(modifier = Modifier, content = upcomingSchedules) {
                        val calendar = Calendar.getInstance()
                        Column(modifier = Modifier
                            .padding(5.dp)
                            .border(
                                BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                            .fillMaxWidth()
                            .clickable {
                                onActiveScheduleItemClicked(upcomingSchedules[it])
                            },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Column (modifier = Modifier
                                .padding(5.dp)
                            ) {
                                calendar.timeInMillis = upcomingSchedules[it].startTimeInMillis
                                Text(text = "Schedule Starting: ${
                                    ScheduleDetailsFragment.printDateAndTime(
                                        calendar.time
                                    )
                                }",
                                    modifier = Modifier,
                                    fontSize = 10.sp)
                            }
                        }
                    }
                    /*AutoScrollingLazyRow(modifier = Modifier)
                    {
                        val calendar = Calendar.getInstance()
                        items(upcomingSchedules ?: listOf()) {
                            Column(modifier = Modifier
                                .padding(5.dp)
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                                .clickable {
                                    onActiveScheduleItemClicked(it)
                                }
                            ) {
                                Column (modifier = Modifier
                                    .padding(5.dp)
                                ) {
                                    calendar.timeInMillis = it.startTimeInMillis
                                    Text(text = "Schedule Starting: ${
                                        ScheduleDetailsFragment.printDateAndTime(
                                            calendar.time
                                        )
                                    }",
                                        modifier = Modifier,
                                        fontSize = 10.sp)
                                }
                            }
                        }
                    }*/
                }
            }
        }
        @OptIn(ExperimentalFoundationApi::class)
        @Composable
        fun <T>AutoScrollingHorizontalPager(modifier: Modifier, content: List<T>,  pageContent: @Composable (PagerScope.(Int) -> Unit)) {
            val listState = rememberPagerState(0, pageCount = {content.size})

            // Start the auto-scroll coroutine when the component is first composed
            LaunchedEffect(true) {
                while (true) {
                    // Check if the bottom of the list is reached
                    if (listState.currentPage == listState.pageCount - 1) {
                        // If bottom is reached, reset scroll to the top
                        listState.animateScrollToPage(0)
                    } else {
                        // Scroll down by one item
                        listState.animateScrollToPage(listState.currentPage + 1)
                    }

                    // Delay for a certain duration before scrolling again
                    delay(SCROLL_INTERVAL_MS)


                }
            }
            HorizontalPager(modifier = modifier,
                state = listState,
                pageContent = pageContent,
                verticalAlignment = Alignment.CenterVertically
                )
        }
        @Composable
        fun AutoScrollingLazyRow(modifier: Modifier, content: LazyListScope.() -> Unit) {
            val listState = rememberLazyListState()

            // Start the auto-scroll coroutine when the component is first composed
            LaunchedEffect(true) {
                while (true) {
                    // Check if the bottom of the list is reached
                    if (listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
                        == listState.layoutInfo.totalItemsCount) {
                        // If bottom is reached, reset scroll to the top
                        listState.animateScrollToItem(
                            index = 0
                        )
                    } else {
                        // Scroll down by one item
                        listState.animateScrollToItem(
                            index = listState.firstVisibleItemIndex + 1
                        )
                    }

                    // Delay for a certain duration before scrolling again
                    delay(SCROLL_INTERVAL_MS)


                }
            }

            LazyRow(modifier = modifier, state = listState, content = content)
        }

        @Composable
        fun AutoScrollingLazyColumn(modifier: Modifier, content: LazyListScope.() -> Unit) {
            val listState = rememberLazyListState()

            // Start the auto-scroll coroutine when the component is first composed
            LaunchedEffect(true) {
                while (true) {
                    // Scroll down by one item
                    listState.animateScrollToItem(
                        index = listState.firstVisibleItemIndex + 1
                    )
                    // Delay for a certain duration before scrolling again
                    delay(SCROLL_INTERVAL_MS)

                    // Check if the bottom of the list is reached
                    if (listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
                        == listState.layoutInfo.totalItemsCount) {
                        // If bottom is reached, reset scroll to the top
                        listState.animateScrollToItem(
                            index = 0
                        )
                    }
                }
            }

            LazyColumn(modifier = modifier, state = listState, content = content)
        }

    }
}

@Composable
@Preview(showBackground = true,)
fun PictureInPictureFragmentPreview() {
    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PictureInPictureFragment.BaseContent()
        }
    }
}
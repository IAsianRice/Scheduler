package com.example.scheduler.fragments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheduler.MainActivity
import com.example.scheduler.composables.SchedulerNavComposables
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.ActiveScheduleQuotaCrossRef
import com.example.scheduler.database.entities.QuotaItem
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerMockData.Companion.generateRandomUniqueActiveScheduleItems
import com.example.scheduler.utility.SchedulerMockData.Companion.generateRandomUniqueActiveScheduleQuotaCrossRefs
import com.example.scheduler.utility.SchedulerMockData.Companion.generateRandomUniqueQuotas
import com.example.scheduler.utility.SchedulerMockData.Companion.generateRandomUniqueScheduleItems
import com.example.scheduler.viewmodels.FragmentViewModel
import kotlinx.coroutines.delay
import java.util.Calendar

class ActiveScheduleFragment() : AppFragment {
    // Overrides
    @Composable
    override fun Content() {
        val scheduleItemMap by MainActivity.databaseViewModel.scheduleItemMapStateFlow.collectAsState()
        val activeScheduleItemListStateFlow by MainActivity.databaseViewModel.currentSchedulesStateFlow.collectAsState()
        val quotaItems by MainActivity.databaseViewModel.quotasListStateFlow.collectAsState()
        val activeScheduleQuotaCrossRefs by MainActivity.databaseViewModel.activeScheduleQuotaCrossRefsListStateFlow.collectAsState()
        BaseContent(
            fragmentViewModel = MainActivity.fragmentViewModel,
            currentActiveScheduleItems = activeScheduleItemListStateFlow,
            scheduleItemMap = scheduleItemMap,
            quotaItemMap = quotaItems.associateBy { it.quotaId },
            activeScheduleQuotaCrossRefs = activeScheduleQuotaCrossRefs.groupBy { it.activeScheduleId },
            updateActiveScheduleQuotaCrossRef = {MainActivity.databaseViewModel.updateActiveScheduleQuotaCrossRef(it)}
            )
    }

    // Static Functions
    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BaseContent(
            fragmentViewModel: FragmentViewModel = FragmentViewModel(),
            currentActiveScheduleItems: List<ActiveScheduleItem> = generateRandomUniqueActiveScheduleItems(10),
            activeScheduleQuotaCrossRefs: Map<Long, List<ActiveScheduleQuotaCrossRef>> = generateRandomUniqueActiveScheduleQuotaCrossRefs(10,3).groupBy { it.activeScheduleId },
            quotaItemMap: Map<Long, QuotaItem> = generateRandomUniqueQuotas(10).associateBy { it.quotaId },
            scheduleItemMap: Map<Long, ScheduleItem> = generateRandomUniqueScheduleItems(10).associateBy { it.scheduleId },
            updateActiveScheduleQuotaCrossRef: (ActiveScheduleQuotaCrossRef) -> Unit = {}
            ) {
            // MutableState to hold remaining time
            var currentTime by remember { mutableStateOf(Calendar.getInstance().timeInMillis) } // Initial time: 60 seconds

            // Update remaining time every second
            LaunchedEffect(Unit) {
                while (true) {
                    delay(1000) // Wait for 1 second
                    currentTime = Calendar.getInstance().timeInMillis // Decrease remaining time
                }
            }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                Scaffold(
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Active Schedule") },
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
                            LazyColumn(
                                modifier = Modifier.padding(3.dp)
                            ) {
                                items(currentActiveScheduleItems) {
                                    Column (modifier = Modifier
                                        .border(
                                            BorderStroke(
                                                2.dp,
                                                MaterialTheme.colorScheme.primaryContainer
                                            ), MaterialTheme.shapes.large
                                        )
                                        .padding(5.dp)
                                    ) {
                                        androidx.compose.material3.ListItem(
                                            headlineContent = {
                                                Text(
                                                    fontSize = 30.sp,
                                                    text = scheduleItemMap[it.scheduleID]?.title ?: "Title"
                                                )
                                            },
                                            supportingContent = {
                                                Text(
                                                    fontSize = 15.sp,
                                                    text = scheduleItemMap[it.scheduleID]?.description ?: "Description"
                                                )
                                            },
                                            trailingContent = {
                                                Text(
                                                    fontSize = 15.sp,
                                                    text = "${((it.startTimeInMillis+it.durationInMillis) - currentTime)}"
                                                )
                                            }
                                        )


                                        Column{
                                            for (i in (activeScheduleQuotaCrossRefs[it.activeScheduleId] ?: listOf()).sortedBy { it.quotaFinished/it.quotaNeeded })
                                            {
                                                androidx.compose.material3.ListItem(
                                                    headlineContent = {
                                                        Text(text = quotaItemMap[i.quotaId]?.title ?: "Title",
                                                            color = if(i.quotaFinished >= i.quotaNeeded) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary)
                                                    },
                                                    trailingContent = {
                                                        Row {
                                                            IconButton(onClick = {
                                                                if(i.quotaFinished - 1 >= 0)
                                                                {
                                                                    updateActiveScheduleQuotaCrossRef(
                                                                        ActiveScheduleQuotaCrossRef(
                                                                            activeScheduleId = i.activeScheduleId,
                                                                            quotaId = i.quotaId,
                                                                            quotaNeeded = i.quotaNeeded,
                                                                            quotaFinished = i.quotaFinished - 1
                                                                        )
                                                                    )
                                                                }
                                                            }) {
                                                                Icon(
                                                                    imageVector = Icons.Default.KeyboardArrowLeft,
                                                                    contentDescription = "Subtract",
                                                                    tint = if(i.quotaFinished >= i.quotaNeeded) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary
                                                                )
                                                            }
                                                            Text(text = "${i.quotaFinished}/${i.quotaNeeded}",
                                                                color = if(i.quotaFinished >= i.quotaNeeded) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary)
                                                            IconButton(onClick = {
                                                                if(i.quotaFinished + 1 <= i.quotaNeeded)
                                                                {
                                                                    updateActiveScheduleQuotaCrossRef(
                                                                        ActiveScheduleQuotaCrossRef(
                                                                            activeScheduleId = i.activeScheduleId,
                                                                            quotaId = i.quotaId,
                                                                            quotaNeeded = i.quotaNeeded,
                                                                            quotaFinished = i.quotaFinished + 1
                                                                        )
                                                                    )
                                                                }

                                                            }) {
                                                                Icon(
                                                                    imageVector = Icons.Default.KeyboardArrowRight,
                                                                    contentDescription = "Add",
                                                                    tint = if(i.quotaFinished >= i.quotaNeeded) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary
                                                                )
                                                            }
                                                        }
                                                    },
                                                    colors = ListItemDefaults.colors(
                                                        containerColor = if(i.quotaFinished >= i.quotaNeeded) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
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
fun ActiveScheduleFragmentPreview() {
    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ActiveScheduleFragment.BaseContent()
            //LandingPageFragment.Sidebar(0.dp)
        }
    }
}


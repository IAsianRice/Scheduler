package com.example.scheduler.fragments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scheduler.MainActivity
import com.example.scheduler.composables.SchedulerNavComposables
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.viewmodels.FragmentViewModel

class UnimplementedFragment() : AppFragment {
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
                            title = { Text(text = "Unimplemented") },
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
                                Text(text = "This Feature has not been implemented yet. It will come in a later update!")
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
fun UnimplementedFragmentPreview() {
    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            UnimplementedFragment.BaseContent()
            //LandingPageFragment.Sidebar(0.dp)
        }
    }
}


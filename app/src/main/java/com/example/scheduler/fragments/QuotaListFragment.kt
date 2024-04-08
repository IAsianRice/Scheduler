package com.example.scheduler.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheduler.MainActivity
import com.example.scheduler.composables.SchedulerDesignComposables.Companion.HighlightedText
import com.example.scheduler.composables.SchedulerNavComposables
import com.example.scheduler.database.entities.QuotaItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerMockData.Companion.generateRandomUniqueQuotas
import com.example.scheduler.viewmodels.FragmentViewModel

class QuotaListFragment() : AppFragment {
    // Overrides
    @Composable
    override fun Content() {
        val selectedQuotaItems by MainActivity.scheduleViewModel.selectedQuotas.collectAsState()
        val quotaItems by MainActivity.databaseViewModel.quotasListStateFlow.collectAsState()
        BaseContent(
            fragmentViewModel = MainActivity.fragmentViewModel,
            quotaItems = quotaItems.filterNot { selectedQuotaItems.contains(it) },
            selectedQuotaItems = selectedQuotaItems,
            quotaItemSelected = {
                MainActivity.scheduleViewModel.setSelectedQuotas(selectedQuotaItems + listOf(it))
            }
        )
    }

    // Static Functions
    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BaseContent(
            fragmentViewModel: FragmentViewModel = FragmentViewModel(),
            quotaItems: List<QuotaItem> = generateRandomUniqueQuotas(30),
            selectedQuotaItems: List<QuotaItem> = generateRandomUniqueQuotas(3),
            quotaItemSelected: (QuotaItem) -> Unit = {}) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                var search by remember { mutableStateOf("") }
                Scaffold(
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Select Quotas") },
                            scrollBehavior = scrollBehavior,
                            actions = {
                            }
                        )
                    },
                    bottomBar = {
                        SchedulerNavComposables.SchedulerNavBottomBar(fragmentViewModel)
                    }
                    ,
                    content = {
                        Box(modifier = Modifier.padding(it)) {
                            TextField(value = search,
                                onValueChange = {search = it},
                                placeholder = { Text(text = "Search")},
                                modifier = Modifier
                                    .padding(3.dp)
                                    .height(50.dp)
                                    .fillMaxWidth())
                            LazyColumn(
                                modifier = Modifier
                                    .padding(3.dp)
                                    .offset(y = 50.dp)
                            ) {
                                items(selectedQuotaItems.filter { it.title.contains(search) }) {
                                    ListItem(
                                        modifier = Modifier.clickable {
                                            quotaItemSelected(it)
                                        },
                                        headlineContent = {
                                            HighlightedText(it.title, search,
                                                style = TextStyle(
                                                    fontSize = 18.sp,
                                                    color = MaterialTheme.colorScheme.onSecondary
                                                )
                                            )
                                            //Text(text = it.title)
                                        },
                                        supportingContent = {
                                            //HighlightedText(it.description, "i")
                                            Text(text = it.description,
                                                color = MaterialTheme.colorScheme.onSecondary)
                                        },
                                        colors = ListItemDefaults.colors(
                                            containerColor = MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                }
                                items(quotaItems.filter { it.title.contains(search) }) {
                                    ListItem(
                                        modifier = Modifier.clickable {
                                            quotaItemSelected(it)
                                        },
                                        headlineContent = {
                                            HighlightedText(it.title, search,
                                                style = TextStyle(
                                                    fontSize = 18.sp,
                                                    color = MaterialTheme.colorScheme.onPrimary
                                                )
                                            )
                                        },
                                        supportingContent = {
                                            Text(text = it.description,
                                                color = MaterialTheme.colorScheme.onPrimary)
                                        },
                                        colors = ListItemDefaults.colors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
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
fun QuotaListFragmentPreview() {
    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            QuotaListFragment.BaseContent()
            //LandingPageFragment.Sidebar(0.dp)
        }
    }
}


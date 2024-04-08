package com.example.scheduler.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.scheduler.viewmodels.FragmentState
import com.example.scheduler.viewmodels.FragmentViewModel

/** Not Abstracted at All!
 *  Very Software Specific Code Presented here!
 */


class SchedulerNavComposables {
    companion object {
        @Composable
        fun <T> ChoiceBar(
            values: List<T>,
            initalValue: T,
            onValueChange: (T) -> Unit
        ) {
            var rememList by remember { mutableStateOf<T>(initalValue) }
            Row(modifier = Modifier.fillMaxWidth()) {
                for (element in values) {
                    Column(modifier = Modifier
                        .weight(1.0f)
                        .height(60.dp)
                        .clickable {
                            rememList = element
                            onValueChange(element)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Text(text = "$element")
                        if (rememList == element) {
                            Divider(
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 4.dp,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }
                    }
                }
            }
        }

        @Composable
        fun SchedulerNavBottomBar(fragmentViewModel: FragmentViewModel = FragmentViewModel()) {
            val selected by fragmentViewModel.fragmentStateFlow.collectAsState()
            BottomAppBar() {
                Row (
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column(modifier = Modifier
                        .weight(1.0f)
                        .fillMaxHeight()
                        .clickable {
                            fragmentViewModel.setFragmentState(FragmentState.LandingPageFragment)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home")
                        Text(text = "Home")
                        if (selected == FragmentState.LandingPageFragment) {
                            Divider(
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 4.dp,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }

                    }
                    Column(modifier = Modifier
                        .weight(1.0f)
                        .fillMaxHeight()
                        .clickable {
                            fragmentViewModel.setFragmentState(FragmentState.ScheduleListFragment)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Icon(imageVector = Icons.Outlined.List, contentDescription = "List")
                        Text(text = "List")
                        if (selected == FragmentState.ScheduleListFragment) {
                            Divider(
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 4.dp,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }
                    }
                    Column(modifier = Modifier
                        .weight(1.0f)
                        .fillMaxHeight()
                        .clickable {
                            fragmentViewModel.setFragmentState(FragmentState.TasksFragment)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "")
                        Text(text = "Tasks")
                        if (selected == FragmentState.TasksFragment) {
                            Divider(
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 4.dp,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }
                    }
                    Column(modifier = Modifier
                        .weight(1.0f)
                        .fillMaxHeight()
                        .clickable {
                            fragmentViewModel.setFragmentState(FragmentState.UnimplementedFragment)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Icon(imageVector = Icons.Outlined.Star, contentDescription = "Stats")
                        Text(text = "Stats")
                        if (selected == FragmentState.UnimplementedFragment) {
                            Divider(
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 4.dp,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }
                    }
                    Column(modifier = Modifier
                        .weight(1.0f)
                        .fillMaxHeight()
                        .clickable {
                            fragmentViewModel.setFragmentState(FragmentState.CalendarFragment)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "Calender")
                        Text(text = "Calender")
                        if (selected == FragmentState.CalendarFragment) {
                            Divider(
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 4.dp,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}
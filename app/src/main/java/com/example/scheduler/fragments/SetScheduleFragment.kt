package com.example.scheduler.fragments

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scheduler.MainActivity
import com.example.scheduler.composables.SchedulerDesignComposables
import com.example.scheduler.composables.SchedulerDesignComposables.Companion.CardColumn
import com.example.scheduler.composables.SchedulerFieldComposables.Companion.NumberTextField
import com.example.scheduler.composables.SchedulerModalComposables
import com.example.scheduler.composables.SchedulerModalComposables.Companion.CustomModal
import com.example.scheduler.composables.SchedulerPickerComposables.Companion.DateRangePickerModal
import com.example.scheduler.composables.SchedulerPickerComposables.Companion.TimePickerModal
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.QuotaItem
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerMockData.Companion.generateRandomQuotas
import com.example.scheduler.utility.SchedulerMockData.Companion.generateRandomScheduleItems
import com.example.scheduler.utility.SchedulerTimeFunctions.Companion.formatTime
import java.util.Calendar
import java.util.TimeZone

class SetScheduleFragment(): AppFragment {
    @Composable
    override fun Content() {
        // Collect Lists from database ViewModel
        val scheduleItemsList by MainActivity.databaseViewModel.scheduleItemListStateFlow.collectAsState()
        val quotaItemsList by MainActivity.databaseViewModel.quotasListStateFlow.collectAsState()

        // Selected Schedule Item to be moved
        var selectedScheduleItem by remember { mutableStateOf<ScheduleItem?>(null) }

        BaseContent(
            scheduleItems = scheduleItemsList,
            quotaItems = quotaItemsList.associateBy { it.quotaId },
            selectedScheduleItem = selectedScheduleItem,
            onScheduleItemChanged = {selectedScheduleItem = it},
            onSubmit = { s, e, qs ->
                Log.d("SubmitSetSSSSSSChed", "${e - s}")
                MainActivity.databaseViewModel.addActiveScheduleItem(
                    ActiveScheduleItem(
                        scheduleID = selectedScheduleItem!!.scheduleId,
                        repeatFlag = 0,
                        startTimeInMillis = s,
                        durationInMillis = e - s
                    ),
                    convertMap(qs, quotaItemsList)
                )
                MainActivity.fragmentViewModel.back()
            }
        )
    }

    private fun convertMap(map: Map<Long, Int>, quotaItemList: List<QuotaItem>): Map<QuotaItem, Int> {
        val convertedMap = mutableMapOf<QuotaItem, Int>()

        for ((key, value) in map) {
            val quotaItem = quotaItemList.find { it.quotaId == key }
            if (quotaItem != null) {
                convertedMap[quotaItem] = value
            }
        }

        return convertedMap
    }


    companion object {
        // maybe move to a different location
        sealed class ScheduleRepetition {
            object OneTime : ScheduleRepetition() {
                override fun toString(): String {
                    return "One Time"
                }
            }
            object Daily : ScheduleRepetition() {
                override fun toString(): String {
                    return "Daily"
                }
            }
            object Weekly : ScheduleRepetition() {
                override fun toString(): String {
                    return "Weekly"
                }
            }
            object Monthly : ScheduleRepetition() {
                override fun toString(): String {
                    return "Monthly"
                }
            }
            object Yearly : ScheduleRepetition() {
                override fun toString(): String {
                    return "Yearly"
                }
            }
        }
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BaseContent(
            onSubmit: (startMillis: Long, endMillis: Long, quotaItems: Map<Long,Int>) -> Unit = {_,_,_ ->},
            scheduleItems: List<ScheduleItem> = generateRandomScheduleItems(10),
            quotaItems: Map<Long, QuotaItem> = generateRandomQuotas(3).associateBy { it.quotaId },
            selectedScheduleItem: ScheduleItem? = null,
            onScheduleItemChanged: (ScheduleItem) -> Unit = {},
            ) {
            // States
            val dateRangePickerState = rememberDateRangePickerState()
            val startTimePickerState = rememberTimePickerState()
            val endTimePickerState = rememberTimePickerState()
            // Temporary Calendar
            val calendar = Calendar.getInstance()
            // remember states
            var errorText by remember { mutableStateOf("")}
            var hasSelectedSchedule by remember { mutableStateOf(false)}
            var search by remember { mutableStateOf("") }
            var showDateRangeDialog by remember { mutableStateOf(false) }
            var showStartTimeDialog by remember { mutableStateOf(false) }
            var showEndTimeDialog by remember { mutableStateOf(false) }
            var showQuotaDialog by remember { mutableStateOf(false) }
            var showSelectScheduleDialog by remember { mutableStateOf(false) }
            var quotaIdList by remember { mutableStateOf(quotaItems.keys.toList())}
            var selectedQuotaIdMap by remember { mutableStateOf(mapOf<Long,Int>())}
            // Revamp

            // Get the default time zone of the device
            val localTimeZone = TimeZone.getDefault()


                //Log.d("LaunchedEffect(dateRangePickerState)","${dateRangePickerState.selectedStartDateMillis}:${dateRangePickerState.selectedEndDateMillis}")
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Set Schedule") },
                        )
                    }
                    ,
                    content = {
                        Column(modifier = Modifier.padding(it)) {
                            var scheduleType by remember { mutableStateOf<AddScheduleFragment.Companion.ScheduleType>(
                                AddScheduleFragment.Companion.ScheduleType.Schedule) }

                            /*ChoiceBar(
                                listOf(AddScheduleFragment.Companion.ScheduleType.Schedule, AddScheduleFragment.Companion.ScheduleType.Quota),
                                AddScheduleFragment.Companion.ScheduleType.Schedule) { it ->
                                scheduleType = it
                            }
                            /*var yearRange by remember { mutableStateOf(false) }
                            var monthRange by remember { mutableStateOf(false) }
                            var dayRange by remember { mutableStateOf(false) }*/

                            var scheduleRepetition by remember { mutableStateOf<ScheduleRepetition>(
                                ScheduleRepetition.OneTime) }
                            ChoiceBar(
                                values = listOf(
                                    ScheduleRepetition.OneTime,
                                    ScheduleRepetition.Daily,
                                    ScheduleRepetition.Weekly,
                                    ScheduleRepetition.Monthly,
                                    ScheduleRepetition.Yearly,),
                                initalValue = scheduleRepetition,
                                onValueChange = {scheduleRepetition = it}
                            )*/
                            // Select Time
                            CardColumn() {
                                Button(onClick = {showDateRangeDialog = true}) {
                                    Text(text = "Select Date Range")
                                }


                                Row (modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround){
                                    Column (modifier = Modifier
                                        .weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally)
                                    {
                                        Text(text = "From")
                                        calendar.timeInMillis = (dateRangePickerState.selectedStartDateMillis ?: 0) +
                                                -localTimeZone.getOffset((dateRangePickerState.selectedStartDateMillis ?: 0)).toLong()
                                        Text(text = ScheduleDetailsFragment.printDate(calendar.time))
                                        Text(text = formatTime(startTimePickerState.hour,startTimePickerState.minute))
                                        Button(onClick = {
                                            showStartTimeDialog = true
                                        }) {
                                            Text(text = "Select Start Time")
                                        }
                                    }

                                    Column (modifier = Modifier
                                        .weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally)
                                    {
                                        Text(text = "To")
                                        //Text(text = "${localTimeZone.getOffset((dateRangePickerState.selectedEndDateMillis ?: 0)).toLong()}")
                                        calendar.timeInMillis = (dateRangePickerState.selectedEndDateMillis  ?: 0) +
                                                -localTimeZone.getOffset((dateRangePickerState.selectedEndDateMillis ?: 0)).toLong()
                                        Text(text = ScheduleDetailsFragment.printDate(calendar.time))
                                        Text(text = formatTime(endTimePickerState.hour,endTimePickerState.minute))
                                        Button(onClick = {
                                            showEndTimeDialog = true
                                        }) {
                                            Text(text = "Select End Time")
                                        }
                                    }
                                }
                            }
                            // Time Picker Modals
                            TimePickerModal(showDialog = showStartTimeDialog,
                                onDismiss = {showStartTimeDialog = false},
                                timePickerState = startTimePickerState)
                            TimePickerModal(showDialog = showEndTimeDialog,
                                onDismiss = {showEndTimeDialog = false},
                                timePickerState = endTimePickerState)
                            DateRangePickerModal( showDialog = showDateRangeDialog,
                                onDismiss = {showDateRangeDialog = false},
                                dateRangePickerState = dateRangePickerState
                            )
                            // Select Schedule
                            CardColumn(modifier = Modifier.fillMaxWidth(),
                                borderColor = if (hasSelectedSchedule) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.errorContainer
                            ) {
                                Button(onClick = { showSelectScheduleDialog = true }) {
                                    Text(text = "Select Schedule")
                                    Log.d("Select Schedule", "${scheduleItems.size}")
                                }
                                Text(text = selectedScheduleItem?.title ?: "[Schedule Title]")
                                Text(
                                    text = selectedScheduleItem?.description
                                        ?: "[Schedule Description]"
                                )
                            }
                            // Select Schedule Modal
                            CustomModal(
                                showDialog = showSelectScheduleDialog,
                                onDismiss = {showSelectScheduleDialog = false},
                            )
                            {
                                SearchBar(
                                    query = search,
                                    onQueryChange = {query -> search = query},
                                    onSearch = {},
                                    active = true,
                                    onActiveChange = {},
                                    modifier = Modifier.height(75.dp)
                                ) {}
                                LazyColumn(modifier = Modifier) {
                                    items(scheduleItems) { item ->
                                        if (item.title.contains(search)) {
                                            ListItem(
                                                modifier = Modifier
                                                    .clickable
                                                    {
                                                        onScheduleItemChanged(item)
                                                        it()

                                                    },

                                                headlineContent = {
                                                    SchedulerDesignComposables.HighlightedText(
                                                        text = item.description,
                                                        highlight = search,
                                                        textColor = MaterialTheme.colorScheme.onTertiary
                                                    )
                                                },
                                                overlineContent = {
                                                    SchedulerDesignComposables.HighlightedText(
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


                            // Select Quotas
                            CardColumn() {
                                Row(
                                    modifier = Modifier.fillMaxWidth(1f),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Add Quota")
                                    Button(onClick = { showQuotaDialog = true }) {
                                        Icon(
                                            imageVector = Icons.Filled.Add,
                                            contentDescription = "Add a Quota"
                                        )
                                    }
                                }
                                LazyColumn() {
                                    items(selectedQuotaIdMap.keys.toList()) { id ->
                                        var quotaNumber by remember { mutableStateOf(0)}
                                        ListItem(
                                            modifier = Modifier
                                                .background(
                                                    MaterialTheme.colorScheme.tertiary,
                                                    shape = RoundedCornerShape(16.dp)
                                                ),

                                            headlineContent = {
                                                Text(quotaItems[id]?.description ?: "Description")
                                            },
                                            overlineContent = {
                                                Text(quotaItems[id]?.title ?: "Title")
                                            },
                                            trailingContent = {
                                                NumberTextField(
                                                    value = quotaNumber,
                                                    onValueChange =
                                                    {
                                                        quotaNumber = it
                                                    },
                                                    modifier = Modifier.width(100.dp)
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                            // Quota Modal
                            SchedulerModalComposables.SearchableMultiChoiceModalDialog(
                                showDialog = showQuotaDialog,
                                onDismiss = {showQuotaDialog = false},
                                contentList = quotaIdList,
                                selectedList = selectedQuotaIdMap.keys.toList(),
                                title = {
                                    Text(text = "Just a little Selection Test!")
                                },
                                onContentListUpdated =
                                {
                                    quotaIdList = quotaIdList - listOf(it)
                                    var map = selectedQuotaIdMap.toMutableMap()
                                    map[it] = 0
                                    selectedQuotaIdMap = map
                                },
                                onSelectedListUpdated =
                                {
                                    quotaIdList = quotaIdList + listOf(it)
                                    var map = selectedQuotaIdMap.toMutableMap()
                                    map.remove(it)
                                    selectedQuotaIdMap = map
                                },
                                searchPredicate = {item, search -> quotaItems[item]?.title?.contains(search) ?: false},
                                contentLayout = {
                                    Column(
                                        modifier = Modifier
                                            .border(
                                                BorderStroke(1.dp, Color.Black),
                                                CircleShape
                                            )
                                            .background(Color.Gray, CircleShape)
                                    ) {
                                        Text(modifier = Modifier.padding(10.dp), text = quotaItems[it]?.title ?: "Title")
                                    }
                                })
                            // Submit Button
                            Button(
                                onClick =
                                {
                                    //
                                    val startCalendar = Calendar.getInstance()
                                    val endCalendar = Calendar.getInstance()
                                    startCalendar.timeInMillis = (dateRangePickerState.selectedStartDateMillis ?: 0L) - localTimeZone.getOffset((dateRangePickerState.selectedStartDateMillis ?: 0)).toLong()
                                    endCalendar.timeInMillis = (dateRangePickerState.selectedEndDateMillis ?: 0L) - localTimeZone.getOffset((dateRangePickerState.selectedEndDateMillis ?: 0)).toLong()

                                    startCalendar.set(Calendar.HOUR_OF_DAY, startTimePickerState.hour)
                                    startCalendar.set(Calendar.MINUTE, startTimePickerState.minute)
                                    startCalendar.set(Calendar.SECOND, 0)
                                    startCalendar.set(Calendar.MILLISECOND, 0)

                                    endCalendar.set(Calendar.HOUR_OF_DAY, endTimePickerState.hour)
                                    endCalendar.set(Calendar.MINUTE, endTimePickerState.minute)
                                    endCalendar.set(Calendar.SECOND, 0)
                                    endCalendar.set(Calendar.MILLISECOND, 0)

                                    if (selectedScheduleItem == null)
                                    {
                                        errorText = "Please Select a Schedule Item"
                                    }
                                    else
                                    {
                                        onSubmit(startCalendar.timeInMillis, endCalendar.timeInMillis, selectedQuotaIdMap)
                                    }

                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text(text = "Submit", modifier = Modifier.padding(8.dp))
                            }

                            Text(text = errorText, color = MaterialTheme.colorScheme.onError)
                        }
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true,)
fun SetScheduleFragmentPreview() {

    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SetScheduleFragment.BaseContent()
        }
    }
}


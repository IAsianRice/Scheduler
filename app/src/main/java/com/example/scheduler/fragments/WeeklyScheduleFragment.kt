package com.example.scheduler.fragments

import android.text.format.DateUtils.MINUTE_IN_MILLIS
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scheduler.MainActivity
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerHelperFunctions.Companion.pxToDp
import com.example.scheduler.utility.SchedulerMockData.Companion.randomizeActiveScheduleItemList
import com.example.scheduler.viewmodels.FragmentState
import com.example.scheduler.viewmodels.ScheduleViewModel
import java.util.Calendar

class WeeklyScheduleFragment() : AppFragment {
    @Composable
    override fun Content() {
        val activeSchedules by MainActivity.scheduleViewModel.selectedDayActiveScheduleItems.collectAsState()
        val scheduleItems by MainActivity.databaseViewModel.scheduleItemListStateFlow.collectAsState()

        BaseContent(scheduleViewModel = MainActivity.scheduleViewModel,
            activeScheduleItems = activeSchedules ?: listOf(),
            onSetScheduleClicked = {
                MainActivity.fragmentViewModel.setFragmentState(FragmentState.SetScheduleFragment)
            }
        )
    }

    companion object {
        @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
        @Composable
        fun BaseContent(
            scheduleViewModel: ScheduleViewModel = ScheduleViewModel(),
            activeScheduleItems: List<ActiveScheduleItem> = randomizeActiveScheduleItemList(20),
            onSetScheduleClicked: () -> Unit = {}
        ) {
            var activeScheduleItemsSorted = activeScheduleItems.sortedBy { it.startTimeInMillis }

            var scheduleColumns = mutableListOf<MutableList<ActiveScheduleItem>>()
            val items = (1..7).toList()
            //
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            //
            var weekTitle by remember { mutableStateOf("This week's schedule") }
            //Log.d("WeeklyScheduleFragment", "Current Day: ${scheduleViewModel.currentDay.timeInMillis}")
            //Log.d("WeeklyScheduleFragment", "Selected Day: ${scheduleViewModel.selectedDay.timeInMillis}")
            val HOURS = 24
            val MINUTE_LIST = listOf("00","15","30","45")
            // Keep track of pages to adapt to changes
            // Compose UI
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(
                        title = { Text(text = weekTitle) },
                        scrollBehavior = scrollBehavior,
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = onSetScheduleClicked
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                    }
                },
                content = {
                    Column(modifier = Modifier.padding(it))
                    {

                        val scrollState = rememberScrollState()
                        var selectedIndex by remember { mutableStateOf(-1) }
                        val swipeDirection = remember { mutableStateOf(0.0f) }
                        var selectedScheduleItem: ActiveScheduleItem? = if (selectedIndex < 0) {
                            null
                        } else {
                            activeScheduleItems[selectedIndex]
                        }
                        val visibleSchedules = 30
                        val dragThresh = 50.0f
                        //selectedIndex = if (swipeDirection.value != 0.0f) (swipeDirection.value / dragThresh).roundToInt() else 0
                        //if (selectedIndex >= activeScheduleItems.size) {selectedIndex = activeScheduleItems.size - 1}
                        val maxDrag = visibleSchedules * dragThresh
                        val selectedScheduleCalStart = Calendar.getInstance()
                        val selectedScheduleCalEnd = Calendar.getInstance()
                        selectedScheduleCalStart.timeInMillis = selectedScheduleItem?.startTimeInMillis ?: 0
                        selectedScheduleCalEnd.timeInMillis = (selectedScheduleItem?.startTimeInMillis ?: 0) + (selectedScheduleItem?.durationInMillis ?: 0)



                        // The Day Info
                        Column(modifier = Modifier.height(200.dp)) {
                            Text(text = "Schedule Data")
                            Text(
                                text = "${selectedScheduleItem?.scheduleID ?: "No Selected Schedule"}"
                            )
                            Text(text = "Start Time: ${selectedScheduleCalStart.time}",
                            )
                            Text(text = "End Time: ${selectedScheduleCalEnd.time}",
                                )
                            //Text(text = "Scroll: ${scrollState.value}" )
                        }


                        Column(modifier = Modifier
                            .fillMaxWidth(),
                            horizontalAlignment = Alignment.End)
                        {
                            Text(text = "Scroll More ->")
                        }
                        // TimeLine
                        Box(modifier =
                            Modifier.verticalScroll(scrollState)
                        ) {

                            // Time Ticks
                            Column (modifier = Modifier
                                .fillMaxWidth()) {

                                for (hour in (0..<HOURS))
                                {
                                    Row {
                                        Text(text = "$hour:")
                                        Column(modifier = Modifier
                                            .height(100.dp)) {
                                            for (minute in MINUTE_LIST)
                                            {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(25.dp)
                                                )
                                                {
                                                    Text(text = "$minute")
                                                    Divider(thickness = 1.dp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            var height by remember { mutableStateOf(0.dp) }
                            // Schedules
                            Row (modifier = Modifier
                                .padding(start = 50.dp)
                                /*.pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        swipeDirection.value -= dragAmount.x
                                        if (swipeDirection.value > maxDrag) {
                                            swipeDirection.value = maxDrag
                                        } else if (swipeDirection.value < 0) {
                                            swipeDirection.value = 0.0f
                                        }
                                    }
                                }

                                 */
                                .horizontalScroll(rememberScrollState())
                                .onGloballyPositioned { layoutCoordinates ->
                                    // Get the height of the Box
                                    height = layoutCoordinates.size.height.dp
                                }
                            ){
                                // Loop through all necessary schedules
                                activeScheduleItemsSorted.forEachIndexed { index, activeScheduleItem ->
                                    val scheduleCalStart = Calendar.getInstance()
                                    val scheduleCalEnd = Calendar.getInstance()
                                    scheduleCalStart.timeInMillis = activeScheduleItem.startTimeInMillis
                                    scheduleCalEnd.timeInMillis = activeScheduleItem.startTimeInMillis + activeScheduleItem.durationInMillis
                                    var offsetBeforeDp = (100 * scheduleCalStart.get(Calendar.HOUR_OF_DAY)) + (100.0f * (scheduleCalStart.get(Calendar.MINUTE) / 60.0f))
                                    var durationInMinutes = activeScheduleItem.durationInMillis / MINUTE_IN_MILLIS
                                    if(scheduleCalStart.timeInMillis < scheduleViewModel.getSelectedDayCalendar().timeInMillis) {
                                        offsetBeforeDp = 0.0f
                                        durationInMinutes = (activeScheduleItem.durationInMillis - (scheduleViewModel.getSelectedDayCalendar().timeInMillis - activeScheduleItem.startTimeInMillis)) / MINUTE_IN_MILLIS
                                    }
                                    val maxHeightBeforeDp = 25.0f * MINUTE_LIST.size * HOURS
                                    var heightBeforeDp = 100.0f * (durationInMinutes / 60.0f)
                                    val isSelected = (selectedIndex == index)

                                    if(offsetBeforeDp + heightBeforeDp > maxHeightBeforeDp) {
                                        val excess = heightBeforeDp - (maxHeightBeforeDp - offsetBeforeDp)
                                        heightBeforeDp -= excess
                                    }

                                    if ((offsetBeforeDp + heightBeforeDp).dp > scrollState.value.pxToDp()
                                        && (offsetBeforeDp).dp < 500.dp + scrollState.value.pxToDp())
                                    {
                                        Column(modifier = Modifier
                                            //.weight(if (isSelected) 1.0f else 1.0f / visibleSchedules)
                                            .width(if (isSelected) 150.dp else 30.dp)
                                            .padding(start = 1.dp)
                                            .offset(y = offsetBeforeDp.dp)
                                            .height(heightBeforeDp.dp)
                                            .background(MaterialTheme.colorScheme.primary)
                                            .clickable {
                                                selectedIndex =
                                                    if (selectedIndex == index) -1 else index
                                            }
                                        ) {
                                            val infoDpOffset = if (offsetBeforeDp.dp < scrollState.value.pxToDp()) scrollState.value.pxToDp() - offsetBeforeDp.dp else 0.dp
                                            if (isSelected)
                                            {
                                                Column (modifier = Modifier
                                                    .offset(y = infoDpOffset)){
                                                    Text(
                                                        text = "${activeScheduleItem.scheduleID}",
                                                        color = MaterialTheme.colorScheme.onPrimary
                                                    )
                                                    Text(text = "Start Time: ${scheduleCalStart.time}",
                                                        color = MaterialTheme.colorScheme.onPrimary
                                                    )
                                                    Text(text = "End Time: ${scheduleCalEnd.time}",
                                                        color = MaterialTheme.colorScheme.onPrimary,

                                                        )
                                                    /*Text(text = "Scroll: ${scrollState.value}",
                                                        color = MaterialTheme.colorScheme.onPrimary,

                                                        )*/
                                                }

                                            }
                                            else
                                            {
                                                Column (modifier = Modifier
                                                    .offset(y = infoDpOffset)) {
                                                    Text(
                                                        text = "Test ${activeScheduleItem.scheduleID}",
                                                        modifier = Modifier
                                                            .vertical()
                                                            .rotate(90f),
                                                        textAlign = TextAlign.Center,
                                                        softWrap = false,
                                                        overflow = TextOverflow.Visible,
                                                        color = MaterialTheme.colorScheme.onPrimary,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }

    }

}

@Composable
@Preview(showBackground = true, widthDp = 320)
fun WeeklySchedulePreview() {

    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WeeklyScheduleFragment.BaseContent()
        }
    }
}

package com.example.scheduler.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SchedulerPickerComposables {
    companion object {

        @Composable
        fun <T>RangedSpinner(
            rangedOption: Boolean = true,
            ranged: Boolean = false,
            i1items: List<T>,
            i2items: List<T> = i1items,
            i1: T? = null,
            i2: T? = null,
            oni1Changed: (T) -> Unit = {},
            oni2Changed: (T) -> Unit = {},
            onRangedClicked: (Boolean) -> Unit = {}) {

            Row (modifier = Modifier, verticalAlignment = Alignment.CenterVertically){
                SchedulerDropdownComposables.SelectableDropdown(
                    modifier = Modifier.width(100.dp),
                    i1items,
                    i1
                ) { oni1Changed(it) }
                if (ranged)
                {

                    Text(text = "To")
                    SchedulerDropdownComposables.SelectableDropdown(
                        modifier = Modifier.width(100.dp),
                        i2items,
                        i2
                    ) { oni2Changed(it) }
                }
                if (rangedOption)
                {
                    Text(text = "Range?")
                    Checkbox(checked = ranged, onCheckedChange = {onRangedClicked(it)})
                }
            }
        }

        @Composable
        fun <T>Spinner(
            i1items: List<T>,
            i1: T? = null,
            oni1Changed: (T) -> Unit = {}) {

            Row (modifier = Modifier, verticalAlignment = Alignment.CenterVertically){
                SchedulerDropdownComposables.SelectableDropdown(
                    modifier = Modifier.width(100.dp),
                    i1items,
                    i1
                ) { oni1Changed(it) }
            }
        }

        @Composable
        fun YearSpinner(
            year: Int = 0,
            onYearChanged: (Int) -> Unit = {},) {

            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val years = (currentYear..(currentYear+100)).toList() // Change the range as needed

            Spinner(
                i1items = years,
                i1 = year,
                oni1Changed = {onYearChanged(it)}
            )
        }

        @Composable
        fun MonthSpinner(
            monthInt: Int = 0,
            onMonthIntChanged: (Int) -> Unit = {},) {
            val months = listOf(
                "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
            )
            var month by remember { mutableStateOf(months[monthInt]) }

            Spinner(
                i1items = months,
                i1 = month,
                oni1Changed = {
                    month = it
                    onMonthIntChanged(months.indexOf(it))
                }
            )
        }
        @Composable
        fun DaySpinner(
            daysInMonth: Int = 30,
            day: Int = 0,
            onDayChanged: (Int) -> Unit = {}) {

            Spinner(
                i1items = (1..daysInMonth).toList(),
                i1 = day,
                oni1Changed = {onDayChanged(it)})
        }

        @Composable
        fun YearRangeSpinner(
            rangedOption: Boolean = true,
            ranged: Boolean = false,
            startYear: Int = 0,
            endYear: Int = 0,
            onStartYearChanged: (Int) -> Unit = {},
            onEndYearChanged: (Int) -> Unit = {},
            onRangedClicked: (Boolean) -> Unit = {}) {

            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val years = (currentYear..(currentYear+100)).toList() // Change the range as needed

            RangedSpinner(
                rangedOption = rangedOption,
                ranged = ranged,
                i1items = years,
                i1 = startYear,
                i2 = endYear,
                oni1Changed = {onStartYearChanged(it)},
                oni2Changed = {onEndYearChanged(it)},
                onRangedClicked = {onRangedClicked(it)})
        }
        @Composable
        fun MonthRangeSpinner(
            rangedOption: Boolean = true,
            ranged: Boolean = false,
            startMonthInt: Int = 0,
            endMonthInt: Int = 0,
            onStartMonthIntChanged: (Int) -> Unit = {},
            onEndMonthIntChanged: (Int) -> Unit = {},
            onRangedClicked: (Boolean) -> Unit = {}) {
            val months = listOf(
                "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
            )
            var startMonth by remember { mutableStateOf(months[startMonthInt]) }
            var endMonth by remember { mutableStateOf(months[endMonthInt]) }

            RangedSpinner(
                rangedOption = rangedOption,
                ranged = ranged,
                i1items = months,
                i1 = startMonth,
                i2 = endMonth,
                oni1Changed = {
                    startMonth = it
                    onStartMonthIntChanged(months.indexOf(it))
                },
                oni2Changed = {
                    endMonth = it
                    onEndMonthIntChanged(months.indexOf(it))
                },
                onRangedClicked = {onRangedClicked(it)})
        }

        @Composable
        fun DayRangeSpinner(
            rangedOption: Boolean = true,
            ranged: Boolean = false,
            daysInStartMonth: Int = 30,
            daysInEndMonth: Int = 30,
            startDay: Int = 0,
            endDay: Int = 0,
            onStartDayChanged: (Int) -> Unit = {},
            onEndDayChanged: (Int) -> Unit = {},
            onRangedClicked: (Boolean) -> Unit = {}) {

            RangedSpinner(
                rangedOption = rangedOption,
                ranged = ranged,
                i1items = (1..daysInStartMonth).toList(),
                i2items = (1..daysInEndMonth).toList(),
                i1 = startDay,
                i2 = endDay,
                oni1Changed = {onStartDayChanged(it)},
                oni2Changed = {onEndDayChanged(it)},
                onRangedClicked = {onRangedClicked(it)})
        }

        /*@Composable
        fun TimeRangePicker(
            startHour: Int = 0,
            startMinute: Int = 0,
            endHour: Int = 0,
            endMinute: Int = 0,
            onStartTimePicked: (Int, Int) -> Unit = { i: Int, i1: Int -> },
            onEndTimePicked: (Int, Int) -> Unit = { i: Int, i1: Int -> }
        ) {

            Row (modifier = Modifier, verticalAlignment = Alignment.CenterVertically){
                TimePickerModal(startHour, startMinute) {hour, minute -> onStartTimePicked(hour, minute)}
                Text(text = "To")
                TimePickerModal(endHour, endMinute) {hour, minute -> onEndTimePicked(hour, minute)}
            }
        }
        */


        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun DateRangePickerModal(
            showDialog: Boolean,
            onDismiss: () -> Unit,
            dateRangePickerState: DateRangePickerState)
        {
            if (showDialog) {
                DatePickerDialog(
                    onDismissRequest = onDismiss,
                    confirmButton = {
                        TextButton(onClick = onDismiss) {
                            Text("Ok")
                        }
                    },
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Box(modifier = Modifier
                        .align(Alignment.CenterHorizontally))
                    {
                        DateRangePicker(state = dateRangePickerState,
                            modifier= Modifier,
                        )
                    }
                }
            }
        }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun TimePickerModal(
            showDialog: Boolean,
            onDismiss: () -> Unit,
            timePickerState: TimePickerState
        ) {
            if (showDialog) {
                DatePickerDialog(
                    onDismissRequest = onDismiss,
                    confirmButton = {
                        TextButton(onClick = onDismiss) {
                            Text("Ok")
                        }
                    },
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Box(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp))
                    {
                        TimePicker(state = timePickerState,
                            modifier= Modifier,
                            colors= TimePickerDefaults.colors(),
                            layoutType= TimePickerLayoutType.Vertical,)
                    }
                }
            }
        }
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun TimerSetterModal(onTimerPicked: (Int, Int, Int) -> Unit = { i: Int, i1: Int, i2: Int -> }) {
            var showTimePicker by remember { mutableStateOf(false) }
            var hourSelected by remember { mutableStateOf(0) }
            var minuteSelected by remember { mutableStateOf(0) }
            var secondSelected by remember { mutableStateOf(0) }
            Row (modifier= Modifier.clickable {
                showTimePicker = !showTimePicker
            }){
                Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "Date")
                Text(text = "Select Timer Time: ${hourSelected}:${minuteSelected}:${secondSelected}")
            }
            if (showTimePicker) {
                DatePickerDialog(
                    onDismissRequest = { showTimePicker = false },
                    confirmButton = {
                        TextButton(onClick = { showTimePicker = false
                            onTimerPicked(hourSelected,minuteSelected,secondSelected)}) {
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Cancel")
                        }
                    },
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Row(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp))
                    {
                        SchedulerPagerComposables.VerticalScrollDial((0..99).map { it }) {
                            hourSelected = it
                        }

                        SchedulerPagerComposables.VerticalScrollDial((0..99).map { it }) {
                            minuteSelected = it
                        }

                        SchedulerPagerComposables.VerticalScrollDial((0..99).map { it }) {
                            secondSelected = it
                        }
                    }
                }
            }
        }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun TimeDatePicker () {

            var showTimePicker by remember { mutableStateOf(false) }
            val timePickerState = rememberTimePickerState()
            Row (modifier= Modifier.clickable {
                showTimePicker = !showTimePicker
            }){
                Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "Date")
                Text(text = "Select Time ${timePickerState.hour}:${timePickerState.minute}")
            }
            if (showTimePicker) {
                DatePickerDialog(
                    onDismissRequest = { showTimePicker = false },
                    confirmButton = {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Cancel")
                        }
                    },
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Box(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp))
                    {
                        TimePicker(state = timePickerState,
                            modifier= Modifier,
                            colors= TimePickerDefaults.colors(),
                            layoutType= TimePickerLayoutType.Vertical,)
                    }
                }
            }
            var showDatePicker by remember { mutableStateOf(false) }
            val datePickerState = rememberDatePickerState()
            Row (modifier= Modifier.clickable {
                showDatePicker = !showDatePicker
            }){
                Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "Date")
                Text(text = "Select Date ${datePickerState.selectedDateMillis?.let { it1 ->
                    SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(it1))
                }}")
            }
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    },
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Box(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp))
                    {
                        DatePicker(state = datePickerState,
                            modifier= Modifier,
                            colors= DatePickerDefaults.colors())
                    }
                }
            }
        }
    }
}
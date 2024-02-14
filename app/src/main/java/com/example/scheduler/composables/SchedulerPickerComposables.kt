package com.example.scheduler.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
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
import java.util.Date
import java.util.Locale

class SchedulerPickerComposables {
    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun TimePickerModal(onTimePicked: (Int, Int) -> Unit = { i: Int, i1: Int -> }) {
            var showTimePicker by remember { mutableStateOf(false) }
            val timePickerState = rememberTimePickerState()
            Row (modifier= Modifier.clickable {
                showTimePicker = !showTimePicker
            }){
                Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "Date")
                Text(text = "Select Start Time: ${timePickerState.hour}:${timePickerState.minute}")
            }
            if (showTimePicker) {
                DatePickerDialog(
                    onDismissRequest = { showTimePicker = false },
                    confirmButton = {
                        TextButton(onClick = { showTimePicker = false
                            onTimePicked(timePickerState.hour, timePickerState.minute)}) {
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

            Spacer(modifier = Modifier.height(16.dp))


            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
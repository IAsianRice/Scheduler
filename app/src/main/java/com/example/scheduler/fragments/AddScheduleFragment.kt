package com.example.scheduler.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.scheduler.MainActivity
import com.example.scheduler.composables.SchedulerNavComposables.Companion.ChoiceBar
import com.example.scheduler.composables.SchedulerPickerComposables.Companion.TimePickerModal
import com.example.scheduler.composables.SchedulerPickerComposables.Companion.TimerSetterModal
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.ui.theme.SchedulerTheme

class AddScheduleFragment(

): AppFragment {
    @Composable
    override fun Content() {
        BaseContent()
    }

    companion object {
        sealed class ScheduleType {
            object Schedule : ScheduleType() {
                override fun toString(): String {
                    return "Schedule"
                }
            }
            object Quota : ScheduleType(){
                override fun toString(): String {
                    return "Quota"
                }
            }
        }

        /*sealed class RepeatType {
            object Daily : RepeatType() {
                override fun toString(): String {
                    return "Daily"
                }
            }
            object Weekly : RepeatType(){
                override fun toString(): String {
                    return "Weekly"
                }
            }
            object Monthly : RepeatType(){
                override fun toString(): String {
                    return "Monthly"
                }
            }
            object Yearly : RepeatType(){
                override fun toString(): String {
                    return "Yearly"
                }
            }
            object Custom : RepeatType(){
                override fun toString(): String {
                    return "Custom"
                }
            }
        }*/

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BaseContent() {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Add Schedule") },
                        )
                    }
                    ,
                    content = {
                        Column(modifier = Modifier.padding(it)) {
                            var scheduleType by remember { mutableStateOf<ScheduleType>(ScheduleType.Schedule) }
                            var name by remember { mutableStateOf("") }
                            var description by remember { mutableStateOf("") }
                            ChoiceBar(
                                listOf(ScheduleType.Schedule, ScheduleType.Quota),
                                ScheduleType.Schedule) { it ->
                                scheduleType = it
                            }

                            Text(text = "Schedule Name")
                            TextField(
                                value = name,
                                onValueChange = { name = it },
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .heightIn(min = 32.dp),
                                textStyle = TextStyle(color = Color.Black),
                                placeholder = { Text(text = "Name") },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                                maxLines = 1 // Adjust max lines as needed
                            )

                            Text(text = "Description")
                            TextField(
                                value = description,
                                onValueChange = { description = it },
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .heightIn(min = 4 * 32.dp),
                                textStyle = TextStyle(color = Color.Black),
                                placeholder = { Text(text = "Enter description") },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                                maxLines = 50 // Adjust max lines as needed
                            )

                            // Submit Button
                            Button(
                                onClick = {/*onSubmit(ScheduleItem(title = name, description = description, startingTime = ))*/},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text(text = "Submit", modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true,)
fun AddScheduleFragmentPreview() {

    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AddScheduleFragment.BaseContent()
        }
    }
}


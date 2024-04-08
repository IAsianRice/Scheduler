package com.example.scheduler.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.example.scheduler.ui.theme.SchedulerTheme

class EditScheduleFragment(

): AppFragment {
    @Composable
    override fun Content() {
        BaseContent()
    }

    companion object {
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
                            title = { Text(text = "Edit Schedule") },
                        )
                    }
                    ,
                    content = {
                        Column(modifier = Modifier.padding(it)) {
                            /*var scheduleType by remember { mutableStateOf<ScheduleType>(ScheduleType.Schedule) }
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
                            }*/
                        }
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true,)
fun EditScheduleFragmentPreview() {

    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            EditScheduleFragment.BaseContent()
        }
    }
}


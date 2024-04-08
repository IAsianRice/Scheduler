package com.example.scheduler.fragments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.scheduler.MainActivity
import com.example.scheduler.composables.SchedulerModalComposables
import com.example.scheduler.composables.SchedulerNavComposables.Companion.ChoiceBar
import com.example.scheduler.database.entities.QuotaItem
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.database.entities.TagItem
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerMockData.Companion.generateRandomTags

class AddScheduleFragment(): AppFragment {
    @Composable
    override fun Content() {
        val tags by MainActivity.databaseViewModel.tagsListStateFlow.collectAsState()
        BaseContent(
            tagsList = tags,
            tagSubmit = {
                MainActivity.databaseViewModel.addTagItem(it)
                MainActivity.fragmentViewModel.back()
            },
            scheduleSubmit = { scheduleItem, selectedTagIds ->
                MainActivity.databaseViewModel.addScheduleItem(scheduleItem, selectedTagIds)
                MainActivity.fragmentViewModel.back()
             },
            quotaSubmit = { quotaItem, selectedTagIds ->
                MainActivity.databaseViewModel.addQuotaItem(quotaItem, selectedTagIds)
                MainActivity.fragmentViewModel.back()
            })
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
            object Tag : ScheduleType(){
                override fun toString(): String {
                    return "Tag"
                }
            }
        }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BaseContent(
            tagsList: List<TagItem> = generateRandomTags(30),
            tagSubmit: (TagItem) -> Unit = {},
            quotaSubmit: (quotaItem: QuotaItem, selectedTagIds: List<Long>) -> Unit = {_,_ ->},
            scheduleSubmit: (scheduleItem: ScheduleItem, selectedTagIds: List<Long>) -> Unit = {_,_ ->}
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                var scheduleType by remember { mutableStateOf<ScheduleType>(ScheduleType.Schedule) }
                var name by remember { mutableStateOf("") }
                var description by remember { mutableStateOf("") }
                var showTagDialog by remember { mutableStateOf(false) }
                var optionsList by remember { mutableStateOf(tagsList)}
                var selectedOptionsList by remember { mutableStateOf(listOf<TagItem>())}
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Add Schedule") },
                        )
                    },
                    content = {
                        Column(modifier = Modifier.padding(it)) {

                            ChoiceBar(
                                listOf(ScheduleType.Schedule, ScheduleType.Quota, ScheduleType.Tag),
                                scheduleType) { it ->
                                scheduleType = it
                            }

                            when(scheduleType)
                            {
                                ScheduleType.Quota -> AddQuotaItem(
                                    name = name,
                                    onNameChange = {name = it},
                                    description = description,
                                    onDescriptionChange = {description = it},
                                    tagsList = selectedOptionsList,
                                    onAddTagClicked = {showTagDialog = true})
                                ScheduleType.Schedule -> AddScheduleItem(
                                    name = name,
                                    onNameChange = {name = it},
                                    description = description,
                                    onDescriptionChange = {description = it},
                                    tagsList = selectedOptionsList,
                                    onAddTagClicked = {showTagDialog = true})
                                ScheduleType.Tag -> AddTagItem(
                                    name = name,
                                    onNameChange = {name = it},
                                    description = description,
                                    onDescriptionChange = {description = it}
                                )
                            }
                            SchedulerModalComposables.MultiChoiceModalDialog(
                                showDialog = showTagDialog,
                                onDismiss = {showTagDialog = false},
                                contentList = optionsList,
                                selectedList = selectedOptionsList,
                                title = {
                                    Text(text = "Just a little Selection Test!")
                                },
                                onContentListUpdated = { optionsList = it },
                                onSelectedListUpdated = { selectedOptionsList = it },
                                contentLayout = {
                                    Column(
                                        modifier = Modifier
                                            .border(
                                                BorderStroke(1.dp, Color.Black),
                                                CircleShape
                                            )
                                            .background(Color.Gray, CircleShape)
                                    ) {
                                        Text(modifier = Modifier.padding(10.dp), text = it.name)
                                    }
                                })


                            // Submit Button
                            Button(
                                onClick =
                                {
                                    when(scheduleType)
                                    {
                                        ScheduleType.Quota -> quotaSubmit(
                                            QuotaItem(title = name, description = description),
                                            selectedOptionsList.map {tag -> tag.tagId}
                                        )
                                        ScheduleType.Schedule -> scheduleSubmit(
                                            ScheduleItem(title = name, description = description),
                                            selectedOptionsList.map {tag -> tag.tagId}
                                        )
                                        ScheduleType.Tag -> tagSubmit(TagItem(name = name, description = description))
                                    }
                                },
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
        @OptIn(ExperimentalLayoutApi::class)
        @Composable
        fun AddQuotaItem(
            name: String,
            onNameChange: (String) -> Unit,
            description: String,
            onDescriptionChange: (String) -> Unit,
            tagsList: List<TagItem>,
            onAddTagClicked: () -> Unit) {
            Text(text = "Quota Name")
            TextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 32.dp),
                placeholder = { Text(text = "Name") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                maxLines = 1 // Adjust max lines as needed
            )

            Text(text = "Description")
            TextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 4 * 32.dp),
                placeholder = { Text(text = "Enter description") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                maxLines = 50 // Adjust max lines as needed
            )
            Row(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Add Tag")
                Button(onClick = onAddTagClicked) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add a Tag")
                }
            }
            FlowRow {
                for (item in tagsList)
                    Column(
                        modifier = Modifier
                            .border(
                                BorderStroke(1.dp, Color.Black),
                                CircleShape
                            )
                            .background(Color.Gray, CircleShape)
                    ) {
                        Text(modifier = Modifier.padding(10.dp), text = item.name)
                    }
            }
        }
        @Composable
        fun AddTagItem(
            name: String,
            onNameChange: (String) -> Unit,
            description: String,
            onDescriptionChange: (String) -> Unit) {
            Text(text = "Tag Name")
            TextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 32.dp),
                placeholder = { Text(text = "Name") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                maxLines = 1 // Adjust max lines as needed
            )

            Text(text = "Description")
            TextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 4 * 32.dp),
                placeholder = { Text(text = "Enter description") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                maxLines = 50 // Adjust max lines as needed
            )
        }

        @OptIn(ExperimentalLayoutApi::class)
        @Composable
        fun AddScheduleItem(
            name: String,
            onNameChange: (String) -> Unit,
            description: String,
            onDescriptionChange: (String) -> Unit,
            tagsList: List<TagItem>,
            onAddTagClicked: () -> Unit) {
            Text(text = "Schedule Name")
            TextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 32.dp),
                placeholder = { Text(text = "Name") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                maxLines = 1 // Adjust max lines as needed
            )
            Text(text = "Description")
            TextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 4 * 32.dp),
                placeholder = { Text(text = "Enter description") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                maxLines = 50 // Adjust max lines as needed
            )
            Row (modifier = Modifier.fillMaxWidth(1f),horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "Add Tag")
                Button(onClick = onAddTagClicked) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add a Tag")
                }
            }
            FlowRow {
                for (item in tagsList)
                {
                    Column(
                        modifier = Modifier
                            .border(
                                BorderStroke(1.dp, Color.Black),
                                CircleShape
                            )
                            .background(Color.Gray, CircleShape)
                    ) {
                        Text(modifier = Modifier.padding(10.dp), text = item.name)
                    }
                }
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


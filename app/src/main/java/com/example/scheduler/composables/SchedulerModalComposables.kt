package com.example.scheduler.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class SchedulerModalComposables {
    companion object {

        @Composable
        fun CustomModal(
            showDialog: Boolean,
            onDismiss: () -> Unit,
            content:  @Composable() (ColumnScope.(onDismiss:() -> Unit) -> Unit) = { Text(text = "Content") },
        ) {
            if (showDialog) {
                Dialog(
                    onDismissRequest = { onDismiss() },
                    content = {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            color = Color.Transparent
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.shapes.medium
                                    ),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,

                                ) {
                                content(onDismiss)
                            }
                        }
                    }
                )
            }
        }

        @Composable
        fun YesNoModalDialog(
            showDialog: Boolean,
            onDismiss: () -> Unit,
            onYes: () -> Unit = {},
            onNo: () -> Unit = {},
            title:  @Composable() (ColumnScope.() -> Unit) = { Text(text = "Title") },
            description: @Composable() (ColumnScope.() -> Unit) = { Text(text = "Description") }
        ) {
            if (showDialog) {
                Dialog(
                    onDismissRequest = { onDismiss() },
                    content = {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            color = Color.Transparent
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.shapes.medium
                                    ),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,

                            ) {
                                title()
                                Spacer(modifier = Modifier.height(16.dp))
                                description()
                                Spacer(modifier = Modifier.height(16.dp))
                                Row {
                                    Button(onClick = { onYes() }) {
                                        Text(text = "Yes")
                                    }
                                    Button(onClick = { onNo() }) {
                                        Text(text = "No")
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }

        @OptIn(ExperimentalLayoutApi::class)
        @Composable
        fun <T>MultiChoiceModalDialog(
            showDialog: Boolean,
            onDismiss: () -> Unit,
            contentList: List<T>,
            selectedList: List<T>,
            onContentListUpdated: (List<T>) -> Unit,
            onSelectedListUpdated: (List<T>) -> Unit,
            title: @Composable() (ColumnScope.() -> Unit) = { Text(text = "Title") },
            contentLayout: @Composable() (ColumnScope.(T) -> Unit) = { Text(text = "Content") },
        ) {
            if (showDialog) {
                Dialog(
                    onDismissRequest = { onDismiss() },
                    content = {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            color = Color.Transparent
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.shapes.medium
                                    ),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,

                                ) {
                                title()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "Selected")
                                FlowRow {
                                    for (item in selectedList)
                                    {
                                        Column(modifier = Modifier.clickable {
                                            onContentListUpdated(contentList + item)
                                            onSelectedListUpdated(selectedList - item)
                                        }) {
                                            contentLayout(item)

                                        }
                                    }
                                }
                                Text(text = "Options")
                                FlowRow {
                                    for (item in contentList)
                                    {
                                        Column(modifier = Modifier.clickable {
                                            onContentListUpdated(contentList - item)
                                            onSelectedListUpdated(selectedList + item)
                                        }) {
                                            contentLayout(item)
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
        @OptIn(ExperimentalLayoutApi::class)
        @Composable
        fun <T>SearchableMultiChoiceModalDialog(
            showDialog: Boolean,
            onDismiss: () -> Unit,
            contentList: List<T>,
            selectedList: List<T>,
            onContentListUpdated: (T) -> Unit,
            onSelectedListUpdated: (T) -> Unit,
            searchPredicate: (T, String) -> Boolean,
            title: @Composable() (ColumnScope.() -> Unit) = { Text(text = "Title") },
            contentLayout: @Composable() (ColumnScope.(T) -> Unit) = { Text(text = "Content") },
        ) {
            if (showDialog) {
                // States
                var search by remember { mutableStateOf("") }

                // Composable
                Dialog(
                    onDismissRequest = { onDismiss() },
                    content = {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            color = Color.Transparent
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.shapes.medium
                                    ),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,

                                ) {
                                // Title
                                title()

                                Spacer(modifier = Modifier.height(16.dp))

                                // Search Bar
                                TextField(value = search,
                                    onValueChange = {search = it},
                                    placeholder = { Text(text = "Search")},
                                    modifier = Modifier
                                        .padding(3.dp)
                                        .height(50.dp)
                                        .fillMaxWidth())
                                // Content
                                Text(text = "Selected")
                                FlowRow {
                                    for (item in selectedList.filter { searchPredicate(it, search) })
                                    {
                                        Column(modifier = Modifier.clickable {
                                            onSelectedListUpdated(item)
                                        }) {
                                            contentLayout(item)
                                        }
                                    }
                                }
                                Text(text = "Options")
                                FlowRow {
                                    for (item in contentList.filter { searchPredicate(it, search) })
                                    {
                                        Column(modifier = Modifier.clickable {
                                            onContentListUpdated(item)
                                        }) {
                                            contentLayout(item)
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
}
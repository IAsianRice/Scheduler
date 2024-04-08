package com.example.scheduler.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class SchedulerAAGComposables {
    companion object {
        @Composable
        fun ShortList(
            showing: Int = 3,
            listItem: @Composable ((index: Int) -> Unit) = {},
            header: @Composable (() -> Unit) = {},
            listSize: Int = 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column (
                    modifier = Modifier.padding(16.dp)
                ) {
                    header()
                    LazyColumn(modifier = Modifier) {
                        items(if (showing < listSize) showing else listSize) { index ->
                            listItem(index)
                        }
                    }
                }
            }
        }
    }
}
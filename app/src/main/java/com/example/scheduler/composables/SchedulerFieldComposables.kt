package com.example.scheduler.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

class SchedulerFieldComposables {
    companion object
    {
        @Composable
        fun NumberTextField(
            value: Int,
            onValueChange: (Int) -> Unit,
            modifier: Modifier = Modifier
        ) {
            TextField(
                value = "$value",
                onValueChange =
                {
                    val intValue = try {
                        it.toInt()
                    } catch (e: NumberFormatException) {
                        0
                    }
                    onValueChange(intValue)

                },
                modifier = modifier,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )
        }

        @Composable
        fun EditableTextField(
            value: String,
            onValueChange: (String) -> Unit,
            modifier: Modifier = Modifier
        ) {
            var editedText by remember { mutableStateOf(value) }
            var editingText by remember { mutableStateOf(false) }
            if (editingText)
            {
                Row {
                    TextField(value = editedText,
                        onValueChange = {editedText = it})
                    Icon(Icons.Filled.Done, "Save", modifier = Modifier.clickable {
                        editingText = false
                        onValueChange(editedText)
                    })
                    Icon(Icons.Filled.Close, "Cancel", modifier = Modifier.clickable {
                        editingText = false
                    })
                }
            }
            else
            {
                Row {
                    Icon(Icons.Filled.Edit, "edit", modifier = Modifier.clickable {
                        editingText = true
                    })
                    Text(
                        text = value,

                        )
                }

            }
        }
    }
}
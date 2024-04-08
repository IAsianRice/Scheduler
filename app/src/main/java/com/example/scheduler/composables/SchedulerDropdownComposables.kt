package com.example.scheduler.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class SchedulerDropdownComposables {
    companion object {

        @Composable
        fun <T>SelectableDropdown(
            modifier: Modifier = Modifier,
            items: List<T>,
            selectedItem: T?,
            onItemSelected: (T) -> Unit
        ) {
            var expanded by remember { mutableStateOf(false) }
            val interactionSource = remember { MutableInteractionSource() }

            Column(
                modifier = modifier
            ) {
                Box(
                    modifier = modifier
                ) {
                    Text(
                        text = selectedItem?.toString() ?: "Select an item",
                        modifier = modifier
                            .clickable(onClick = { expanded = !expanded })
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,

                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Arrow",
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = modifier
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                onItemSelected(item)
                                expanded = false
                            },
                            interactionSource = interactionSource,
                            text = { Text(
                                text = item.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )}
                        )
                    }

                    /*LazyColumn() {
                        items(items){ item ->
                            /*Text(
                                modifier = Modifier.clickable {
                                    onItemSelected(item)
                                    expanded = false
                                },
                                text = item.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface)*/
                            DropdownMenuItem(
                                onClick = {
                                    onItemSelected(item)
                                    expanded = false
                                },
                                interactionSource = interactionSource,
                                text = { Text(
                                    text = item.toString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )}
                            )
                            /*DropdownMenuItem2(onClick = {
                                onItemSelected(item)
                                expanded = false
                            }) {
                                Text(text = item.toString())
                            }*/
                        }
                    }*/
                }
            }
        }


        @Composable
        fun DropdownMenuItem2(onClick: () -> Unit, content: @Composable () -> Unit) {
            Column(modifier = Modifier.clickable(onClick = onClick)) {
                content()
            }
        }
    }
}
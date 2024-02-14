package com.example.scheduler.fragments

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.utility.SchedulerTimeFunctions.Companion.generateWeeks
import com.example.scheduler.viewmodels.FragmentViewModel
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.scheduler.utility.SchedulerTimeFunctions

class CalenderFragment(

): AppFragment {
    @Composable
    override fun Content() {
        var serverName by remember { mutableStateOf("") }
        BaseContent()
    }

    companion object {
        val monthColors = listOf(
            Color.White,        // January
            Color.Red,          // February
            Color.Green,        // March
            Color(0xFFFFC0CB),  // April (Pastel Pink)
            Color(0xFFE6E6FA),  // May (Lavender)
            Color.Yellow,       // June
            Color(0xFF87CEEB),  // July (Sky Blue)
            Color(0xFFFFA500),  // August (Orange)
            Color(0xFF006400),  // September (Dark Green)
            Color(0xFFFFA500),  // October (Orange)
            Color(0xFFA52A2A),  // November (Brown)
            Color(0xFF00008B)   // December (Deep Blue)
        )
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BaseContent() {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Get local density from composable
                val weekHeight = 30
                val topappbvrg = rememberTopAppBarState()
                val contentScroll = rememberScrollState()
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topappbvrg)
                val elevation by animateDpAsState(if (topappbvrg.overlappedFraction == 0.0f) 0.dp else 50.dp,
                    label = ""
                )
                var scrollValue by remember {
                    mutableStateOf("")
                }
                Scaffold(
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        Surface(shadowElevation = elevation) {
                            TopAppBar(
                                title = { Text(text = "Calender") },
                                scrollBehavior = scrollBehavior
                            )
                        }
                    }
                ) {
                    Row(modifier = Modifier
                        .padding(it)
                        .verticalScroll(contentScroll)
                    ) {
                        var weeks = generateWeeks(2024)
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary)
                                .height((weekHeight * weeks.size).dp)
                                .padding(top = 30.dp, bottom = 30.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            SchedulerTimeFunctions.Companion.Months.values().forEachIndexed { index, month ->
                                Box(modifier = Modifier.clickable {  }) {
                                    Text(
                                        text = month.name,
                                        modifier = Modifier
                                            .vertical()
                                            .rotate(-90f)
                                            .padding(top = 12.5.dp, bottom = 12.5.dp)
                                            .width(100.dp)
                                            .background(monthColors[index]),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        Column(modifier = Modifier) {
                            var month = -1
                            for ((index, week) in weeks.withIndex()) {
                                Row(
                                    modifier = Modifier
                                        .height(weekHeight.dp)
                                        .background(MaterialTheme.colorScheme.secondary)
                                ) {
                                    for (day in week) {
                                        if (day == 1) {
                                            month++
                                        }
                                        Text(
                                            text = "${day}",
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(vertical = 4.dp)
                                                .border(
                                                    width = 1.dp,
                                                    color = Color.Gray,
                                                    shape = CircleShape
                                                )
                                                .background(if (month >= 0) monthColors[month] else Color.Gray),
                                            textAlign = TextAlign.Center,
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
}

fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }
@Composable
@Preview(showBackground = true, widthDp = 640)
fun CalenderFragmentPreview() {

    SchedulerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CalenderFragment.BaseContent()
        }
    }
}


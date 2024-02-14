package com.example.scheduler.fragments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/*@Composable
fun ScheduleBlockView() {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        // Dummy data for demonstration
        val schedule = generateDummySchedule()

        // Grid of schedule blocks
        LazyVerticalGrid(
            columns = GridCells.Fixed(7), // 7 cells for days of the week
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(schedule) { index, daySchedule ->
                DayScheduleColumn(daySchedule = daySchedule)
            }
        }
    }
}*/
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransparentPagerScreen() {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        // Dummy data for demonstration
        val items = (1..10).toList()

        // Pager state
        val pagerState = rememberPagerState( initialPage = 300/2 - (300/2 % items.size), pageCount = { 300 })

        // Pager content
        /*VerticalPager(state = pagerState) { num ->
            Column {
                Box(modifier = Modifier
                    .padding(4.dp)
                    .size(80.dp)
                    .background(Color.LightGray),
                    contentAlignment = Alignment.Center ) {
                    Text(text = items[num % items.size].toString())
                }
            }
        }*/
    }
}
/*
@Composable
fun LazyItemScope.PagerPage(color: Color, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}*/
@Preview
@Composable
fun PreviewYearCalendar() {
    TransparentPagerScreen()
}
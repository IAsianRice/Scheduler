package com.example.scheduler.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class SchedulerPagerComposables {
    companion object {
        @OptIn(ExperimentalFoundationApi::class)
        @Composable
        fun <T>VerticalScrollDial(items: List<T>, onItemSelected: (T) -> Unit) {
            val pagerState = rememberPagerState( initialPage = Int.MAX_VALUE/2 - (Int.MAX_VALUE/2 % items.size), pageCount = { Int.MAX_VALUE })
            val selectedItemIndex = remember { mutableStateOf(-1) }

            val size = 80
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(modifier = Modifier
                    .size(size.dp)
                    .background(Color.LightGray)
                    .offset(y=(-size*pagerState.currentPageOffsetFraction).dp),
                    contentAlignment = Alignment.Center ) {
                    Text(text = items[(pagerState.currentPage - 1) % items.size].toString())
                }
                VerticalPager(modifier =Modifier
                    .size(size.dp)
                    .background(Color.LightGray),
                    state = pagerState) { num ->
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center ) {
                        Text(text = items[num % items.size].toString())
                    }
                }
                Box(modifier = Modifier
                    .size(size.dp)
                    .background(Color.LightGray)
                    .offset(y=(-size*pagerState.currentPageOffsetFraction).dp),
                    contentAlignment = Alignment.Center ) {
                    Text(text = items[(pagerState.currentPage + 1) % items.size].toString())
                }
            }

            LaunchedEffect(pagerState.currentPage) {
                // Update the selected item index when the scroll position changes
                selectedItemIndex.value = pagerState.currentPage
                onItemSelected(items[selectedItemIndex.value % items.size])
                pagerState.scrollToPage(selectedItemIndex.value)
            }
        }
        @OptIn(ExperimentalFoundationApi::class)
        @Composable
        fun <T>HorizontalScrollDial(items: List<T>, onItemSelected: (T) -> Unit) {
            val pagerState = rememberPagerState( initialPage = Int.MAX_VALUE/2 - (Int.MAX_VALUE/2 % items.size), pageCount = { Int.MAX_VALUE })
            val selectedItemIndex = remember { mutableStateOf(-1) }

            val size = 80
            Row(
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(modifier = Modifier
                    .size(size.dp)
                    .background(Color.LightGray)
                    .offset(x=(-size*pagerState.currentPageOffsetFraction).dp),
                    contentAlignment = Alignment.Center ) {
                    Text(text = items[(pagerState.currentPage - 1) % items.size].toString())
                }
                HorizontalPager(modifier =Modifier
                    .size(size.dp)
                    .background(Color.LightGray),
                    state = pagerState) { num ->
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center ) {
                        Text(text = items[num % items.size].toString())
                    }
                }
                Box(modifier = Modifier
                    .size(size.dp)
                    .background(Color.LightGray)
                    .offset(x=(-size*pagerState.currentPageOffsetFraction).dp),
                    contentAlignment = Alignment.Center ) {
                    Text(text = items[(pagerState.currentPage + 1) % items.size].toString())
                }
            }

            LaunchedEffect(pagerState.currentPage) {
                // Update the selected item index when the scroll position changes
                selectedItemIndex.value = pagerState.currentPage
                onItemSelected(items[selectedItemIndex.value % items.size])
                pagerState.scrollToPage(selectedItemIndex.value)
            }
        }
    }
}
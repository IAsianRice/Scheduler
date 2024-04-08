package com.example.scheduler.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

class SchedulerDesignComposables {
    companion object {
        @Composable
        fun CardColumn (modifier: Modifier = Modifier,
                        borderColor: Color = MaterialTheme.colorScheme.primaryContainer,
                        content: @Composable (ColumnScope.() -> Unit),) {
            Column(modifier = modifier
                .padding(5.dp)
                .border(
                    BorderStroke(
                        2.dp,
                        color = borderColor
                    )
                    , MaterialTheme.shapes.medium
                )) {
                Column(modifier = Modifier.padding(5.dp)) {
                    content()
                }
            }
        }
        @Composable
        fun HighlightedText(text: String,
                            highlight: String,
                            style: TextStyle = TextStyle(),
                            textColor: Color = Color.Black,
                            highlightColor: Color = Color.Red,
                            ) {
            val annotatedString = buildAnnotatedString {
                val startIndex = text.indexOf(highlight)
                val endIndex = startIndex + highlight.length
                append(text)
                addStyle(
                    SpanStyle(color = textColor),
                    0,
                    text.length
                )
                addStyle(
                    SpanStyle(color = highlightColor, textDecoration = TextDecoration.Underline),
                    startIndex,
                    endIndex
                )
            }

            BasicText(
                text = annotatedString,
                style = style,
            )
        }
    }
}
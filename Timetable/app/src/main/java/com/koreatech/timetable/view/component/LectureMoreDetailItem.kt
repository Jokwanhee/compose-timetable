package com.koreatech.timetable.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LectureMoreDetailItem(
    modifier: Modifier = Modifier,
    onClickDayOfWeek: () -> Unit,
    onClickTime: () -> Unit
) {
    Column {
        Row {
            TimeDropdown(
                title = "월요일",
                modifier = Modifier
                    .background(Color.White)
                    .padding(10.dp),
                onClick = onClickDayOfWeek
            )
            TimeDropdown(
                title = "00:00 ~ 00:00",
                modifier = Modifier
                    .background(Color.White)
                    .padding(10.dp),
                onClick = onClickTime
            )
        }
        LectureDetailItem(
            title = "장소",
            essential = false
        )
    }
}

@Preview
@Composable
private fun LectureMoreDetailItemPreview() {
    LectureMoreDetailItem(
        onClickDayOfWeek = {},
        onClickTime = {}
    )
}
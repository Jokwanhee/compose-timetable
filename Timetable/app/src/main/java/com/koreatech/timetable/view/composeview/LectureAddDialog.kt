package com.koreatech.timetable.view.composeview

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.timetable.data.model.Lecture
import com.koreatech.timetable.view.component.CustomAlertDialog

@Composable
fun LectureAddDialog(
    modifier: Modifier = Modifier,
    context: Context,
    visible: Boolean,
    lecture: Lecture,
    duplication: Boolean,
    onDismissRequest: () -> Unit,
    onAddLecture: (Lecture) -> Unit,
) {
    if (visible) {
        CustomAlertDialog(
            onDismissRequest = onDismissRequest,
            content = {
                Column(
                    modifier = modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${lecture.name}(${lecture.lectureClass})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = if (duplication) "기존 강의 대신\n새로운 강의를 추가하시겠습니까?" else "강의를 추가하시겠습니까?",
                        fontSize = 12.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = { onAddLecture(lecture) }
                    ) {
                        Text(text = "추가하기")
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LectureAddDialogPreview() {
    LectureAddDialog(
        context = LocalContext.current,
        visible = true,
        lecture = Lecture(name = "건축개론", lectureClass = "01"),
        duplication = true,
        onDismissRequest = {},
        onAddLecture = {}
    )
}
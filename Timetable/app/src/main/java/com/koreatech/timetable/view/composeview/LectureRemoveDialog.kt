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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.timetable.data.model.Lecture
import com.koreatech.timetable.view.component.CustomAlertDialog

@Composable
fun LectureRemoveDialog(
    modifier: Modifier = Modifier,
    context: Context,
    visible: Boolean,
    lecture: Lecture,
    onDismissRequest: () -> Unit,
    onRemoveLecture: (Lecture) -> Unit
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
                        text = "강의를 삭제하시겠습니까?",
                        fontSize = 12.sp,
                        color = Color.Black
                    )

                    Button(
                        onClick = { onRemoveLecture(lecture) }
                    ) {
                        Text(text = "삭제하기")
                    }
                }
            }
        )
    }
}

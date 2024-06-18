package com.koreatech.timetable.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LectureDetailItem(
    title: String,
    essential: Boolean,
    modifier: Modifier = Modifier,
) {
    var value by remember {
        mutableStateOf("")
    }

    Row(
        modifier = modifier
            .background(color = Color.White, shape = RoundedCornerShape(4.dp))
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                modifier = Modifier
            )
            if (essential) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Top)
                        .size(10.dp),
                )
            }
        }
        Spacer(
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 10.dp)
                .width(1.dp)
                .fillMaxHeight()
                .background(Color.Gray)
        )
        TextField(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier.padding(2.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
                backgroundColor = Color.White,
            ),
            placeholder = {
                Text(text = "${title}을 입력하세요.")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LectureDetailItemPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        LectureDetailItem(
            title = "수업명",
            essential = false,
            modifier = Modifier
        )
    }
}
package com.koreatech.timetable.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LectureExtraButton(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onAdd
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "시간 및 장소 추가")
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LectureExtraButtonPreview() {
    LectureExtraButton(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        onAdd = {}
    )
}
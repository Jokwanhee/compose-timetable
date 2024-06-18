package com.koreatech.timetable.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TimeDropdown(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeDropdownPreview() {
    Box(modifier = Modifier.fillMaxWidth()) {
        TimeDropdown(
            title = "월요일",
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp),
            onClick = {}
        )
    }
}
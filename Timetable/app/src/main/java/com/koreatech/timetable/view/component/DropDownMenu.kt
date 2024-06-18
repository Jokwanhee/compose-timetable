package com.koreatech.timetable.view.component

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.timetable.data.model.Semester

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownMenu(
    semesters: List<Semester>,
    onSemesterTextChanged: (Semester) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedText by remember {
        mutableStateOf("")
    }

    selectedText.ifBlank {
        if (semesters.isNotEmpty()) semesters[0].formatSemester()
        else ""
    }.let {
        selectedText = it
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            readOnly = true,
            value = selectedText,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                focusedTrailingIconColor = Color.Black,
                textColor = Color.Black
            ),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .wrapContentSize()
                .border(width = 1.dp, Color.Black, shape = RoundedCornerShape(4.dp))
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            semesters.forEach { semester ->
                DropdownMenuItem(
                    onClick = {
                        onSemesterTextChanged(semester)
                        selectedText = semester.formatSemester()
                        expanded = false
                    }
                ) {
                    Text(
                        text = semester.formatSemester(),
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DropDownMenuPreview() {
    val semesters = listOf(
        Semester(1, "20241"),
        Semester(2, "20242"),
    )
    DropDownMenu(
        semesters = semesters,
        onSemesterTextChanged = {}
    )
}
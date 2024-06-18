package com.koreatech.timetable.view.composeview

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koreatech.timetable.data.model.Department
import com.koreatech.timetable.data.model.Lecture
import com.koreatech.timetable.data.model.TimetableEvent
import com.koreatech.timetable.view.component.LectureItem
import com.koreatech.timetable.view.component.MajorCarouselCard
import com.koreatech.timetable.view.component.SearchRow

@Composable
fun TimetableBottomSheetContent(
    colors: List<Color>,
    lectures: List<Lecture>,
    selectedLectures: Lecture,
    currentDepartments: List<Department>,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClickLecture: (List<TimetableEvent>) -> Unit,
    onSelectedLecture: (Lecture) -> Unit,
    onAddLecture: (Lecture) -> Unit,
    onSetting: () -> Unit,
    onCancel: (Department) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
    ) {
        SearchRow(
            searchText = searchText,
            onSearchTextChanged = onSearchTextChanged,
            onSetting = onSetting
        )
        LazyRow(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        ) {
            itemsIndexed(currentDepartments) { index, department ->
                MajorCarouselCard(
                    modifier = Modifier.padding(
                        end = if (index == currentDepartments.size) 0.dp else 4.dp
                    ),
                    department = department,
                    onCancel = onCancel
                )
            }
        }
        LazyColumn {
            itemsIndexed(lectures) { _, lecture ->
                LectureItem(
                    colors = colors,
                    lecture = lecture,
                    selectedLecture = selectedLectures,
                    onClick = onClickLecture,
                    onSelect = onSelectedLecture,
                    onAddLecture = onAddLecture
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimetableBottomSheetContentPreview() {
    TimetableBottomSheetContent(
        colors = emptyList(),
        lectures = emptyList(),
        selectedLectures = Lecture(),
        currentDepartments = emptyList(),
        searchText = "",
        onSearchTextChanged = {},
        onClickLecture = {},
        onSelectedLecture = {},
        onAddLecture = {},
        onSetting = {},
        onCancel = {}
    )
}
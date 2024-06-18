package com.koreatech.timetable.view.composeview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koreatech.timetable.data.model.Department
import com.koreatech.timetable.view.component.CustomAlertDialog
import com.koreatech.timetable.view.component.MajorBox
import com.koreatech.timetable.view.component.MajorButton
import com.koreatech.timetable.view.viewmodel.HomeViewModel

@Composable
fun MajorDialog(
    viewModel: HomeViewModel,
    visible: Boolean,
    departments: List<Department>,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClick: (Department, Boolean) -> Unit,
    onCompleted: (List<Department>) -> Unit,
) {
    val selectedDepartments by viewModel.selectedDepartments.collectAsStateWithLifecycle()

    if (visible) {
        CustomAlertDialog(
            onDismissRequest = onDismissRequest
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(
                        vertical = 20.dp,
                        horizontal = 16.dp
                    )
            ) {
                Text(
                    text = "전공선택",
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier
                        .background(Color.White)
                ) {
                    itemsIndexed(departments) { index, department ->
                        MajorBox(
                            modifier = Modifier.padding(
                                start = if (index % 2 == 0) 0.dp else 2.dp,
                                end = if (index % 2 == 0) 2.dp else 0.dp,
                                bottom = 4.dp
                            ),
                            department = department,
                            selected = selectedDepartments.contains(department),
                            onClick = { selectedDepartment, isSelected ->
                                onClick(selectedDepartment, isSelected)
                            },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                MajorButton(
                    modifier = Modifier.align(Alignment.End),
                    onCompleted = {
                        onCompleted(selectedDepartments)
                    }
                )
            }
        }
    }
}
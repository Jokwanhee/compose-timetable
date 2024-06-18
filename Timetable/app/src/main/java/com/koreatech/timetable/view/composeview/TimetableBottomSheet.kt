package com.koreatech.timetable.view.composeview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.koreatech.timetable.utils.pxToDp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimetableBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: BottomSheetState,
    scaffoldState: BottomSheetScaffoldState,
    content: @Composable ColumnScope.() -> Unit,
    sheetContent: @Composable ColumnScope.() -> Unit,
) {
    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = sheetContent,
        sheetBackgroundColor = Color.White,
        sheetPeekHeight = 0.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            content()
        }
    }
}

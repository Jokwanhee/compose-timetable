package com.koreatech.timetable.widget.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.layout.Column
import androidx.glance.layout.height

@Composable
fun WidgetColumn(
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier
            .height(40.dp)
    ) {
        for (i in 9..24) {
        }
    }

}
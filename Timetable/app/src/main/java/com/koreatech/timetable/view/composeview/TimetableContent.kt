package com.koreatech.timetable.view.composeview

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.koreatech.timetable.data.model.TimetableEvent
import com.koreatech.timetable.data.model.TimetableEventType
import com.koreatech.timetable.utils.sampleEvents
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Composable
fun TimetableContent(
    modifier: Modifier = Modifier,
    eventContent: @Composable (event: TimetableEvent, eventType: TimetableEventType, onEventClick: (TimetableEvent) -> Unit) -> Unit = { event, eventType, onClick ->
        TimetableEventTime(event = event, eventType = eventType, onEventClick = onClick)
    },
    clickEvent: List<TimetableEvent> = emptyList(),
    events: List<TimetableEvent>,
    dayWidth: Dp,
    hourHeight: Dp,
    onEventClick: (event: TimetableEvent) -> Unit,
) {
    val days = 5
    val dividerColor = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
    val times = 15

    Layout(
        content = {
            events.sortedBy(TimetableEvent::start).forEach { event ->
                Box(modifier = Modifier.eventData(event)) {
                    eventContent(event, TimetableEventType.BASIC, onEventClick = onEventClick)
                }
            }
            if (clickEvent.isNotEmpty()) {
                clickEvent.sortedBy(TimetableEvent::start).forEach { event ->
                    Box(modifier = Modifier.eventData(event)) {
                        eventContent(
                            event,
                            TimetableEventType.SELECTED,
                            onEventClick = onEventClick
                        )
                    }
                }
            }
        },
        modifier = modifier.drawBehind {
            drawLine(
                dividerColor,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                dividerColor,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = 1.dp.toPx()
            )

            repeat(times * 2) {
                drawLine(
                    dividerColor,
                    start = Offset(0f, (it + 1) * (hourHeight / 2).toPx()),
                    end = Offset(size.width, (it + 1) * (hourHeight / 2).toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            }
            repeat(days - 1) {
                drawLine(
                    dividerColor,
                    start = Offset((it + 1) * dayWidth.toPx(), 0f),
                    end = Offset((it + 1) * dayWidth.toPx(), size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    ) { measureables, constraints ->
        val height = hourHeight.roundToPx() * 15
        val width = dayWidth.roundToPx() * days
        val placeablesWithEvents = measureables.map { measurable ->
            val event = measurable.parentData as TimetableEvent
            val eventDurationMinutes = ChronoUnit.MINUTES.between(event.start, event.end)
            val eventHeight = ((eventDurationMinutes / 60f) * hourHeight.toPx()).roundToInt()
            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = dayWidth.roundToPx(), maxWidth = dayWidth.roundToPx(),
                    minHeight = eventHeight, maxHeight = eventHeight
                )
            )
            Pair(placeable, event)
        }

        layout(width, height) {
            placeablesWithEvents.forEach { (placeable, event) ->
                val initStartTime = LocalTime.of(9, 0)
                val eventOffsetMinutes =
                    ChronoUnit.MINUTES.between(initStartTime, event.start)
                val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()

                val eventOffsetDays: Int = when (event.dayOfWeek) {
                    DayOfWeek.MONDAY -> 0
                    DayOfWeek.TUESDAY -> 1
                    DayOfWeek.WEDNESDAY -> 2
                    DayOfWeek.THURSDAY -> 3
                    DayOfWeek.FRIDAY -> 4
                    else -> -1
                }
                val eventX = eventOffsetDays * dayWidth.roundToPx()
                placeable.place(eventX, eventY)
            }
        }
    }
}

private class EventDataModifier(
    val event: TimetableEvent,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = event
}

private fun Modifier.eventData(event: TimetableEvent) =
    this.then(EventDataModifier(event))

@Preview(showBackground = true)
@Composable
fun TimetableContentPreview() {
    TimetableContent(
        events = sampleEvents,
        dayWidth = 68.dp,
        hourHeight = 64.dp,
        onEventClick = {

        }
    )
}
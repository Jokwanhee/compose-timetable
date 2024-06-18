package com.koreatech.timetable.view.composeview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.timetable.data.model.TimetableEvent
import com.koreatech.timetable.data.model.TimetableEventType
import com.koreatech.timetable.data.model.TimetableEventType.BASIC
import com.koreatech.timetable.data.model.TimetableEventType.SELECTED
import com.koreatech.timetable.utils.sampleEvents
import java.time.format.DateTimeFormatter

val timetableEventTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun TimetableEventTime(
    modifier: Modifier = Modifier,
    event: TimetableEvent,
    eventType: TimetableEventType? = null,
    onEventClick: (event: TimetableEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(end = 2.dp, bottom = 2.dp)
            .background(
                color = if (eventType == SELECTED) Color.Transparent else event.color,
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                color = if (eventType == SELECTED) Color.Red else Color.Transparent,
                width = if (eventType == SELECTED) 1.dp else 0.dp,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
            .clickable { onEventClick(event) }
    ) {
        Divider(color = Color.White, thickness = 1.dp)
        Spacer(modifier = Modifier.height(2.dp))
        when (eventType) {
            BASIC -> {
                Text(
                    text = event.start.format(timetableEventTimeFormatter)
                            + " - " +
                            event.end.format(timetableEventTimeFormatter),
                    fontSize = 8.sp,
                    color = Color.White
                )
                Text(
                    text = event.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (event.description != null) {
                    Text(
                        text = event.description,
                        fontSize = 8.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            SELECTED -> {}
            null -> {}
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TimetableEventTimePreview() {
    TimetableEventTime(
        event = sampleEvents[0],
        eventType = BASIC,
        modifier = Modifier.sizeIn(maxHeight = 64.dp),
        onEventClick = {}
    )
}

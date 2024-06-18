package com.koreatech.timetable.view

import android.content.Context
import android.util.AttributeSet
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.koreatech.timetable.data.model.Lecture
import com.koreatech.timetable.data.model.TimetableEvent
import com.koreatech.timetable.utils.basicColors
import com.koreatech.timetable.view.composeview.Timetable
import com.koreatech.timetable.view.viewmodel.HomeViewModel

class TimetableView @OptIn(ExperimentalMaterialApi::class)
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val sheetState: BottomSheetState,
) : AbstractComposeView(context, attrs, defStyleAttr) {
    lateinit var onTimetableEventClickListener: OnTimetableEventClickListener

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val viewModel = viewModel<HomeViewModel>()
        val colors by viewModel.colors.collectAsStateWithLifecycle()
        val lectureEvents by viewModel.lectureEvent.collectAsStateWithLifecycle()
        val timetableEvents by viewModel.timetableEvents.collectAsStateWithLifecycle()

        Timetable(
            colors = colors,
            sheetState = sheetState,
            events = generateTimetableEvents(timetableEvents, colors),
            onEventClick = onTimetableEventClickListener::onEventClick,
            clickEvent = lectureEvents
        )
    }

    private fun generateTimetableEvents(timetableEvents: List<Lecture>, colors: List<Color>): List<TimetableEvent> {
        val updateTimetableEvents = mutableListOf<TimetableEvent>()
        timetableEvents.mapIndexed { index, lecture ->
            lecture.toTimetableEvents(index, colors)
        }.map {
            it.forEach {
                updateTimetableEvents.add(it)
            }
        }

        return updateTimetableEvents
    }

    interface OnTimetableEventClickListener {
        fun onEventClick(event: TimetableEvent)
    }

    inline fun setOnTimetableEventClickListener(crossinline onEventClick: (TimetableEvent) -> Unit) {
        this.onTimetableEventClickListener = object : OnTimetableEventClickListener {
            override fun onEventClick(event: TimetableEvent) {
                onEventClick(event)
            }
        }
    }
}
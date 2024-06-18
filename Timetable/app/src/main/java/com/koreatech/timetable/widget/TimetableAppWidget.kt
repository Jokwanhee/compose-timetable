package com.koreatech.timetable.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.LocalAppWidgetOptions
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koreatech.timetable.R
import com.koreatech.timetable.data.datastore.DataStoreImpl
import com.koreatech.timetable.data.model.Lecture
import com.koreatech.timetable.data.model.TimetableEvent
import com.koreatech.timetable.utils.basicColors
import com.koreatech.timetable.utils.toTimeBlocks
import com.koreatech.timetable.widget.component.WidgetHeader
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import java.time.LocalTime

object TimetableAppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext
        val dataStoreImpl = getDataStoreImpl(appContext)

        provideContent {
            val state = currentState<Preferences>()
            val realSemester = state[stringPreferencesKey("semester")] ?: ""
            val lastUpdated = state[stringPreferencesKey("time")] ?: ""
            var lectureString by rememberSaveable {
                mutableStateOf("")
            }
            var colorString by rememberSaveable {
                mutableStateOf("")
            }
            val timetableEvents = remember {
                mutableStateOf<MutableList<TimetableEvent>>(mutableListOf())
            }
            val timeBlocks = remember {
                mutableStateOf<MutableList<List<TimeBlock?>>>(mutableListOf())
            }
            val colors = remember {
                mutableStateOf<MutableList<Color>>(mutableListOf())
            }

            Log.e("aaa", "realSemester : $realSemester")
            Log.e("aaa", "lastUpdated : $lastUpdated")
            Log.e("aaa", "lectureString : $lectureString")
            Log.e("aaa", "colorString : $colorString")
            Log.e("aaa", "timetableEvents : $timetableEvents")
            Log.e("aaa", "timeBlocks : $timeBlocks")
            Log.e("aaa", "timeBlocks size : ${timeBlocks.value.size}")

            LaunchedEffect(key1 = lastUpdated) {
                colorString = dataStoreImpl.getColorString().first()
                val colorType = object : TypeToken<List<Color>>() {}.type
                val gson = Gson()
                val updateColors = gson.fromJson<List<Color>>(colorString, colorType).orEmpty()
                colors.value = if(updateColors.isEmpty()) basicColors.toMutableList() else updateColors.toMutableList()

                lectureString = dataStoreImpl.getString(realSemester).first()
                val dataType = object : TypeToken<List<Lecture>>() {}.type
                val lectures = gson.fromJson<List<Lecture>>(lectureString, dataType).orEmpty()
                val getTimetableEvents = generateTimetableEvents(lectures, colors.value)
                timetableEvents.value = getTimetableEvents.toMutableList()

                val lists = MutableList<List<TimeBlock?>>(5) { emptyList() }
                val mondays =
                    generateTimetableEvents(lectures, colors.value).filter { it.dayOfWeek == DayOfWeek.MONDAY }
                        .sortedBy { it.start }.toTimeBlocks()
                val tuesdays =
                    generateTimetableEvents(lectures, colors.value).filter { it.dayOfWeek == DayOfWeek.TUESDAY }
                        .sortedBy { it.start }.toTimeBlocks()
                val wednesdays =
                    generateTimetableEvents(lectures, colors.value).filter { it.dayOfWeek == DayOfWeek.WEDNESDAY }
                        .sortedBy { it.start }.toTimeBlocks()
                val thursdays =
                    generateTimetableEvents(lectures, colors.value).filter { it.dayOfWeek == DayOfWeek.THURSDAY }
                        .sortedBy { it.start }.toTimeBlocks()
                val fridays =
                    generateTimetableEvents(lectures, colors.value).filter { it.dayOfWeek == DayOfWeek.FRIDAY }
                        .sortedBy { it.start }.toTimeBlocks()

                lists[0] = mondays
                lists[1] = tuesdays
                lists[2] = wednesdays
                lists[3] = thursdays
                lists[4] = fridays

                timeBlocks.value = lists
            }

            val width =
                LocalAppWidgetOptions.current.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)

            LazyColumn(
                modifier = GlanceModifier.fillMaxSize()
                    .background(imageProvider = ImageProvider(R.drawable.dragon))
            ) {
                item {
                    WidgetHeader()
                }
                item {
                    Row(
                        modifier = GlanceModifier.fillMaxWidth()
                    ) {
                        if (timeBlocks.value.isNotEmpty()) {
                            repeat(timeBlocks.value.size + 2) { index ->
                                Column(
                                    modifier = GlanceModifier
                                        .width((width / 6f).dp)
                                        .fillMaxHeight(),
                                ) {
                                    for (i in 9..18) {
                                        if (index == 0) {
                                            Box(
                                                modifier = GlanceModifier
                                                    .fillMaxWidth()
                                                    .height(60.dp)
                                                    .background(imageProvider = ImageProvider(R.drawable.shape_timetable_row)),
                                                contentAlignment = Alignment.TopEnd
                                            ) {
                                                Text(
                                                    text = "$i",
                                                    style = TextStyle(
                                                        fontSize = 14.sp,
                                                        color = ColorProvider(Color.Gray)
                                                    ),
                                                    modifier = GlanceModifier
                                                        .padding(top = 2.dp, end = 2.dp)
                                                )
                                            }
                                        } else if (index in 1..5) {
                                            val timeBlock = timeBlocks.value[index - 1][i - 9]
                                            Box(
                                                modifier = GlanceModifier
                                                    .fillMaxWidth()
                                                    .height(
                                                        if (timeBlock == null) {
                                                            60.dp
                                                        } else {
                                                            (timeBlock.duration * 60).dp
                                                        }
                                                    )
                                                    .padding(
                                                        top = ((timeBlock?.startDuration
                                                            ?: 0f) * 60).dp,
                                                        end = 2.dp
                                                    )
                                                    .background(imageProvider = ImageProvider(R.drawable.shape_timetable_row))
                                            ) {
                                                Column(
                                                    modifier = GlanceModifier
                                                        .fillMaxWidth()
                                                        .height(
                                                            ((timeBlock?.endDuration ?: 0f) * 60).dp
                                                        )
                                                        .padding(2.dp)
                                                        .background(
                                                            timeBlock?.color ?: Color.Transparent
                                                        )
                                                ) {
                                                    Box(
                                                        modifier = GlanceModifier
                                                            .fillMaxWidth()
                                                            .height(2.dp)
                                                            .background(Color.White)
                                                    ) {

                                                    }
                                                    Text(
                                                        text = timeBlock?.title ?: "",
                                                        style = TextStyle(
                                                            fontSize = 12.sp,
                                                            color = ColorProvider(Color.White),
                                                            fontWeight = FontWeight.Bold
                                                        ),
                                                    )
                                                    Text(
                                                        text = if (timeBlock?.start == null || timeBlock?.end == null) "" else "${timeBlock.start} - ${timeBlock.end}",
                                                        style = TextStyle(
                                                            fontSize = 8.sp,
                                                            color = ColorProvider(Color.White),
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    private fun generateTimetableEvents(
        timetableEvents: List<Lecture>,
        colors: List<Color>,
    ): List<TimetableEvent> {
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

    private fun getDataStoreImpl(context: Context): DataStoreImpl {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context, WidgetEntryPoint::class.java
        )
        return hiltEntryPoint.dataStoreImpl()
    }

}

data class TimeBlock(
    val title: String = "",
    val start: LocalTime = LocalTime.of(0, 0),
    val end: LocalTime = LocalTime.of(0, 0),
    val startDuration: Float = 0f,
    val endDuration: Float = 0f,
    val duration: Float = 0f,
    val color: Color? = null,
)

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun dataStoreImpl(): DataStoreImpl
}


suspend fun updateWidget(
    context: Context,
    semester: String,
) {
    val currentTimeMillis = System.currentTimeMillis().toString()
    GlanceAppWidgetManager(context).getGlanceIds(TimetableAppWidget.javaClass).forEach { glanceId ->
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[stringPreferencesKey("semester")] = semester
            prefs[stringPreferencesKey("time")] = currentTimeMillis
        }
    }
    TimetableAppWidget.updateAll(context)
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        TimetableAppWidget.update(context, glanceId)
    }
}
package com.koreatech.timetable.data.model

import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName
import com.koreatech.timetable.utils.basicColors
import com.koreatech.timetable.utils.toQueryString
import java.time.DayOfWeek
import java.time.LocalTime

data class Lectures(
    @SerializedName("lectures")
    val lectures: List<Lecture> = emptyList(),
)

data class Lecture(
    var id: Int? = 0,
    @SerializedName("code") // 과목코드 : BSM314
    val code: String? = "",
    @SerializedName("name") // 강의명 : 물리적 사고
    val name: String? = "",
    @SerializedName("grades") // 학년 : 3
    val grades: String? = "",
    @SerializedName("lecture_class") // 분반 : 01
    val lectureClass: String? = "",
    @SerializedName("regular_number") // 수강인원 : 0~40
    val regularNumber: String? = "",
    @SerializedName("department") // 학부 : 교양학부
    val department: String? = "",
    @SerializedName("target") // 대상 : 기공1
    val target: String? = "",
    @SerializedName("professor") // 교수 : 이미리
    val professor: String? = "",
    @SerializedName("is_english") // 영어수업인지 : N/Y
    val isEnglish: String? = "",
    @SerializedName("design_score") // 설계학점 : 0
    val designScore: String? = "",
    @SerializedName("is_elearning") // 이러닝인지 : N/Y
    val isElearning: String? = "",
    @SerializedName("class_time") // 강의시간 : 0~429
    val classTime: List<Int> = emptyList(),
) {
    fun toTimetableEvents(index: Int? = null, colors: List<Color>): List<TimetableEvent> {
        val events = mutableListOf<TimetableEvent>()
        /**
         * @input : {MONDAY=[09:00, 09:30], TUESDAY=[09:00, 09:30]}
         */
        findDayOfWeekAndTime().forEach { (key, value) ->
            val timetableEvent = TimetableEvent(
                id = id ?: 0,
                name = name.toString(),
                color = colors[index ?: 0],
                dayOfWeek = key,
                start = value.firstOrNull() ?: LocalTime.of(0, 0),
                end = value.lastOrNull()?.plusMinutes(30) ?: LocalTime.of(0, 0),
                description = null
            )
            events.add(timetableEvent)
        }
        /**
         * @output :
         * [
         *  TimetableEvent(0, "강의이름1", 색상1, MONDAY, 09:00, 09:30, null),
         *  TimetableEvent(0, "강의이름2", 색상2, TUESDAY, 09:00, 09:30, null),
         * ]
         */
        return events
    }

    fun findDayOfWeekAndTime(): Map<DayOfWeek?, List<LocalTime>> {
        return classTime.groupBy { it / 100 }
            .mapValues { entry ->
                /**
                 * @input : [0,1,100,101]
                 */
                entry.value.sorted().map { value ->
                    val timeIndex = if (entry.key == 0) value else value % (entry.key * 100)
                    LocalTime.of(9 + timeIndex / 2, (timeIndex % 2) * 30)
                }
                /**
                 * @output : [09:00, 09:30], [09:00, 09:30]
                 */
            }
            .mapKeys {
                /**
                 * @input : {0=[09:00, 09:30], 1=[09:00, 09:30]}
                 */
                when (it.key) {
                    0 -> DayOfWeek.MONDAY
                    1 -> DayOfWeek.TUESDAY
                    2 -> DayOfWeek.WEDNESDAY
                    3 -> DayOfWeek.THURSDAY
                    4 -> DayOfWeek.FRIDAY
                    else -> null
                }
                /**
                 * @output : {MONDAY=[09:00, 09:30], TUESDAY=[09:00, 09:30]}
                 */
            }
    }

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$name",
            "${name?.first()}"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }

    fun doesMatchDepartmentSearchQuery(departments: List<String>): Boolean {
        val matchingCombination = "${department?.toQueryString()}"

        return departments.any {
            it.contains(matchingCombination, ignoreCase = true)
        }
    }

    /**
     * 시간표 강의 중복
     * @example : 강의 시간 겹침 + 완전 준복
     */
    fun duplicate(lectures: List<Lecture>): Boolean {
        var flag = false
        classTime.forEach { time ->
            if (lectures.filter { it.classTime.contains(time) }.isNotEmpty()) {
                flag = true
            }
        }
        return flag
    }
}



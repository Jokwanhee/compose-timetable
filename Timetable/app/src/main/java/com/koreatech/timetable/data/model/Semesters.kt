package com.koreatech.timetable.data.model

import com.google.gson.annotations.SerializedName

data class Semesters(
    @SerializedName("semesters")
    val semesters: List<Semester> = emptyList(),
)

data class Semester(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("semester")
    val semester: String? = "",
) {
    /**
     * 20242 => 2024년 2학기
     */
    fun formatSemester() = "${semester?.take(4)}년 ${semester?.drop(4)}학기"
}
package com.koreatech.timetable.data.source

import android.content.Context
import android.util.Log
import com.koreatech.timetable.data.model.Department
import com.koreatech.timetable.data.model.Departments
import com.koreatech.timetable.data.model.Lecture
import com.koreatech.timetable.data.model.Lectures
import com.koreatech.timetable.data.model.Semester
import com.koreatech.timetable.data.model.Semesters
import com.koreatech.timetable.utils.readData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LocalDataSource(
    private val context: Context,
) {
    suspend fun loadLectures(): List<Lecture>? = withContext(Dispatchers.IO) {
        val lectures = context.readData<Lectures>("timetable.json")?.lectures

        lectures?.forEachIndexed { index, lecture ->
            lecture.id = index + 1
        }

        return@withContext lectures
    }

    fun loadSemesters(): List<Semester>? =
        context.readData<Semesters>("semester.json")?.semesters

    fun loadDepartments(): List<Department>? =
        context.readData<Departments>("department.json")?.departments
}


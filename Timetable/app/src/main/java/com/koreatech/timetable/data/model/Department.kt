package com.koreatech.timetable.data.model

import com.google.gson.annotations.SerializedName

data class Departments(
    @SerializedName("departments")
    val departments: List<Department> = emptyList(),
)

data class Department(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
)
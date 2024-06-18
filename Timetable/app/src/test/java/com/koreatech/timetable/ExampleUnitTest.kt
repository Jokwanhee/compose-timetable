package com.koreatech.timetable

import org.junit.Test

import org.junit.Assert.*
import java.time.Duration
import java.time.LocalTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val startTime = LocalTime.of(9, 0)
        val endTime = LocalTime.of(11, 30)

        val duration = Duration.between(startTime, endTime)

        println(duration.toHours())
        println(duration.toMinutes() / 60.0)
    }
    @Test
    fun sortDuration() {
        val times = listOf(
            LocalTime.of(9,30),
            LocalTime.of(9,0),
            LocalTime.of(9,50),
            LocalTime.of(9,10),
        )

        println(times.sorted())
    }
}
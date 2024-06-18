package com.koreatech.timetable.utils

import androidx.compose.ui.graphics.Color
import com.koreatech.timetable.data.model.TimetableEvent
import java.time.DayOfWeek
import java.time.LocalTime

val sampleEvents = listOf(
    TimetableEvent(
        id = 1,
        name = "Google I/O Keynote",
        color = Color(0xFFAFBBF2),
        dayOfWeek = DayOfWeek.FRIDAY,
        start = LocalTime.of(16, 0),
        end = LocalTime.of(18, 0),
        description = "Tune in to find out about how we're furthering our mission to organize the world’s information and make it universally accessible and useful.",
    ),
    TimetableEvent(
        id = 2,
        name = "Developer Keynote",
        color = Color(0xFFDEE4FF),
        dayOfWeek = DayOfWeek.THURSDAY,
        start = LocalTime.of(14, 0),
        end = LocalTime.of(16, 0),
        description = "Learn about the latest updates to our developer products and platforms from Google Developers.",
    ),
    TimetableEvent(
        id = 3,
        name = "What's new in Android",
        color = Color(0xFF1B998B),
        dayOfWeek = DayOfWeek.MONDAY,
        start = LocalTime.of(12, 0),
        end = LocalTime.of(13, 30),
        description = "In this Keynote, Chet Haase, Dan Sandler, and Romain Guy discuss the latest Android features and enhancements for developers.",
    ),
    TimetableEvent(
        id = 4,
        name = "What's new in Machine Learning",
        color = Color(0xFFF4BFDB),
        dayOfWeek = DayOfWeek.WEDNESDAY,
        start = LocalTime.of(9, 0),
        end = LocalTime.of(10, 30),
        description = "Learn about the latest and greatest in ML from Google. We’ll cover what’s available to developers when it comes to creating, understanding, and deploying models for a variety of different applications.",
    ),
    TimetableEvent(
        id = 5,
        name = "What's new in Material Design",
        color = Color(0xFF6DD3CE),
        dayOfWeek = DayOfWeek.TUESDAY,
        start = LocalTime.of(16, 0),
        end = LocalTime.of(18, 0),
        description = "Learn about the latest design improvements to help you build personal dynamic experiences with Material Design.",
    ),
    TimetableEvent(
        id = 6,
        name = "Jetpack Compose Basics",
        color = Color(0xFF1B998B),
        dayOfWeek = DayOfWeek.WEDNESDAY,
        start = LocalTime.of(16, 0),
        end = LocalTime.of(18, 0),
        description = "This Workshop will take you through the basics of building your first app with Jetpack Compose, Android's new modern UI toolkit that simplifies and accelerates UI development on Android.",
    ),
)

val ironManColors = listOf(
    Color(0xFFFF3C3C),
    Color(0xFFFF0D0D),
    Color(0xFFD50000),
    Color(0xFFFFE170),
    Color(0xFFFFD740),
    Color(0xFFEECD53),
    Color(0xFFEAEFF1),
    Color(0xFFCFD8DC),
    Color(0xFFAAC4CF)
)

val basicColors = listOf(
    Color(0xFFBFC8D7),
    Color(0xFFE2D2D2),
    Color(0xFFE3E3B4),
    Color(0xFFA2B59F),
    Color(0xFFA8B0BD),
    Color(0xFFC9BBBB),
    Color(0xFFC9C9A0),
    Color(0xFF8B9C89),
    Color(0xFF9198A3),
    Color(0xFFB0A3A3),
    Color(0xFFB0B08C),
    Color(0xFF748272),
)

val ceramicColors = listOf(
    Color(0xFFE1E5E8),
    Color(0xFFC1BFCD),
    Color(0xFFB2D1E4),
    Color(0xFF8ABAE0),
    Color(0xFFA6AAAD),
    Color(0xFFA8A6B2),
    Color(0xFF9DB9C9),
    Color(0xFF7BA5C7),
    Color(0xFF8E9194),
    Color(0xFF908F99),
    Color(0xFF89A2B0),
    Color(0xFF6B90AD),
)

val macaroonColors = listOf(
    Color(0xFFF8DE9D),
    Color(0xFFFFCECA),
    Color(0xFFFFD0A7),
    Color(0xFFE098AE),
    Color(0xFFDEC78C),
    Color(0xFFE5B9B6),
    Color(0xFFE5BB96),
    Color(0xFFC7879B),
    Color(0xFFC4B07C),
    Color(0xFFCCA5A2),
    Color(0xFFCCA786),
    Color(0xFFAD7686),
)
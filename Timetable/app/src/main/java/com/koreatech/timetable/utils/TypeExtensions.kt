package com.koreatech.timetable.utils

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Float.pxToDp: Dp
    get() = (this / Resources.getSystem().displayMetrics.density).dp

fun String.toQueryString(): String = when(this) {
    "HRD학과" -> "HRD"
    "고용서비스정책학과" -> "고용서비스"
    "교양학부" -> "교양"
    "디자인ㆍ건축공학부" -> "디자인"
    "메카트로닉스공학부" -> "메카트로닉스"
    "산업경영학부" -> "산업경영"
    "에너지신소재화학공학부" -> "에너지신소재"
    "융합학과" -> "융합"
    "전기ㆍ전자ㆍ통신공학부" -> "전기"
    "컴퓨터공학부" -> "컴퓨터"
    "안전공학과" -> "안전"
    "기계공학부" -> "기계"
    else -> ""
}
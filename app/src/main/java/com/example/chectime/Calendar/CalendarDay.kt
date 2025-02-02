package com.example.chectime

data class CalendarDay(
    val year: Int,
    val month: Int,
    val day: Int,
    val isValid: Boolean // 유효한 날짜인지 여부 (빈 날짜가 아닌 경우)
)

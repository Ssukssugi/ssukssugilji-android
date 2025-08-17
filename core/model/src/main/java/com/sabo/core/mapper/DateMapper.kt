package com.sabo.core.mapper

import java.time.DayOfWeek
import java.time.LocalDate

object DateMapper {
    fun LocalDate.toDisplayDayOfWeek(): String {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> "월요일"
            DayOfWeek.TUESDAY -> "화요일"
            DayOfWeek.WEDNESDAY -> "수요일"
            DayOfWeek.THURSDAY -> "목요일"
            DayOfWeek.FRIDAY -> "금요일"
            DayOfWeek.SATURDAY -> "토요일"
            DayOfWeek.SUNDAY -> "일요일"
            null -> ""
        }
    }

    fun String.toLocalDate(): LocalDate {
        val formatted = this.split("-").map { it.toInt() }
        return LocalDate.of(formatted[0], formatted[1], formatted[2])
    }
}
package ru.eng.ai.tool

import kotlin.time.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun getCurrentTimeAsClock(): String {
    val currentTimeInstant = Clock.System.now()
    val userTimezone = TimeZone.currentSystemDefault()
    val clockFormat = LocalDateTime.Format { hour(); char(':'); minute() }
    val localDateTime = currentTimeInstant.toLocalDateTime(userTimezone)
    return localDateTime.format(clockFormat)
}

fun getTomorrowTimestamp(): Long {
    val now = Clock.System.now()
    val systemTimeZone = TimeZone.currentSystemDefault()
    val tomorrow = now.plus(1, DateTimeUnit.DAY, systemTimeZone)
    val localDateTime = tomorrow.toLocalDateTime(systemTimeZone)
    val tomorrowAtStartOfDay = localDateTime.date.atTime(LocalTime(0, 0))
    val instant = tomorrowAtStartOfDay.toInstant(systemTimeZone)
    return instant.epochSeconds
}
package com.theophiluskibet.dtasks.helpers

import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private val timeZone = TimeZone.currentSystemDefault()

@OptIn(ExperimentalTime::class)
val Instant.LocalDateTime: LocalDateTime
    get() = toLocalDateTime(timeZone)

@OptIn(ExperimentalTime::class)
val LocalDateTime.Instant: Instant
    get() = toInstant(timeZone)

@OptIn(ExperimentalTime::class)
fun LocalDateTime.asEpochMilliseconds(): Long = this.Instant.toEpochMilliseconds()

@OptIn(ExperimentalTime::class)
fun Long.asLocalDateTime(): LocalDateTime = Instant.fromEpochMilliseconds(this).LocalDateTime

@OptIn(ExperimentalTime::class)
fun LocalDateTime?.formatDueDate(): String {
    if (this == null) return "No due date"

    val today = Clock.System.now().toLocalDateTime(timeZone).date
    val dueLocalDate = this.date

    val daysDiff = today.daysUntil(dueLocalDate)

    return when {
        daysDiff == 0 -> "Due Today"
        daysDiff == 1 -> "Due Tomorrow"
        daysDiff == -1 -> "Due Yesterday"
        daysDiff > 1 -> "Due in $daysDiff days"
        daysDiff < -1 -> "Overdue by ${-daysDiff} days"
        else -> dueLocalDate.format(LocalDate.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            dayOfMonth()
            chars(", ")
            year()
        })
    }
}

@OptIn(ExperimentalTime::class)
fun String.asLocalDateTime(): LocalDateTime {
    val localDateTime = LocalDateTime.parse(this)
    val instant = localDateTime.toInstant(timeZone)
    return instant.LocalDateTime
}

@OptIn(ExperimentalTime::class)
fun Long.asString(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    return instant.toString()
}


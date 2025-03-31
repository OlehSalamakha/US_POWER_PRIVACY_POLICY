package com.example.uspower

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.example.uspower.core.LoadState
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.toDuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration


fun ComponentContext.componentCoroutineScope(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    } else {
        scope.cancel()
    }

    return scope
}



fun <T : Any> Value<T>.toStateFlow(lifecycle: Lifecycle): StateFlow<T> {
    val state = MutableStateFlow(this.value)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        val observer = { value: T -> state.value = value }
        val cancellation = subscribe(observer)
        lifecycle.doOnDestroy {
            cancellation.cancel()
        }
    }

    return state
}


fun <T> Flow<T>.wrapToLoadState(): Flow<LoadState<T>> {
    return this
        .map<T, LoadState<T>> { data -> LoadState.Success(data) } // Wrap data in Loaded
        .onStart { emit(LoadState.Loading) } // Emit Loading at the start
        .catch {
            throwable -> emit(LoadState.Error(throwable))
        } // Emit Failed on error
}

fun Duration.toHourOfDay(): String {
    val dateFormat = LocalDateTime.Format {
        amPmHour()
        char(':')
        minute()
        amPmMarker(" AM", " PM")
    }
    val fromEpochMillisecondsFromEpochMilliseconds = Instant.fromEpochMilliseconds(this.inWholeMilliseconds).toLocalDateTime(TimeZone.currentSystemDefault())
    return dateFormat.format(fromEpochMillisecondsFromEpochMilliseconds)
//    val instant = Instant.fromEpochMilliseconds(this.inWholeMilliseconds)
//    val time = instant.toLocalDateTime(TimeZone.currentSystemDefault()).time
//    return "${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}"
}
fun isYesterday(instant: Instant): Boolean {
    val now = Clock.System.now()

    val today = now.toLocalDateTime(TimeZone.UTC).date
    val yesterday = today.minus(1, DateTimeUnit.DAY)
    val dateToCheck = instant.toLocalDateTime(TimeZone.UTC).date

    return dateToCheck == yesterday
}

fun isToday(instant: Instant): Boolean {
    val now = Clock.System.now()
    val today = now.toLocalDateTime(TimeZone.UTC).date
    val dateToCheck = instant.toLocalDateTime(TimeZone.UTC).date

    return dateToCheck == today
}

fun getDateText(currentTimestamp: Timestamp): String {
    currentTimestamp.apply {
        val instant = Instant.fromEpochMilliseconds(currentTimestamp.toDuration().inWholeMilliseconds)
        println("1005001, ${instant.toLocalDateTime(TimeZone.currentSystemDefault())}")
        return when {
            isToday(instant) -> "Today"
            isYesterday(instant) -> "Yesterday"
            else -> formatDate(currentTimestamp)
        }
    }
}

private fun formatDate(timestamp: Timestamp): String {
    val dateFormat = LocalDateTime.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        char(',')
        char(' ')
        dayOfMonth()
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
    }
    val fromEpochMillisecondsFromEpochMilliseconds = Instant.fromEpochMilliseconds(timestamp.toDuration().inWholeMilliseconds).toLocalDateTime(TimeZone.currentSystemDefault())
    return dateFormat.format(fromEpochMillisecondsFromEpochMilliseconds)
}

fun shouldShowDate(previousTimestamp: Timestamp?, currentTimestamp: Timestamp): Boolean {
    if (previousTimestamp == null) return true

    val previousCalendar = Instant.fromEpochMilliseconds(previousTimestamp.toDuration().inWholeMilliseconds).toLocalDateTime(TimeZone.currentSystemDefault())
    val currentCalendar = Instant.fromEpochMilliseconds(currentTimestamp.toDuration().inWholeMilliseconds).toLocalDateTime(TimeZone.currentSystemDefault())

    return previousCalendar.year != currentCalendar.year ||
            previousCalendar.dayOfYear != currentCalendar.dayOfYear
}

fun Log(tag: String, message: String) {
    println("$tag $message")
}
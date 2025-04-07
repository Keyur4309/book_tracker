package com.keyur43.book_tracker.ui.stats

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keyur43.book_tracker.db.ProgressDao
import com.keyur43.book_tracker.util.toHourAndMinute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val progressDao: ProgressDao
) : ViewModel() {

    var timeRead by mutableStateOf("")
    var pagesRead by mutableStateOf("")

    init {
        getStats()
    }

    fun getStats() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance().apply {
                timeZone = TimeZone.getDefault()
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                val dayOfWeek = get(Calendar.DAY_OF_WEEK)
                val daysFromMonday = (dayOfWeek + 5) % 7
                add(Calendar.DAY_OF_MONTH, -daysFromMonday)
            }

            val formatted = SimpleDateFormat("HH:mm dd MM yy", Locale.getDefault())
                .format(Date(calendar.timeInMillis))

            Timber.d("Start of Week = ${calendar.timeInMillis} === $formatted")

            timeRead = progressDao.getMinutesReadThisWeek(calendar.timeInMillis)?.toHourAndMinute() ?: "0 h 0 m"
            pagesRead = progressDao.getPagesReadThisWeek(calendar.timeInMillis)?.toString() ?: "0"

            Timber.d("Read time = $timeRead, Pages read = $pagesRead")
        }
    }
}
package com.example.scheduleapp.data.classes

import androidx.annotation.StringRes
import com.example.scheduleapp.R

enum class ScheduleSortMode(
    @StringRes val displayName: Int
) {
    ALPHABETICAL(R.string.alphabetical),
    ALPHABETICAL_DESC(R.string.alphabeticalDesc),
    RECENTLY_USED(R.string.recentlyUsed)
}
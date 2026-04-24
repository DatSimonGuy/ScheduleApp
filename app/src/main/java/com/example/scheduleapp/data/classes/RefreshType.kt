package com.example.scheduleapp.data.classes

import androidx.annotation.StringRes
import com.example.scheduleapp.R

enum class RefreshType(
    @StringRes val displayName: Int
) {
    AUTOMATIC(R.string.automatic),
    ON_APP_RELOAD(R.string.onReload),
    MANUAL(R.string.manual)
}
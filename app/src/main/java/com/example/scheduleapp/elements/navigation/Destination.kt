import android.annotation.SuppressLint
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.example.scheduleapp.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@Serializable
sealed class Destination {
    abstract val displayName: Int?
    abstract val id: Int
    @Serializable
    object Home : Destination() {
        override val displayName = R.string.home
        override val id = 0
    }
    @Serializable
    object Schedule : Destination() {
        override val displayName = R.string.schedule
        override val id = 1
    }
    @Serializable
    object Settings : Destination() {
        override val displayName = R.string.settings
        override val id = 2
    }

    companion object {
        val main by lazy { listOf(Home, Schedule, Settings) }
    }
}

@Serializable
sealed class SettingsDestination : Destination() {
    override val displayName: Int? = null
    @Serializable
    object SettingsPage : SettingsDestination() { override val id = 20 }
    @Serializable
    object GeneralSettings : SettingsDestination() { override val id = 21 }
    @Serializable
    object SchedulesSettings: SettingsDestination() { override val id = 22 }
    @Serializable
    object AppearanceSettings: SettingsDestination() { override val id = 23 }
    @Serializable
    object AccessibilitySettings: SettingsDestination() { override val id = 24}
    @Serializable
    object AboutSettings: SettingsDestination() { override val id = 25 }
    @Serializable
    object ColorSettings: SettingsDestination() { override val id = 230 }
}

@Serializable
sealed class ScheduleDestination : Destination() {
    override val displayName: Int? = null
    @Serializable
    object ScheduleScreen : ScheduleDestination() { override val id = 10 }
    @Serializable
    data class LessonScreen(
        val dayOfWeek: DayOfWeek,
        val lessonId: String
    ) : ScheduleDestination() { override val id = 11 }
}

fun NavDestination?.isScheduleDestination(): Boolean =
    this?.let {
        it.hasRoute<ScheduleDestination.ScheduleScreen>() ||
        it.hasRoute<ScheduleDestination.LessonScreen>()
    } == true

fun NavDestination?.isSettingsDestination(): Boolean =
    this?.let {
        it.hasRoute<SettingsDestination.SettingsPage>() ||
        it.hasRoute<SettingsDestination.GeneralSettings>() ||
        it.hasRoute<SettingsDestination.SchedulesSettings>() ||
        it.hasRoute<SettingsDestination.AppearanceSettings>() ||
        it.hasRoute<SettingsDestination.AccessibilitySettings>() ||
        it.hasRoute<SettingsDestination.AboutSettings>() ||
        it.hasRoute<SettingsDestination.ColorSettings>()
    } == true
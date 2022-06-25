package ge.gmikeladze.assignment4.view_model

import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ge.gmikeladze.assignment4.R
import ge.gmikeladze.assignment4.model.AlarmItem

class MainViewModel(
    systemTheme: Int,
    private val preferences: SharedPreferences
) : ViewModel() {
    var switchText: MutableLiveData<Int> = MutableLiveData()

    init {
        initTheme(systemTheme)
    }

    private fun initTheme(systemTheme: Int) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        when (systemTheme) {
            Configuration.UI_MODE_NIGHT_YES -> {
                switchText.value = R.string.switch_to_light
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                switchText.value = R.string.switch_to_dark
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    fun changeSwitchText() {
        if (switchText.value == R.string.switch_to_dark) {
            switchText.value = R.string.switch_to_light
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            preferences
                .edit()
                .putInt(THEME_PREFERENCE_NAME, Configuration.UI_MODE_NIGHT_YES)
                .apply()
        } else {
            switchText.value = R.string.switch_to_dark
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            preferences
                .edit()
                .putInt(THEME_PREFERENCE_NAME, Configuration.UI_MODE_NIGHT_NO)
                .apply()
        }

    }

    fun saveToSharedPreferences(alarmItems: MutableList<AlarmItem>) {
        val temp = mutableSetOf<String>()
        for (alarm in alarmItems) {
            temp.add(alarm.time + if (alarm.isSwitched) "T" else "F")
        }
        preferences.edit().putStringSet(ALARM_ITEMS_NAME, temp).apply()
    }

    companion object {
        const val PREFERENCE_NAME = "alarmAppPref"
        const val THEME_PREFERENCE_NAME = "alarmAppTheme"
        const val ALARM_ITEMS_NAME = "alarmItems"
    }

}
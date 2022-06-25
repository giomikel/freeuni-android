package ge.gmikeladze.assignment4.view_model_factory

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ge.gmikeladze.assignment4.view_model.MainViewModel
import ge.gmikeladze.assignment4.view_model.MainViewModel.Companion.PREFERENCE_NAME
import ge.gmikeladze.assignment4.view_model.MainViewModel.Companion.THEME_PREFERENCE_NAME

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val theme = preferences.getInt(
                THEME_PREFERENCE_NAME, context.resources.configuration.uiMode
                        and Configuration.UI_MODE_NIGHT_MASK
            )
            return MainViewModel(
                theme,
                preferences
            ) as T
        }
        throw IllegalStateException("Invalid View Model")
    }
}
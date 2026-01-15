package com.example.eatsbuddy.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val prefs = application.getSharedPreferences("eatsbuddy_prefs", Context.MODE_PRIVATE)
    
    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()
    
    private fun loadThemeMode(): ThemeMode {
        val savedMode = prefs.getString("theme_mode", ThemeMode.SYSTEM.name)
        return try {
            ThemeMode.valueOf(savedMode ?: ThemeMode.SYSTEM.name)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }
    }
    
    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            prefs.edit().putString("theme_mode", mode.name).apply()
            _themeMode.value = mode
        }
    }
    
    fun toggleTheme() {
        val newMode = when (_themeMode.value) {
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.SYSTEM -> ThemeMode.DARK
        }
        setThemeMode(newMode)
    }
}

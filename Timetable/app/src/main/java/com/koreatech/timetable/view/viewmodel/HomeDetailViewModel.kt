package com.koreatech.timetable.view.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeDetailViewModel : ViewModel() {
    private val _className = MutableStateFlow<String>("")
    val className = _className.asStateFlow()

    fun updateClassName (name: String) {
        _className.update { name }
    }
}
package com.koreatech.timetable.view.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koreatech.timetable.data.datastore.DataStoreImpl
import com.koreatech.timetable.data.model.Department
import com.koreatech.timetable.data.model.Lecture
import com.koreatech.timetable.data.model.Semester
import com.koreatech.timetable.data.model.TimetableEvent
import com.koreatech.timetable.data.source.LocalDataSource
import com.koreatech.timetable.utils.basicColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val dataStoreImpl: DataStoreImpl,
) : ViewModel() {
    private val _colors = MutableStateFlow<List<Color>>(emptyList())
    val colors = _colors.asStateFlow()

    private val _departments = MutableStateFlow<List<Department>>(emptyList())
    val departments = _departments.asStateFlow()

    private val _selectedDepartments = MutableStateFlow<List<Department>>(emptyList())
    val selectedDepartments = _selectedDepartments.asStateFlow()

    private val _currentDepartments = MutableStateFlow<List<Department>>(emptyList())
    val currentDepartments = _currentDepartments.asStateFlow()

    private val _semesters = MutableStateFlow<List<Semester>>(emptyList())
    val semesters = _semesters.asStateFlow()

    private val _currentSemester = MutableStateFlow(Semester())
    val currentSemester = _currentSemester.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _lectures = MutableStateFlow<List<Lecture>>(emptyList())
    val lectures = searchText
        .combine(_currentDepartments) { text, currentDepartments -> text to currentDepartments }
        .combine(_lectures) { (text, currentDepartments), lectures ->
            Triple(text, currentDepartments, lectures)
        }.map { (text, currentDepartments, lectures) ->
            if (text.isBlank() && currentDepartments.isEmpty()) {
                lectures
            } else if (currentDepartments.isEmpty()) {
                lectures.filter { lecture ->
                    lecture.doesMatchSearchQuery(text)
                }
            } else {
                lectures.filter { lecture ->
                    lecture.doesMatchDepartmentSearchQuery(currentDepartments.map { it.name }) &&
                            (text.isBlank() || lecture.doesMatchSearchQuery(text))
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    private val _lectureEvent = MutableStateFlow<List<TimetableEvent>>(emptyList())
    val lectureEvent = _lectureEvent.asStateFlow()

    private val _selectedLecture = MutableStateFlow<Lecture>(Lecture())
    val selectedLecture = _selectedLecture.asStateFlow()

    private val _timetableEvents = MutableStateFlow<List<Lecture>>(emptyList())
    val timetableEvents = _timetableEvents.asStateFlow()

    private val _clickLecture = MutableStateFlow<Lecture>(Lecture())
    val clickLecture = _clickLecture.asStateFlow()

    fun clear() {
        _selectedLecture.value = Lecture()
        _lectureEvent.value = emptyList()
    }

    fun clearSelectedDepartments() {
        _selectedDepartments.value = emptyList()
    }

    fun loadColors() {
        viewModelScope.launch {
            val colors = dataStoreImpl.getString("colors").first()
            if (colors.isEmpty()) {
                _colors.update { basicColors }
                dataStoreImpl.putColorsString(Gson().toJson(basicColors))
            } else {
                val colorType = object : TypeToken<List<Color>>() {}.type
                val gson = Gson()
                val updateColors = gson.fromJson<List<Color>>(colors, colorType)
                _colors.update { updateColors.ifEmpty { basicColors } }
            }

        }
    }

    fun loadDepartments() {
        viewModelScope.launch {
            _departments.update {
                localDataSource.loadDepartments() ?: emptyList()
            }
        }
    }

    fun loadLectures() {
        viewModelScope.launch {
            _lectures.update {
                localDataSource.loadLectures() ?: emptyList()
            }
        }
    }

    fun loadSemesters() {
        viewModelScope.launch {
            _semesters.update {
                localDataSource.loadSemesters() ?: emptyList()
            }
        }
    }

    fun updateColors(colors: List<Color>) {
        _colors.update { colors }
        viewModelScope.launch {
            dataStoreImpl.putColorsString(Gson().toJson(colors))
        }
    }

    fun updateClickLecture(timetable: TimetableEvent) {
        val updateLecture =
            _timetableEvents.value.filter { it.id == timetable.id }.getOrElse(0) { Lecture() }
        _clickLecture.value = updateLecture
    }

    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    fun updateLectureEvent(timetableEvents: List<TimetableEvent>) {
        _lectureEvent.value = timetableEvents
    }

    fun updateSelectedLecture(lecture: Lecture) {
        _selectedLecture.value = lecture
        if (lecture == Lecture()) {
            updateLectureEvent(emptyList())
        }
    }

    fun updateCurrentSemester(semester: Semester) {
        _currentSemester.value = semester
        clear()
        viewModelScope.launch {
            dataStoreImpl.putSemesterString(semester.semester ?: "")
            val lectureString = dataStoreImpl.getString(semester.semester ?: "").first()
            val lectureType = object : TypeToken<List<Lecture>>() {}.type
            val gson = Gson()
            val updateLecture =
                gson.fromJson<List<Lecture>>(lectureString, lectureType).orEmpty()
            _timetableEvents.update { updateLecture }
        }
    }

    fun updateSelectedDepartment(department: Department) {
        val updatedDepartments = _selectedDepartments.value.toMutableList()
        if (_selectedDepartments.value.contains(department)) {
            updatedDepartments.remove(department)
        } else {
            updatedDepartments.add(department)
        }
        _selectedDepartments.value = updatedDepartments
    }

    fun updateCurrentDepartment(departments: List<Department>) {
        _currentDepartments.value = departments
    }

    fun duplicateLecture(lecture: Lecture) {
        lecture.classTime.forEach { time ->
            _timetableEvents.value.filter { it.classTime.contains(time) }.forEach { lecture ->
                removeLecture(currentSemester.value, lecture)
            }
        }
        addLecture(currentSemester.value, lecture)
    }

    fun addLecture(semester: Semester, lecture: Lecture) {
        val updateTimetableEvents = _timetableEvents.value.toMutableList()
        updateTimetableEvents.add(lecture)
        _timetableEvents.update { updateTimetableEvents }

        viewModelScope.launch {
            dataStoreImpl.putString(
                semester.semester.toString(),
                Gson().toJson(updateTimetableEvents)
            )
        }
    }

    fun removeLecture(semester: Semester, lecture: Lecture) {
        val updateTimetableEvents = _timetableEvents.value.toMutableList()
        updateTimetableEvents.remove(lecture)
        _timetableEvents.update { updateTimetableEvents }

        viewModelScope.launch {
            dataStoreImpl.putString(
                semester.semester.toString(),
                Gson().toJson(updateTimetableEvents)
            )
        }
    }

    fun removeDepartment(department: Department) {
        val updateDepartments = _currentDepartments.value.toMutableList()
        updateDepartments.remove(department)
        _currentDepartments.value = updateDepartments
        _selectedDepartments.value = updateDepartments
    }
}
package com.koreatech.timetable.view

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.koreatech.timetable.R
import com.koreatech.timetable.data.model.Semester
import com.koreatech.timetable.data.model.TimetableEvent
import com.koreatech.timetable.databinding.ActivityHomeBinding
import com.koreatech.timetable.utils.BitmapUtils
import com.koreatech.timetable.utils.basicColors
import com.koreatech.timetable.utils.ceramicColors
import com.koreatech.timetable.utils.macaroonColors
import com.koreatech.timetable.utils.showToast
import com.koreatech.timetable.view.component.DropDownMenu
import com.koreatech.timetable.view.composeview.LectureAddDialog
import com.koreatech.timetable.view.composeview.LectureRemoveDialog
import com.koreatech.timetable.view.composeview.MajorDialog
import com.koreatech.timetable.view.composeview.TimetableBottomSheet
import com.koreatech.timetable.view.composeview.TimetableBottomSheetContent
import com.koreatech.timetable.view.viewmodel.HomeViewModel
import com.koreatech.timetable.widget.TimetableWidgetReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private var timetableView: MutableState<TimetableView>? = null

    private val homeDetailFragment by lazy {
        HomeDetailFragment.newInstance()
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.composeView.setContent {
            val context = LocalContext.current
            val viewModel: HomeViewModel = viewModel()
            val colors by viewModel.colors.collectAsStateWithLifecycle()
            val departments by viewModel.departments.collectAsStateWithLifecycle()
            val semesters by viewModel.semesters.collectAsStateWithLifecycle()
            val lectures by viewModel.lectures.collectAsStateWithLifecycle()
            val searchText by viewModel.searchText.collectAsStateWithLifecycle()
            val selectedLecture by viewModel.selectedLecture.collectAsStateWithLifecycle()
            val clickLecture by viewModel.clickLecture.collectAsStateWithLifecycle()
            val currentSemester by viewModel.currentSemester.collectAsStateWithLifecycle()
            val currentDepartments by viewModel.currentDepartments.collectAsStateWithLifecycle()
            val timetableEvents by viewModel.timetableEvents.collectAsStateWithLifecycle()

            sendBroadcastReceiver(currentSemester)
            Log.e("aaa", "recomposition")

            LaunchedEffect(key1 = Unit) {
                if (lectures.isEmpty()) {
                    viewModel.loadLectures()
                }
                if (semesters.isEmpty()) {
                    viewModel.loadSemesters()
                }
                if (departments.isEmpty()) {
                    viewModel.loadDepartments()
                }
                if (colors.isEmpty()) {
                    viewModel.loadColors()
                }
            }

            if (currentSemester.semester?.isBlank() == true && semesters.isNotEmpty()) {
                viewModel.updateCurrentSemester(semesters[0])
            }

            val sheetState = rememberBottomSheetState(
                initialValue = BottomSheetValue.Collapsed
            )
            val scaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = sheetState
            )

            val scope = rememberCoroutineScope()

            var isMajorDialogVisible by remember {
                mutableStateOf(false)
            }
            var isAddDialogVisible by remember {
                mutableStateOf(false)
            }
            var isRemoveDialogVisible by remember {
                mutableStateOf(false)
            }


            TimetableBottomSheet(
                sheetState = sheetState,
                scaffoldState = scaffoldState,
                content = {
                    /**
                     * 학과 필터링 모달
                     */
                    MajorDialog(
                        viewModel = viewModel,
                        visible = isMajorDialogVisible,
                        departments = departments,
                        onDismissRequest = {
                            if (currentDepartments.isEmpty()) {
                                viewModel.clearSelectedDepartments()
                            }
                            isMajorDialogVisible = false
                        },
                        onClick = { department, isSelected ->
                            viewModel.updateSelectedDepartment(department)
                        },
                        onCompleted = { completedDepartments ->
                            viewModel.updateCurrentDepartment(completedDepartments)
                            isMajorDialogVisible = false
                        }
                    )

                    /**
                     * 강의 추가 모달
                     */
                    LectureAddDialog(
                        context = context,
                        visible = isAddDialogVisible,
                        lecture = selectedLecture,
                        duplication = selectedLecture.duplicate(timetableEvents),
                        onDismissRequest = {
                            isAddDialogVisible = false
                        },
                        onAddLecture = { lecture ->
                            if (selectedLecture.duplicate(timetableEvents)) {
                                viewModel.duplicateLecture(lecture)
                            } else {
                                viewModel.addLecture(currentSemester, lecture)
                            }
                            isAddDialogVisible = false
                            sendBroadcastReceiver(currentSemester)
                        }
                    )

                    /**
                     * 강의 삭제 모달
                     */
                    LectureRemoveDialog(
                        context = context,
                        visible = isRemoveDialogVisible,
                        lecture = clickLecture,
                        onDismissRequest = {
                            isRemoveDialogVisible = false
                        },
                        onRemoveLecture = { lecture ->
                            viewModel.removeLecture(currentSemester, lecture)
                            viewModel.clear()
                            isRemoveDialogVisible = false
                        }
                    )

                    Row {
                        /**
                         * 학기 드롭다운 메뉴
                         */
                        DropDownMenu(
                            semesters = semesters,
                            onSemesterTextChanged = viewModel::updateCurrentSemester
                        )
                        /**
                         * 시간표 저장
                         */
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                BitmapUtils(this@HomeActivity).apply {
                                    timetableView?.value?.let { view ->
                                        capture(view) { bitmap ->
                                            saveBitmapImage(bitmap)
                                        }
                                    } ?: showToast("저장을 다시해주세요.")
                                }
                            })
                        /**
                         * 바텀 시트 올리기 (강의 추가)
                         */
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    if (sheetState.isCollapsed) sheetState.expand()
                                    else sheetState.collapse()
                                }
                            })

                        /**
                         * 강의 클릭 초기화
                         */
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                viewModel.clear()
                            })

                        /**
                         * 강의 직접 추가
                         */
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.fcv_detail, homeDetailFragment, "HomeDetailFragment")
                                    .commit()
                            })
                    }

                    /**
                     * 강의 블럭 색상 템플릿 변경 원
                     */
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        Canvas(modifier = Modifier
                            .size(30.dp)
                            .clickable(onClick = {
                                viewModel.updateColors(basicColors)
                            })
                        ) {
                            drawCircle(brush = Brush.horizontalGradient(basicColors))
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Canvas(modifier = Modifier
                            .size(30.dp)
                            .clickable(onClick = {
                                viewModel.updateColors(ceramicColors)
                            })
                        ) {
                            drawCircle(brush = Brush.horizontalGradient(ceramicColors))
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Canvas(modifier = Modifier
                            .size(30.dp)
                            .clickable(onClick = {
                                viewModel.updateColors(macaroonColors)
                            })
                        ) {
                            drawCircle(brush = Brush.horizontalGradient(macaroonColors))
                        }
                    }

                    /**
                     * 시간표 UI
                     */
                    TimetableUI(
                        sheetState = sheetState,
                        onEventClick = { timetableEvent ->
                            viewModel.updateClickLecture(timetableEvent)
                            isRemoveDialogVisible = true
                        }
                    )
                },
                sheetContent = {
                    /**
                     * 바텀 시트 내부
                     */
                    TimetableBottomSheetContent(
                        colors = colors,
                        lectures = lectures,
                        selectedLectures = selectedLecture,
                        currentDepartments = currentDepartments,
                        searchText = searchText,
                        onSearchTextChanged = viewModel::updateSearchText,
                        onClickLecture = viewModel::updateLectureEvent,
                        onSelectedLecture = viewModel::updateSelectedLecture,
                        onAddLecture = {
                            isAddDialogVisible = true
                        },
                        onSetting = {
                            isMajorDialogVisible = true
                        },
                        onCancel = viewModel::removeDepartment
                    )
                }
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun TimetableUI(
        sheetState: BottomSheetState,
        onEventClick: (TimetableEvent) -> Unit,
    ) {
        timetableView = remember {
            mutableStateOf(
                TimetableView(
                    context = this@HomeActivity,
                    sheetState = sheetState,
                )
            )
        }

        AndroidView(factory = {
            TimetableView(
                context = it,
                sheetState = sheetState,
            ).apply {
                post {
                    timetableView?.value = this
                }
                setOnTimetableEventClickListener { timetableEvent ->
                    onEventClick(timetableEvent)
                }
            }
        })
    }

    fun sendBroadcastReceiver(semester: Semester) {
        val intent = Intent(this, TimetableWidgetReceiver::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra("semester", semester.semester.toString())
        }
        sendBroadcast(intent)
    }
}

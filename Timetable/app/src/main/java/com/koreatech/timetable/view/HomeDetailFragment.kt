package com.koreatech.timetable.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.koreatech.timetable.data.model.Lecture
import com.koreatech.timetable.data.model.TimetableEvent
import com.koreatech.timetable.databinding.FragmentHomedetailBinding
import com.koreatech.timetable.view.component.LectureDetailItem
import com.koreatech.timetable.view.component.LectureExtraButton
import com.koreatech.timetable.view.component.LectureMoreDetailItem
import com.koreatech.timetable.view.component.TimeEditDialog
import com.koreatech.timetable.view.composeview.Timetable
import com.koreatech.timetable.view.viewmodel.HomeDetailViewModel
import com.koreatech.timetable.view.viewmodel.HomeViewModel

class HomeDetailFragment : Fragment() {
    private var _binding: FragmentHomedetailBinding? = null
    private val binding get() = requireNotNull(_binding) { "HomeDetailFragment binding Error" }

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomedetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed()

        binding.composeView.setContent {
            val colors by homeViewModel.colors.collectAsStateWithLifecycle()
            val timetableEvents by homeViewModel.timetableEvents.collectAsStateWithLifecycle()

            val viewModel: HomeDetailViewModel = viewModel()

            var dynamicItem by remember {
                mutableIntStateOf(0)
            }

            var isVisible by remember {
                mutableStateOf(false)
            }

            if (isVisible) {
                TimeEditDialog(
                    visible = isVisible,
                    onDismissRequest = {
                        isVisible = !isVisible
                    }
                )
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    title = {
                        Text(text = "수업추가")
                    },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        }
                    },
                    backgroundColor = Color.White
                )
                Timetable(
                    modifier = Modifier.height(200.dp),
                    colors = colors,
                    events = generateTimetableEvents(timetableEvents, colors),
//                    clickEvent = lectureEvents,
                    onEventClick = {

                    }
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        LectureDetailItem(
                            title = "수업명",
                            essential = true,
                        )
                    }
                    item {
                        LectureDetailItem(
                            title = "교수명",
                            essential = false,
                        )
                    }
                    items(dynamicItem) {
                        LectureMoreDetailItem(
                            onClickDayOfWeek = {
                                isVisible = true
                            },
                            onClickTime = {
                                isVisible = true
                            }
                        )
                    }

                    item {
                        LectureExtraButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            dynamicItem++
                        }
                    }
                }

            }
        }
    }

    private fun generateTimetableEvents(
        timetableEvents: List<Lecture>,
        colors: List<Color>,
    ): List<TimetableEvent> {
        val updateTimetableEvents = mutableListOf<TimetableEvent>()
        timetableEvents.mapIndexed { index, lecture ->
            lecture.toTimetableEvents(index, colors)
        }.map {
            it.forEach {
                updateTimetableEvents.add(it)
            }
        }

        return updateTimetableEvents
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val homeDetailFragment =
                        parentFragmentManager.findFragmentByTag("HomeDetailFragment")
                    if (homeDetailFragment != null) {
                        parentFragmentManager.beginTransaction()
                            .remove(homeDetailFragment)
                            .commit()
                    }
                }
            })
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(): HomeDetailFragment {
            val args = Bundle()

            val fragment = HomeDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
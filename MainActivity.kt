package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Screen
import com.example.ui.screens.CreateExamScreen
import com.example.ui.screens.ExamArenaScreen
import com.example.ui.screens.ExamConfirmScreen
import com.example.ui.screens.ExamResultScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CbtViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CbtApp()
                }
            }
        }
    }
}

@Composable
fun CbtApp(viewModel: CbtViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val allExams by viewModel.allExams.collectAsStateWithLifecycle()
    val allAttempts by viewModel.allAttempts.collectAsStateWithLifecycle()
    val overallStats by viewModel.overallStats.collectAsStateWithLifecycle()
    val studentProfile by viewModel.studentProfile.collectAsStateWithLifecycle()

    // Arena State
    val activeExam by viewModel.activeExam.collectAsStateWithLifecycle()
    val activeQuestions by viewModel.activeQuestions.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentQuestionIndex.collectAsStateWithLifecycle()
    val userAnswers by viewModel.userAnswers.collectAsStateWithLifecycle()
    val remainingSeconds by viewModel.remainingSeconds.collectAsStateWithLifecycle()
    val textScale by viewModel.textScale.collectAsStateWithLifecycle()
    val antiCheatViolations by viewModel.antiCheatViolations.collectAsStateWithLifecycle()
    val showAntiCheatWarning by viewModel.showAntiCheatWarning.collectAsStateWithLifecycle()
    val showQuestionMatrix by viewModel.showQuestionMatrix.collectAsStateWithLifecycle()
    val showFinishConfirmDialog by viewModel.showFinishConfirmDialog.collectAsStateWithLifecycle()

    // Result State
    val activeAttempt by viewModel.activeAttempt.collectAsStateWithLifecycle()
    val reviewItems by viewModel.reviewItems.collectAsStateWithLifecycle()
    val reviewFilter by viewModel.reviewFilter.collectAsStateWithLifecycle()

    when (val screen = currentScreen) {
        is Screen.Home -> {
            HomeScreen(
                exams = allExams,
                stats = overallStats,
                studentProfile = studentProfile,
                onStartExamConfirm = { examId ->
                    viewModel.navigateTo(Screen.Confirm(examId))
                },
                onVerifyToken = { token, callback ->
                    viewModel.verifyToken(token, callback)
                },
                onUpdateProfile = { name, id, className ->
                    viewModel.updateStudentProfile(name, id, className)
                },
                onNavigate = { dest ->
                    viewModel.navigateTo(dest)
                },
                onDeleteExam = { examId ->
                    viewModel.deleteExam(examId)
                }
            )
        }

        is Screen.Confirm -> {
            val exam = allExams.find { it.id == screen.examId }
            if (exam != null) {
                ExamConfirmScreen(
                    exam = exam,
                    studentProfile = studentProfile,
                    onStartExam = {
                        viewModel.startExam(screen.examId)
                    },
                    onBack = {
                        viewModel.navigateBack()
                    }
                )
            } else {
                viewModel.goHome()
            }
        }

        is Screen.Arena -> {
            val exam = activeExam
            if (exam != null && activeQuestions.isNotEmpty()) {
                ExamArenaScreen(
                    exam = exam,
                    questions = activeQuestions,
                    currentIndex = currentIndex,
                    userAnswers = userAnswers,
                    remainingSeconds = remainingSeconds,
                    textScale = textScale,
                    antiCheatViolations = antiCheatViolations,
                    showAntiCheatWarning = showAntiCheatWarning,
                    showQuestionMatrix = showQuestionMatrix,
                    showFinishConfirmDialog = showFinishConfirmDialog,
                    onOptionSelected = { opt ->
                        val currentQ = activeQuestions.getOrNull(currentIndex)
                        if (currentQ != null) {
                            viewModel.selectOption(currentQ.id, opt)
                        }
                    },
                    onToggleDoubtful = {
                        val currentQ = activeQuestions.getOrNull(currentIndex)
                        if (currentQ != null) {
                            viewModel.toggleDoubtful(currentQ.id)
                        }
                    },
                    onGoToQuestion = { idx ->
                        viewModel.goToQuestion(idx)
                    },
                    onNextQuestion = {
                        viewModel.nextQuestion()
                    },
                    onPrevQuestion = {
                        viewModel.prevQuestion()
                    },
                    onTextScaleChange = { scale ->
                        viewModel.setTextScale(scale)
                    },
                    onOpenMatrix = {
                        viewModel.toggleQuestionMatrix(true)
                    },
                    onCloseMatrix = {
                        viewModel.toggleQuestionMatrix(false)
                    },
                    onOpenFinishDialog = {
                        viewModel.setFinishConfirmDialog(true)
                    },
                    onCloseFinishDialog = {
                        viewModel.setFinishConfirmDialog(false)
                    },
                    onConfirmFinish = {
                        viewModel.finishExam(autoSubmit = false)
                    },
                    onRecordCheatViolation = {
                        viewModel.recordCheatViolation()
                    },
                    onDismissAntiCheat = {
                        viewModel.dismissAntiCheatWarning()
                    },
                    onBackCancelExam = {
                        viewModel.goHome()
                    }
                )
            } else {
                viewModel.goHome()
            }
        }

        is Screen.Result -> {
            ExamResultScreen(
                attempt = activeAttempt,
                reviewItems = reviewItems,
                reviewFilter = reviewFilter,
                onFilterChange = { filter ->
                    viewModel.setReviewFilter(filter)
                },
                onRetakeExam = { examId ->
                    viewModel.startExam(examId)
                },
                onGoHome = {
                    viewModel.goHome()
                }
            )
        }

        is Screen.CreateExam -> {
            CreateExamScreen(
                onSaveExam = { title, subject, token, durationMinutes, passingGrade, description, questions ->
                    viewModel.createCustomExam(
                        title, subject, token, durationMinutes, passingGrade, description, questions
                    ) {
                        viewModel.goHome()
                    }
                },
                onBack = {
                    viewModel.navigateBack()
                }
            )
        }

        is Screen.History -> {
            HistoryScreen(
                attempts = allAttempts,
                onSelectAttempt = { attemptId ->
                    viewModel.loadAttempt(attemptId)
                    viewModel.navigateTo(Screen.Result(attemptId))
                },
                onDeleteAttempt = { attemptId ->
                    viewModel.deleteAttempt(attemptId)
                },
                onClearAllAttempts = {
                    viewModel.clearAllAttempts()
                },
                onBack = {
                    viewModel.navigateBack()
                }
            )
        }
    }
}

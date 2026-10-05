package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.data.local.ExamEntity
import com.example.data.local.QuestionEntity
import com.example.model.TextScale
import com.example.model.UserAnswerRecord
import com.example.ui.components.AntiCheatDialog
import com.example.ui.components.CbtArenaTopBar
import com.example.ui.components.CbtNavigationRow
import com.example.ui.components.FinishExamDialog
import com.example.ui.components.QuestionCard
import com.example.ui.components.QuestionNavigationSheet
import com.example.ui.theme.CbtError

@Composable
fun ExamArenaScreen(
    exam: ExamEntity,
    questions: List<QuestionEntity>,
    currentIndex: Int,
    userAnswers: Map<String, UserAnswerRecord>,
    remainingSeconds: Long,
    textScale: TextScale,
    antiCheatViolations: Int,
    showAntiCheatWarning: Boolean,
    showQuestionMatrix: Boolean,
    showFinishConfirmDialog: Boolean,
    onOptionSelected: (String) -> Unit,
    onToggleDoubtful: () -> Unit,
    onGoToQuestion: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onPrevQuestion: () -> Unit,
    onTextScaleChange: (TextScale) -> Unit,
    onOpenMatrix: () -> Unit,
    onCloseMatrix: () -> Unit,
    onOpenFinishDialog: () -> Unit,
    onCloseFinishDialog: () -> Unit,
    onConfirmFinish: () -> Unit,
    onRecordCheatViolation: () -> Unit,
    onDismissAntiCheat: () -> Unit,
    onBackCancelExam: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showExitPrompt by remember { mutableStateOf(false) }

    // Intercept hardware/gesture Back key
    BackHandler {
        showExitPrompt = true
    }

    // Anti-cheat lifecycle listener: detect if student switches to another app
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                onRecordCheatViolation()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val currentQuestion = questions.getOrNull(currentIndex) ?: questions[0]
    val currentRecord = userAnswers[currentQuestion.id]
    val selectedOption = currentRecord?.selectedOption
    val isDoubtful = currentRecord?.isDoubtful ?: false

    val answeredCount = questions.count { q ->
        val rec = userAnswers[q.id]
        !rec?.selectedOption.isNullOrBlank() && !rec?.isDoubtful!!
    }
    val doubtfulCount = questions.count { q ->
        userAnswers[q.id]?.isDoubtful == true
    }
    val unansweredCount = questions.size - (answeredCount + doubtfulCount)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CbtArenaTopBar(
                remainingSeconds = remainingSeconds,
                currentIndex = currentIndex,
                totalQuestions = questions.size,
                answeredCount = answeredCount,
                textScale = textScale,
                onTextScaleChange = onTextScaleChange,
                onOpenMatrix = onOpenMatrix,
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            CbtNavigationRow(
                currentIndex = currentIndex,
                totalQuestions = questions.size,
                isDoubtful = isDoubtful,
                onPrevClick = onPrevQuestion,
                onNextClick = onNextQuestion,
                onToggleDoubtful = onToggleDoubtful,
                onFinishClick = onOpenFinishDialog
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            QuestionCard(
                question = currentQuestion,
                selectedOption = selectedOption,
                isDoubtful = isDoubtful,
                textScale = textScale,
                onOptionSelected = onOptionSelected,
                modifier = Modifier.weight(1f)
            )
        }
    }

    // Question Navigation Matrix BottomSheet
    if (showQuestionMatrix) {
        QuestionNavigationSheet(
            questions = questions,
            userAnswers = userAnswers,
            currentIndex = currentIndex,
            onSelectQuestion = { index ->
                onGoToQuestion(index)
            },
            onDismiss = onCloseMatrix
        )
    }

    // Finish Confirmation Modal
    if (showFinishConfirmDialog) {
        FinishExamDialog(
            answeredCount = answeredCount,
            unansweredCount = unansweredCount,
            doubtfulCount = doubtfulCount,
            onConfirmFinish = onConfirmFinish,
            onDismiss = onCloseFinishDialog
        )
    }

    // Anti-Cheat Warning Modal
    if (showAntiCheatWarning) {
        AntiCheatDialog(
            violationCount = antiCheatViolations,
            onDismiss = onDismissAntiCheat
        )
    }

    // Exit Exam Prompt (from BackHandler)
    if (showExitPrompt) {
        AlertDialog(
            onDismissRequest = { showExitPrompt = false },
            title = { Text("Batalkan Ujian?", fontWeight = FontWeight.Bold) },
            text = {
                Text("Keluar sekarang akan membatalkan sesi ujian ini. Apakah Anda ingin keluar ke beranda atau menyelesaikan ujian untuk dinilai?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitPrompt = false
                        onOpenFinishDialog()
                    }
                ) {
                    Text("Selesaikan & Nilai")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showExitPrompt = false
                        onBackCancelExam()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = CbtError
                    )
                ) {
                    Text("Keluar & Batalkan")
                }
            }
        )
    }
}

package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CbtDatabase
import com.example.data.local.ExamAttemptEntity
import com.example.data.local.ExamEntity
import com.example.data.local.QuestionEntity
import com.example.data.repository.CbtRepository
import com.example.model.OverallStats
import com.example.model.QuestionReviewItem
import com.example.model.ReviewFilter
import com.example.model.Screen
import com.example.model.StudentProfile
import com.example.model.TextScale
import com.example.model.UserAnswerRecord
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class CbtViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CbtRepository

    init {
        val db = CbtDatabase.getDatabase(application)
        repository = CbtRepository(db.examDao())
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    // Navigation
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenBackStack = mutableListOf<Screen>()

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenBackStack.isNotEmpty()) {
            val previous = screenBackStack.removeAt(screenBackStack.lastIndex)
            _currentScreen.value = previous
            return true
        }
        return false
    }

    fun goHome() {
        screenBackStack.clear()
        _currentScreen.value = Screen.Home
    }

    // Exam Lists & Attempts from Room
    val allExams: StateFlow<List<ExamEntity>> = repository.allExams
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAttempts: StateFlow<List<ExamAttemptEntity>> = repository.allAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val overallStats: StateFlow<OverallStats> = allAttempts.map { attempts ->
        if (attempts.isEmpty()) {
            OverallStats()
        } else {
            val total = attempts.size
            val avg = attempts.map { it.score }.average()
            val max = attempts.maxOfOrNull { it.score } ?: 0.0
            val passed = attempts.count { it.passed }
            OverallStats(
                totalAttempts = total,
                averageScore = avg,
                highestScore = max,
                passedCount = passed
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OverallStats())

    // Student Profile
    private val _studentProfile = MutableStateFlow(StudentProfile())
    val studentProfile: StateFlow<StudentProfile> = _studentProfile.asStateFlow()

    fun updateStudentProfile(name: String, studentId: String, className: String) {
        _studentProfile.value = StudentProfile(
            name = name.ifBlank { "Siswa CBT" },
            studentId = studentId.ifBlank { "CBT-001" },
            className = className.ifBlank { "Umum" }
        )
    }

    // Token verification
    fun verifyToken(token: String, onResult: (ExamEntity?) -> Unit) {
        viewModelScope.launch {
            val exam = repository.getExamByToken(token)
            onResult(exam)
        }
    }

    // Active Exam Arena State
    private val _activeExam = MutableStateFlow<ExamEntity?>(null)
    val activeExam: StateFlow<ExamEntity?> = _activeExam.asStateFlow()

    private val _activeQuestions = MutableStateFlow<List<QuestionEntity>>(emptyList())
    val activeQuestions: StateFlow<List<QuestionEntity>> = _activeQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _userAnswers = MutableStateFlow<Map<String, UserAnswerRecord>>(emptyMap())
    val userAnswers: StateFlow<Map<String, UserAnswerRecord>> = _userAnswers.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(0L)
    val remainingSeconds: StateFlow<Long> = _remainingSeconds.asStateFlow()

    private val _totalDurationSeconds = MutableStateFlow(0L)
    val totalDurationSeconds: StateFlow<Long> = _totalDurationSeconds.asStateFlow()

    private val _textScale = MutableStateFlow(TextScale.NORMAL)
    val textScale: StateFlow<TextScale> = _textScale.asStateFlow()

    private val _showQuestionMatrix = MutableStateFlow(false)
    val showQuestionMatrix: StateFlow<Boolean> = _showQuestionMatrix.asStateFlow()

    private val _showFinishConfirmDialog = MutableStateFlow(false)
    val showFinishConfirmDialog: StateFlow<Boolean> = _showFinishConfirmDialog.asStateFlow()

    private val _antiCheatViolations = MutableStateFlow(0)
    val antiCheatViolations: StateFlow<Int> = _antiCheatViolations.asStateFlow()

    private val _showAntiCheatWarning = MutableStateFlow(false)
    val showAntiCheatWarning: StateFlow<Boolean> = _showAntiCheatWarning.asStateFlow()

    private var timerJob: Job? = null
    private var examStartTime: Long = 0L

    fun startExam(examId: String) {
        viewModelScope.launch {
            val exam = repository.getExamById(examId) ?: return@launch
            val questions = repository.getQuestionsForExam(examId)
            if (questions.isEmpty()) return@launch

            _activeExam.value = exam
            _activeQuestions.value = questions
            _currentQuestionIndex.value = 0
            _userAnswers.value = emptyMap()
            _antiCheatViolations.value = 0
            _showAntiCheatWarning.value = false
            _showQuestionMatrix.value = false
            _showFinishConfirmDialog.value = false

            val totalSec = exam.durationMinutes * 60L
            _totalDurationSeconds.value = totalSec
            _remainingSeconds.value = totalSec
            examStartTime = System.currentTimeMillis()

            startTimer()
            _currentScreen.value = Screen.Arena(examId)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive && _remainingSeconds.value > 0) {
                delay(1000)
                _remainingSeconds.value -= 1
                if (_remainingSeconds.value <= 0) {
                    finishExam(autoSubmit = true)
                    break
                }
            }
        }
    }

    fun selectOption(questionId: String, option: String) {
        val current = _userAnswers.value[questionId]
        val updated = current?.copy(selectedOption = option)
            ?: UserAnswerRecord(questionId = questionId, selectedOption = option, isDoubtful = false)
        _userAnswers.value = _userAnswers.value + (questionId to updated)
    }

    fun toggleDoubtful(questionId: String) {
        val current = _userAnswers.value[questionId]
        val updated = if (current != null) {
            current.copy(isDoubtful = !current.isDoubtful)
        } else {
            UserAnswerRecord(questionId = questionId, selectedOption = null, isDoubtful = true)
        }
        _userAnswers.value = _userAnswers.value + (questionId to updated)
    }

    fun goToQuestion(index: Int) {
        if (index in 0 until _activeQuestions.value.size) {
            _currentQuestionIndex.value = index
            _showQuestionMatrix.value = false
        }
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value < _activeQuestions.value.size - 1) {
            _currentQuestionIndex.value += 1
        }
    }

    fun prevQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
        }
    }

    fun setTextScale(scale: TextScale) {
        _textScale.value = scale
    }

    fun toggleQuestionMatrix(show: Boolean) {
        _showQuestionMatrix.value = show
    }

    fun setFinishConfirmDialog(show: Boolean) {
        _showFinishConfirmDialog.value = show
    }

    fun recordCheatViolation() {
        if (_currentScreen.value is Screen.Arena) {
            _antiCheatViolations.value += 1
            _showAntiCheatWarning.value = true
        }
    }

    fun dismissAntiCheatWarning() {
        _showAntiCheatWarning.value = false
    }

    fun finishExam(autoSubmit: Boolean = false) {
        timerJob?.cancel()
        _showFinishConfirmDialog.value = false
        _showQuestionMatrix.value = false

        val exam = _activeExam.value ?: return
        val questions = _activeQuestions.value
        val answers = _userAnswers.value

        var correctCount = 0
        var incorrectCount = 0
        var unansweredCount = 0

        val answersJsonArray = JSONArray()

        for (q in questions) {
            val record = answers[q.id]
            val chosen = record?.selectedOption
            val isDoubt = record?.isDoubtful ?: false

            val jsonObj = JSONObject().apply {
                put("questionId", q.id)
                put("selectedOption", chosen ?: "")
                put("isDoubtful", isDoubt)
                put("correctAnswer", q.correctAnswer)
            }
            answersJsonArray.put(jsonObj)

            if (chosen.isNullOrBlank()) {
                unansweredCount++
            } else if (chosen.trim().equals(q.correctAnswer.trim(), ignoreCase = true)) {
                correctCount++
            } else {
                incorrectCount++
            }
        }

        val totalQ = questions.size
        val score = if (totalQ > 0) (correctCount.toDouble() / totalQ.toDouble()) * 100.0 else 0.0
        val passed = score >= exam.passingGrade

        val durationTaken = (_totalDurationSeconds.value - _remainingSeconds.value).coerceAtLeast(0L)
        val profile = _studentProfile.value

        val attemptId = UUID.randomUUID().toString()
        val attempt = ExamAttemptEntity(
            id = attemptId,
            examId = exam.id,
            examTitle = exam.title,
            subject = exam.subject,
            studentName = profile.name,
            studentId = profile.studentId,
            score = Math.round(score * 10.0) / 10.0,
            passed = passed,
            passingGrade = exam.passingGrade,
            correctCount = correctCount,
            incorrectCount = incorrectCount,
            unansweredCount = unansweredCount,
            totalQuestions = totalQ,
            durationTakenSeconds = durationTaken,
            totalDurationSeconds = _totalDurationSeconds.value,
            completedAt = System.currentTimeMillis(),
            answersJson = answersJsonArray.toString()
        )

        viewModelScope.launch {
            repository.saveAttempt(attempt)
            loadAttempt(attemptId)
            _currentScreen.value = Screen.Result(attemptId)
        }
    }

    // Result & Pembahasan Review State
    private val _activeAttempt = MutableStateFlow<ExamAttemptEntity?>(null)
    val activeAttempt: StateFlow<ExamAttemptEntity?> = _activeAttempt.asStateFlow()

    private val _reviewItems = MutableStateFlow<List<QuestionReviewItem>>(emptyList())
    val reviewItems: StateFlow<List<QuestionReviewItem>> = _reviewItems.asStateFlow()

    private val _reviewFilter = MutableStateFlow(ReviewFilter.ALL)
    val reviewFilter: StateFlow<ReviewFilter> = _reviewFilter.asStateFlow()

    fun setReviewFilter(filter: ReviewFilter) {
        _reviewFilter.value = filter
    }

    fun loadAttempt(attemptId: String) {
        viewModelScope.launch {
            val attempt = repository.getAttemptById(attemptId) ?: return@launch
            _activeAttempt.value = attempt
            val questions = repository.getQuestionsForExam(attempt.examId)

            val answersMap = mutableMapOf<String, Pair<String?, Boolean>>()
            try {
                val array = JSONArray(attempt.answersJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val qId = obj.getString("questionId")
                    val sel = obj.optString("selectedOption").ifEmpty { null }
                    val isDoubt = obj.optBoolean("isDoubtful", false)
                    answersMap[qId] = Pair(sel, isDoubt)
                }
            } catch (e: Exception) {
                // fallback
            }

            val items = questions.map { q ->
                val (chosen, isDoubt) = answersMap[q.id] ?: Pair(null, false)
                val isCorrect = chosen != null && chosen.equals(q.correctAnswer, ignoreCase = true)
                QuestionReviewItem(
                    question = q,
                    selectedOption = chosen,
                    isDoubtful = isDoubt,
                    isCorrect = isCorrect
                )
            }
            _reviewItems.value = items
        }
    }

    // Custom Exam Creation
    fun createCustomExam(
        title: String,
        subject: String,
        token: String,
        durationMinutes: Int,
        passingGrade: Int,
        description: String,
        questions: List<QuestionEntity>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val examId = "custom_" + UUID.randomUUID().toString().take(8)
            val finalQuestions = questions.mapIndexed { index, q ->
                q.copy(id = "cq_${examId}_$index", examId = examId, orderIndex = index + 1)
            }
            val exam = ExamEntity(
                id = examId,
                title = title.ifBlank { "Ujian Kustom CBT" },
                subject = subject.ifBlank { "Umum" },
                category = "Kustom / Guru",
                token = token.ifBlank { "TOKEN" }.uppercase(),
                durationMinutes = durationMinutes.coerceAtLeast(1),
                passingGrade = passingGrade.coerceIn(1, 100),
                description = description.ifBlank { "Ujian kustom dibuat mandiri." },
                isCustom = true,
                totalQuestions = finalQuestions.size
            )
            repository.createCustomExam(exam, finalQuestions)
            onSuccess()
        }
    }

    fun deleteExam(examId: String) {
        viewModelScope.launch {
            repository.deleteExam(examId)
        }
    }

    fun deleteAttempt(attemptId: String) {
        viewModelScope.launch {
            repository.deleteAttempt(attemptId)
        }
    }

    fun clearAllAttempts() {
        viewModelScope.launch {
            repository.clearAllAttempts()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

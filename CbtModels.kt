package com.example.model

import com.example.data.local.ExamEntity
import com.example.data.local.QuestionEntity

sealed interface Screen {
    data object Home : Screen
    data class Confirm(val examId: String) : Screen
    data class Arena(val examId: String) : Screen
    data class Result(val attemptId: String) : Screen
    data object CreateExam : Screen
    data object History : Screen
}

data class StudentProfile(
    val name: String = "Budi Pratama",
    val studentId: String = "CBT-2026-0842",
    val className: String = "Kelas XII MIPA 1"
)

data class UserAnswerRecord(
    val questionId: String,
    val selectedOption: String? = null,
    val isDoubtful: Boolean = false
)

enum class TextScale(val multiplier: Float, val label: String) {
    SMALL(0.85f, "A-"),
    NORMAL(1.0f, "A"),
    LARGE(1.25f, "A+")
}

data class QuestionReviewItem(
    val question: QuestionEntity,
    val selectedOption: String?,
    val isDoubtful: Boolean,
    val isCorrect: Boolean
)

data class OverallStats(
    val totalAttempts: Int = 0,
    val averageScore: Double = 0.0,
    val highestScore: Double = 0.0,
    val passedCount: Int = 0
)

enum class ReviewFilter(val label: String) {
    ALL("Semua"),
    WRONG("Salah"),
    CORRECT("Benar"),
    DOUBTFUL("Ragu-ragu")
}

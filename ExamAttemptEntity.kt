package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_attempts")
data class ExamAttemptEntity(
    @PrimaryKey
    val id: String,
    val examId: String,
    val examTitle: String,
    val subject: String,
    val studentName: String,
    val studentId: String,
    val score: Double,
    val passed: Boolean,
    val passingGrade: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val unansweredCount: Int,
    val totalQuestions: Int,
    val durationTakenSeconds: Long,
    val totalDurationSeconds: Long,
    val completedAt: Long = System.currentTimeMillis(),
    val answersJson: String // Serialized JSON format of user answers
)

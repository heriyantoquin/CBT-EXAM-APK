package com.example.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = ExamEntity::class,
            parentColumns = ["id"],
            childColumns = ["examId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["examId"])]
)
data class QuestionEntity(
    @PrimaryKey
    val id: String,
    val examId: String,
    val orderIndex: Int,
    val questionText: String,
    val passageText: String? = null,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val optionE: String? = null,
    val correctAnswer: String, // "A", "B", "C", "D", or "E"
    val explanation: String
)

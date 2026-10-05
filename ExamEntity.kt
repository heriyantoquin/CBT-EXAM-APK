package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exams")
data class ExamEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val subject: String,
    val category: String, // e.g., "UTBK / SNBT", "Ujian Nasional / Sekolah", "Kedinasan / CPNS", "Latihan Mandiri"
    val token: String,
    val durationMinutes: Int,
    val passingGrade: Int, // KKM (e.g. 75)
    val description: String,
    val isCustom: Boolean = false,
    val totalQuestions: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

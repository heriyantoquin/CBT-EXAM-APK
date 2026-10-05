package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {

    @Query("SELECT * FROM exams ORDER BY createdAt DESC")
    fun getAllExams(): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE id = :examId LIMIT 1")
    suspend fun getExamById(examId: String): ExamEntity?

    @Query("SELECT * FROM exams WHERE UPPER(token) = UPPER(:token) LIMIT 1")
    suspend fun getExamByToken(token: String): ExamEntity?

    @Query("SELECT * FROM questions WHERE examId = :examId ORDER BY orderIndex ASC")
    suspend fun getQuestionsForExam(examId: String): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE examId = :examId ORDER BY orderIndex ASC")
    fun getQuestionsForExamFlow(examId: String): Flow<List<QuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: ExamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Transaction
    suspend fun insertExamWithQuestions(exam: ExamEntity, questions: List<QuestionEntity>) {
        insertExam(exam)
        insertQuestions(questions)
    }

    @Query("DELETE FROM exams WHERE id = :examId")
    suspend fun deleteExam(examId: String)

    @Query("SELECT COUNT(*) FROM exams")
    suspend fun getExamCount(): Int

    // Attempts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: ExamAttemptEntity)

    @Query("SELECT * FROM exam_attempts ORDER BY completedAt DESC")
    fun getAllAttempts(): Flow<List<ExamAttemptEntity>>

    @Query("SELECT * FROM exam_attempts WHERE id = :attemptId LIMIT 1")
    suspend fun getAttemptById(attemptId: String): ExamAttemptEntity?

    @Query("DELETE FROM exam_attempts WHERE id = :attemptId")
    suspend fun deleteAttempt(attemptId: String)

    @Query("DELETE FROM exam_attempts")
    suspend fun clearAllAttempts()
}

package com.example.data.repository

import com.example.data.local.CbtDatabase
import com.example.data.local.ExamAttemptEntity
import com.example.data.local.ExamDao
import com.example.data.local.ExamEntity
import com.example.data.local.PresetExamData
import com.example.data.local.QuestionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CbtRepository(private val examDao: ExamDao) {

    val allExams: Flow<List<ExamEntity>> = examDao.getAllExams()
    val allAttempts: Flow<List<ExamAttemptEntity>> = examDao.getAllAttempts()

    suspend fun checkAndSeedInitialData() = withContext(Dispatchers.IO) {
        val count = examDao.getExamCount()
        if (count == 0) {
            val presets = PresetExamData.getPresetBundles()
            for (bundle in presets) {
                examDao.insertExamWithQuestions(bundle.exam, bundle.questions)
            }
        }
    }

    suspend fun getExamById(examId: String): ExamEntity? = withContext(Dispatchers.IO) {
        examDao.getExamById(examId)
    }

    suspend fun getExamByToken(token: String): ExamEntity? = withContext(Dispatchers.IO) {
        examDao.getExamByToken(token.trim())
    }

    suspend fun getQuestionsForExam(examId: String): List<QuestionEntity> = withContext(Dispatchers.IO) {
        examDao.getQuestionsForExam(examId)
    }

    suspend fun saveAttempt(attempt: ExamAttemptEntity) = withContext(Dispatchers.IO) {
        examDao.insertAttempt(attempt)
    }

    suspend fun getAttemptById(attemptId: String): ExamAttemptEntity? = withContext(Dispatchers.IO) {
        examDao.getAttemptById(attemptId)
    }

    suspend fun deleteAttempt(attemptId: String) = withContext(Dispatchers.IO) {
        examDao.deleteAttempt(attemptId)
    }

    suspend fun clearAllAttempts() = withContext(Dispatchers.IO) {
        examDao.clearAllAttempts()
    }

    suspend fun createCustomExam(exam: ExamEntity, questions: List<QuestionEntity>) = withContext(Dispatchers.IO) {
        examDao.insertExamWithQuestions(exam, questions)
    }

    suspend fun deleteExam(examId: String) = withContext(Dispatchers.IO) {
        examDao.deleteExam(examId)
    }
}

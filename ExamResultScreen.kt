package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ExamAttemptEntity
import com.example.model.QuestionReviewItem
import com.example.model.ReviewFilter
import com.example.ui.theme.CbtAnswered
import com.example.ui.theme.CbtDoubtful
import com.example.ui.theme.CbtError
import com.example.ui.theme.CbtPrimary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamResultScreen(
    attempt: ExamAttemptEntity?,
    reviewItems: List<QuestionReviewItem>,
    reviewFilter: ReviewFilter,
    onFilterChange: (ReviewFilter) -> Unit,
    onRetakeExam: (String) -> Unit,
    onGoHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onGoHome() }

    if (attempt == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val filteredReviewItems = when (reviewFilter) {
        ReviewFilter.ALL -> reviewItems
        ReviewFilter.WRONG -> reviewItems.filter { !it.isCorrect && !it.selectedOption.isNullOrBlank() }
        ReviewFilter.CORRECT -> reviewItems.filter { it.isCorrect }
        ReviewFilter.DOUBTFUL -> reviewItems.filter { it.isDoubtful }
    }

    val minutesTaken = attempt.durationTakenSeconds / 60
    val secondsTaken = attempt.durationTakenSeconds % 60
    val timeFormatted = String.format(Locale.getDefault(), "%02d m %02d s", minutesTaken, secondsTaken)

    val accuracy = if (attempt.totalQuestions > 0) {
        (attempt.correctCount.toDouble() / attempt.totalQuestions.toDouble()) * 100.0
    } else 0.0

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Laporan Hasil Ujian CBT", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onGoHome) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali ke Beranda"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onRetakeExam(attempt.examId) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("button_retake_exam")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Ulangi",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ulangi Ujian")
                    }

                    Button(
                        onClick = onGoHome,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CbtPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("button_back_home")
                    ) {
                        Text("Selesai & Keluar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Score Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = attempt.examTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Peserta: ${attempt.studentName} (${attempt.studentId})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Score Circle
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = if (attempt.passed) {
                                            listOf(Color(0xFF22C55E), Color(0xFF16A34A))
                                        } else {
                                            listOf(Color(0xFFEF4444), Color(0xFFDC2626))
                                        }
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = String.format(Locale.getDefault(), "%.1f", attempt.score),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "/ 100",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Status Badge
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (attempt.passed) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                            border = BorderStroke(
                                1.dp,
                                if (attempt.passed) CbtAnswered else CbtError
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (attempt.passed) Icons.Default.CheckCircle else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (attempt.passed) Color(0xFF166534) else Color(0xFF991B1B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (attempt.passed) "LULUS (KKM ${attempt.passingGrade})" else "REMEDIAL (KKM ${attempt.passingGrade})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (attempt.passed) Color(0xFF166534) else Color(0xFF991B1B)
                                )
                            }
                        }
                    }
                }
            }

            // Metrics Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricBox(
                        title = "Benar",
                        value = "${attempt.correctCount}",
                        color = CbtAnswered,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = "Salah",
                        value = "${attempt.incorrectCount}",
                        color = CbtError,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = "Kosong",
                        value = "${attempt.unansweredCount}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = "Akurasi",
                        value = String.format(Locale.getDefault(), "%.0f%%", accuracy),
                        color = CbtPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Duration info banner
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Waktu",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Waktu Pengerjaan: $timeFormatted",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Pembahasan Section Header & Filter Tabs
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pembahasan & Kunci Jawaban",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${filteredReviewItems.size} Soal",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(ReviewFilter.entries) { filter ->
                            val isSelected = reviewFilter == filter
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onFilterChange(filter) },
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = filter.label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Review Items List
            if (filteredReviewItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada soal dalam kategori filter ini.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(filteredReviewItems, key = { it.question.id }) { item ->
                    ReviewQuestionCard(item = item)
                }
            }
        }
    }
}

@Composable
private fun MetricBox(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.08f)
        ),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ReviewQuestionCard(item: QuestionReviewItem) {
    val q = item.question
    val isCorrect = item.isCorrect
    val userChoice = item.selectedOption

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            1.dp,
            if (isCorrect) CbtAnswered.copy(alpha = 0.5f) else CbtError.copy(alpha = 0.4f)
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Row status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Soal No. ${q.orderIndex}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when {
                        isCorrect -> Color(0xFFDCFCE7)
                        userChoice.isNullOrBlank() -> Color(0xFFF1F5F9)
                        else -> Color(0xFFFEE2E2)
                    }
                ) {
                    Text(
                        text = when {
                            isCorrect -> "Jawaban Benar (+1)"
                            userChoice.isNullOrBlank() -> "Tidak Dijawab (0)"
                            else -> "Jawaban Salah (0)"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            isCorrect -> Color(0xFF166534)
                            userChoice.isNullOrBlank() -> Color(0xFF475569)
                            else -> Color(0xFF991B1B)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Question statement
            Text(
                text = q.questionText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            // User Answer vs Correct Key
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "Jawaban Anda: ${userChoice ?: '-'}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCorrect) CbtAnswered else if (userChoice.isNullOrBlank()) MaterialTheme.colorScheme.onSurfaceVariant else CbtError
                )
                Text(
                    text = "Kunci Benar: ${q.correctAnswer}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CbtAnswered
                )
            }

            // Explanation box
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                color = Color(0xFFEFF6FF),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Pembahasan",
                            tint = Color(0xFF1D4ED8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pembahasan & Langkah Pengerjaan:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E40AF)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = q.explanation,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF1E3A8A),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

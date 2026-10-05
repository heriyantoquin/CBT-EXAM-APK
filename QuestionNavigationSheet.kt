package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuestionEntity
import com.example.model.UserAnswerRecord
import com.example.ui.theme.CbtAnswered
import com.example.ui.theme.CbtCurrent
import com.example.ui.theme.CbtDoubtful
import com.example.ui.theme.CbtUnanswered

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionNavigationSheet(
    questions: List<QuestionEntity>,
    userAnswers: Map<String, UserAnswerRecord>,
    currentIndex: Int,
    onSelectQuestion: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val answeredCount = questions.count { q ->
        val record = userAnswers[q.id]
        !record?.selectedOption.isNullOrBlank() && !record?.isDoubtful!!
    }
    val doubtfulCount = questions.count { q ->
        userAnswers[q.id]?.isDoubtful == true
    }
    val unansweredCount = questions.size - (answeredCount + doubtfulCount)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Lembar Navigasi Soal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Ketuk nomor untuk langsung menuju soal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Legend indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendChip(color = CbtAnswered, label = "Dijawab ($answeredCount)")
                LegendChip(color = CbtDoubtful, label = "Ragu ($doubtfulCount)")
                LegendChip(color = CbtUnanswered, label = "Belum ($unansweredCount)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Matrix Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 56.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                items(questions.size) { index ->
                    val q = questions[index]
                    val record = userAnswers[q.id]
                    val chosen = record?.selectedOption
                    val isDoubt = record?.isDoubtful ?: false
                    val isCurrent = index == currentIndex

                    val (tileBg, tileText, borderStroke) = when {
                        isCurrent -> Triple(
                            CbtCurrent,
                            Color.White,
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                        )
                        isDoubt -> Triple(
                            Color(0xFFFEF08A),
                            Color(0xFF854D0E),
                            BorderStroke(1.5.dp, CbtDoubtful)
                        )
                        !chosen.isNullOrBlank() -> Triple(
                            Color(0xFFDCFCE7),
                            Color(0xFF166534),
                            BorderStroke(1.5.dp, CbtAnswered)
                        )
                        else -> Triple(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.onSurfaceVariant,
                            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onSelectQuestion(index)
                            }
                            .testTag("matrix_tile_${index + 1}"),
                        shape = RoundedCornerShape(12.dp),
                        color = tileBg,
                        border = borderStroke,
                        shadowElevation = if (isCurrent) 3.dp else 0.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = tileText
                            )
                            if (!chosen.isNullOrBlank()) {
                                Text(
                                    text = chosen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = tileText
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LegendChip(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

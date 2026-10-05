package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuestionEntity
import com.example.model.TextScale
import com.example.ui.theme.CbtDoubtful
import com.example.ui.theme.CbtPrimary

@Composable
fun QuestionCard(
    question: QuestionEntity,
    selectedOption: String?,
    isDoubtful: Boolean,
    textScale: TextScale,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val scrollState = rememberScrollState()

    val baseFontSize = (15 * textScale.multiplier).sp
    val passageFontSize = (14 * textScale.multiplier).sp
    val lineSpacing = (22 * textScale.multiplier).sp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Ragu-ragu banner if flagged
        if (isDoubtful) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                color = Color(0xFFFEF3C7),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, CbtDoubtful)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Ragu-ragu",
                        tint = Color(0xFFB45309),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ditandai Ragu-Ragu (Perlu diperiksa ulang)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                }
            }
        }

        // Reading Passage box if available
        question.passageText?.let { passage ->
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Teks Bacaan",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Bacaan / Konteks Soal:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = passage,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = passageFontSize,
                            lineHeight = lineSpacing
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Question text
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = question.questionText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = baseFontSize,
                        lineHeight = lineSpacing,
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Options List (A, B, C, D, E)
        val options = mutableListOf<Pair<String, String>>()
        options.add("A" to question.optionA)
        options.add("B" to question.optionB)
        options.add("C" to question.optionC)
        options.add("D" to question.optionD)
        question.optionE?.let {
            if (it.isNotBlank()) options.add("E" to it)
        }

        options.forEach { (key, text) ->
            val isSelected = selectedOption == key
            val animatedBorderColor by animateColorAsState(
                targetValue = if (isSelected) CbtPrimary else MaterialTheme.colorScheme.outlineVariant,
                label = "optionBorder"
            )
            val animatedBgColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                else MaterialTheme.colorScheme.surface,
                label = "optionBg"
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        onOptionSelected(key)
                    }
                    .testTag("option_$key"),
                shape = RoundedCornerShape(12.dp),
                color = animatedBgColor,
                border = BorderStroke(if (isSelected) 2.dp else 1.dp, animatedBorderColor),
                shadowElevation = if (isSelected) 2.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Option badge circle (A, B, C, D, E)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) CbtPrimary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = key,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = baseFontSize,
                            lineHeight = lineSpacing
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    RadioButton(
                        selected = isSelected,
                        onClick = null, // handled by row clickable
                        colors = RadioButtonDefaults.colors(
                            selectedColor = CbtPrimary,
                            unselectedColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

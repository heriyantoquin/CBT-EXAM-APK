package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CbtDoubtful
import com.example.ui.theme.CbtPrimary

@Composable
fun CbtNavigationRow(
    currentIndex: Int,
    totalQuestions: Int,
    isDoubtful: Boolean,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onToggleDoubtful: () -> Unit,
    onFinishClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLastQuestion = currentIndex == totalQuestions - 1
    val isFirstQuestion = currentIndex == 0

    val doubtfulBg by animateColorAsState(
        targetValue = if (isDoubtful) Color(0xFFFEF08A) else MaterialTheme.colorScheme.surfaceVariant,
        label = "doubtfulBg"
    )
    val doubtfulBorder by animateColorAsState(
        targetValue = if (isDoubtful) CbtDoubtful else MaterialTheme.colorScheme.outlineVariant,
        label = "doubtfulBorder"
    )
    val doubtfulText by animateColorAsState(
        targetValue = if (isDoubtful) Color(0xFF854D0E) else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "doubtfulText"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Button: Sebelumnya
            OutlinedButton(
                onClick = onPrevClick,
                enabled = !isFirstQuestion,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("button_prev")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Sebelumnya",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Sebelum", maxLines = 1)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Button: Ragu-Ragu toggle
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onToggleDoubtful() }
                    .testTag("button_toggle_doubtful"),
                shape = RoundedCornerShape(10.dp),
                color = doubtfulBg,
                border = BorderStroke(1.dp, doubtfulBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isDoubtful) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Tandai Ragu-Ragu",
                        tint = doubtfulText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ragu",
                        fontWeight = if (isDoubtful) FontWeight.Bold else FontWeight.Medium,
                        color = doubtfulText,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Button: Berikutnya / Selesai
            if (isLastQuestion) {
                Button(
                    onClick = onFinishClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF16A34A),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("button_finish")
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selesai",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Selesai", maxLines = 1)
                }
            } else {
                Button(
                    onClick = onNextClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CbtPrimary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("button_next")
                ) {
                    Text("Berikut", maxLines = 1)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Berikutnya",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

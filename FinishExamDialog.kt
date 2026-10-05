package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CbtAnswered
import com.example.ui.theme.CbtDoubtful
import com.example.ui.theme.CbtError

@Composable
fun FinishExamDialog(
    answeredCount: Int,
    unansweredCount: Int,
    doubtfulCount: Int,
    onConfirmFinish: () -> Unit,
    onDismiss: () -> Unit
) {
    var hasAgreed by remember { mutableStateOf(false) }
    val hasIncomplete = unansweredCount > 0 || doubtfulCount > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Konfirmasi Selesai Ujian",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Apakah Anda yakin ingin mengakhiri sesi ujian ini? Jawaban yang telah dikumpulkan tidak dapat diubah lagi.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Stats breakdown
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatPill("Dijawab", "$answeredCount", CbtAnswered)
                    StatPill("Ragu", "$doubtfulCount", CbtDoubtful)
                    StatPill("Kosong", "$unansweredCount", CbtError)
                }

                if (hasIncomplete) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFFEF3C7),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.WarningAmber,
                                contentDescription = "Peringatan",
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Masih terdapat $unansweredCount soal belum dijawab dan $doubtfulCount soal ragu-ragu!",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF92400E)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Checkbox confirmation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = hasAgreed,
                        onCheckedChange = { hasAgreed = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.testTag("confirm_finish_checkbox")
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Saya yakin telah menyelesaikan ujian ini.",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmFinish,
                enabled = hasAgreed,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CbtError,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("button_confirm_submit")
            ) {
                Text("Kumpulkan Jawaban")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Kembali Mengerjakan")
            }
        }
    )
}

@Composable
private fun StatPill(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

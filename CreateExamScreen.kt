package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.local.QuestionEntity
import com.example.ui.theme.CbtPrimary
import java.util.UUID

data class DraftQuestion(
    val id: String = UUID.randomUUID().toString(),
    var questionText: String = "",
    var optionA: String = "",
    var optionB: String = "",
    var optionC: String = "",
    var optionD: String = "",
    var optionE: String = "",
    var correctAnswer: String = "A",
    var explanation: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateExamScreen(
    onSaveExam: (
        title: String,
        subject: String,
        token: String,
        durationMinutes: Int,
        passingGrade: Int,
        description: String,
        questions: List<QuestionEntity>
    ) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var durationMinutesText by remember { mutableStateOf("20") }
    var passingGradeText by remember { mutableStateOf("75") }
    var description by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val questionsList = remember {
        mutableStateListOf(
            DraftQuestion(
                questionText = "Manakah organel sel yang berfungsi sebagai penghasil energi utama (ATP)?",
                optionA = "Mitokondria",
                optionB = "Ribosom",
                optionC = "Retikulum Endoplasma",
                optionD = "Badan Golgi",
                optionE = "Vakuola",
                correctAnswer = "A",
                explanation = "Mitokondria merupakan 'powerhouse of the cell' tempat respirasi seluler berlangsung untuk memproduksi ATP."
            )
        )
    }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Buat Ujian Baru (Mode Guru)", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
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
                Button(
                    onClick = {
                        if (title.isBlank()) {
                            errorMessage = "Judul ujian tidak boleh kosong!"
                            return@Button
                        }
                        if (token.isBlank()) {
                            errorMessage = "Token ujian harus diisi!"
                            return@Button
                        }
                        if (questionsList.isEmpty()) {
                            errorMessage = "Tambahkan minimal 1 butir soal!"
                            return@Button
                        }
                        val duration = durationMinutesText.toIntOrNull() ?: 20
                        val kkm = passingGradeText.toIntOrNull() ?: 75

                        val finalEntities = questionsList.mapIndexed { index, draft ->
                            QuestionEntity(
                                id = draft.id,
                                examId = "",
                                orderIndex = index + 1,
                                questionText = draft.questionText.ifBlank { "Pertanyaan No. ${index + 1}" },
                                optionA = draft.optionA.ifBlank { "Opsi A" },
                                optionB = draft.optionB.ifBlank { "Opsi B" },
                                optionC = draft.optionC.ifBlank { "Opsi C" },
                                optionD = draft.optionD.ifBlank { "Opsi D" },
                                optionE = draft.optionE.ifBlank { null },
                                correctAnswer = draft.correctAnswer,
                                explanation = draft.explanation.ifBlank { "Pembahasan belum tersedia." }
                            )
                        }

                        onSaveExam(
                            title,
                            subject.ifBlank { "Umum" },
                            token.trim().uppercase(),
                            duration,
                            kkm,
                            description.ifBlank { "Ujian kustom CBT." },
                            finalEntities
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CbtPrimary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("button_save_exam")
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Simpan")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Simpan & Terbitkan Ujian CBT", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // General Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "1. Data Umum Ujian",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it; errorMessage = null },
                        label = { Text("Judul Ujian *") },
                        placeholder = { Text("Contoh: Kuis Biologi Sel") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = subject,
                            onValueChange = { subject = it },
                            label = { Text("Mata Pelajaran") },
                            placeholder = { Text("Biologi") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = token,
                            onValueChange = { token = it.uppercase(); errorMessage = null },
                            label = { Text("Token Ujian *") },
                            placeholder = { Text("BIO101") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = durationMinutesText,
                            onValueChange = { durationMinutesText = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Durasi (Menit)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = passingGradeText,
                            onValueChange = { passingGradeText = it.filter { ch -> ch.isDigit() } },
                            label = { Text("KKM (Skor Kelulusan)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Petunjuk / Deskripsi Ujian") },
                        placeholder = { Text("Ujian materi bab sel dan metabolisme.") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            errorMessage?.let { err ->
                Surface(
                    color = Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = err,
                        color = Color(0xFF991B1B),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Questions Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "2. Butir Soal (${questionsList.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(
                    onClick = {
                        questionsList.add(DraftQuestion())
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Soal", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tambah Soal")
                }
            }

            // Questions List
            questionsList.forEachIndexed { index, q ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Soal No. ${index + 1}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            if (questionsList.size > 1) {
                                IconButton(
                                    onClick = { questionsList.removeAt(index) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Hapus Soal",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = q.questionText,
                            onValueChange = {
                                q.questionText = it
                                questionsList[index] = q.copy(questionText = it)
                            },
                            label = { Text("Pertanyaan") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Options A, B, C, D
                        OutlinedTextField(
                            value = q.optionA,
                            onValueChange = {
                                q.optionA = it
                                questionsList[index] = q.copy(optionA = it)
                            },
                            label = { Text("Pilihan A") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = q.optionB,
                            onValueChange = {
                                q.optionB = it
                                questionsList[index] = q.copy(optionB = it)
                            },
                            label = { Text("Pilihan B") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = q.optionC,
                            onValueChange = {
                                q.optionC = it
                                questionsList[index] = q.copy(optionC = it)
                            },
                            label = { Text("Pilihan C") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = q.optionD,
                            onValueChange = {
                                q.optionD = it
                                questionsList[index] = q.copy(optionD = it)
                            },
                            label = { Text("Pilihan D") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Correct Answer Selector Chips
                        Column {
                            Text(
                                text = "Kunci Jawaban Benar:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row(
                                modifier = Modifier.padding(top = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("A", "B", "C", "D").forEach { opt ->
                                    val isSelected = q.correctAnswer == opt
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) CbtPrimary else MaterialTheme.colorScheme.surfaceVariant
                                            )
                                            .clickable {
                                                q.correctAnswer = opt
                                                questionsList[index] = q.copy(correctAnswer = opt)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = opt,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        // Explanation
                        OutlinedTextField(
                            value = q.explanation,
                            onValueChange = {
                                q.explanation = it
                                questionsList[index] = q.copy(explanation = it)
                            },
                            label = { Text("Pembahasan & Alasan") },
                            placeholder = { Text("Jelaskan mengapa pilihan ini benar...") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

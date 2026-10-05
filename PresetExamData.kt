package com.example.data.local

object PresetExamData {

    data class PresetBundle(
        val exam: ExamEntity,
        val questions: List<QuestionEntity>
    )

    fun getPresetBundles(): List<PresetBundle> {
        return listOf(
            getTpsSnbtBundle(),
            getMatematikaBundle(),
            getBahasaIndonesiaBundle(),
            getEnglishBundle(),
            getIpaBundle()
        )
    }

    private fun getTpsSnbtBundle(): PresetBundle {
        val examId = "exam_snbt_01"
        val exam = ExamEntity(
            id = examId,
            title = "TPS UTBK-SNBT: Penalaran Umum & Kuantitatif",
            subject = "Tes Potensi Skolastik",
            category = "UTBK / SNBT",
            token = "SNBT26",
            durationMinutes = 20,
            passingGrade = 75,
            description = "Simulasi Tes Skolastik resmi dengan tipe soal silogisme, pola bilangan, penalaran analitis, dan pemecahan masalah kuantitatif.",
            isCustom = false,
            totalQuestions = 6
        )

        val questions = listOf(
            QuestionEntity(
                id = "snbt_q1",
                examId = examId,
                orderIndex = 1,
                questionText = "Semua atlet profesional menjaga pola makan yang ketat. Sebagian orang yang menjaga pola makan yang ketat mengonsumsi suplemen vitamin. Kesimpulan yang tepat dari premis tersebut adalah...",
                passageText = "Premis 1: Semua atlet profesional menjaga pola makan yang ketat.\nPremis 2: Sebagian orang yang menjaga pola makan yang ketat mengonsumsi suplemen vitamin.",
                optionA = "Semua atlet profesional pasti mengonsumsi suplemen vitamin.",
                optionB = "Sebagian atlet profesional mungkin mengonsumsi suplemen vitamin.",
                optionC = "Semua orang yang mengonsumsi suplemen vitamin adalah atlet profesional.",
                optionD = "Tidak ada atlet profesional yang mengonsumsi suplemen vitamin.",
                optionE = "Orang yang tidak berolahraga tidak butuh suplemen vitamin.",
                correctAnswer = "B",
                explanation = "Berdasarkan silogisme partikular, jika 'Semua A adalah B' dan 'Sebagian B adalah C', maka simpulan logis yang valid adalah 'Sebagian A mungkin C' atau 'Sebagian orang yang mengonsumsi suplemen adalah atlet'. Pilihan B adalah pernyataan yang paling tepat secara kaidah logika."
            ),
            QuestionEntity(
                id = "snbt_q2",
                examId = examId,
                orderIndex = 2,
                questionText = "Perhatikan barisan bilangan berikut: 3, 5, 9, 17, 33, ...\nBilangan berikutnya pada pola deret tersebut adalah...",
                optionA = "49",
                optionB = "55",
                optionC = "65",
                optionD = "67",
                optionE = "71",
                correctAnswer = "C",
                explanation = "Analisis selisih antar suku:\n5 - 3 = 2 (+2^1)\n9 - 5 = 4 (+2^2)\n17 - 9 = 8 (+2^3)\n33 - 17 = 16 (+2^4)\nMaka selisih berikutnya adalah 16 × 2 = 32 (+2^5).\nSehingga suku selanjutnya adalah 33 + 32 = 65."
            ),
            QuestionEntity(
                id = "snbt_q3",
                examId = examId,
                orderIndex = 3,
                questionText = "Suatu pekerjaan pengecatan gedung dapat diselesaikan oleh 12 orang pekerja dalam waktu 15 hari. Jika proyek tersebut harus selesai dalam waktu 10 hari, berapa banyak tambahan pekerja yang dibutuhkan?",
                optionA = "4 orang",
                optionB = "6 orang",
                optionC = "8 orang",
                optionD = "18 orang",
                optionE = "20 orang",
                correctAnswer = "B",
                explanation = "Ini adalah persoalan perbandingan berbalik nilai:\nPekerja_1 × Hari_1 = Pekerja_2 × Hari_2\n12 × 15 = P2 × 10\n180 = 10 P2\nP2 = 18 orang.\nJumlah tambahan pekerja = 18 - 12 = 6 orang."
            ),
            QuestionEntity(
                id = "snbt_q4",
                examId = examId,
                orderIndex = 4,
                questionText = "Jika diketahui fungsi f(2x + 1) = 4x² + 6x - 5, maka nilai dari f(5) adalah...",
                optionA = "11",
                optionB = "23",
                optionC = "25",
                optionD = "31",
                optionE = "45",
                correctAnswer = "B",
                explanation = "Untuk mencari nilai f(5), substitusi argumen:\n2x + 1 = 5\n2x = 4  =>  x = 2.\nMasukkan nilai x = 2 ke rumus fungsi:\nf(5) = 4(2)² + 6(2) - 5\n= 4(4) + 12 - 5\n= 16 + 12 - 5 = 23."
            ),
            QuestionEntity(
                id = "snbt_q5",
                examId = examId,
                orderIndex = 5,
                questionText = "Lima orang mahasiswa (A, B, C, D, E) duduk berjajar di bangku kuliah. A duduk di sebelah kiri B tetapi di sebelah kanan C. D berada di ujung paling kanan. E duduk tepat di sebelah kiri C. Urutan posisi duduk dari kiri ke kanan adalah...",
                optionA = "E - C - A - B - D",
                optionB = "C - E - A - B - D",
                optionC = "E - A - C - B - D",
                optionD = "A - B - C - D - E",
                optionE = "E - C - B - A - D",
                correctAnswer = "A",
                explanation = "1. 'A di sebelah kiri B dan kanan C' => C, A, B.\n2. 'D di ujung paling kanan' => ... D.\n3. 'E duduk tepat di sebelah kiri C' => E, C, A, B, D.\nJadi urutan yang tepat dari kiri ke kanan adalah E - C - A - B - D."
            ),
            QuestionEntity(
                id = "snbt_q6",
                examId = examId,
                orderIndex = 6,
                questionText = "Rata-rata nilai ujian matematika dari 29 siswa adalah 72. Jika nilai seorang siswa baru yang bernama Budi digabungkan, nilai rata-ratanya menjadi 73. Berapakah nilai ujian matematika Budi?",
                optionA = "98",
                optionB = "100",
                optionC = "102",
                optionD = "96",
                optionE = "92",
                correctAnswer = "C",
                explanation = "Total nilai awal 29 siswa = 29 × 72 = 2.088.\nTotal nilai setelah Budi masuk (30 siswa) = 30 × 73 = 2.190.\nNilai Budi = Total baru - Total awal = 2.190 - 2.088 = 102."
            )
        )

        return PresetBundle(exam, questions)
    }

    private fun getMatematikaBundle(): PresetBundle {
        val examId = "exam_mat_01"
        val exam = ExamEntity(
            id = examId,
            title = "Simulasi Ujian Matematika Umum",
            subject = "Matematika",
            category = "Ujian Nasional / Sekolah",
            token = "MTK001",
            durationMinutes = 25,
            passingGrade = 70,
            description = "Paket soal matematika aljabar, persamaan kuadrat, trigonometri, dan statistika standar kelulusan sekolah menengah.",
            isCustom = false,
            totalQuestions = 5
        )

        val questions = listOf(
            QuestionEntity(
                id = "mat_q1",
                examId = examId,
                orderIndex = 1,
                questionText = "Akar-akar persamaan kuadrat x² - 5x + 6 = 0 adalah x₁ dan x₂. Nilai dari x₁² + x₂² adalah...",
                optionA = "11",
                optionB = "13",
                optionC = "19",
                optionD = "25",
                optionE = "31",
                correctAnswer = "B",
                explanation = "Dari persamaan x² - 5x + 6 = 0:\nx₁ + x₂ = -b/a = 5\nx₁ · x₂ = c/a = 6\nRumus aljabar: x₁² + x₂² = (x₁ + x₂)² - 2(x₁ · x₂)\n= (5)² - 2(6) = 25 - 12 = 13."
            ),
            QuestionEntity(
                id = "mat_q2",
                examId = examId,
                orderIndex = 2,
                questionText = "Nilai dari sin 30° + cos 60° - tan 45° adalah...",
                optionA = "-1",
                optionB = "0",
                optionC = "1/2",
                optionD = "1",
                optionE = "√3",
                correctAnswer = "B",
                explanation = "Nilai trigonometri sudut istimewa:\nsin 30° = 1/2\ncos 60° = 1/2\ntan 45° = 1\nPerhitungan: (1/2) + (1/2) - 1 = 1 - 1 = 0."
            ),
            QuestionEntity(
                id = "mat_q3",
                examId = examId,
                orderIndex = 3,
                questionText = "Sebuah dadu bersisi enam yang adil dilempar satu kali. Peluang munculnya mata dadu bilangan prima adalah...",
                optionA = "1/6",
                optionB = "1/3",
                optionC = "1/2",
                optionD = "2/3",
                optionE = "5/6",
                correctAnswer = "C",
                explanation = "Ruang sampel dadu S = {1, 2, 3, 4, 5, 6}, maka n(S) = 6.\nKejadian mata dadu prima A = {2, 3, 5}, maka n(A) = 3.\nPeluang P(A) = n(A) / n(S) = 3/6 = 1/2."
            ),
            QuestionEntity(
                id = "mat_q4",
                examId = examId,
                orderIndex = 4,
                questionText = "Diketahui barisan aritmatika dengan suku ke-3 adalah 11 dan suku ke-7 adalah 23. Suku ke-20 barisan tersebut adalah...",
                optionA = "56",
                optionB = "62",
                optionC = "68",
                optionD = "71",
                optionE = "75",
                correctAnswer = "B",
                explanation = "U3 = a + 2b = 11\nU7 = a + 6b = 23\nKurangkan: 4b = 12  =>  b = 3.\na + 2(3) = 11  =>  a = 11 - 6 = 5.\nSuku ke-20: U20 = a + 19b = 5 + 19(3) = 5 + 57 = 62."
            ),
            QuestionEntity(
                id = "mat_q5",
                examId = examId,
                orderIndex = 5,
                questionText = "Bentuk sederhana dari (a³ b⁻² c⁴) / (a⁻¹ b² c²) adalah...",
                optionA = "a⁴ b⁻⁴ c²",
                optionB = "a² b⁰ c²",
                optionC = "a⁴ b⁴ c²",
                optionD = "a² b⁻⁴ c⁶",
                optionE = "a⁻⁴ b⁴ c⁻²",
                correctAnswer = "A",
                explanation = "Sifat eksponen pangkat pecahan/pembagian:\na^(3 - (-1)) = a^(3 + 1) = a⁴\nb^(-2 - 2) = b^(-4)\nc^(4 - 2) = c²\nSehingga hasilnya adalah a⁴ b⁻⁴ c² (atau a⁴ c² / b⁴)."
            )
        )

        return PresetBundle(exam, questions)
    }

    private fun getBahasaIndonesiaBundle(): PresetBundle {
        val examId = "exam_indo_01"
        val exam = ExamEntity(
            id = examId,
            title = "Literasi & Kebahasaan Indonesia (EYD V)",
            subject = "Bahasa Indonesia",
            category = "Ujian Nasional / Sekolah",
            token = "BINDO1",
            durationMinutes = 20,
            passingGrade = 75,
            description = "Uji pemahaman membaca, ide pokok paragraf, kalimat efektif, konjungsi, dan kaidah ejaan bahasa Indonesia yang disempurnakan.",
            isCustom = false,
            totalQuestions = 5
        )

        val questions = listOf(
            QuestionEntity(
                id = "indo_q1",
                examId = examId,
                orderIndex = 1,
                questionText = "Ide pokok yang mendasari paragraf tersebut adalah...",
                passageText = "Kecerdasan buatan (Artificial Intelligence) terus berkembang pesat dan mulai mentransformasi sektor pendidikan secara signifikan. Guru kini dapat memanfaatkan sistem pembelajaran adaptif untuk memetakan kelemahan belajar siswa secara presisi. Selain itu, perangkat lunak berbasis AI memungkinkan personalisasi kurikulum sesuai dengan kecepatan pemahaman masing-masing individu.",
                optionA = "Peran kecerdasan buatan dalam mentransformasi sektor pendidikan.",
                optionB = "Peta kelemahan siswa dalam menyerap kurikulum sekolah.",
                optionC = "Kecerdasan buatan akan menggantikan peran guru di masa depan.",
                optionD = "Biaya pengadaan perangkat lunak teknologi di sekolah modern.",
                optionE = "Perbedaan kecepatan belajar siswa di dalam kelas.",
                correctAnswer = "A",
                explanation = "Kalimat utama terletak pada kalimat pertama: 'Kecerdasan buatan terus berkembang pesat dan mulai mentransformasi sektor pendidikan secara signifikan.' Kalimat-kalimat selanjutnya merupakan penjelas bagaimana AI mengubah cara mengajar dan belajar."
            ),
            QuestionEntity(
                id = "indo_q2",
                examId = examId,
                orderIndex = 2,
                questionText = "Manakah kalimat berikut yang memenuhi kaidah kalimat efektif?",
                optionA = "Bagi para peserta ujian diharapkan untuk segera memasuki ruangan.",
                optionB = "Peserta ujian diharapkan segera memasuki ruangan.",
                optionC = "Siswa-siswa saling bersalam-salaman setelah acara perpisahan.",
                optionD = "Dalam rapat itu membicarakan tentang kenaikan iuran komite.",
                optionE = "Karena hujan lebat menyebabkan jembatan penghubung itu terputus.",
                correctAnswer = "B",
                explanation = "Pilihan B adalah kalimat efektif karena subjeknya jelas ('Peserta ujian'), predikatnya jelas ('diharapkan segera memasuki ruangan'), dan tidak menggunakan pemborosan kata (pleonasme) atau kata depan di awal subjek."
            ),
            QuestionEntity(
                id = "indo_q3",
                examId = examId,
                orderIndex = 3,
                questionText = "Penulisan kata serapan yang baku menurut Kamus Besar Bahasa Indonesia (KBBI) dan EYD V terdapat pada kelompok kata...",
                optionA = "Analisa, metoda, sistematis",
                optionB = "Analisis, metode, sistematis",
                optionC = "Analisa, metoda, sistematik",
                optionD = "Analisis, methoda, sistim",
                optionE = "Analisa, metode, sistem",
                correctAnswer = "B",
                explanation = "Bentuk baku serapan bahasa asing yang benar adalah: Analisis (bukan analisa), Metode (bukan metoda), dan Sistematis (bukan sistimatis)."
            ),
            QuestionEntity(
                id = "indo_q4",
                examId = examId,
                orderIndex = 4,
                questionText = "Kata penghubung (konjungsi) yang paling tepat untuk melengkapi bagian rumpang adalah...",
                passageText = "Pemerintah gencar mempromosikan energi terbarukan [...] ketergantungan pada bahan bakar fosil belum dapat dikurangi secara drastis dalam waktu singkat.",
                optionA = "sehingga",
                optionB = "karena",
                optionC = "namun",
                optionD = "bahkan",
                optionE = "agar",
                correctAnswer = "C",
                explanation = "Hubungan antara kedua klausa tersebut bersifat pertentangan (konsesif/antitesis), sehingga konjungsi pertentangan antarklausa seperti 'namun' atau 'tetapi' adalah yang paling tepat."
            ),
            QuestionEntity(
                id = "indo_q5",
                examId = examId,
                orderIndex = 5,
                questionText = "Tujuan utama penulis dalam paragraf persuasif di atas adalah...",
                passageText = "Pengelolaan sampah plastik yang tidak bertanggung jawab menjadi ancaman nyata bagi biota laut Indonesia. Setiap tahunnya, jutaan ton limbah plastik mencemari perairan kita. Oleh sebab itu, marilah kita mulai beralih menggunakan kantong belanja guna ulang dan mengurangi konsumsi wadah sekali pakai demi masa depan bumi kita.",
                optionA = "Menginformasikan jumlah kapal penangkap ikan di laut.",
                optionB = "Mengajak pembaca mengurangi penggunaan plastik sekali pakai.",
                optionC = "Mengecam kinerja instansi pengolah sampah daerah.",
                optionD = "Mempromosikan merek kantong belanja tertentu.",
                optionE = "Menjelaskan siklus daur ulang limbah anorganik di pabrik.",
                correctAnswer = "B",
                explanation = "Teks persuasif ditandai dengan kata ajakan 'marilah kita mulai beralih...'. Tujuannya adalah mengajak masyarakat pembaca untuk peduli lingkungan dengan mengurangi pemakaian plastik sekali pakai."
            )
        )

        return PresetBundle(exam, questions)
    }

    private fun getEnglishBundle(): PresetBundle {
        val examId = "exam_eng_01"
        val exam = ExamEntity(
            id = examId,
            title = "English Proficiency & Reading Comprehension",
            subject = "Bahasa Inggris",
            category = "Kedinasan / CPNS",
            token = "ENG202",
            durationMinutes = 20,
            passingGrade = 70,
            description = "Reading skills, grammatical accuracy, tenses, conditional clauses, and contextual vocabulary.",
            isCustom = false,
            totalQuestions = 5
        )

        val questions = listOf(
            QuestionEntity(
                id = "eng_q1",
                examId = examId,
                orderIndex = 1,
                questionText = "According to the passage, why are solar panels becoming more widely adopted worldwide?",
                passageText = "Solar energy has witnessed exponential adoption in recent decades. The primary catalysts include drastic reductions in photovoltaic manufacturing expenses and supportive governmental tax incentives. Furthermore, efficiency breakthroughs allow modern panels to generate significant electricity even in overcast conditions.",
                optionA = "They require no installation or maintenance whatsoever.",
                optionB = "Manufacturing costs have dropped and governments offer incentives.",
                optionC = "Fossil fuel plants have ceased operation completely.",
                optionD = "They only function properly during sunny summer days.",
                optionE = "They are lighter in weight than standard roof tiles.",
                correctAnswer = "B",
                explanation = "The passage explicitly states: 'The primary catalysts include drastic reductions in photovoltaic manufacturing expenses and supportive governmental tax incentives.'"
            ),
            QuestionEntity(
                id = "eng_q2",
                examId = examId,
                orderIndex = 2,
                questionText = "Choose the correct sentence to complete the conditional sentence:\n\"If Sarah had studied harder for the final exam, she ______ with honors.\"",
                optionA = "will graduate",
                optionB = "would graduate",
                optionC = "would have graduated",
                optionD = "graduated",
                optionE = "has graduated",
                correctAnswer = "C",
                explanation = "This is a Third Conditional sentence expressing an unreal past condition: 'If + past perfect (had studied), would have + past participle (would have graduated)'."
            ),
            QuestionEntity(
                id = "eng_q3",
                examId = examId,
                orderIndex = 3,
                questionText = "Which passive form correctly transforms the sentence:\n\"The senior engineers are designing a state-of-the-art spacecraft.\"",
                optionA = "A state-of-the-art spacecraft was designed by the senior engineers.",
                optionB = "A state-of-the-art spacecraft is being designed by the senior engineers.",
                optionC = "A state-of-the-art spacecraft has been designed by the senior engineers.",
                optionD = "A state-of-the-art spacecraft had been designed by the senior engineers.",
                optionE = "A state-of-the-art spacecraft will be designed by the senior engineers.",
                correctAnswer = "B",
                explanation = "The original sentence is in Present Continuous tense ('are designing'). The passive structure is: object + is/are + being + V3 ('is being designed')."
            ),
            QuestionEntity(
                id = "eng_q4",
                examId = examId,
                orderIndex = 4,
                questionText = "The word 'tenacious' in the sentence: \"Despite facing repeated failures, the young entrepreneur remained tenacious until the breakthrough occurred\" is closest in meaning to...",
                optionA = "hesitant",
                optionB = "persistent",
                optionC = "fragile",
                optionD = "indifferent",
                optionE = "impatient",
                correctAnswer = "B",
                explanation = "'Tenacious' means holding fast, persistent, determined, and refusing to give up in the face of obstacles. Thus, 'persistent' is the closest synonym."
            ),
            QuestionEntity(
                id = "eng_q5",
                examId = examId,
                orderIndex = 5,
                questionText = "Choose the grammatically correct sentence:",
                optionA = "Neither the manager nor the employees was available for comment.",
                optionB = "Neither the manager nor the employees were available for comment.",
                optionC = "Neither the manager or the employees was available for comment.",
                optionD = "Both the manager nor the employees were available for comment.",
                optionE = "Either the manager and the employees were available for comment.",
                correctAnswer = "B",
                explanation = "With the correlative conjunction 'Neither ... nor', the verb agrees with the subject closest to it. Here, 'the employees' is plural, so 'were available' is correct."
            )
        )

        return PresetBundle(exam, questions)
    }

    private fun getIpaBundle(): PresetBundle {
        val examId = "exam_ipa_01"
        val exam = ExamEntity(
            id = examId,
            title = "Simulasi IPA Terpadu (Fisika, Kimia, Biologi)",
            subject = "Ilmu Pengetahuan Alam",
            category = "Latihan Mandiri",
            token = "IPA303",
            durationMinutes = 20,
            passingGrade = 75,
            description = "Konsep dasar gaya dan gerak, fotosintesis, rantai makanan, ikatan kimia, dan asam-basa.",
            isCustom = false,
            totalQuestions = 5
        )

        val questions = listOf(
            QuestionEntity(
                id = "ipa_q1",
                examId = examId,
                orderIndex = 1,
                questionText = "Sebuah balok bermassa 4 kg mula-mula diam di atas lantai mendatar yang licin. Balok tersebut didorong dengan gaya konstan 20 N selama 3 detik. Kecepatan balok pada detik ke-3 adalah...",
                optionA = "5 m/s",
                optionB = "12 m/s",
                optionC = "15 m/s",
                optionD = "24 m/s",
                optionE = "60 m/s",
                correctAnswer = "C",
                explanation = "Berdasarkan Hukum II Newton:\nF = m · a\n20 = 4 · a  =>  a = 5 m/s².\nGerak Lurus Berubah Beraturan (GLBB):\nVt = V0 + a · t\nVt = 0 + (5 m/s²)(3 s) = 15 m/s."
            ),
            QuestionEntity(
                id = "ipa_q2",
                examId = examId,
                orderIndex = 2,
                questionText = "Organel sel tumbuhan yang berfungsi sebagai tempat berlangsungnya proses fotosintesis dan menghasilkan glukosa adalah...",
                optionA = "Mitokondria",
                optionB = "Kloroplas",
                optionC = "Ribosom",
                optionD = "Badan Golgi",
                optionE = "Retikulum Endoplasma",
                correctAnswer = "B",
                explanation = "Kloroplas mengandung pigmen klorofil yang menangkap energi cahaya matahari untuk melangsungkan fotosintesis (reaksi terang dan siklus Calvin). Mitokondria berfungsi sebagai tempat respirasi sel penghasil ATP."
            ),
            QuestionEntity(
                id = "ipa_q3",
                examId = examId,
                orderIndex = 3,
                questionText = "Suatu larutan memiliki nilai pH = 3. Karakteristik yang tepat mengenai larutan tersebut adalah...",
                optionA = "Bersifat basa kuat dan mengubah lakmus merah menjadi biru.",
                optionB = "Bersifat netral seperti air murni.",
                optionC = "Bersifat asam dan memiliki konsentrasi ion H⁺ lebih tinggi dari OH⁻.",
                optionD = "Bersifat basa lemah dan berasa pahit.",
                optionE = "Tidak menghantarkan arus listrik sama sekali.",
                correctAnswer = "C",
                explanation = "Skala pH < 7 mengindikasikan larutan bersifat asam. Pada larutan asam, konsentrasi ion hidrogen [H⁺] lebih besar daripada ion hidroksida [OH⁻]. Larutan asam mengubah lakmus biru menjadi merah."
            ),
            QuestionEntity(
                id = "ipa_q4",
                examId = examId,
                orderIndex = 4,
                questionText = "Dalam suatu ekosistem sawah: Padi -> Belalang -> Katak -> Ular -> Elang. Jika populasi katak mengalami kepunahan mendadak akibat pestisida, dampak langsung yang kemungkinan besar terjadi adalah...",
                optionA = "Populasi belalang meningkat dan populasi ular menurun.",
                optionB = "Populasi padi meningkat drastis.",
                optionC = "Populasi elang meningkat pesat.",
                optionD = "Populasi belalang menurun dan populasi ular bertambah.",
                optionE = "Semua rantai makanan tidak terpengaruh.",
                correctAnswer = "A",
                explanation = "Katak merupakan predator belalang dan sekaligus mangsa ular. Jika katak punah, belalang kehilangan predator alaminya sehingga populasi belalang melonjak (merugikan tanaman padi), sedangkan ular kehilangan sumber makanannya sehingga populasinya menurun."
            ),
            QuestionEntity(
                id = "ipa_q5",
                examId = examId,
                orderIndex = 5,
                questionText = "Sebuah benda bermassa 2 kg jatuh bebas dari ketinggian 10 meter di atas tanah (percepatan gravitasi g = 10 m/s²). Energi kinetik benda sesaat sebelum menyentuh tanah adalah...",
                optionA = "50 Joule",
                optionB = "100 Joule",
                optionC = "150 Joule",
                optionD = "200 Joule",
                optionE = "400 Joule",
                correctAnswer = "D",
                explanation = "Berdasarkan Hukum Kekekalan Energi Mekanik:\nEnergi Mekanik awal = Energi Mekanik akhir\nEp_awal + Ek_awal = Ep_akhir + Ek_akhir\n(m · g · h) + 0 = 0 + Ek_akhir\nEk_akhir = 2 kg × 10 m/s² × 10 m = 200 Joule."
            )
        )

        return PresetBundle(exam, questions)
    }
}

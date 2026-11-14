package com.kemal.goldenhorn

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class QuizQ(
    val qTr: String, val qEn: String,
    val options: List<Pair<String, String>>, // tr, en
    val correct: Int
)

private val questions = listOf(
    QuizQ("2026-2027 FTC sezonunun adı nedir?", "What is the 2026-2027 FTC season called?",
        listOf("BIOBUZZ" to "BIOBUZZ", "DECODE" to "DECODE", "CENTERSTAGE" to "CENTERSTAGE", "POWERPLAY" to "POWERPLAY"), 0),
    QuizQ("Bir FTC maçında Auto (otonom) süresi kaç saniyedir?", "How long is the Autonomous period in an FTC match?",
        listOf("30 saniye" to "30 seconds", "60 saniye" to "60 seconds", "15 saniye" to "15 seconds", "90 saniye" to "90 seconds"), 0),
    QuizQ("Golden Horn Jr takımının numarası kaçtır?", "What is Golden Horn Jr's team number?",
        listOf("25039" to "25039", "12345" to "12345", "8159" to "8159", "25030" to "25030"), 0),
    QuizQ("FTC sahası kaç feet uzunluğundadır?", "How long is an FTC field?",
        listOf("12 ft" to "12 ft", "10 ft" to "10 ft", "8 ft" to "8 ft", "16 ft" to "16 ft"), 0),
    QuizQ("TeleOp dönemi kaç dakikadır?", "How long is the TeleOp period?",
        listOf("2 dakika" to "2 minutes", "1 dakika" to "1 minute", "3 dakika" to "3 minutes", "5 dakika" to "5 minutes"), 0)
)

@Composable
fun QuizScreen() {
    LangManager.isTurkish
    var index by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var answered by remember { mutableStateOf<Int?>(null) }
    var done by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (done) {
            SectionLabel("BIOBUZZ Quiz")
            BioCard {
                Text(LangManager.t("Sonuç", "Result"), color = Biobuzz.mutedG, fontSize = 12.sp)
                Text("$score / ${questions.size}", color = Biobuzz.honey,
                    fontSize = 40.sp, fontWeight = FontWeight.Black)
            }
            Button(onClick = { index = 0; score = 0; answered = null; done = false },
                colors = ButtonDefaults.buttonColors(containerColor = Biobuzz.honey)) {
                Text(LangManager.t("Tekrar", "Restart"), color = Biobuzz.bark)
            }
            return@Column
        }

        val q = questions[index]
        SectionLabel("BIOBUZZ Quiz  (${index + 1}/${questions.size})")
        Text(LangManager.t(q.qTr, q.qEn), color = Biobuzz.cream,
            fontSize = 18.sp, fontWeight = FontWeight.Bold)

        q.options.forEachIndexed { i, opt ->
            val isAnswered = answered != null
            val color = when {
                !isAnswered -> Biobuzz.bgCard
                i == q.correct -> Biobuzz.leaf
                i == answered -> Biobuzz.bloodO
                else -> Biobuzz.bgCard
            }
            Button(
                onClick = {
                    if (answered == null) {
                        answered = i
                        if (i == q.correct) score++
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = color),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(LangManager.t(opt.first, opt.second),
                    color = if (color == Biobuzz.bgCard) Biobuzz.cream else Biobuzz.bark)
            }
        }

        if (answered != null) {
            Button(
                onClick = {
                    if (index + 1 >= questions.size) done = true
                    else { index++; answered = null }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Biobuzz.honey),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (index + 1 >= questions.size) LangManager.t("Bitir", "Finish")
                    else LangManager.t("Sonraki", "Next"),
                    color = Biobuzz.bark
                )
            }
        }
    }
}

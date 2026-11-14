package com.kemal.goldenhorn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TechScreen() {
    LangManager.isTurkish
    var distance by remember { mutableStateOf("") }   // metre
    var speed by remember { mutableStateOf("") }       // m/s

    val time = run {
        val d = distance.toDoubleOrNull()
        val s = speed.toDoubleOrNull()
        if (d != null && s != null && s > 0) d / s else null
    }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionLabel(LangManager.t("Sprint Hesaplayıcı", "Sprint Calculator"))
        Text(
            LangManager.t(
                "Robotun belirli bir mesafeyi sabit hızla ne kadar sürede katedeceğini hesaplar.",
                "Calculates how long the robot takes to cover a distance at constant speed."
            ),
            color = Biobuzz.mutedG, fontSize = 12.sp
        )

        BioTextField(distance, { distance = it }, LangManager.t("Mesafe (metre)", "Distance (m)"), numeric = true)
        BioTextField(speed, { speed = it }, LangManager.t("Hız (m/s)", "Speed (m/s)"), numeric = true)

        BioCard {
            Text(LangManager.t("Tahmini Süre", "Estimated Time"),
                color = Biobuzz.mutedG, fontSize = 12.sp)
            Text(
                if (time != null) String.format("%.2f s", time) else "—",
                color = Biobuzz.honey, fontSize = 34.sp, fontWeight = FontWeight.Black
            )
        }

        // Birkaç referans değer
        BioCard {
            Text(LangManager.t("FTC İpuçları", "FTC Tips"),
                color = Biobuzz.cream, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(
                LangManager.t(
                    "• Tipik FTC robot hızı: 1.2–1.8 m/s\n• Saha uzunluğu: 3.66 m (12 ft)\n• Auto süresi: 30 s · TeleOp: 2 dk",
                    "• Typical FTC robot speed: 1.2–1.8 m/s\n• Field length: 3.66 m (12 ft)\n• Auto: 30 s · TeleOp: 2 min"
                ),
                color = Biobuzz.mutedG, fontSize = 13.sp, lineHeight = 20.sp
            )
        }
    }
}

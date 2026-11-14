package com.kemal.goldenhorn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(store: Store) {
    val team = TeamManager.selectedTeam
    val isDefault = TeamManager.isDefaultTeam
    LangManager.isTurkish // recompose on language change

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // HERO
        item {
            BioCard {
                Text(
                    text = if (isDefault) "FTC TRACKER" else team.name,
                    color = Biobuzz.honey,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (isDefault)
                        LangManager.t("Tüm FTC takımlarının maç takibi", "Track every FTC team's matches")
                    else "#${team.teamNumber} · ${team.location}",
                    color = Biobuzz.cream,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = LangManager.t(
                        "Golden Horn Jr #25039 tarafından geliştirildi",
                        "Developed by Golden Horn Jr #25039"
                    ),
                    color = Biobuzz.mutedG,
                    fontSize = 11.sp
                )
            }
        }

        // Takım seçici
        item { TeamSelectorBar() }

        // Açıklama / disclaimer
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Biobuzz.forest.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = LangManager.t(
                        "Bu uygulamadaki projeler, teknik bilgiler ve takım içeriği Golden Horn Jr #25039'a aittir ve bilgi paylaşımı amacıyla sunulmuştur. Maç sonuçları tüm FTC takımları için resmi FTC Events API'den gelir.",
                        "The projects, tech notes and team content in this app belong to Golden Horn Jr #25039 and are shared for informational purposes. Match results come from the official FTC Events API for all FTC teams."
                    ),
                    color = Biobuzz.cream,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }

        // Duyurular
        item { SectionLabel(LangManager.t("Duyurular", "Announcements")) }
        if (store.announcements.isEmpty()) {
            item {
                Text(
                    LangManager.t("Henüz duyuru yok.", "No announcements yet."),
                    color = Biobuzz.mutedG, fontSize = 13.sp
                )
            }
        } else {
            items(store.announcements) { a ->
                BioCard {
                    Text(
                        LangManager.t(a.titleTr ?: "", a.titleEn ?: ""),
                        color = Biobuzz.cream, fontSize = 15.sp, fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        LangManager.t(a.bodyTr ?: "", a.bodyEn ?: ""),
                        color = Biobuzz.mutedG, fontSize = 13.sp
                    )
                }
            }
        }
    }
}

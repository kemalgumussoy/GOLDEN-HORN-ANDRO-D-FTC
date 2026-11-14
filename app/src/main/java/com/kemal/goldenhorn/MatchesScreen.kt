package com.kemal.goldenhorn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun MatchesScreen(store: Store) {
    LangManager.isTurkish
    val scope = rememberCoroutineScope()
    var syncing by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf<String?>(null) }

    val wins = store.matchResults.count { it.result == "win" }
    val losses = store.matchResults.count { it.result == "loss" }
    val ties = store.matchResults.count { it.result == "tie" }

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { TeamSelectorBar() }

        item {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        syncing = true; msg = null
                        scope.launch {
                            val (added, err) = store.syncFromFtc(TeamManager.selectedTeam.teamNumber)
                            syncing = false
                            msg = when {
                                err != null -> LangManager.t("Hata: $err", "Error: $err")
                                added == 0 -> LangManager.t("Güncel!", "Up to date!")
                                else -> LangManager.t("$added maç eklendi! 🎉", "$added matches added! 🎉")
                            }
                        }
                    },
                    enabled = !syncing,
                    colors = ButtonDefaults.buttonColors(containerColor = Biobuzz.honey)
                ) {
                    if (syncing) CircularProgressIndicator(
                        color = Biobuzz.bark, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                    else Icon(Icons.Filled.Refresh, null, tint = Biobuzz.bark)
                    Spacer(Modifier.width(6.dp))
                    Text(LangManager.t("FTC'den Çek", "Sync from FTC"), color = Biobuzz.bark)
                }
            }
        }
        msg?.let { item { Text(it, color = Biobuzz.leaf, fontSize = 13.sp) } }

        // İstatistik
        item {
            BioCard {
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    StatCell(wins.toString(), LangManager.t("Galibiyet", "Wins"), Biobuzz.leaf)
                    StatCell(losses.toString(), LangManager.t("Mağlubiyet", "Losses"), Biobuzz.bloodO)
                    StatCell(ties.toString(), LangManager.t("Berabere", "Ties"), Biobuzz.mutedG)
                }
            }
        }

        item { SectionLabel(LangManager.t("Maç Sonuçları", "Match Results")) }
        if (store.matchResults.isEmpty()) {
            item {
                Text(LangManager.t("Henüz maç yok. 'FTC'den Çek'e bas.", "No matches yet. Tap 'Sync from FTC'."),
                    color = Biobuzz.mutedG, fontSize = 13.sp)
            }
        } else {
            items(store.matchResults) { m ->
                val emoji = when (m.result) { "win" -> "🏆"; "loss" -> "💪"; else -> "🤝" }
                BioCard {
                    Text("$emoji ${LangManager.t(m.eventNameTr ?: "", m.eventNameEn ?: "")}",
                        color = Biobuzz.cream, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(2.dp))
                    Text("M${m.matchNumber}:  ${m.ourScore} - ${m.opponentScore}   " +
                            LangManager.t("Rakip", "vs") + " ${m.opponent}",
                        color = Biobuzz.mutedG, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun RowScope.StatCell(value: String, label: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
        Text(value, color = color, fontSize = 22.sp, fontWeight = FontWeight.Black)
        Text(label, color = Biobuzz.mutedG, fontSize = 11.sp)
    }
}

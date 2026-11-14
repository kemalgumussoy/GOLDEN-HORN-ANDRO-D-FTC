package com.kemal.goldenhorn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun TeamSelectorBar() {
    val team = TeamManager.selectedTeam
    LangManager.isTurkish
    BioCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("#${team.teamNumber} · ${team.name}",
                    color = Biobuzz.cream, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(LangManager.t("Takım değiştirmek için 'Takımlar' sekmesi",
                    "Use the 'Teams' tab to switch team"),
                    color = Biobuzz.mutedG, fontSize = 11.sp)
            }
            Icon(Icons.Filled.CheckCircle, null, tint = Biobuzz.leaf)
        }
    }
}

@Composable
fun TeamsScreen() {
    LangManager.isTurkish
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    var searching by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<FtcTeam?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { SectionLabel(LangManager.t("Takım Numarası ile Ara", "Search by Team Number")) }
        item {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BioTextField(query, { query = it },
                    LangManager.t("Örn: 25039", "e.g. 25039"),
                    numeric = true, modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        val num = query.toIntOrNull()
                        if (num == null) { error = LangManager.t("Geçerli numara gir", "Enter a valid number"); return@Button }
                        searching = true; error = null; result = null
                        scope.launch {
                            try { result = FtcApi.searchTeam(num)
                                if (result == null) error = LangManager.t("Takım bulunamadı", "Team not found")
                            } catch (e: Exception) { error = e.message }
                            searching = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Biobuzz.honey)
                ) { Text(LangManager.t("Ara", "Search"), color = Biobuzz.bark) }
            }
        }

        if (searching) item { CircularProgressIndicator(color = Biobuzz.honey) }
        error?.let { e -> item { Text(e, color = Biobuzz.bloodO, fontSize = 13.sp) } }

        result?.let { t ->
            item {
                val following = TeamManager.followedTeams.any { it.teamNumber == t.teamNumber }
                BioCard {
                    Text(t.displayName, color = Biobuzz.cream, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("#${t.teamNumber}  ${t.location}", color = Biobuzz.mutedG, fontSize = 12.sp)
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val ft = FollowedTeam(t.teamNumber, t.displayName, t.location)
                            if (following) TeamManager.select(ft) else TeamManager.follow(ft)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Biobuzz.leaf)
                    ) {
                        Text(
                            if (following) LangManager.t("Seç", "Select") else LangManager.t("Takip Et", "Follow"),
                            color = Biobuzz.bark
                        )
                    }
                }
            }
        }

        item { SectionLabel(LangManager.t("Takip Ettiklerim", "My Teams") + " (${TeamManager.followedTeams.size})") }
        items(TeamManager.followedTeams) { ft ->
            val selected = TeamManager.selectedTeam.teamNumber == ft.teamNumber
            BioCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("#${ft.teamNumber} · ${ft.name}", color = Biobuzz.cream,
                            fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        if (ft.location.isNotBlank())
                            Text(ft.location, color = Biobuzz.mutedG, fontSize = 11.sp)
                    }
                    if (selected) {
                        Icon(Icons.Filled.CheckCircle, null, tint = Biobuzz.leaf)
                    } else {
                        TextButton(onClick = { TeamManager.select(ft) }) {
                            Text(LangManager.t("Seç", "Select"), color = Biobuzz.honey)
                        }
                    }
                    if (TeamManager.followedTeams.size > 1 && ft.teamNumber != Config.DEFAULT_TEAM_NUMBER) {
                        IconButton(onClick = { TeamManager.unfollow(ft) }) {
                            Icon(Icons.Filled.Close, null, tint = Biobuzz.mutedG)
                        }
                    }
                }
            }
        }
    }
}

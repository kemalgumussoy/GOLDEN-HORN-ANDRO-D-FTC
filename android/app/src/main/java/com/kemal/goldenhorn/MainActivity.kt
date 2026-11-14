package com.kemal.goldenhorn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoldenHornTheme {
                RootView()
            }
        }
    }
}

private data class Tab(val tr: String, val en: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootView(store: Store = viewModel()) {
    var tab by rememberSaveable { mutableStateOf(0) }
    // Dil değişince başlıklar güncellensin diye okunuyor
    val lang = LangManager.isTurkish

    LaunchedEffect(Unit) { store.fetchAll() }

    val tabs = listOf(
        Tab("Ana Sayfa", "Home", Icons.Filled.Home),
        Tab("Takımlar", "Teams", Icons.Filled.Search),
        Tab("Maçlar", "Matches", Icons.Filled.SportsScore),
        Tab("Teknik", "Tech", Icons.Filled.Calculate),
        Tab("Quiz", "Quiz", Icons.Filled.Quiz),
        Tab("Admin", "Admin", Icons.Filled.Lock)
    )

    Scaffold(
        containerColor = Biobuzz.bark,
        topBar = {
            TopAppBar(
                title = { Text(LangManager.t(tabs[tab].tr, tabs[tab].en), color = Biobuzz.cream) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Biobuzz.bgCard,
                    titleContentColor = Biobuzz.cream
                ),
                actions = {
                    TextButton(onClick = { LangManager.toggle() }) {
                        Text(if (lang) "EN" else "TR", color = Biobuzz.honey)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Biobuzz.bgCard) {
                tabs.forEachIndexed { i, t ->
                    NavigationBarItem(
                        selected = tab == i,
                        onClick = { tab = i },
                        icon = { Icon(t.icon, contentDescription = null) },
                        label = { Text(LangManager.t(t.tr, t.en), maxLines = 1) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Biobuzz.bark,
                            selectedTextColor = Biobuzz.honey,
                            indicatorColor = Biobuzz.honey,
                            unselectedIconColor = Biobuzz.mutedG,
                            unselectedTextColor = Biobuzz.mutedG
                        )
                    )
                }
            }
        }
    ) { pad ->
        Box(Modifier.fillMaxSize().padding(pad)) {
            when (tab) {
                0 -> HomeScreen(store)
                1 -> TeamsScreen()
                2 -> MatchesScreen(store)
                3 -> TechScreen()
                4 -> QuizScreen()
                else -> AdminScreen(store)
            }
        }
    }
}

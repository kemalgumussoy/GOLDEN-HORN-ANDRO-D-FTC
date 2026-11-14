package com.kemal.goldenhorn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun AdminScreen(store: Store) {
    LangManager.isTurkish
    var loggedIn by remember { mutableStateOf(false) }
    var pw by remember { mutableStateOf("") }
    var err by remember { mutableStateOf(false) }

    if (!loggedIn) {
        Column(
            Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionLabel(LangManager.t("Takım Girişi", "Team Login"))
            Text(
                LangManager.t(
                    "Şifre takım yöneticisinden alınır. Sadece yetkili kişiler içerik ekleyebilir.",
                    "The password is provided by the team lead. Only authorized members can add content."
                ),
                color = Biobuzz.mutedG, fontSize = 12.sp
            )
            OutlinedTextField(
                value = pw, onValueChange = { pw = it; err = false },
                label = { Text(LangManager.t("Şifre", "Password")) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = err,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Biobuzz.honey, unfocusedBorderColor = Biobuzz.mutedG,
                    focusedTextColor = Biobuzz.cream, unfocusedTextColor = Biobuzz.cream,
                    cursorColor = Biobuzz.honey
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (err) Text(LangManager.t("Hatalı şifre", "Wrong password"),
                color = Biobuzz.bloodO, fontSize = 13.sp)
            Button(
                onClick = { if (pw == Config.TEAM_PASSWORD) loggedIn = true else err = true },
                colors = ButtonDefaults.buttonColors(containerColor = Biobuzz.honey),
                modifier = Modifier.fillMaxWidth()
            ) { Text(LangManager.t("Giriş", "Log in"), color = Biobuzz.bark) }
        }
        return
    }

    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            Text(LangManager.t("Yönetim Paneli", "Admin Panel"),
                color = Biobuzz.honey, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f))
            TextButton(onClick = { loggedIn = false }) {
                Text(LangManager.t("Çıkış", "Logout"), color = Biobuzz.mutedG)
            }
        }
        status?.let { Text(it, color = Biobuzz.leaf, fontSize = 13.sp) }

        // Yeni üye
        AddForm(
            title = LangManager.t("Yeni Üye", "New Member"),
            fields = listOf(
                LangManager.t("İsim", "Name"),
                LangManager.t("Rol (TR)", "Role (TR)"),
                LangManager.t("Rol (EN)", "Role (EN)")
            )
        ) { v ->
            scope.launch {
                try {
                    store.addMember(NewMember(v[0], v[1], v[2]))
                    status = LangManager.t("Üye eklendi ✅", "Member added ✅")
                } catch (e: Exception) { status = "Hata: ${e.message}" }
            }
        }

        // Yeni proje
        AddForm(
            title = LangManager.t("Yeni Proje", "New Project"),
            fields = listOf(
                LangManager.t("Başlık (TR)", "Title (TR)"),
                LangManager.t("Başlık (EN)", "Title (EN)"),
                LangManager.t("Açıklama (TR)", "Description (TR)"),
                LangManager.t("Açıklama (EN)", "Description (EN)")
            )
        ) { v ->
            scope.launch {
                try {
                    store.addProject(NewProject(v[0], v[1], v[2], v[3]))
                    status = LangManager.t("Proje eklendi ✅", "Project added ✅")
                } catch (e: Exception) { status = "Hata: ${e.message}" }
            }
        }

        // Yeni duyuru
        AddForm(
            title = LangManager.t("Yeni Duyuru", "New Announcement"),
            fields = listOf(
                LangManager.t("Başlık (TR)", "Title (TR)"),
                LangManager.t("Başlık (EN)", "Title (EN)"),
                LangManager.t("İçerik (TR)", "Body (TR)"),
                LangManager.t("İçerik (EN)", "Body (EN)")
            )
        ) { v ->
            scope.launch {
                try {
                    store.addAnnouncement(NewAnnouncement(v[0], v[1], v[2], v[3]))
                    status = LangManager.t("Duyuru eklendi ✅", "Announcement added ✅")
                } catch (e: Exception) { status = "Hata: ${e.message}" }
            }
        }

        // Yeni teknik not
        AddForm(
            title = LangManager.t("Yeni Teknik Not", "New Tech Note"),
            fields = listOf(
                LangManager.t("Başlık (TR)", "Title (TR)"),
                LangManager.t("Başlık (EN)", "Title (EN)"),
                LangManager.t("İçerik (TR)", "Content (TR)"),
                LangManager.t("İçerik (EN)", "Content (EN)")
            )
        ) { v ->
            scope.launch {
                try {
                    store.addTechNote(NewTechNote(v[0], v[1], v[2], v[3]))
                    status = LangManager.t("Not eklendi ✅", "Note added ✅")
                } catch (e: Exception) { status = "Hata: ${e.message}" }
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun AddForm(title: String, fields: List<String>, onSubmit: (List<String>) -> Unit) {
    val values = remember(title) { mutableStateListOf(*Array(fields.size) { "" }) }
    BioCard {
        Text(title, color = Biobuzz.cream, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        fields.forEachIndexed { i, f ->
            BioTextField(values[i], { values[i] = it }, f)
            Spacer(Modifier.height(6.dp))
        }
        Button(
            onClick = {
                if (values.all { it.isNotBlank() }) {
                    onSubmit(values.toList())
                    for (i in values.indices) values[i] = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Biobuzz.leaf),
            modifier = Modifier.fillMaxWidth()
        ) { Text(LangManager.t("Ekle", "Add"), color = Biobuzz.bark) }
    }
}

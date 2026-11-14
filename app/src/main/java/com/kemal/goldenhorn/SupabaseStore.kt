package com.kemal.goldenhorn

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class Store : ViewModel() {

    var members by mutableStateOf<List<Member>>(emptyList()); private set
    var projects by mutableStateOf<List<Project>>(emptyList()); private set
    var techNotes by mutableStateOf<List<TechNote>>(emptyList()); private set
    var announcements by mutableStateOf<List<Announcement>>(emptyList()); private set
    var matchResults by mutableStateOf<List<MatchResult>>(emptyList()); private set
    var loading by mutableStateOf(false); private set
    var lastError by mutableStateOf<String?>(null); private set

    private val client = createSupabaseClient(
        supabaseUrl = Config.SUPABASE_URL,
        supabaseKey = Config.SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        })
    }

    fun fetchAll() {
        viewModelScope.launch {
            loading = true
            lastError = null
            try {
                members = client.from("members").select().decodeList()
                projects = client.from("projects").select().decodeList()
                techNotes = client.from("tech_notes").select().decodeList()
                announcements = client.from("announcements").select()
                    .decodeList<Announcement>()
                    .sortedByDescending { it.createdAt ?: "" }
                matchResults = client.from("match_results").select().decodeList()
            } catch (e: Exception) {
                lastError = e.message
            }
            loading = false
        }
    }

    suspend fun addMember(v: NewMember) {
        client.from("members").insert(v); fetchAll()
    }
    suspend fun addProject(v: NewProject) {
        client.from("projects").insert(v); fetchAll()
    }
    suspend fun addTechNote(v: NewTechNote) {
        client.from("tech_notes").insert(v); fetchAll()
    }
    suspend fun addAnnouncement(v: NewAnnouncement) {
        client.from("announcements").insert(v); fetchAll()
    }

    /** FTC API'den seçili takımın maçlarını çekip Supabase'e ekler */
    suspend fun syncFromFtc(teamNumber: Int): Pair<Int, String?> {
        return try {
            val matches = FtcApi.fetchAllMatches(teamNumber)
            val existing = matchResults.map { "${it.matchNumber}-${it.opponent}" }.toSet()
            var added = 0
            for (pm in matches) {
                if ("${pm.matchNumber}-${pm.opponent}" in existing) continue
                val nm = NewMatchResult(
                    matchNumber = pm.matchNumber,
                    eventNameTr = pm.eventName,
                    eventNameEn = pm.eventName,
                    opponent = pm.opponent,
                    ourScore = pm.ourScore,
                    opponentScore = pm.opponentScore,
                    result = pm.result,
                    autoPoints = pm.autoPoints,
                    teleopPoints = maxOf(0, pm.ourScore - pm.autoPoints),
                    notesTr = "FTC API'den çekildi",
                    notesEn = "From FTC API"
                )
                client.from("match_results").insert(nm)
                added++
            }
            fetchAll()
            Pair(added, null)
        } catch (e: Exception) {
            Pair(0, e.message)
        }
    }
}

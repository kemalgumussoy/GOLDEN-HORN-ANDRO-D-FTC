package com.kemal.goldenhorn

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class ParsedMatch(
    val matchNumber: Int,
    val eventName: String,
    val opponent: String,
    val ourScore: Int,
    val opponentScore: Int,
    val autoPoints: Int,
    val result: String // win / loss / tie
)

object FtcApi {
    private const val BASE = "https://ftc-api.firstinspires.org/v2.0"

    private fun authHeader(): String {
        val cred = "${Config.FTC_USERNAME}:${Config.FTC_TOKEN}"
        val b64 = Base64.encodeToString(cred.toByteArray(), Base64.NO_WRAP)
        return "Basic $b64"
    }

    private fun get(path: String): JSONObject? {
        return try {
            val url = URL("$BASE$path")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Authorization", authHeader())
            conn.setRequestProperty("Accept", "application/json")
            conn.connectTimeout = 15000
            conn.readTimeout = 15000
            if (conn.responseCode == 200) {
                val text = conn.inputStream.bufferedReader().use { it.readText() }
                JSONObject(text)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    /** Takım numarasıyla arama */
    suspend fun searchTeam(number: Int): FtcTeam? = withContext(Dispatchers.IO) {
        val d = get("/${Config.FTC_SEASON}/teams?teamNumber=$number") ?: return@withContext null
        val arr = d.optJSONArray("teams") ?: return@withContext null
        if (arr.length() == 0) return@withContext null
        val t = arr.getJSONObject(0)
        val name = t.optString("nameShort").ifBlank { t.optString("nameFull") }.ifBlank { "Team $number" }
        val loc = listOf(t.optString("city"), t.optString("stateProv"), t.optString("country"))
            .filter { it.isNotBlank() }.joinToString(", ")
        FtcTeam(number, name, loc)
    }

    /** Seçili takımın tüm (oynanmış) maçlarını çeker */
    suspend fun fetchAllMatches(teamNumber: Int): List<ParsedMatch> = withContext(Dispatchers.IO) {
        val out = mutableListOf<ParsedMatch>()
        val eventsData = get("/${Config.FTC_SEASON}/events?teamNumber=$teamNumber")
        val events = eventsData?.optJSONArray("events") ?: return@withContext out

        val count = minOf(events.length(), 5)
        for (i in 0 until count) {
            val ev = events.getJSONObject(i)
            val code = ev.optString("code")
            val evName = ev.optString("name")
            if (code.isBlank()) continue

            for (level in listOf("qual", "playoff")) {
                val md = get("/${Config.FTC_SEASON}/matches/$code?tournamentLevel=$level&teamNumber=$teamNumber")
                val matches = md?.optJSONArray("matches") ?: continue
                for (j in 0 until matches.length()) {
                    val m = matches.getJSONObject(j)
                    if (m.optString("actualStartTime").isBlank()) continue // oynanmamış
                    parseMatch(m, teamNumber, evName)?.let { out.add(it) }
                }
            }
        }
        out
    }

    private fun parseMatch(m: JSONObject, num: Int, eventName: String): ParsedMatch? {
        val teams = m.optJSONArray("teams") ?: return null
        var ally: String? = null
        for (k in 0 until teams.length()) {
            val t = teams.getJSONObject(k)
            if (t.optInt("teamNumber") == num) {
                ally = if (t.optString("station").startsWith("Red")) "Red" else "Blue"
            }
        }
        if (ally == null) return null

        val our = if (ally == "Red") m.optInt("scoreRedFinal") else m.optInt("scoreBlueFinal")
        val opp = if (ally == "Red") m.optInt("scoreBlueFinal") else m.optInt("scoreRedFinal")
        val auto = if (ally == "Red") m.optInt("scoreRedAuto") else m.optInt("scoreBlueAuto")
        val result = when {
            our > opp -> "win"
            our < opp -> "loss"
            else -> "tie"
        }
        val oppStation = if (ally == "Red") "Blue" else "Red"
        val opponents = buildList {
            for (k in 0 until teams.length()) {
                val t = teams.getJSONObject(k)
                if (t.optString("station").startsWith(oppStation)) add(t.optInt("teamNumber").toString())
            }
        }.joinToString(", ").ifBlank { "—" }

        return ParsedMatch(
            matchNumber = m.optInt("matchNumber"),
            eventName = eventName,
            opponent = opponents,
            ourScore = our,
            opponentScore = opp,
            autoPoints = auto,
            result = result
        )
    }
}

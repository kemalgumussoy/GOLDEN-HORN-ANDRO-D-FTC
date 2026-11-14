package com.kemal.goldenhorn

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Supabase tablolarından okunan modeller.
// Bilinmeyen kolonlar yok sayılır (Json ignoreUnknownKeys = true),
// bu yüzden alanlar nullable/defaultlu — şema farkında çökmez.

@Serializable
data class Member(
    val id: Int? = null,
    val name: String? = null,
    @SerialName("role_tr") val roleTr: String? = null,
    @SerialName("role_en") val roleEn: String? = null,
    @SerialName("photo_url") val photoUrl: String? = null
)

@Serializable
data class Project(
    val id: Int? = null,
    @SerialName("title_tr") val titleTr: String? = null,
    @SerialName("title_en") val titleEn: String? = null,
    @SerialName("desc_tr") val descTr: String? = null,
    @SerialName("desc_en") val descEn: String? = null
)

@Serializable
data class TechNote(
    val id: Int? = null,
    @SerialName("title_tr") val titleTr: String? = null,
    @SerialName("title_en") val titleEn: String? = null,
    @SerialName("content_tr") val contentTr: String? = null,
    @SerialName("content_en") val contentEn: String? = null
)

@Serializable
data class Announcement(
    val id: Int? = null,
    @SerialName("title_tr") val titleTr: String? = null,
    @SerialName("title_en") val titleEn: String? = null,
    @SerialName("body_tr") val bodyTr: String? = null,
    @SerialName("body_en") val bodyEn: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

@Serializable
data class MatchResult(
    val id: Int? = null,
    @SerialName("match_number") val matchNumber: Int? = null,
    @SerialName("event_name_tr") val eventNameTr: String? = null,
    @SerialName("event_name_en") val eventNameEn: String? = null,
    val opponent: String? = null,
    @SerialName("our_score") val ourScore: Int? = null,
    @SerialName("opponent_score") val opponentScore: Int? = null,
    val result: String? = null,
    @SerialName("auto_points") val autoPoints: Int? = null,
    @SerialName("teleop_points") val teleopPoints: Int? = null,
    @SerialName("endgame_points") val endgamePoints: Int? = null,
    @SerialName("penalty_points") val penaltyPoints: Int? = null,
    @SerialName("notes_tr") val notesTr: String? = null,
    @SerialName("notes_en") val notesEn: String? = null
)

// ---- Insert (yeni kayıt) modelleri ----

@Serializable
data class NewMember(
    val name: String,
    @SerialName("role_tr") val roleTr: String,
    @SerialName("role_en") val roleEn: String
)

@Serializable
data class NewProject(
    @SerialName("title_tr") val titleTr: String,
    @SerialName("title_en") val titleEn: String,
    @SerialName("desc_tr") val descTr: String,
    @SerialName("desc_en") val descEn: String
)

@Serializable
data class NewTechNote(
    @SerialName("title_tr") val titleTr: String,
    @SerialName("title_en") val titleEn: String,
    @SerialName("content_tr") val contentTr: String,
    @SerialName("content_en") val contentEn: String
)

@Serializable
data class NewAnnouncement(
    @SerialName("title_tr") val titleTr: String,
    @SerialName("title_en") val titleEn: String,
    @SerialName("body_tr") val bodyTr: String,
    @SerialName("body_en") val bodyEn: String
)

@Serializable
data class NewMatchResult(
    @SerialName("match_number") val matchNumber: Int,
    @SerialName("event_name_tr") val eventNameTr: String,
    @SerialName("event_name_en") val eventNameEn: String,
    val opponent: String,
    @SerialName("our_score") val ourScore: Int,
    @SerialName("opponent_score") val opponentScore: Int,
    val result: String,
    @SerialName("auto_points") val autoPoints: Int,
    @SerialName("teleop_points") val teleopPoints: Int,
    @SerialName("endgame_points") val endgamePoints: Int = 0,
    @SerialName("penalty_points") val penaltyPoints: Int = 0,
    @SerialName("notes_tr") val notesTr: String,
    @SerialName("notes_en") val notesEn: String
)

// ---- Takım modelleri ----

data class FollowedTeam(
    val teamNumber: Int,
    val name: String,
    val location: String = ""
)

data class FtcTeam(
    val teamNumber: Int,
    val displayName: String,
    val location: String
)

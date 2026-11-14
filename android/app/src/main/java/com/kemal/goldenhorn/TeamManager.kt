package com.kemal.goldenhorn

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Takip edilen takımlar + seçili takım. Varsayılan Golden Horn (silinemez).
object TeamManager {
    private val defaultTeam =
        FollowedTeam(Config.DEFAULT_TEAM_NUMBER, Config.DEFAULT_TEAM_NAME, "İstanbul, TR")

    val followedTeams = mutableStateListOf(defaultTeam)

    var selectedTeam by mutableStateOf(defaultTeam)
        private set

    val isDefaultTeam: Boolean
        get() = selectedTeam.teamNumber == Config.DEFAULT_TEAM_NUMBER

    fun follow(team: FollowedTeam) {
        if (followedTeams.none { it.teamNumber == team.teamNumber }) {
            followedTeams.add(team)
        }
        selectedTeam = team
    }

    fun select(team: FollowedTeam) {
        selectedTeam = team
    }

    fun unfollow(team: FollowedTeam) {
        // Son takım silinemez
        if (followedTeams.size <= 1) return
        followedTeams.removeAll { it.teamNumber == team.teamNumber }
        if (selectedTeam.teamNumber == team.teamNumber) {
            selectedTeam = followedTeams.first()
        }
    }
}

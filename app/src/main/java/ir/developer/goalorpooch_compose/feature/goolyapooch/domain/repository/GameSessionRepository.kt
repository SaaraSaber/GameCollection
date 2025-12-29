package ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository

import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameCardModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.TeamModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterTeam

interface GameSessionRepository {
    fun getAllCards(): List<GameCardModel>
    fun saveSelectedCards(team: StarterTeam, cards: List<GameCardModel>)
    fun getSelectedCards(team: StarterTeam): List<GameCardModel>
    fun clearSession()
    fun getTeam(teamId: Int): TeamModel
    fun updateTeam(team: TeamModel)
}
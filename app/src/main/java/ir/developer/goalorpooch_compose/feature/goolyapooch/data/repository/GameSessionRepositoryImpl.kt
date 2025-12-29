package ir.developer.goalorpooch_compose.feature.goolyapooch.data.repository

import ir.developer.goalorpooch_compose.R
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.GameCardModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.models.TeamModel
import ir.developer.goalorpooch_compose.feature.goolyapooch.domain.repository.GameSessionRepository
import ir.developer.goalorpooch_compose.feature.goolyapooch.presentation.utils.StarterTeam
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameSessionRepositoryImpl @Inject constructor() : GameSessionRepository {

    private val team1Cards = mutableListOf<GameCardModel>()
    private val team2Cards = mutableListOf<GameCardModel>()

    private var team1 = TeamModel(id = 0, name = "تیم اول")
    private var team2 = TeamModel(id = 1, name = "تیم دوم")

    override fun getAllCards(): List<GameCardModel> {
        return listOf(
            GameCardModel(
                id = 1,
                title = R.string.free_stone_free_sparrow,
                description = R.string.description_free_stone_free_sparrow,
                imageRes = R.drawable.pic_card
            ),
            GameCardModel(
                id = 2,
                title = R.string.big_ball,
                description = R.string.description_bid_ball,
                imageRes = R.drawable.pic_card
            ),
            GameCardModel(
                id = 3,
                title = R.string.sound_ball,
                description = R.string.description_sound_ball,
                imageRes = R.drawable.pic_card
            ),
            GameCardModel(
                id = 4,
                title = R.string.one_arrow_and_two_badges,
                description = R.string.description_one_arrow_and_two_badges,
                imageRes = R.drawable.pic_card
            ),
            GameCardModel(
                id = 5,
                title = R.string.antenna,
                description = R.string.description_antenna,
                imageRes = R.drawable.pic_card
            ),
            GameCardModel(
                id = 6,
                title = R.string.duel,
                description = R.string.description_duel,
                imageRes = R.drawable.pic_card
            ),
            GameCardModel(
                id = 7,
                title = R.string.empty_game,
                description = R.string.description_empty_game,
                imageRes = R.drawable.pic_card
            ),
            GameCardModel(
                id = 8,
                title = R.string.remove_one_hand,
                description = R.string.description_remove_one_hand,
                imageRes = R.drawable.pic_card
            ),
            GameCardModel(
                id = 9,
                title = R.string.free_stone_free_sparrow,
                description = R.string.description_free_stone_free_sparrow,
                imageRes = R.drawable.pic_card
            ),
        )
    }

    override fun saveSelectedCards(team: StarterTeam, cards: List<GameCardModel>) {
        if (team == StarterTeam.TEAM_1) {
            team1Cards.clear()
            team1Cards.addAll(cards)
        } else {
            team2Cards.clear()
            team2Cards.addAll(cards)
        }
    }

    override fun getSelectedCards(team: StarterTeam): List<GameCardModel> {
        return if (team == StarterTeam.TEAM_1) {
            team1Cards.toList()
        } else {
            team2Cards.toList()
        }
    }

    override fun clearSession() {
        team1Cards.clear()
        team2Cards.clear()
        team1 = TeamModel(id = 0, name = "تیم اول")
        team2 = TeamModel(id = 1, name = "تیم دوم")
    }

    override fun getTeam(teamId: Int): TeamModel {
        return if (teamId == 0) team1 else team2
    }

    override fun updateTeam(team: TeamModel) {
        if (team.id == 0) team1 = team else team2 = team
    }
}
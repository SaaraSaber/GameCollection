package ir.developer.goalorpooch_compose.core.navigation

object Routes {
    const val HOME = "home_screen"
    const val GOOLYAPOOCH_SETTING = "goolyapooch_setting_screen"
    const val GOOLYAPOOCH_STARTER = "goolyapooch_starter_screen"
    const val GOOLYAPOOCH_CARD_SELECTION = "card_selection_screen/{currentTeamId}/{starterTeamId}"
    fun getCardSelectionRoute(currentTeamId: Int, starterTeamId: Int) = "card_selection_screen/$currentTeamId/$starterTeamId"
    const val GOOLYAPOOCH_DISPLAY_CARDS = "card_display_screen/{currentTeamId}/{starterTeamId}"
    fun getCardDisplayRoute(currentTeamId: Int, starterTeamId: Int): String {
        return "card_display_screen/$currentTeamId/$starterTeamId"
    }
    const val GOOLYAPOOCH_CARD_DISPLAY = "card_display_screen/{starterTeamId}"
    const val GOOLYAPOOCH_START_GAME = "start_game"


    const val MAFIA = "mafia_screen"
}
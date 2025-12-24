package ir.developer.goalorpooch_compose.core.navigation

object Routes {
    const val HOME = "home_screen"
    const val GOOLYAPOOCH_SETTING = "goolyapooch_setting_screen"
    const val GOOLYAPOOCH_STARTER = "goolyapooch_starter_screen"
    const val GOOLYAPOOCH_CARD_SELECTION = "card_selection_screen/{starterTeamId}"
    fun getCardSelectionRoute(teamId: Int) = "card_selection_screen/$teamId"
    const val GOOLYAPOOCH_DISPLAY_CARDS = "goolyapooch_display_cards_screen"


    const val MAFIA = "mafia_screen"
}
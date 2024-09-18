package at.htlleonding.pepperInElderlyCare.ui.model

import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens

data class Games(val title:String,
                 val screen:Screens,
                 val suggestions:List<String>
)

fun getGames(): List<Games> {
    return listOf(
        Games(title = "Tic Tac Toe", screen = Screens.TicTacToeGameSelectScreen, listOf("Tik","Tak","To")),
        Games(title = "Vier Gewinnt", screen = Screens.ConnectFourGameSelectScreen, listOf("vier","gewinnt")),
        Games(title = "Memory", screen = Screens.MemoryGameScreen, listOf("Memory","Memori")),
        Games(title = "Mitmachgeschichte", screen = Screens.HandsOnStorySelectionScreen, listOf("Mitmach", "geschichte")),
        Games(title = "Mathe Spiel", screen = Screens.MathGame, listOf("mathe", "spiel")))
}
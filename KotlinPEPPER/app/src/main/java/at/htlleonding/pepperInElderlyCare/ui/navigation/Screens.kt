package at.htlleonding.pepperInElderlyCare.ui.navigation

import at.htlleonding.pepperInElderlyCare.ui.screens.games.hands_on_story.HandsOnStorySelectionScreen
import java.lang.IllegalArgumentException

enum class Screens {
    MainScreen,
    DanceSelectionScreen,
    DanceScreen,
    GameSelectionScreen,
    TicTacToeGameSelectScreen,
    TicTacToeGameScreen,
    ConnectFourGameSelectScreen,
    ConnectFourGameScreen,
    MemoryGameScreen,
    HandsOnStoryScreen,
    HandsOnStorySelectionScreen,
    MathGame;
    companion object{
        fun fromRoute(route: String?): Screens
                = when (route?.substringBefore("/")) {
            MainScreen.name -> MainScreen
            DanceSelectionScreen.name -> DanceSelectionScreen
            DanceScreen.name -> DanceScreen
            GameSelectionScreen.name->GameSelectionScreen
            TicTacToeGameSelectScreen.name->TicTacToeGameSelectScreen
            TicTacToeGameScreen.name->TicTacToeGameScreen
            ConnectFourGameSelectScreen.name->ConnectFourGameSelectScreen
            ConnectFourGameScreen.name->ConnectFourGameScreen
            MemoryGameScreen.name->MemoryGameScreen
            HandsOnStoryScreen.name->HandsOnStoryScreen
            HandsOnStorySelectionScreen.name->HandsOnStorySelectionScreen
            MathGame.name->MathGame
            null -> MainScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }


}
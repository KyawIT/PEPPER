package at.htlleonding.pepperInElderlyCare.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import at.htlleonding.pepperInElderlyCare.ui.HelpFunctions
import at.htlleonding.pepperInElderlyCare.ui.screens.MainScreen
import at.htlleonding.pepperInElderlyCare.ui.screens.TicTacToeGameSelectScreen
import at.htlleonding.pepperInElderlyCare.ui.screens.dances.DanceScreen
import at.htlleonding.pepperInElderlyCare.ui.screens.dances.DanceSelectionScreen
import at.htlleonding.pepperInElderlyCare.ui.screens.games.GameSelectionScreen
import at.htlleonding.pepperInElderlyCare.ui.screens.games.connectFour.ConnectFourGameScreen
import at.htlleonding.pepperInElderlyCare.ui.screens.games.connectFour.ConnectFourGameSelectScreen
import at.htlleonding.pepperInElderlyCare.ui.screens.games.hands_on_story.HandsOnStoryScreen
import at.htlleonding.pepperInElderlyCare.ui.screens.games.hands_on_story.HandsOnStorySelectionScreen
import at.htlleonding.pepperInElderlyCare.ui.screens.games.math_game.MathGame
import at.htlleonding.pepperInElderlyCare.ui.screens.games.memory.MemoryGameScreen
import at.htlleonding.pepperInElderlyCare.ui.screens.games.ticTacToe.TicTacToeGameScreen


@Composable
@ExperimentalAnimationApi
fun ScreenNavigation(){
    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = Screens.MainScreen.name ) {

        HelpFunctions.navController=navController;
        composable(Screens.MainScreen.name){
            MainScreen(navController = navController)
        }
        composable(Screens.DanceSelectionScreen.name){
            DanceSelectionScreen(navController = navController)
        }
        composable(

            Screens.DanceScreen.name+"/{danceId}",
            arguments = listOf(navArgument(name = "danceId") {
                type = NavType.StringType})) {
                backStackEntry ->
            DanceScreen(navController = navController,
                backStackEntry.arguments?.getString("danceId"))
        }
        composable(Screens.GameSelectionScreen.name){
            GameSelectionScreen(navController = navController)
        }
        composable(
            Screens.TicTacToeGameScreen.name+"/{index}",
            arguments = listOf(navArgument(name = "index") {
                type = NavType.StringType})) {
                backStackEntry ->
            TicTacToeGameScreen(
                navController = navController,
                backStackEntry.arguments!!.getString("index"))
        }
        composable(Screens.ConnectFourGameSelectScreen.name){
            ConnectFourGameSelectScreen(navController = navController)
        }
        composable(
            Screens.ConnectFourGameScreen.name+"/{index}",
            arguments = listOf(navArgument(name = "index") {
                type = NavType.StringType})) {
                backStackEntry ->
            ConnectFourGameScreen(
                navController = navController,
                backStackEntry.arguments!!.getString("index"))
        }
        composable(Screens.TicTacToeGameSelectScreen.name){

            TicTacToeGameSelectScreen(navController = navController)
        }
        composable(Screens.MemoryGameScreen.name){
            MemoryGameScreen(navController = navController);
        }
        composable(Screens.HandsOnStorySelectionScreen.name){
           HandsOnStorySelectionScreen(navController = navController);
        }
        composable(

            Screens.HandsOnStoryScreen.name+"/{handsOnStoryId}",
            arguments = listOf(navArgument(name = "handsOnStoryId") {
                type = NavType.IntType})) {
                backStackEntry ->
            HandsOnStoryScreen(navController = navController,
                backStackEntry.arguments?.getInt("handsOnStoryId"))
        }
        composable(Screens.MathGame.name){
            MathGame(navController = navController);
        }
    }
}
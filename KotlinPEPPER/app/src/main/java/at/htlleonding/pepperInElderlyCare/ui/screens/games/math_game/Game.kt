package at.htlleonding.pepperInElderlyCare.ui.screens.games.math_game

import android.util.Log
import java.util.logging.Handler

class Game(isGameModeEasy: Boolean) {

    val max_easy = 50
    val max_hard = 100
    val max_hard_factor = 10

    val number_rounds = 5

    public var gameModeEasy: Boolean = false
        get() = field
    public var roundCounter: Int = 0
        get() = field
        private set(value) {
            field += value
        }
    var roboterWinCounter: Int = 0
        get() = field
        public set(value) {
            field += value
        }

    var playerWinCounter: Int = 0
        get() = field
        public set(value) {
            field += value
        }

    var gameFinished = false

    var last_solution = 0

    init {
        gameModeEasy = isGameModeEasy
    }

    fun startNewRound(): String {
        Log.d("tag", String.format("Start new Round: %s", roundCounter))
        if (roundCounter >= number_rounds) {
            gameFinished = true
            return "Game Finished"
        } else {
            roundCounter++
            if (gameModeEasy) {
                var rndNumber1 = (1..max_easy).shuffled().last();
                var rndNumber2 = (1..max_easy).shuffled().last();
                var rndOperator = (0..2).shuffled().last();
                if (rndOperator == 1) { // minus
                    last_solution = rndNumber1 - rndNumber2
                    while (last_solution < 1) {
                        rndNumber2 = (0..max_easy).shuffled().last();
                        last_solution = rndNumber1 - rndNumber2;
                    }
                    return String.format("%s - %s", rndNumber1, rndNumber2)
                } else { // add
                    last_solution = rndNumber1 + rndNumber2;
                    return String.format("%s + %s", rndNumber1, rndNumber2)
                }
            } else {
                var rndNumber1 = (1..max_hard).shuffled().last();
                var rndNumber2 = (1..max_hard).shuffled().last();
                var rndOperator = (0..3).shuffled().last();
                if (rndOperator == 2) { // minus
                    last_solution = rndNumber1 - rndNumber2
                    while (last_solution < 1) {
                        rndNumber2 = (0..max_easy).shuffled().last();
                        last_solution = rndNumber1 - rndNumber2;
                    }
                    return String.format("%s - %s", rndNumber1, rndNumber2)
                } else if (rndOperator == 3) { // multiply
                    rndNumber2 = (1..max_hard_factor).shuffled().last();
                    last_solution = rndNumber1 * rndNumber2
                    return String.format("%s * %s", rndNumber1, rndNumber2)
                } else { // add
                    last_solution = rndNumber1 + rndNumber2;
                    return String.format("%s + %s", rndNumber1, rndNumber2)
                }
            }
        }

    }

    fun startNewTimeRound(): String {
        Log.d("tag", String.format("Start new Round: %s", roundCounter))
        if (roundCounter >= number_rounds) {
            gameFinished = true
            return "Game Finished"
        } else {
            roundCounter++
            if (gameModeEasy) {
                var rndNumber1 = (1..max_easy).shuffled().last();
                var rndNumber2 = (1..max_easy).shuffled().last();
                var rndOperator = (0..2).shuffled().last();
                if (rndOperator == 1) { // minus
                    last_solution = rndNumber1 - rndNumber2
                    while (last_solution < 1) {
                        rndNumber2 = (0..max_easy).shuffled().last();
                        last_solution = rndNumber1 - rndNumber2;
                    }
                    return String.format("%s - %s", rndNumber1, rndNumber2)
                } else { // add
                    last_solution = rndNumber1 + rndNumber2;
                    return String.format("%s + %s", rndNumber1, rndNumber2)
                }
            } else {
                var rndNumber1 = (1..max_hard).shuffled().last();
                var rndNumber2 = (1..max_hard).shuffled().last();
                var rndOperator = (0..3).shuffled().last();
                if (rndOperator == 2) { // minus
                    last_solution = rndNumber1 - rndNumber2
                    while (last_solution < 1) {
                        rndNumber2 = (0..max_easy).shuffled().last();
                        last_solution = rndNumber1 - rndNumber2;
                    }
                    return String.format("%s - %s", rndNumber1, rndNumber2)
                } else if (rndOperator == 3) { // multiply
                    rndNumber2 = (1..max_hard_factor).shuffled().last();
                    last_solution = rndNumber1 * rndNumber2
                    return String.format("%s * %s", rndNumber1, rndNumber2)
                } else { // add
                    last_solution = rndNumber1 + rndNumber2;
                    return String.format("%s + %s", rndNumber1, rndNumber2)
                }
            }
        }

    }

}
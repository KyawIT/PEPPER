package at.htlleonding.pepperInElderlyCare.ui.screens.games.math_game

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavHostController
import at.htlleonding.pepperInElderlyCare.R
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.`object`.conversation.Say
import kotlin.random.Random

class MathGame(navController: NavHostController) : RobotActivity(), RobotLifecycleCallbacks, View.OnClickListener {

    val max_rnd_number = 120

    val max_rnd_time = 14000
    val min_rnd_time = 7000

    private val tag = "MathGame"
    private var game: Game? = null
    private var btnTrueOption: Int = 0
    private var gameModeStandard: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.start_page)

        QiSDK.register(this, this)

    }

    override fun onDestroy() {
        QiSDK.unregister(this, this)
        super.onDestroy()
    }

    override fun onRobotFocusGained(qiContext: QiContext) {
        Log.d(tag, "onRobotFocusGained")

        val say: Say = SayBuilder.with(qiContext)
            .withText("Willkommen zum Rechenspiel")
            .build()

        say.run()
    }

    override fun onRobotFocusLost() {
    }

    override fun onRobotFocusRefused(reason: String?) {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnStart -> {
                setContentView(R.layout.choose_modus_page)
            }

            R.id.btnStandardGame -> {
                setContentView(R.layout.description_standard)
            }

            R.id.btnTimeGame -> {
                setContentView(R.layout.description_zeitspiel)
            }

            R.id.btnStandardGameStart -> {
                setContentView(R.layout.choose_difficulty_page)
                gameModeStandard = true
            }

            R.id.btnTimeGameStart -> {
                setContentView(R.layout.choose_difficulty_page)
                gameModeStandard = false
            }

            R.id.btnBack -> {
                setContentView(R.layout.choose_modus_page)
            }

            R.id.btnDifficultyEasy -> {
                game = Game(true)
                setContentView(R.layout.game_page)
                nextRound()
            }

            R.id.btnDifficultyHard -> {
                game = Game(false)
                setContentView(R.layout.game_page)
                nextRound()
            }

            R.id.btnGameOption1 -> {
                checkRound(1)
            }

            R.id.btnGameOption2 -> {
                checkRound(2)
            }

            R.id.btnGameOption3 -> {
                checkRound(3)
            }

            R.id.btnNewRound -> {
                setContentView(R.layout.choose_modus_page);
            }

            else -> {
                Log.d("Tag", "test not worked")
            }
        }
    }

    fun checkRound(optionClicked: Int) {
        var txtMessage: TextView = findViewById(R.id.messageText)
        if (optionClicked == btnTrueOption) {
            game?.playerWinCounter = 1
            Log.d(
                "tagcheck",
                String.format("%s", "Player won - PlayerWinCounter: " + game?.playerWinCounter)
            )
            txtMessage.setText("Du hast die richtige Lösung! Die neuen Optionen: ")
        } else {
            game?.roboterWinCounter = 1
            Log.d(
                "tagcheck",
                String.format("%s", "Roboter - won RoboterWinCounter: " + game?.roboterWinCounter)
            )
            if (optionClicked != -1) txtMessage.setText("Leider falsch! Die nächsten Lösungen: ")
            else txtMessage.setText("Der Roboter war schneller! Die nächsten Lösungen: ")
        }
        nextRound()
    }

    fun nextRound() {
        var lblCalculation: TextView = findViewById(R.id.lblCalculation) as TextView
        if (gameModeStandard) {
            var calculation = game?.startNewRound()
            lblCalculation.setText(calculation)
        } else {
            var calculation = game?.startNewTimeRound()
            lblCalculation.setText(calculation)

            var rndTime = (min_rnd_time..max_rnd_time).shuffled().last().toLong()
            var roundCounter = game?.roundCounter

            Log.d("tagcheck", "Start TimeOut$rndTime")
            Handler().postDelayed({
                if (roundCounter == game?.roundCounter && game?.gameFinished == false) {
                    checkRound(-1)
                }
            }, rndTime)
        }

        var numberOptions: IntArray = IntArray(3) { it + 1 }
        numberOptions[0] = game?.last_solution!!

        for (i in 1..2) {
            var rndNumber: Int = 0
            do {
                rndNumber = (1..max_rnd_number).shuffled().last();
            } while (numberOptions.contains(rndNumber))
            numberOptions[i] = rndNumber
        }

        numberOptions = shuffleArray(numberOptions)
        for (i in numberOptions.indices) {
            if (numberOptions[i] == game!!.last_solution) {
                btnTrueOption = i + 1
            }
        }

        var btnGameOption1: Button = findViewById(R.id.btnGameOption1)
        btnGameOption1.setText(String.format("%s", numberOptions[0]))

        var btnGameOption2: Button = findViewById(R.id.btnGameOption2)
        btnGameOption2.setText(String.format("%s", numberOptions[1]))

        var btnGameOption3: Button = findViewById(R.id.btnGameOption3)
        btnGameOption3.setText(String.format("%s", numberOptions[2]))

        /*var lblResult: TextView = findViewById(R.id.lblResult) as TextView
        lblResult.setText(String.format("%s", game?.last_solution))*/

        if (game?.gameFinished == true) {
            if (gameModeStandard) {
                setContentView(R.layout.end_page)

                var lblScore: TextView = findViewById(R.id.lblScore) as TextView
                lblScore.setText(String.format("%s", game!!.playerWinCounter))

                var lblGameEndMessage: TextView = findViewById(R.id.lblGameEndMessage) as TextView
                lblGameEndMessage.setText("Dein Punktestand: ")

            } else {
                setContentView(R.layout.end_page)

                var lblScore: TextView = findViewById(R.id.lblScore) as TextView
                lblScore.setText(
                    String.format(
                        "%s : %s",
                        game!!.playerWinCounter,
                        game!!.roboterWinCounter
                    )
                )

                var lblGameEndMessage: TextView = findViewById(R.id.lblGameEndMessage) as TextView
                if (game!!.roboterWinCounter > game!!.playerWinCounter) {
                    lblGameEndMessage.setText("Der Roboter gewinnt!")
                    // todo
                } else if (game!!.roboterWinCounter < game!!.playerWinCounter) {
                    lblGameEndMessage.setText("Du gewinnst!")
                    // todo
                } else {
                    lblGameEndMessage.setText("Unentschieden")
                    // todo
                }
            }

        }
    }

    fun shuffleArray(nums: IntArray): IntArray {
        return nums.clone().also { arr ->
            // go through the whole new array
            arr.forEachIndexed { index, current ->
                // get a new random index of the element to swap values between current and random one
                val randomIndex = Random.nextInt(arr.size)
                // swap elements
                arr[index] = arr[randomIndex].also { arr[randomIndex] = current }
            }
        }
    }

    fun roboterMessage(text: String) {

    }
}

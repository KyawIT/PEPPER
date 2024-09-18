package at.htlleonding.pepperInElderlyCare.ui.screens.games.ticTacToe

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.htlleonding.pepperInElderlyCare.ui.HelpFunctions
import at.htlleonding.pepperInElderlyCare.ui.HelpFunctions.Companion.enableResultScreen
import at.htlleonding.pepperInElderlyCare.ui.model.GameStats
import at.htlleonding.pepperInElderlyCare.ui.model.TicTacToe
import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens
import at.htlleonding.pepperInElderlyCare.ui.theme.*
import com.aldebaran.qi.Future
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var currGame:TicTacToe?=null
var chatFuture: Future<Void>?=null
var canSet = true;
@Composable
fun TicTacToeGameScreen(navController: NavController, index: String?) {
    var color = LightBlue;
    if (index == "1"){
        color = LightRed;
    }
    var resultBtnEnabled = remember { mutableStateOf(false)  }
    var alpha = remember { mutableStateOf(0f) }
    var message = remember { mutableStateOf("") }
    var bgColor = remember {
        mutableStateOf(Color.Transparent)
    }
    var size = remember{
        mutableStateOf(0.dp)
    }

    var initState = remember{
        mutableStateOf(true)
    }
    var recompVar = remember{
        mutableStateOf("")
    }
    if(initState.value)
    {
        initState.value=false
        var selectedGame = GameStats.defaultTicTacToe[index!!.toInt()]
        currGame = TicTacToe(selectedGame.title)
        if(HelpFunctions.onPepper)
        {
            HelpFunctions.sayAsync("Viel Spaß")?.andThenConsume {
                //chatFuture =HelpFunctions.runBackChat(navController,Screens.TicTacToeGameSelectScreen);
            }
        }
    }

    Button(modifier = Modifier
        .padding(10.dp, 10.dp)
        .fillMaxWidth(0.13f)
        , onClick = { HelpFunctions.navToSite(Screens.TicTacToeGameSelectScreen.name,
            chatFuture) },
        colors = ButtonDefaults.buttonColors(purple))
    {
        Text("Zurück", color = Color.White, fontSize = 25.sp)
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var currPlayer= remember{
            mutableStateOf(currGame!!.currPlayer)
        }
        for(fieldRow in currGame!!.field)
        {
            Spacer(modifier = Modifier.padding(5.dp))
            Row {
                for(fieldCell in fieldRow)
                {
                    Button(modifier = Modifier
                        .width(160.dp)
                        .height(160.dp)
                        ,colors = ButtonDefaults.buttonColors(color), onClick = {
                            var set = currGame!!.setCell(currGame!!.field.indexOf(fieldRow),fieldRow.indexOf(fieldCell))
                            recompVar.value="T"
                            recompVar.value=""// dieser Code forciert eine Recomposition
                            if(currGame!!.winner==currGame!!.playerChar[0])
                            {
                                if(index=="0")
                                {
                                    message.value="Du hast gewonnen! \n      Glückwunsch!"
                                }
                                else{
                                    message.value="Spieler 1 hat gewonnen! \n           Glückwunsch!"
                                }
                                enableResultScreen(alpha,bgColor, resultBtnEnabled,size,message)
                            }
                            if(currGame!!.winner==currGame!!.playerChar[1])
                            {
                                if(index=="0")
                                {
                                    message.value="Du hast verloren :("
                                }
                                else{
                                    message.value="Spieler 2 hat gewonnen! \n           Glückwunsch!"
                                }
                                enableResultScreen(alpha,bgColor, resultBtnEnabled,size,message)
                            }
                            if(currGame!!.isDraw()&&currGame!!.winner==' ')
                            {
                                message.value="Es ist ein Unentschieden!"
                                enableResultScreen(alpha,bgColor, resultBtnEnabled,size,message)
                            }
                            if(set&&currGame!!.winner==' '&& currGame!!.title=="1 Spieler")
                            {
                                canSet=false
                                MainScope().launch {
                                    delay(300)
                                    currGame!!.setRandomCell()
                                    recompVar.value="T"
                                    recompVar.value=""// dieser Code forciert eine Recomposition
                                    if(currGame!!.winner==currGame!!.playerChar[1]&& index=="0")
                                    {
                                        message.value="Du hast verloren :("
                                        enableResultScreen(alpha,bgColor, resultBtnEnabled,size,message)
                                    }
                                    if(currGame!!.isDraw()&&currGame!!.winner==' ')
                                    {
                                        message.value="    Es ist ein \n    Unentschieden!"
                                        enableResultScreen(alpha,bgColor, resultBtnEnabled,size,message)
                                    }

                                }

                            }



                    }){
                        Text(color=currGame!!.field[currGame!!.field.indexOf(fieldRow)][currGame!!.field[currGame!!.field.indexOf(fieldRow)].indexOf(fieldCell)].color,textAlign = TextAlign.Center, fontSize = 100.sp, text= currGame!!.field[currGame!!.field.indexOf(fieldRow)][currGame!!.field[currGame!!.field.indexOf(fieldRow)].indexOf(fieldCell)].playerChar+recompVar.value)
                    }

                    Spacer(modifier = Modifier.padding(5.dp))
                }
            }
        }
    }
    HelpFunctions.ResultScreen(
        bgColor = bgColor,
        resultBtnEnabled = resultBtnEnabled,
        alpha = alpha,
        message = message,
        index = index,
        size = size,
        chatFuture = chatFuture,
        screen = Screens.TicTacToeGameScreen
    )
}


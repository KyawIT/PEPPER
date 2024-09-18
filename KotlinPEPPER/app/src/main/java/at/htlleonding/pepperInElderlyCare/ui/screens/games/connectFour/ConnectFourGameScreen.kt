package at.htlleonding.pepperInElderlyCare.ui.screens.games.connectFour

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import at.htlleonding.pepperInElderlyCare.ui.model.ConnectFour
import at.htlleonding.pepperInElderlyCare.ui.model.GameStats
import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens
import at.htlleonding.pepperInElderlyCare.ui.theme.*
import com.aldebaran.qi.Future
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var currConnectFour:ConnectFour?=null
private var chatFuture: Future<Void>?=null
var canSet= true
@Composable
fun ConnectFourGameScreen(navController: NavController, index: String?) {
    var resultBtnEnabled = remember { mutableStateOf(false)  }
    var alpha = remember { mutableStateOf(0f) }
    var message = remember { mutableStateOf("") }
    var bgColor = remember {
        mutableStateOf(Color.Transparent)
    }
    var fieldColor = lightPurple
    var buttonColor= darkPurple
    if (index == "1"){
        buttonColor= darkOrange
        fieldColor = LightOrange;
    }
    var size = remember{
        mutableStateOf(150.dp)
    }
    var initState = remember{
        mutableStateOf(true)
    }
    if(initState.value)
    {
        initState.value=false
        var selectedGame = GameStats.defaultConnectFour[index!!.toInt()]
        currConnectFour = ConnectFour(selectedGame.field.size,selectedGame.field[0].size,selectedGame.title)
        if(HelpFunctions.onPepper)
        {
            HelpFunctions.sayAsync("Viel Spaß")?.andThenConsume {
                //chatFuture=HelpFunctions.runBackChat(Screens.ConnectFourGameSelectScreen);
            }
        }
    }
    var recomposeVar= remember {
        mutableStateOf("")
    }
    Button(modifier = Modifier
        .absolutePadding(top = 20.dp, left = 10.dp)
        .fillMaxWidth(0.13f)

        , onClick = { HelpFunctions.navToSite(Screens.ConnectFourGameSelectScreen.name,
            chatFuture) })
    {
        Text("Zurück", color = Color.White, fontSize = 25.sp)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(  ){
                for(num in 0 until currConnectFour!!.field[0].size)
            {
                if(num !=0) {
                    Spacer(modifier = Modifier.absolutePadding(right = 9.dp))
                }
                Button(colors=ButtonDefaults.buttonColors(buttonColor),modifier = Modifier
                    .width(113.dp)
                    .height(113.dp)
                    .padding(5.dp, 5.dp)
                    ,onClick ={
                        if(canSet)
                        {
                            var dropped=currConnectFour!!.drop(num,currConnectFour!!.currPlayer)
                            recomposeVar.value="T"
                            recomposeVar.value=""// dieser Code forciert eine Recomposition
                            if(currConnectFour!!.winner!=' ')
                            {
                                if(currConnectFour!!.isFinished(currConnectFour!!.playerChar[0]))
                                {
                                    if(index=="1")
                                    {
                                        message.value="Spieler 1 hat gewonnen! \n           Glückwunsch!"
                                    }
                                    else{
                                        message.value="Du hast gewonnen! \n      Glückwunsch!"
                                    }
                                }
                                else if(currConnectFour!!.isFinished(currConnectFour!!.playerChar[1]))
                                {
                                    if(index=="1")
                                    {
                                        message.value="Spieler 2 hat gewonnen! \n           Glückwunsch!"
                                    }
                                    else{
                                        message.value="Du hast verloren :("
                                    }
                                }
                                HelpFunctions.enableResultScreen(alpha, bgColor ,resultBtnEnabled, size ,message)
                            }
                            else if(index=="0"&&dropped) // wenn der Spieler einen Stein gedroppt hat, dann ist der Bot an der Reihe( im Einzelspieler Modus)
                            {
                                canSet=false
                                MainScope().launch {
                                    delay(300)
                                    currConnectFour!!.dropBot();

                                    canSet=true;
                                    if(currConnectFour!!.winner!=' ')
                                    {
                                        if(currConnectFour!!.isFinished(currConnectFour!!.playerChar[1]))
                                        {
                                            message.value="Du hast verloren :("
                                        }
                                        HelpFunctions.enableResultScreen(alpha, bgColor ,resultBtnEnabled, size ,message)
                                    }
                                    if(currConnectFour!!.isDraw()&& currConnectFour!!.winner==' '){
                                        message.value="Es ist ein Unentschieden!"
                                        HelpFunctions.enableResultScreen(alpha, bgColor ,resultBtnEnabled, size ,message)
                                    }
                                    recomposeVar.value="T"
                                    recomposeVar.value=""// dieser Code forciert eine Recomposition
                                }
                            }
                            if(currConnectFour!!.isDraw()&& currConnectFour!!.winner==' '){
                                message.value="Es ist ein Unentschieden!"
                                HelpFunctions.enableResultScreen(alpha, bgColor ,resultBtnEnabled, size ,message)
                            }
                        }
                    })
                {
                    Text(textAlign = TextAlign.Center, fontSize = 50.sp,text ="↓")
                }
            }
        }

        for(fieldRow in currConnectFour!!.field)
        {
            Row {
                var count=0
                for(fieldCell in fieldRow)
                {
                    val text = remember{ mutableStateOf(currConnectFour!!.field[currConnectFour!!.field.indexOf(fieldRow)][fieldRow.indexOf(fieldCell)]) }
                    Box(modifier = Modifier
                        .width(113.dp)
                        .height(113.dp)
                        .padding(5.dp, 5.dp)
                        .background(fieldColor),
                        contentAlignment = Alignment.Center)
                    {
                        Text( color= currConnectFour!!.field[currConnectFour!!.field.indexOf(fieldRow)][currConnectFour!!.field[currConnectFour!!.field.indexOf(fieldRow)].indexOf(fieldCell)].color,fontSize = 70.sp, text=recomposeVar.value+text.value.playerChar.toString())
                    }
                    count++
                    Spacer(modifier = Modifier.padding(5.dp,5.dp))
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
        screen = Screens.ConnectFourGameScreen

    )
}

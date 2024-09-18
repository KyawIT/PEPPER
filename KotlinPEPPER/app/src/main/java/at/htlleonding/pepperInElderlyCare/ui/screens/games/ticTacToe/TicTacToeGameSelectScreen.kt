package at.htlleonding.pepperInElderlyCare.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.htlleonding.pepperInElderlyCare.ui.HelpFunctions
import at.htlleonding.pepperInElderlyCare.ui.model.GameStats
import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens
import at.htlleonding.pepperInElderlyCare.ui.theme.*
import com.aldebaran.qi.Future

private var chatFuture: Future<Void>?=null

@Composable
fun TicTacToeGameSelectScreen(navController: NavController) {
    var chatState = remember {
        mutableStateOf(true)
    }
    if(chatState.value&&HelpFunctions.onPepper)
    {
        chatState.value=false
        HelpFunctions.sayAsync("Wollen Sie alleine oder zu zweit spielen?")?.andThenConsume()
        {
            //chatFuture= HelpFunctions.runGameDifficultyScreen(navController,Screens.TicTacToeGameSelectScreen);
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(15.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
            //.clip(RoundedCornerShape(10.dp))
            //.background(Color.White)
            //.shadow(elevation = 6.dp, shape = RoundedCornerShape(10.dp))
        ) {

            Button(modifier = Modifier
                .padding(10.dp, 10.dp)
                .fillMaxWidth(0.13f),
                onClick = {HelpFunctions.navToSite(Screens.GameSelectionScreen.name,
                    chatFuture) },
                colors = ButtonDefaults.buttonColors(purple))
            {
                Text("Zurück", color = Color.White, fontSize = 25.sp)
            }
            Text(
                text = "Möchten Sie Alleine oder zu Zweit spielen?",
                fontSize = 35.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            Modifier.padding(7.5.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 25.dp)
                    .padding(horizontal = 25.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Purple200)
                        .fillMaxWidth(0.46f)
                        .fillMaxHeight(0.8f)
                ) {

                    Button(
                        modifier = Modifier.fillMaxSize(),
                        onClick = { HelpFunctions.navToSite(Screens.TicTacToeGameScreen.name + "/" + 0,
                            chatFuture) },
                        colors = ButtonDefaults.buttonColors(LightBlue)
                    ) {
                        Text(
                            text = GameStats.defaultTicTacToe[0].title,
                            fontSize = 85.sp
                        )

                    }
                }


            Spacer(modifier = Modifier.padding(horizontal = 30.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.8f)
            ) {
                Button(
                    modifier = Modifier.fillMaxSize(),
                    onClick = { HelpFunctions.navToSite(Screens.TicTacToeGameScreen.name + "/" + 1,
                        chatFuture)},
                    colors = ButtonDefaults.buttonColors(LightRed)
                ) {
                    Text(
                        text = GameStats.defaultTicTacToe[1].title,
                        fontSize = 85.sp
                    )
                }
            }
            }
        }

    }
}
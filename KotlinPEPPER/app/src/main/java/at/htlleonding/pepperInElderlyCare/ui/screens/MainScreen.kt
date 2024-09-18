package at.htlleonding.pepperInElderlyCare.ui.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.htlleonding.pepperInElderlyCare.R
import at.htlleonding.pepperInElderlyCare.ui.HelpFunctions
import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens
import at.htlleonding.pepperInElderlyCare.ui.theme.backgroundColor
import at.htlleonding.pepperInElderlyCare.ui.theme.danceButtonColor
import at.htlleonding.pepperInElderlyCare.ui.theme.gameButtonColor
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.builder.*
import com.aldebaran.qi.sdk.`object`.conversation.*
private var chatFuture:Future<Void>?=null;

@ExperimentalAnimationApi
@Composable
fun MainScreen(navController: NavController)
{
    var chatState = remember {
        mutableStateOf(true)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row{
                Button(modifier = Modifier
                    .clip(RoundedCornerShape(60.dp))
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.9f)
                    .padding(40.dp),
                    colors = ButtonDefaults.buttonColors(danceButtonColor),
                    onClick = { HelpFunctions.navToSite(Screens.DanceSelectionScreen.name,
                        chatFuture) }) {
                    //Text("Tänze", fontSize = 100.sp)
                    Image(
                        painter = painterResource(id = R.drawable.dance),
                        contentDescription = null
                    )
                }
                Button(modifier = Modifier
                    .clip(RoundedCornerShape(60.dp))
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.9f)
                    .padding(50.dp),
                    colors = ButtonDefaults.buttonColors(gameButtonColor),
                    onClick = { HelpFunctions.navToSite(Screens.GameSelectionScreen.name,
                        chatFuture) }) {
                    //Text("Spiele", fontSize = 100.sp)
                    Image(
                        painter = painterResource(id = R.drawable.game),
                        contentDescription = null
                    )
                }
            }


    }
    if(chatState.value&&HelpFunctions.onPepper) {
        chatState.value=false
        HelpFunctions.sayAsync(
            "Hallo, was wollen Sie machen "
        )?.andThenConsume {
            //runMainScreenChat(navController)
        }
    }
}


fun runMainScreenChat(navController: NavController){
    val topic: Future<Topic>? =
        TopicBuilder.with(HelpFunctions.qIContext)
            .withResource(R.raw.main)
            .buildAsync()
    val qiChatbot: Future<QiChatbot>? = QiChatbotBuilder.with(HelpFunctions.qIContext)
        .withTopic(topic!!.value)
        .buildAsync()

    var chat:Future<Chat> = ChatBuilder.with(HelpFunctions.qIContext)
        .withChatbot(qiChatbot!!.value)
        .buildAsync()
    chatFuture = chat.get().async().run()

    chat.get().async().addOnHeardListener { heardPhrase->
        if(heardPhrase.text.lowercase().contains("spiel"))
        {
            Handler(Looper.getMainLooper()).post {
                HelpFunctions.navToSite(Screens.GameSelectionScreen.name,chatFuture!!)
            }
        }
        else if(heardPhrase.text.lowercase().contains("tanz"))
        {
            Handler(Looper.getMainLooper()).post {
                HelpFunctions.navToSite(Screens.DanceSelectionScreen.name,chatFuture!!)
            }
        }
    }
}


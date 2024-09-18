@file:OptIn(ExperimentalFoundationApi::class)

package at.htlleonding.pepperInElderlyCare.ui.screens.games

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.htlleonding.pepperInElderlyCare.R
import at.htlleonding.pepperInElderlyCare.ui.HelpFunctions
import at.htlleonding.pepperInElderlyCare.ui.model.getGames
import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens
import at.htlleonding.pepperInElderlyCare.ui.theme.LightBlue
import at.htlleonding.pepperInElderlyCare.ui.theme.purple
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.aldebaran.qi.sdk.`object`.conversation.Chat
import com.aldebaran.qi.sdk.`object`.conversation.QiChatbot
import com.aldebaran.qi.sdk.`object`.conversation.Topic

private var chatFuture:Future<Void>?=null;
@ExperimentalAnimationApi
@Composable
fun GameSelectionScreen(navController: NavController)
{

    val pics = remember {
        mutableListOf(R.drawable.tic_tac_toe, R.drawable.connect_four, R.drawable.memory, R.drawable.hike, R.drawable.calculate);
    }
     var picId = 0;

    var chatState = remember{
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Row (modifier = Modifier.fillMaxWidth(),
        ) {

            Button(modifier = Modifier
                .padding(10.dp, 10.dp)
                .fillMaxWidth(0.13f),
                colors = ButtonDefaults.buttonColors(purple),
                onClick = { HelpFunctions.navToSite(Screens.MainScreen.name, chatFuture) })
            {
                Text("Zurück", color = Color.White, fontSize = 25.sp)
            }
            Text(
                text = "Spiele",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(250.dp, 7.dp),
                fontSize = 65.sp
            )
        }
        val games= getGames()
        Column(modifier = Modifier
            .padding(30.dp)
            .fillMaxSize())
        {
            LazyVerticalGrid(modifier = Modifier.fillMaxSize()
                , cells = GridCells.Fixed(4))
            {
                for (game in games) {
                    item {
                        Button(
                            modifier = Modifier
                                .clip(RoundedCornerShape(60.dp))
                                .fillMaxWidth(0.2f)
                                .fillMaxHeight(0.2f)
                                .aspectRatio(1f)
                                .padding(15.dp),
                            colors = ButtonDefaults.buttonColors(LightBlue),
                            onClick = { HelpFunctions.navToSite(game.screen.name, chatFuture) })
                        {
                            Image(
                                painter = painterResource(id = pics[picId]),
                                contentDescription = null
                            )

                            picId++;
                        }
                    }
                }
            }
        }
    }

    if(chatState.value&&HelpFunctions.onPepper) {
        chatState.value=false
        HelpFunctions.sayAsync(
            "Welches Spiel wollen Sie spielen"
        )?.andThenConsume {
           // runGameSelectChat(navController)
        }
    }


}


fun runGameSelectChat(navController: NavController){
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
    chatFuture= chat.get().async().run()

    chat.get().async().addOnHeardListener { heardPhrase->
        if(heardPhrase.text.lowercase().contains("zurück"))
        {
            Handler(Looper.getMainLooper()).post {
                chatFuture!!.requestCancellation()
                navController.navigate(Screens.MainScreen.name)
            }
        }
        for( game in getGames())
        {
            for(suggestion in game.suggestions)
            {
                if(heardPhrase.text.lowercase().contains(suggestion.lowercase()))
                {
                    Handler(Looper.getMainLooper()).post {
                        chatFuture!!.requestCancellation()
                        navController.navigate(game.screen.name)
                    }
                    break;
                }
            }
        }
    }
}


package at.htlleonding.pepperInElderlyCare.ui

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.htlleonding.pepperInElderlyCare.R
import at.htlleonding.pepperInElderlyCare.ui.model.data.Step
import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens
import at.htlleonding.pepperInElderlyCare.ui.theme.gameButtonColor
import at.htlleonding.pepperInElderlyCare.ui.theme.gewinnerScreen
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.aldebaran.qi.sdk.`object`.conversation.Chat
import com.aldebaran.qi.sdk.`object`.conversation.QiChatbot
import com.aldebaran.qi.sdk.`object`.conversation.Say
import com.aldebaran.qi.sdk.`object`.conversation.Topic
import java.util.*

class HelpFunctions {
    companion object {

        //var headTouchSensor: TouchSensor? = null
        var qIContext: QiContext? = null
        //var touch: Touch? = null;

        var navController:NavController?=null;

        // enabled alle Pepper funktionen
        var onPepper:Boolean=false;

        var executedSteps:LinkedList<Step>? = LinkedList();

        fun sayAsync(content: String): Future<Void>? {
            val say: Future<Say>? = SayBuilder.with(qIContext)
                .withText(content)
                .buildAsync()
            while (!say!!.isDone) { }// wartet auf den build von say
            return try {
                say.get().async().run()
            } catch (e: Exception) {
                null
            }
        }

        // wenn ein Seitenwechsel stattfindet, dann soll die Chat Future abgebrochen werden
        fun navToSite(screen:String,chat:Future<Void>?)
        {
            if(chat!=null)
            {
                chat.requestCancellation();
            }
            navController!!.navigate(screen)
        }

        // diese Methode initialisiert einen Chat, der nur für das Zurück-Navigieren zuständig ist
        fun runBackChat(screen:Screens): Future<Void>? {
            val topic: Future<Topic>? =
                TopicBuilder.with(qIContext)
                    .withResource(R.raw.main)
                    .buildAsync()
            val qiChatbot: Future<QiChatbot>? = QiChatbotBuilder.with(qIContext)
                .withTopic(topic!!.value)
                .buildAsync()

            var chat:Future<Chat> = ChatBuilder.with(qIContext)
                .withChatbot(qiChatbot!!.value)
                .buildAsync()
            var chatFuture = chat.get().async().run()

            chat.get().async().addOnHeardListener { heardPhrase->
                if(heardPhrase.text.lowercase().contains("zurück")) {
                    Handler(Looper.getMainLooper()).post {
                        chatFuture!!.requestCancellation()
                        navController!!.navigate(screen.name)
                    }
                }
            }
            return chatFuture
        }

        // diese Methode initialisiert einen Chat, der für das Navigieren zwischen dem GameDifficultyScreen und dem GameScreen(TicTacToe und Connect Four) zuständig ist
        fun runGameDifficultyScreen(currScreen:Screens): Future<Void>? {
            val topic: Future<Topic>? =
                TopicBuilder.with(HelpFunctions.qIContext)
                    .withResource(R.raw.main)
                    .buildAsync()
            val qiChatbot: Future<QiChatbot>? = QiChatbotBuilder.with(qIContext)
                .withTopic(topic!!.value)
                .buildAsync()

            var chat:Future<Chat> = ChatBuilder.with(qIContext)
                .withChatbot(qiChatbot!!.value)
                .buildAsync()
            var chatFuture = chat.get().async().run()

            chat.get().async().addOnHeardListener { heardPhrase->
                if(heardPhrase.text.lowercase().contains("zurück"))
                {
                    Handler(Looper.getMainLooper()).post {
                        chatFuture.requestCancellation()
                        navController!!.navigate(Screens.GameSelectionScreen.name)
                    }
                }
                // wenn der Spieler Einspieler oder Alleine sagt, wird dieses Code Segment ausgefürt
                if(heardPhrase.text.lowercase().contains("ein")||heardPhrase.text.lowercase().contains("allein"))
                {
                    Handler(Looper.getMainLooper()).post {
                        chatFuture.requestCancellation()
                        if(currScreen==Screens.ConnectFourGameSelectScreen)
                        {
                            navController!!.navigate(Screens.ConnectFourGameScreen.name+"/0")
                        }
                        else if(currScreen==Screens.TicTacToeGameSelectScreen){
                            navController!!.navigate(Screens.TicTacToeGameScreen.name+"/0")
                        }
                    }
                }
                // wenn der Spieler Zweispieler sagt, wird dieses Code Segment ausgeführt
                if(heardPhrase.text.lowercase().contains("zwei"))
                {
                    Handler(Looper.getMainLooper()).post {
                        chatFuture!!.requestCancellation()
                        if(currScreen==Screens.ConnectFourGameSelectScreen)
                        {
                            navController!!.navigate(Screens.ConnectFourGameScreen.name+"/1")
                        }
                        else if(currScreen==Screens.TicTacToeGameSelectScreen){
                            navController!!.navigate(Screens.TicTacToeGameScreen.name+"/1")
                        }
                    }
                }
            }
            return chatFuture;
        }
        @Composable
        fun ResultScreen(bgColor: MutableState<Color>, resultBtnEnabled:MutableState<Boolean>, alpha:MutableState<Float>, message: MutableState<String>, index:String?,size:MutableState<Dp>,chatFuture:Future<Void>?, screen:Screens) {
            var textSplitted = message.value.split('!');
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(0.dp, 220.dp)
            ) {

                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .background(bgColor.value)
                        .fillMaxHeight(1f)
                        .fillMaxWidth(1f)
                        .alpha(alpha.value)
                        .border(2.dp, Color.Black)
                        .clip(
                            RoundedCornerShape(60.dp)
                        )
                )
                {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row {
                            Button(
                                enabled = resultBtnEnabled.value,
                                modifier = Modifier.padding(0.dp, 10.dp, 70.dp, 10.dp)
                                    .size(size.value),
                                colors = ButtonDefaults.buttonColors(gameButtonColor),

                                onClick = {
                                    bgColor.value = Color.Transparent
                                    resultBtnEnabled.value = false
                                    alpha.value = 0f
                                    size.value = 0.dp
                                    if(screen==Screens.ConnectFourGameScreen)
                                    {
                                        navToSite(
                                            Screens.ConnectFourGameScreen.name + "/" + index,
                                            chatFuture
                                        )
                                    }
                                    else if (screen==Screens.TicTacToeGameScreen)
                                    {
                                        navToSite(
                                            Screens.TicTacToeGameScreen.name + "/" + index,
                                            chatFuture
                                        )
                                    }

                                }) {
                                Image(
                                    painter = painterResource(id = R.drawable.restart),
                                    contentDescription = null
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(20.dp, 20.dp, 70.dp, 20.dp)
                            )
                            {
                                Text(
                                    text = message.value,
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                enabled = resultBtnEnabled.value,
                                modifier = Modifier.size(size.value)
                                    .padding(0.dp, 10.dp, 0.dp, 0.dp),
                                colors = ButtonDefaults.buttonColors(gameButtonColor),

                                onClick = {
                                    if(screen==Screens.ConnectFourGameScreen)
                                    {
                                        navToSite(
                                            Screens.ConnectFourGameSelectScreen.name,
                                            chatFuture
                                        )
                                    }
                                    else if (screen==Screens.TicTacToeGameScreen)
                                    {
                                        navToSite(
                                            Screens.TicTacToeGameSelectScreen.name,
                                            chatFuture
                                        )
                                    }
                                }) {
                                Image(
                                    painter = painterResource(id = R.drawable.home),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }


        @Composable
        fun ResultScreenMemory(bgColor: MutableState<Color>, resultBtnEnabled:MutableState<Boolean>, alpha:MutableState<Float>, message: MutableState<String>,size:MutableState<Dp>) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(0.dp, 220.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier
                    .background(bgColor.value)
                    .fillMaxHeight(1f)
                    .fillMaxWidth(1f)
                    .alpha(alpha.value)
                    .border(2.dp, Color.Black)
                    .clip(
                        RoundedCornerShape(60.dp)
                    )
                )
                {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Row{
                            Button(
                                enabled = resultBtnEnabled.value,
                                modifier = Modifier.padding(10.dp, 10.dp, 60.dp, 10.dp).width(200.dp).height(200.dp)
                                    .size(size.value),
                                colors = ButtonDefaults.buttonColors(gameButtonColor),

                                onClick = {
                                    bgColor.value = Color.Transparent
                                    resultBtnEnabled.value = false
                                    alpha.value = 0f
                                    size.value = 0.dp
                                    navController!!.navigate(Screens.MemoryGameScreen.name)
                                }) {
                                Image(
                                    painter = painterResource(id = R.drawable.restart),
                                    contentDescription = null
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(20.dp, 20.dp, 90.dp, 20.dp))
                            {
                                Text(text = message.value, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                enabled = resultBtnEnabled.value,
                                modifier = Modifier.size(size.value).width(12.dp).height(12.dp).padding(50.dp, 10.dp, 9.dp, 10.dp),
                                colors = ButtonDefaults.buttonColors(gameButtonColor),

                                onClick = {

                                    navController!!.navigate(Screens.GameSelectionScreen.name)
                                }) {


                                Image(
                                    painter = painterResource(id = R.drawable.home),
                                    contentDescription = null
                                )
                            }
                        }

                    }
                }
            }
        }
        fun enableResultScreen(alpha: MutableState<Float>,bgColor:MutableState<Color>,resultBtnEnabled:MutableState<Boolean>,size:MutableState<Dp>, message:MutableState<String>
        ){
            alpha.value=1f
            bgColor.value= gewinnerScreen
            resultBtnEnabled.value=true
            if(message.value.contains("Gratuliere"))
            {
                size.value=250.dp
            }
            else{
                size.value=150.dp

            }
            if(onPepper)
            {
                sayAsync(message.value)
            }
        }
    }


}
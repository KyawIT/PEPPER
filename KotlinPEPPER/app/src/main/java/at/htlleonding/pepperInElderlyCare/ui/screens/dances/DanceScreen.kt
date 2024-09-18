package at.htlleonding.pepperInElderlyCare.ui.screens.dances

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.htlleonding.pepperInElderlyCare.R
import at.htlleonding.pepperInElderlyCare.ui.HelpFunctions
import at.htlleonding.pepperInElderlyCare.ui.model.Dance
import at.htlleonding.pepperInElderlyCare.ui.model.getDances
import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.builder.*
import com.aldebaran.qi.sdk.`object`.actuation.Animate
import com.aldebaran.qi.sdk.`object`.actuation.Animation
import com.aldebaran.qi.sdk.`object`.conversation.Chat
import com.aldebaran.qi.sdk.`object`.conversation.QiChatbot
import com.aldebaran.qi.sdk.`object`.conversation.Topic

private var chatFuture:Future<Void>?=null;
var mediaPlayer:MediaPlayer?=null
var animate:Future<Animate>? = null
var animation: Future<Animation>?=null
@ExperimentalAnimationApi
@Composable
fun DanceScreen(navController:NavController,danceID:String?)
{
    val pics = remember {
        mutableListOf(R.drawable.matrosen, R.drawable.spannenlangerhansel, R.drawable.affenbande);
    }
    var selectedDance: Dance? = getDances().find{ it.id==danceID}
    var musicState = remember{
        mutableStateOf(true)
    }
    if(musicState.value&&HelpFunctions.onPepper)
    {
        musicState.value=false
        animation = AnimationBuilder.with(HelpFunctions.qIContext)
            .withResources(selectedDance?.animPath)
            .buildAsync()
        animate = AnimateBuilder.with(HelpFunctions.qIContext) // Create the builder with the context.
            .withAnimation(animation!!.get()) // Set the animation.
            .buildAsync() // Build the animate action.
        animate!!.get().async().run()

        mediaPlayer = MediaPlayer.create(HelpFunctions.qIContext,selectedDance!!.soundPath)
        mediaPlayer!!.setVolume(1f,1f)
        mediaPlayer!!.start()
        //chatFuture= HelpFunctions.runBackChat(Screens.DanceSelectionScreen)
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier
                .padding(10.dp, 10.dp)
                .fillMaxWidth(0.13f),
            onClick = {
                if(mediaPlayer!=null)
                {
                    mediaPlayer!!.release()
                }
                if(HelpFunctions.onPepper)
                {
                    animation!!.requestCancellation()
                    animate?.requestCancellation()

                    AnimationBuilder
                        .with(HelpFunctions.qIContext)
                        .withResources(R.raw.emote_stand_still)
                        .buildAsync()
                        .thenConsume { futureAnimation ->
                            animate = AnimateBuilder
                                .with(HelpFunctions.qIContext)
                                .withAnimation(futureAnimation?.value)
                                .buildAsync()
                            animate?.andThenConsume {
                                it.async().run().thenConsume(){
                                    HelpFunctions.navToSite(Screens.DanceSelectionScreen.name, chatFuture)
                                }
                            }
                        }
                }
            })
        {
            Text("Zurück", color = Color.White, fontSize = 25.sp)
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(0.dp,60.dp
    )){
        /*
        Text(
            text = "Tanze mit mir!",
            fontWeight = FontWeight.Bold,
            fontSize = 65.sp,
        )

         */
        var id = danceID;
        if (id != null) {
            Image(
                painter = painterResource(id = pics[id.toInt()-1]),
                contentDescription = null
            )
        }
    }


}




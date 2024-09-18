package at.htlleonding.pepperInElderlyCare.ui.screens.dances

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
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
import at.htlleonding.pepperInElderlyCare.ui.model.getDances
import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens
import at.htlleonding.pepperInElderlyCare.ui.theme.LightBlue
import at.htlleonding.pepperInElderlyCare.ui.theme.purple
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.builder.*
import com.aldebaran.qi.sdk.`object`.conversation.Chat
import com.aldebaran.qi.sdk.`object`.conversation.QiChatbot
import com.aldebaran.qi.sdk.`object`.conversation.Topic

private var chatFuture:Future<Void>?=null;

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun DanceSelectionScreen(navController: NavController)
{
    val pics = remember {
        mutableListOf(R.drawable.captain, R.drawable.pear, R.drawable.monkey);
    }
    val dances= getDances()

    var picId = 0;

    var chatState = remember{
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row (modifier = Modifier.fillMaxWidth()
            ) {

            Button(modifier = Modifier
                .padding(10.dp, 10.dp)
                .fillMaxWidth(0.13f),
                colors = ButtonDefaults.buttonColors(purple),
                onClick = { HelpFunctions.navToSite(Screens.MainScreen.name, chatFuture)  })
            {
                Text("Zurück", color = Color.White, fontSize = 25.sp)
            }
            Text(
                text = "Tänze",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(250.dp, 7.dp),
                fontSize = 65.sp
            )
        }

        Column(modifier = Modifier
            .padding(40.dp)
            .fillMaxSize())

        {
            LazyVerticalGrid(modifier = Modifier.fillMaxSize()
                , cells = GridCells.Fixed(3))
            {

                for (dance in dances) {
                    item {
                        Button(
                            modifier = Modifier
                                .clip(RoundedCornerShape(60.dp))
                                .fillMaxSize()
                                .fillMaxWidth(0.7f)
                                .fillMaxHeight(0.3f)
                                .aspectRatio(1f)
                                .padding(15.dp),
                            colors = ButtonDefaults.buttonColors(LightBlue),
                            onClick = { HelpFunctions.navToSite(Screens.DanceScreen.name+"/"+dance.id, chatFuture) })
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

        LazyColumn(modifier = Modifier.fillMaxHeight(0.9f)){
            for(dance in dances)
                item{
                    Spacer(modifier = Modifier.padding(10.dp))
                    Button(modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .fillMaxHeight(0.3f),
                        colors = ButtonDefaults.buttonColors(purple),
                        onClick = { HelpFunctions.navToSite(Screens.DanceScreen.name+"/"+dance.id, chatFuture) })
                    {
                        Text(
                            text =dance.title,
                            fontSize = 60.sp
                        )
                    }
                }
        }
    }
    if(chatState.value&&HelpFunctions.onPepper) {
        chatState.value=false
        var animation= AnimationBuilder.with(HelpFunctions.qIContext)
            .withResources(R.raw.emote_stand_still)
            .buildAsync()
        animate = AnimateBuilder.with(HelpFunctions.qIContext) // Create the builder with the context.
            .withAnimation(animation!!.get()) // Set the animation.
            .buildAsync() // Build the animate action.
        animate!!.get().async().run()
        HelpFunctions.sayAsync(
            "Welchen Tanz wollen Sie tanzen"
        )
    }
}




fun runDanceSelectScreen(navController: NavController){
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
        if(heardPhrase.text.lowercase().contains("zurück"))
        {
            Handler(Looper.getMainLooper()).post {
                chatFuture!!.requestCancellation()
                navController.navigate(Screens.MainScreen.name)
            }
        }
        for( dance in getDances())
        {
            for(suggestion in dance.suggestions)
            {
                if(heardPhrase.text.lowercase().contains(suggestion.lowercase()))
                {
                    Handler(Looper.getMainLooper()).post {
                        chatFuture!!.requestCancellation()
                        navController.navigate(Screens.DanceScreen.name+"/"+dance.id)
                    }
                    break;
                }
            }

        }
    }
}

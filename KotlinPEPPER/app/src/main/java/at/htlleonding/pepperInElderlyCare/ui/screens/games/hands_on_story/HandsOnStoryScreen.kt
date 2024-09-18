package at.htlleonding.pepperInElderlyCare.ui.screens.games.hands_on_story

import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.htlleonding.pepperInElderlyCare.R
import at.htlleonding.pepperInElderlyCare.ui.HelpFunctions
import at.htlleonding.pepperInElderlyCare.ui.model.api.TagAlongStoryRequest
import at.htlleonding.pepperInElderlyCare.ui.model.data.Step
import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.builder.AnimateBuilder
import com.aldebaran.qi.sdk.builder.AnimationBuilder
import com.aldebaran.qi.sdk.`object`.actuation.Animation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var chatFuture: Future<Void>? = null
var mediaPlayer: MediaPlayer? = null

@ExperimentalAnimationApi
@Composable
fun HandsOnStoryScreen(navController: NavController, handsOnStoryId: Int?) {

    var selectedStorySteps by remember { mutableStateOf<List<Step>?>(null) }
    var musicState by remember { mutableStateOf(true) }
    var handsOnStoryStep by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) } // Track loading state

    handsOnStoryId?.let { TagAlongStoryRequest.getStoryById(id = it) }!!.enqueue(object : Callback<List<Step>?> {
        override fun onResponse(call: Call<List<Step>?>, response: Response<List<Step>?>) {

            if (!response.isSuccessful) {
                Log.e("API_CALL", "API call failed with code ${response.code()}")
            }
            if (response.isSuccessful) {
                selectedStorySteps = response.body()
                isLoading = false
                Log.d("API_CALL", "API call successful with code ${response.code()}")
                nextStep(selectedStorySteps?.get(0), navController);
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onFailure(call: Call<List<Step>?>, t: Throwable) {
            isLoading = false
            Log.e("API_CALL", "API call failed", t)
        }
    }
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier
                .padding(10.dp, 10.dp)
                .fillMaxWidth(0.13f),
            onClick = {
                if (mediaPlayer != null) {
                    mediaPlayer!!.release()
                }
                selectedStorySteps = null;

                HelpFunctions.navToSite(Screens.HandsOnStorySelectionScreen.name, chatFuture)
            }
        ) {
            Text("Menü", color = Color.White, fontSize = 25.sp)
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Button(
            modifier = Modifier
                .padding(10.dp, 10.dp)
                .width(150.dp)
                .fillMaxWidth(0.13f),
            onClick = {
                handsOnStoryStep = maxOf(0, handsOnStoryStep - 1)
                nextStep(selectedStorySteps?.get(handsOnStoryStep),  navController)            }) {
            Text("Zurück", color = Color.White, fontSize = 25.sp)
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Button(
            enabled = handsOnStoryStep < (selectedStorySteps?.size ?: (0 - 1)),
            modifier = Modifier
                .padding(10.dp, 10.dp)
                .width(150.dp)
                .fillMaxWidth(0.13f),
            onClick = {
                handsOnStoryStep = minOf(
                    handsOnStoryStep + 1,
                    selectedStorySteps?.size ?: (0 - 1)
                )
                if(handsOnStoryStep == (selectedStorySteps?.size!!)) {
                    selectedStorySteps = null;

                    HelpFunctions.navToSite(Screens.HandsOnStorySelectionScreen.name, chatFuture)
                }
                else{
                    nextStep(selectedStorySteps!![handsOnStoryStep],  navController)
                }            }) {
            Text("Weiter", color = Color.White, fontSize = 25.sp)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        if ((selectedStorySteps != null) && (handsOnStoryStep < selectedStorySteps!!.size)) {

            Image(
                bitmap = decodeBase64ToImage(base64String = selectedStorySteps!![handsOnStoryStep].image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .widthIn(min = 200.dp, max=700.dp)
                    .heightIn(min = 100.dp, max = 400.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        handsOnStoryStep = minOf(
                            handsOnStoryStep + 1,
                            selectedStorySteps?.size ?: (0 - 1)
                        )
                        if(handsOnStoryStep == (selectedStorySteps?.size!!)) {
                            selectedStorySteps = null;
                            HelpFunctions.navToSite(Screens.HandsOnStorySelectionScreen.name, chatFuture)
                        }
                        else{
                            nextStep(selectedStorySteps!![handsOnStoryStep],  navController)
                        }
                    },
            )
        }
    }
}

fun nextStep(selectedTagAlongStory: Step?, navController: NavController) {

    if(HelpFunctions.onPepper) {

        if(HelpFunctions.executedSteps?.contains(selectedTagAlongStory) == true){
            return;
        }
        if (selectedTagAlongStory != null) {
            HelpFunctions.executedSteps?.add(selectedTagAlongStory)
        }


        selectedTagAlongStory?.let {
            HelpFunctions.sayAsync(selectedTagAlongStory.text)

            val animation: Future<Animation>

            Log.d("Animation: ", selectedTagAlongStory.moveNameAndDuration);

            when (selectedTagAlongStory.moveNameAndDuration) {
                "emote_hurra_5" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.emote_hurra_5)
                        .buildAsync()
                }
                "emote_hurra_10" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.emote_hurra_10)
                        .buildAsync()
                }
                "emote_hurra_15" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.emote_hurra_15)
                        .buildAsync()
                }
                "essen_5" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.essen_05)
                        .buildAsync()
                }
                "essen_10" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.essen_10)
                        .buildAsync()
                }
                "essen_15" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.essen_15)
                        .buildAsync()
                }
                "gehen_5" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.gehen_5)
                        .buildAsync()
                }
                "gehen_10" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.gehen_10)
                        .buildAsync()
                }
                "gehen_15" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.gehen_15)
                        .buildAsync()
                }
                "hand_heben_5" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.hand_heben_5)
                        .buildAsync()
                }
                "hand_heben_10" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.hand_heben_10)
                        .buildAsync()
                }
                "hand_heben_15" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.hand_heben_15)
                        .buildAsync()
                }
                "highfive_links_5" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.highfive_links_5)
                        .buildAsync()
                }
                "highfive_links_10" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.highfive_links_10)
                        .buildAsync()
                }
                "highfive_links_15" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.highfive_links_15)
                        .buildAsync()
                }
                "highfive_rechts_5" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.highfive_rechts_5)
                        .buildAsync()
                }
                "highfive_rechts_10" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.highfive_rechts_10)
                        .buildAsync()
                }
                "highfive_rechts_15" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.highfive_rechts_15)
                        .buildAsync()
                }
                "klatschen_5" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.klatschen_5)
                        .buildAsync()
                }
                "klatschen_10" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.klatschen_10)
                        .buildAsync()
                }
                "klatschen_15" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.klatschen_15)
                        .buildAsync()
                }
                "strecken_5" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.strecken_5)
                        .buildAsync()
                }
                "strecken_10" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.strecken_10)
                        .buildAsync()
                }
                "strecken_15" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.strecken_15)
                        .buildAsync()
                }
                "umher_sehen_5" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.umher_sehen_5)
                        .buildAsync()
                }
                "umher_sehen_10" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.umher_sehen_10)
                        .buildAsync()
                }
                "umher_sehen_15" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.umher_sehen_15)
                        .buildAsync()
                }
                "winken_5" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.winken_5)
                        .buildAsync()
                }
                "winken_10" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.winken_10)
                        .buildAsync()
                }
                "winken_15" -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.winken_15)
                        .buildAsync()
                }
                else -> {
                    animation = AnimationBuilder.with(HelpFunctions.qIContext)
                        .withResources(R.raw.emote_hurra_5)
                        .buildAsync()
                }
            }
            val animate = AnimateBuilder.with(HelpFunctions.qIContext)
                .withAnimation(animation.get())
                .buildAsync()
            animate.get().async().run()
        }
    }

}





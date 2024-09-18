package at.htlleonding.pepperInElderlyCare.ui.screens.games.hands_on_story

import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.htlleonding.pepperInElderlyCare.ui.HelpFunctions
import at.htlleonding.pepperInElderlyCare.ui.model.api.TagAlongStoryRequest.Companion.getAllTagAlongStories
import at.htlleonding.pepperInElderlyCare.ui.model.data.TagAlongStory
import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens
import at.htlleonding.pepperInElderlyCare.ui.screens.games.ticTacToe.chatFuture
import at.htlleonding.pepperInElderlyCare.ui.theme.LightBlue
import at.htlleonding.pepperInElderlyCare.ui.theme.purple
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
@ExperimentalAnimationApi
@Composable
fun HandsOnStorySelectionScreen(navController: NavController) {
    var chatState by remember { mutableStateOf(true) }
    var handsOnStories by remember { mutableStateOf<List<TagAlongStory>>(emptyList()) }
    var clearThread = Runnable {
        Thread.sleep(1000)
        HelpFunctions.executedSteps!!.clear()
    }

    Thread(clearThread).start()

    var responseText: String = ""

    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        isLoading = true
        getAllTagAlongStories()?.enqueue(object : Callback<List<TagAlongStory>?> {
            override fun onResponse(
                call: Call<List<TagAlongStory>?>,
                response: Response<List<TagAlongStory>?>
            ) {
                isLoading = false

                if (!response.isSuccessful) {
                    responseText =
                        "Gerade sind keine Geschichten verfügbar. Versuch es später noch einmal"
                    Log.e("API_CALL", "API call failed with code ${response.code()}")
                }
                if (response.isSuccessful) {
                    handsOnStories = response.body()!!
                    Log.d("API_CALL", "API call successful with code ${response.code()}")
                }
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onFailure(call: Call<List<TagAlongStory>?>, t: Throwable) {
                isLoading = false

                responseText =
                    "Ups, da ist etwas schiefgelaufen. Sind Geschichten in der Web app verfügbar?"
                Log.e("API_CALL", "API call failed", t)
            }
        })
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .padding(5.dp),
                colors = buttonColors(purple),
                onClick = {
                    HelpFunctions.navToSite(
                        Screens.GameSelectionScreen.name, chatFuture
                    )
                }
            ) {
                Text("Zurück", color = Color.White, fontSize = 25.sp)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 180.dp)
            ) {
                Text(
                    text = "Geschichten",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 65.sp,
                )
            }

            Button(
                modifier = Modifier
                    .padding(5.dp),
                colors = buttonColors(purple),
                onClick = {
                    isLoading = true
                    getAllTagAlongStories()?.enqueue(object : Callback<List<TagAlongStory>?> {
                        override fun onResponse(
                            call: Call<List<TagAlongStory>?>,
                            response: Response<List<TagAlongStory>?>
                        ) {
                            isLoading = false

                            if (!response.isSuccessful) {
                                responseText =
                                    "Gerade sind keine Geschichten verfügbar. Versuch es später noch einmal"
                                Log.e("API_CALL", "API call failed with code ${response.code()}")
                            }
                            if (response.isSuccessful) {
                                handsOnStories = response.body()!!
                                Log.d("API_CALL", "API call successful with code ${response.code()}")
                            }
                        }

                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onFailure(call: Call<List<TagAlongStory>?>, t: Throwable) {
                            isLoading = false

                            responseText =
                                "Ups, da ist etwas schiefgelaufen. Sind Geschichten in der Web app verfügbar?"
                            Log.e("API_CALL", "API call failed", t)
                        }
                    })
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Neu Laden",
                    tint = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp, 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            itemsIndexed(handsOnStories.chunked(3)) { _, rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (handsOnStory in rowItems) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(20.dp),
                            colors = buttonColors(LightBlue),
                            onClick = {
                                HelpFunctions.navToSite(
                                    "${Screens.HandsOnStoryScreen.name}/${handsOnStory.id}",
                                    chatFuture
                                )
                            }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = handsOnStory.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                )
                                Image(
                                    painter = rememberImagePainter(
                                        data = handsOnStory.storyIcon,
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(300.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (chatState && HelpFunctions.onPepper) {
        HelpFunctions.sayAsync("Welche Geschichte wollen sie hören?")
    }
}

@Composable
fun decodeBase64ToImage(base64String: String): ImageBitmap {
    val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    return bitmap.asImageBitmap()
}

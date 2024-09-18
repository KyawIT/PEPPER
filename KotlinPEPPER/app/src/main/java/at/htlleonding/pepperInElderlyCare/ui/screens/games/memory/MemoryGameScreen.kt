package at.htlleonding.pepperInElderlyCare.ui.screens.games.memory

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens
import at.htlleonding.pepperInElderlyCare.ui.theme.purple

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import at.htlleonding.pepperInElderlyCare.R
import at.htlleonding.pepperInElderlyCare.ui.HelpFunctions
import at.htlleonding.pepperInElderlyCare.ui.theme.gameButtonColor
import at.htlleonding.pepperInElderlyCare.ui.theme.gewinnerScreen
import at.htlleonding.pepperInElderlyCare.ui.theme.purpleLight
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var flipped:Boolean = false
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MemoryGameScreen(navController: NavController){

    var tryCounting = 0;
    var resultBtnEnabled = remember { mutableStateOf(false)  }
    var alpha = remember { mutableStateOf(0f) }
    var message = remember { mutableStateOf("") }
    var bgColor = remember {
        mutableStateOf(Color.Transparent)
    }
    var size = remember{
        mutableStateOf(0.dp)
    }

    val nums = remember {
        mutableListOf(1, 1, 2, 2, 3, 3, 4, 4).shuffled()
    }

    val pics = remember {
        mutableListOf(R.drawable.memory_apple, R.drawable.memory_banana, R.drawable.memory_grapes, R.drawable.memory_melon);
    }

    var flippedCards = remember { mutableStateListOf<Int>() }
    val matchedCards = remember { mutableStateListOf<Int>() }
    val isGameOver = remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(modifier = Modifier
                    .padding(10.dp, 10.dp)
                    .fillMaxWidth(0.13f),
                    colors = ButtonDefaults.buttonColors(purple),
                    onClick = { navController.navigate(Screens.GameSelectionScreen.name) })
                {
                    Text("Zurück", color = Color.White, fontSize = 25.sp)
                }
                Text(
                    text = "Memory",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(250.dp, 7.dp),
                    fontSize = 65.sp
                )
            }
        }
        if (!isGameOver.value) {
            Column(modifier = Modifier
                .padding(100.dp)
                .fillMaxSize()
                .fillMaxWidth()
                .fillMaxHeight()) {
                LazyVerticalGrid(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp), cells = GridCells.Fixed(4)) {
                    itemsIndexed(nums) { index, number ->
                        if (flippedCards.contains(index) || matchedCards.contains(index)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(60.dp))
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .padding(15.dp)
                                    .background(purpleLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = pics[number-1]),
                                    contentDescription = null
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(60.dp))
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .padding(15.dp)
                                    .background(purple)
                                    .clickable {
                                        tryCounting++;
                                        if (!flipped) {
                                            flipCard(
                                                index,
                                                number,
                                                flippedCards,
                                                matchedCards,
                                                nums as MutableList<Int>,
                                                isGameOver
                                            )
                                        }
                                        if (checkGameOver(matchedCards, nums as MutableList<Int>)) {
                                            tryCounting /= 2; //Zwei Kärtchen aufdecken => 1 Versuch
                                            message.value = "       Gratuliere       \n        Versuche: $tryCounting"
                                            HelpFunctions.enableResultScreen(alpha, bgColor ,resultBtnEnabled, size ,message)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "?",
                                    style = MaterialTheme.typography.h6,
                                    fontSize = 50.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    HelpFunctions.ResultScreenMemory(
        bgColor = bgColor,
        resultBtnEnabled = resultBtnEnabled,
        alpha = alpha,
        message = message,
        size =size ,
    )
}


private fun flipCard(
    index: Int,
    number: Int,
    flippedCards: MutableList<Int>,
    matchedCards: MutableList<Int>,
    numbers: MutableList<Int>,
    isGameOver: MutableState<Boolean>
) {
    flippedCards.add(index)
    if (flippedCards.size == 2) {
        flipped=true

        val card1 = flippedCards[0]
        val card2 = flippedCards[1]
        if (numbers[card1] == numbers[card2]) {
            matchedCards.add(card1)
            matchedCards.add(card2)
            flippedCards.clear()
            flipped=false;
        } else {
            isGameOver.value = checkGameOver(matchedCards, numbers)
            MainScope().launch { waitForFlip(flippedCards) }
        }
    }
}

private fun checkGameOver(matchedCards: MutableList<Int>, numbers: MutableList<Int>): Boolean {
    for (i in 0 until numbers.size) {
        if (!matchedCards.contains(i)) {
            return false
        }
    }
    return true;
}
private suspend fun waitForFlip(flippedCards: MutableList<Int>){
    delay(750)
    flipped=false;
    flippedCards.clear()
}


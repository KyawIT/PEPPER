package at.htlleonding.pepperInElderlyCare.ui.model

import androidx.compose.ui.graphics.Color

class Cell(xParam:Int,yParam:Int,player:Char) {
    var x:Int
    var y: Int
    var playerChar:Char
    var color:Color=Color.Black
    init {
        x=xParam
        y=yParam
        playerChar=player
    }
}
package at.htlleonding.pepperInElderlyCare.ui.model

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class TicTacToe( title:String) {
    var field:Array<Array<Cell>>
    var title:String
    val playerChar = charArrayOf('X','O')
    var currPlayer='X'
    var winner:Char=' '
    var moveCount=0


    init {
        var x=0
        var y=0
        field= Array(3){ x++;Array(3){y++;Cell(x,y,' ')}}
        this.title=title
    }


    fun setCell(x:Int,y:Int):Boolean
    {

        if(winner==' '&&field[x][y].playerChar==' ') {
            if(currPlayer==playerChar[0])
            {
                moveCount++
            }
            field[x][y].playerChar=currPlayer
            if(isFinished(currPlayer))
            {
                winner=currPlayer
                field[x][y].color= Color.Green
            }
            else {
                currPlayer = if(currPlayer==playerChar[0]) {
                    playerChar[1]
                } else{
                    playerChar[0]
                }
            }
            return true;
        }
        return false;




    }
    fun setRandomCell(){
        if(moveCount<1)
        {
            var isSet = false
            do {
                val randNumRow = Random.nextInt(0, field.size)
                val randNumCol = Random.nextInt(0, field[0].size)
                if(field[randNumRow][randNumCol].playerChar == ' '){
                    setCell(randNumRow,randNumCol)
                    isSet = true;
                }
            }while (!isSet&&winner==' ')
        }
       else{
           val moveToDo = calcNextMove()
            if(moveToDo[0]!=-1)
            {
                setCell(moveToDo[0],moveToDo[1])
            }
        }

    }
    private fun isFinished(character:Char) :Boolean{

        for(i in 0 until 3)
        {
            for(j in 0 until 3)
            {
                if(checkDiagonals(i,j, character)||checkVerticalAndHorizontal(i,j,character))
                {
                    return true
                }
            }
        }
        return false
    }

    private  fun checkVerticalAndHorizontal(x:Int,y:Int,character: Char): Boolean {
        var count = 0
        for(num in 0..2)
        {
            if(x+num<field.size)
            {
                if(field[x+num][y].playerChar==character)
                {
                    count++
                    field[x+num][y].color=Color.Green
                }
                else{
                    break
                }
            }
        }
        if(count>=3)
        {
            return true
        }
        clearColor()

        count=0
        for(num in 0..2)
        {
            if(x-num>=0)
            {
                if(field[x-num][y].playerChar==character)
                {
                    count++;
                    field[x-num][y].color=Color.Green
                }
                else{
                    break
                }
            }
        }
        if(count>=3)
        {
            return true
        }
        clearColor()
        count=0
        for(num in 0..2)
        {
            if(y+num<field[0].size)
            {
                if(field[x][y+num].playerChar==character)
                {
                    count++
                    field[x][y+num].color=Color.Green
                }
                else{
                    break
                }
            }
        }
        if(count>=3)
        {
            return true
        }
        clearColor()
        count=0
        for(num in 0..2)
        {
            if(y-num>=0)
            {
                if(field[x][y-num].playerChar==character)
                {
                    count++;
                    field[x][y-num].color=Color.Green
                }
                else{
                    break
                }
            }
        }
        if(count>=3)
        {
            return true
        }
        clearColor()
        return false
    }

    private fun checkDiagonals(x: Int, y: Int, character: Char):Boolean
    {
        var count = 0
        for(num in 0 .. 2)
        {
            if(x+num<field.size&&y+num<field[0].size)
            {
                if(field[x+num][y+num].playerChar==character)
                {
                    count++
                    field[x+num][y+num].color=Color.Green
                }
                else{
                    break
                }
            }
        }
        if(count==3)
        {
            return true
        }
        count=0
        clearColor()
        for(num in 0..2)
        {
            if(x-num>=0&&y-num>=0)
            {
                if(field[x-num][y-num].playerChar==character)
                {
                    count++
                    field[x-num][y-num].color=Color.Green

                }
                else{
                    break
                }
            }
        }
        if(count==3)
        {
            return true
        }
        count=0
        clearColor()
        for(num in 0 .. 2)
        {
            if(x+num<field.size&&y-num>=0)
            {
                if(field[x+num][y-num].playerChar==character)
                {
                    count++
                    field[x+num][y-num].color=Color.Green

                }
                else{
                    break
                }
            }
        }
        if(count==3)
        {
            return true
        }
        clearColor()
        count=0
        for(num in 0 .. 2)
        {
            if(x-num>=0&&y+num<field[0].size)
            {
                if(field[x-num][y+num].playerChar==character)
                {
                    count++
                    field[x-num][y+num].color=Color.Green

                }else{
                    break
                }
            }
        }
        if(count==3)
        {
            return true
        }
        clearColor()
        return false
    }

    // This Method uses the minimax algorithm to determine the best possible move, which the bot could play
    private fun calcNextMove():Array<Int>
    {

        var bestScore = Int.MIN_VALUE
        var bestMove= arrayOf(-1,-1)
        for( i in 0 until 3)
        {
            for(j in 0 until 3)
            {
                if(field[i][j].playerChar==' ')
                {
                    field[i][j].playerChar=playerChar[1];
                    var score = minimax(0,false)
                    field[i][j].playerChar=' '
                    if(score>bestScore)
                    {
                        bestScore=score
                        bestMove[0]=i
                        bestMove[1]=j
                    }
                }
            }
        }
        return bestMove
    }
    private fun minimax(depth:Int,isMaximising:Boolean):Int
    {
        if(isFinished(playerChar[0]))
        {
            winner=' '
            return -1;
        }
        if(isFinished(playerChar[1]))
        {
            winner=' '
            return 1;
        }
        if(depth==3)
        {
            return 0;
        }

        if(isMaximising){
            var bestScore =Int.MIN_VALUE
            for( i in 0 until 3)
            {
                for(j in 0 until 3)
                {
                    if(field[i][j].playerChar==' ')
                    {
                        field[i][j].playerChar=playerChar[1];
                        var score = minimax(depth+1,false)
                        field[i][j].playerChar=' '
                        bestScore = max(bestScore,score)
                    }
                }
            }
            return max(bestScore,0);
        }
        else{
            var bestScore = Int.MAX_VALUE
            for( i in 0 until 3)
            {
                for(j in 0 until 3)
                {
                    if(field[i][j].playerChar==' ')
                    {
                        var score = 0
                        field[i][j].playerChar=playerChar[0]
                        score = minimax(depth+1,true)
                        field[i][j].playerChar=' '
                        bestScore = min(bestScore,score)
                    }
                }
            }
            if(bestScore!=Int.MAX_VALUE)
            {
                return bestScore;
            }
            return 0;
        }
    }


    //this method clears the color of the board, in case the winner is set without the game being won
    private fun clearColor(){
        for( i in 0 until 3)
        {
            for(j in 0 until 3)
            {
                field[i][j].color=Color.Black
            }
        }
    }

    // if every field is used up and theres no winner, its a draw
    fun isDraw():Boolean
    {
        for(num in 0 until 3)
        {
            for(j in 0 until 3)
            {
                if(field[num][j].playerChar==' ')
                {
                    return false
                }
            }
        }
        return true
    }
}
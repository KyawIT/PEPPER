package at.htlleonding.pepperInElderlyCare.ui.model

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class ConnectFour(height: Int, width: Int, title: String) {
    var field: Array<Array<Cell>>
    var title: String
    val playerChar = charArrayOf('X', 'O')
    var currPlayer = 'X'
    var aiPlayer = 'O'
    var winner: Char = ' '
    var moveCount = 0

    init {
        var x = 0
        var y = 0
        field = Array(height) {
            x++;
            Array(width) {
                y++;
                Cell(x, y, ' ')
            }
        }
        this.title = title
    }

    private fun removeDrop(y: Int) {
        var x = -1
        for (num in 0..field.size) {
            if (field[num][y].playerChar != ' ') {
                x = num
                break
            }
        }
        if (x != -1) {
            field[x][y].playerChar = ' '
        }
    }

    fun drop(y: Int, character: Char): Boolean {
        var x = -1
        for (num in field.size - 1 downTo 0) {
            if (field[num][y].playerChar == ' ') {
                x = num
                break
            }
        }
        if (x != -1) {
            if (character == playerChar[0]) {
                moveCount++
            }
            if (field[x][y].playerChar == ' ' && winner == ' ') {
                field[x][y].playerChar = character

                if (isFinished(currPlayer)) {
                    winner = currPlayer
                } else {
                    currPlayer = if (currPlayer == playerChar[0]) {
                        playerChar[1]
                    } else {
                        playerChar[0]
                    }
                }
            }
            return true;
        } else {
            return false;
        }

    }

    fun dropBot() {
        if (moveCount < 3) {
            // Random move for the first three moves
            var isSet: Boolean
            do {
                val randNumCol = Random.nextInt(0, field[0].size)
                isSet = drop(randNumCol, currPlayer)
            } while (!isSet)
        } else {
            val winningMove = findWinningMove(aiPlayer)
            if (winningMove != -1) {
                drop(winningMove, aiPlayer)
            } else {
                val bestMove = minimax(4, aiPlayer)
                if (bestMove != -1) {
                    drop(bestMove, aiPlayer)
                }
            }
        }
    }

    private fun findWinningMove(player: Char): Int {
        for (move in 0 until field[0].size) {
            val row = getEmptyRow(move)
            if (row != -1) {
                field[row][move].playerChar = player
                if (isFinished(player)) {
                    removeDrop(move)
                    return move
                }
                removeDrop(move)
            }
        }
        return -1 // No winning move found
    }


    private fun minimax(depth: Int, player: Char): Int {
        val legalMoves = mutableListOf<Int>()

        for (i in 0 until field[0].size) {
            if (field[0][i].playerChar == ' ') {
                legalMoves.add(i)
            }
        }

        var bestScore = if (player == aiPlayer) Int.MIN_VALUE else Int.MAX_VALUE
        var bestMove = -1

        for (move in legalMoves) {
            val row = getEmptyRow(move)
            field[row][move].playerChar = player

            if (isFinished(player)) {
                val score = if (player == aiPlayer) 1 else -1
                removeDrop(move)
                return score
            }

            if (depth > 0) {
                val score = minimax(depth - 1, if (player == 'O') 'X' else 'O')
                if (player == aiPlayer) {
                    if (score > bestScore) {
                        bestScore = score
                        bestMove = move
                    }
                } else {
                    if (score < bestScore) {
                        bestScore = score
                        bestMove = move
                    }
                }
            }

            removeDrop(move)
        }

        return bestMove
    }

    private fun getEmptyRow(col: Int): Int {
        for (row in field.indices.reversed()) {
            if (field[row][col].playerChar == ' ') {
                return row
            }
        }
        return -1
    }

    public fun isFinished(character: Char): Boolean {
        for (i in field.indices) {
            for (j in 0 until field[0].size) {
                if (checkDiagonals(i, j, character) || checkVerticalAndHorizontal(
                        i,
                        j,
                        character
                    )
                ) {
                    return true
                }
            }
        }
        return false
    }

    private fun checkVerticalAndHorizontal(x: Int, y: Int, character: Char): Boolean {
        var canGoDown = true
        var canGoUp = true
        var count = 0
        for (num in 0..3) {
            if (canGoDown && x + num < field.size) {
                if (field[x + num][y].playerChar == character) {
                    count++
                    field[x + num][y].color = Color.Green
                } else {
                    canGoDown = false
                }
            }
            if (canGoUp && x - num - 1 >= 0) {
                if (field[x - num - 1][y].playerChar == character) {
                    count++;
                    field[x - num - 1][y].color = Color.Green

                } else {
                    canGoUp = false
                }
            }
        }
        if (count >= 4) {
            return true
        }

        count = 0
        clearColor()

        canGoDown = true
        canGoUp = true
        for (num in 0..3) {
            if (canGoDown && y + num < field[0].size) {
                if (field[x][y + num].playerChar == character) {
                    count++
                    field[x][y + num].color = Color.Green

                } else {
                    canGoDown = false
                }
            }
            if (canGoUp && y - num - 1 >= 0) {
                if (field[x][y - num - 1].playerChar == character) {
                    count++
                    field[x][y - num - 1].color = Color.Green

                } else {
                    canGoUp = false
                }
            }
        }
        if (count >= 4) {
            return true
        }
        clearColor()

        return false
    }

    public fun isDraw(): Boolean {
        var isDraw = true
        for (num in 0..field[0].size - 1) {
            if (field[0][num].playerChar == ' ') {
                isDraw = false
            }
        }
        return isDraw
    }

    private fun checkDiagonals(x: Int, y: Int, character: Char): Boolean {
        var count = 0
        for (num in 0..3) {
            if (x + num < field.size && y + num < field[0].size) {
                if (field[x + num][y + num].playerChar == character) {
                    count++
                    field[x + num][y + num].color = Color.Green

                } else {
                    break
                }
            }
            if (x - num - 1 >= 0 && y - num - 1 >= 0) {
                if (field[x - num - 1][y - num - 1].playerChar == character) {
                    field[x - num - 1][y - num - 1].color = Color.Green

                    count++
                } else {
                    break
                }
            }
        }
        if (count == 4) {
            return true
        }
        clearColor()

        count = 0
        for (num in 0..3) {
            if (x + num < field.size && y - num >= 0) {
                if (field[x + num][y - num].playerChar == character) {
                    count++
                    field[x + num][y - num].color = Color.Green

                } else {
                    break
                }
            }
            if (x - num - 1 >= 0 && y + num + 1 < field[0].size) {
                if (field[x - num - 1][y + num + 1].playerChar == character) {
                    field[x - num - 1][y + num + 1].color = Color.Green

                    count++
                } else {
                    break
                }
            }
        }
        if (count == 4) {
            return true
        }
        clearColor()
        return false
    }

    //this method clears the color of the board, in case the winner is set without the game being won
    private fun clearColor() {
        for (i in field.indices) {
            for (j in 0 until field[0].size) {
                field[i][j].color = Color.Black
            }

        }
    }
}


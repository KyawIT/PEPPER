package at.htlleonding.pepperInElderlyCare.ui.model

class GameStats {
    companion object{
        var defaultConnectFour=arrayOf(ConnectFour(4,5,"1 Spieler"), ConnectFour(4,5,"2 Spieler"))
        var defaultTicTacToe=arrayOf(TicTacToe("1 Spieler"), TicTacToe("2 Spieler"))
    }
}
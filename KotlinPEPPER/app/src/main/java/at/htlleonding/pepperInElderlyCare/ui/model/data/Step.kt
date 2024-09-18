package at.htlleonding.pepperInElderlyCare.ui.model.data

data class Step(
    val id : Int,
    val text : String = "",
    val image : String = "",
    val duration: Int,
    val moveNameAndDuration: String = ""
)

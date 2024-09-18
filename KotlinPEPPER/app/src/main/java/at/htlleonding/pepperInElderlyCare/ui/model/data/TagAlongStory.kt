package at.htlleonding.pepperInElderlyCare.ui.model.data

data class TagAlongStory(
    val id : Int,
    val name : String,
    val storyIcon: String = "",
    val steps : List<Step>,
    val isEnabled : Boolean
)

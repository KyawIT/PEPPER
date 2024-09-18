package at.htlleonding.pepperInElderlyCare.ui.model
import at.htlleonding.pepperInElderlyCare.R
import at.htlleonding.pepperInElderlyCare.ui.navigation.Screens

data class Dance(val id: String,
                 val title: String,
                 val animPath:Int,
                 val soundPath:Int,
                 val suggestions:List<String>
                 )

fun getDances(): List<Dance> {
    return listOf(
        Dance(id="1",
            title="Ein kleiner Matrose",
            animPath =R.raw.kleiner_matrose,
            soundPath=R.raw.kleiner_matrose_song,
            suggestions = listOf("matrose","kleiner")
        ),
        Dance(id="2",
            title="Spannenlanger Hansel",
            animPath =R.raw.spannenlanger_hansel,
            soundPath=R.raw.spannenlanger_hansl_song,
            suggestions = listOf("Hansel", "Spannenlanger","Span","langer")
        ),
        Dance(id="3",
            title="Die Affen rasen durch den Wald",
            animPath =R.raw.die_affen_rasen_durch_den_wald,
            soundPath=R.raw.die_affen_rasen_durch_den_wald_song,
            suggestions = listOf("Affen","rasen","Wald")
        )
    )
}

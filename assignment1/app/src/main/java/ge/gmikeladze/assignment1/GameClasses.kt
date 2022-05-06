package ge.gmikeladze.assignment1

data class Card (
    val id: Int,
    var isGuessed: Boolean = false,
    var isTurned: Boolean = false
)

class GameState {

    var cards: MutableList<Card> = mutableListOf()
    var numSuccess: Int = 0
    var numFailed: Int = 0
    private var selectedCardPositions: MutableList<Int> = mutableListOf()

    init {
        val images = mutableListOf(
            R.drawable.bullterrier,
            R.drawable.hunter_dachshund,
            R.drawable.swollen_shiba
        )
        images.addAll(images)
        val imagesShuffled = images.shuffled()
        imagesShuffled.forEach { this.cards += Card(it) }
    }

    fun turnCard(position: Int) {
        if (selectedCardPositions.isEmpty()) {
            selectedCardPositions.add(position)
        } else if (selectedCardPositions.size == 2) {
            turnCardsBack()
            selectedCardPositions.add(position)
        } else {
            if (cards[selectedCardPositions[0]].id == cards[position].id) {
                cards[selectedCardPositions[0]].isGuessed = true
                cards[position].isGuessed = true
                selectedCardPositions.clear()
                numSuccess++
            } else {
                numFailed++
                selectedCardPositions.add(position)
            }
        }
        cards[position].isTurned = !cards[position].isTurned
    }

    private fun turnCardsBack() {
        cards.forEach {
            if (!it.isGuessed)
                it.isTurned = false
        }
        selectedCardPositions.clear()
    }
}

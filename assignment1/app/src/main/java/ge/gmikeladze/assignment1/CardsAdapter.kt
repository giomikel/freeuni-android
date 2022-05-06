package ge.gmikeladze.assignment1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView

class CardsAdapter(private val gameState: GameState, private val clickHandler: MainActivity.CardClickHandler) : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        val imageButton = view.findViewById<ImageButton>(R.id.imageButton)
        val imageButtonLayoutParams: ViewGroup.MarginLayoutParams = imageButton.layoutParams as ViewGroup.MarginLayoutParams
        val marginSize = 16
        imageButtonLayoutParams.setMargins(marginSize)
        val elementWidth = parent.width / 2 - marginSize * 2
        val elementHeight = parent.height / 3 - marginSize * 2
        imageButtonLayoutParams.height = elementHeight
        imageButtonLayoutParams.width = elementWidth
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.setIsRecyclable(false) // amis gareshe araprognozirebulad iqceodnen kartebi
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return gameState.cards.size // Make constructor parameter a property
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
        fun bind(position: Int) {
            if (gameState.cards[position].isGuessed)
                imageButton.alpha = 0F
            if (gameState.cards[position].isTurned)
                imageButton.setImageResource(gameState.cards[position].id)
            else {
                imageButton.setImageResource(R.drawable.deck)
                imageButton.setOnClickListener {
                    clickHandler.onClick(position)
                }
            }
        }
    }
}

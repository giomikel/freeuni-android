package ge.gmikeladze.assignment1

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var successText: TextView
    private lateinit var failText: TextView
    private lateinit var cardRV: RecyclerView
    private lateinit var gameState: GameState
    private lateinit var restartButton: Button

    interface CardClickHandler {
        fun onClick(position: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resetGame()
    }

    private fun resetGame() {
        successText = findViewById(R.id.successText)
        successText.text = getString(R.string.success_text, 0)
        successText.setTextColor(Color.GRAY)

        failText = findViewById(R.id.failText)
        failText.text = getString(R.string.fail_text, 0)
        failText.setTextColor(Color.GRAY)

        cardRV = findViewById(R.id.cardRV)
        cardRV.layoutManager = GridLayoutManager(this, 2)

        restartButton = findViewById(R.id.restartButton)

        this.gameState = GameState()

        cardRV.adapter = CardsAdapter(gameState, object : CardClickHandler {
            override fun onClick(position: Int) {
                handleClick(position)
            }
        })

        restartButton.setOnClickListener {
            resetGame()
        }
    }

    private fun handleClick(position: Int) {
        if (!gameState.cards[position].isTurned) {
            gameState.turnCard(position)
            updateText()
            cardRV.adapter!!.notifyDataSetChanged()
        }
    }

    private fun updateText() {
        successText.text = getString(R.string.success_text, gameState.numSuccess)
        if (gameState.numSuccess == 1)
            successText.setTextColor(Color.parseColor("#00FF00"))
        failText.text = getString(R.string.fail_text, gameState.numFailed)
        if (gameState.numFailed == 1)
            failText.setTextColor(Color.parseColor("#FF0000"))
    }
}
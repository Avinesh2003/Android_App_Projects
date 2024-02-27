package edu.muthuselvam.ttt

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // New Game Button
    private lateinit var newGameButton: Button

    // Array for the game board
    private var mBoard = charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9')

    // Various text displayed
    private lateinit var mInfoTextView: TextView

    // Game Variables
    private val BOARD_SIZE = 9
    private val HUMAN_PLAYER = 'X'
    private val COMPUTER_PLAYER = 'O'
    private var turn = 'X'
    private var win = 0
    private var move = -1

    // Buttons making up the board
    private lateinit var mBoardButtons: Array<Button>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get reference to the buttons
        mBoardButtons = arrayOf(
            findViewById(R.id.square1),
            findViewById(R.id.square2),
            findViewById(R.id.square3),
            findViewById(R.id.square4),
            findViewById(R.id.square5),
            findViewById(R.id.square6),
            findViewById(R.id.square7),
            findViewById(R.id.square8),
            findViewById(R.id.square9)
        )

        // Initialize info label
        mInfoTextView = findViewById(R.id.InforLabel)

        // Set click listeners for buttons
        for (i in 0..<BOARD_SIZE) {
            mBoardButtons[i].setOnClickListener { HumanMove(i) }
        }

        // Set click listener for the "New Game" button
        newGameButton = findViewById(R.id.reset)
        newGameButton.setOnClickListener { onNewGameClick() }

    }


    fun HumanMove(int: Int) {
        when (int) {
            int -> {
                if (win == 0) {

                    if (turn == HUMAN_PLAYER) {
                        turn = COMPUTER_PLAYER
                        mInfoTextView.text = "Computer's Turn"
                        mBoard[int] = HUMAN_PLAYER
                        mBoardButtons[int].text = "X"
                        mBoardButtons[int].setTextColor(Color.rgb(200, 0, 0))
                        displayBoard()
                        win = checkForWinner()
                        showStatus()
                        if (win == 0) {
                            getComputerMove()
                            turn = HUMAN_PLAYER
                            mInfoTextView.text = "Human's Turn"
                            displayBoard()
                            win = checkForWinner()
                            showStatus()
                        }
                    }
                }
            }

            }
        }


    fun displayBoard()
    {
        System.out.println();
        System.out.println(mBoard[0] + " | " + mBoard[1] + " | " + mBoard[2]);
        System.out.println("-----------");
        System.out.println(mBoard[3] + " | " + mBoard[4] + " | " + mBoard[5]);
        System.out.println("-----------");
        System.out.println(mBoard[6] + " | " + mBoard[7] + " | " + mBoard[8]);
        System.out.println();

    }

    fun checkForWinner(): Int
    {
        run {
            var i = 0
            while (i <= 6) {
                if (mBoard[i] === HUMAN_PLAYER && mBoard[i + 1] === HUMAN_PLAYER && mBoard[i + 2] === HUMAN_PLAYER) return 2
                if (mBoard[i] === COMPUTER_PLAYER && mBoard[i + 1] === COMPUTER_PLAYER && mBoard[i + 2] === COMPUTER_PLAYER) return 3
                i += 3
            }
        }

        // Check vertical wins

        // Check vertical wins
        for (i in 0..2) {
            if (mBoard[i] === HUMAN_PLAYER && mBoard[i + 3] === HUMAN_PLAYER && mBoard[i + 6] === HUMAN_PLAYER) return 2
            if (mBoard[i] === COMPUTER_PLAYER && mBoard[i + 3] === COMPUTER_PLAYER && mBoard[i + 6] === COMPUTER_PLAYER) return 3
        }

        // Check for diagonal wins

        // Check for diagonal wins
        if (mBoard[0] === HUMAN_PLAYER && mBoard[4] === HUMAN_PLAYER && mBoard[8] === HUMAN_PLAYER || mBoard[2] === HUMAN_PLAYER && mBoard[4] === HUMAN_PLAYER && mBoard[6] === HUMAN_PLAYER) return 2
        if (mBoard[0] === COMPUTER_PLAYER && mBoard[4] === COMPUTER_PLAYER && mBoard[8] === COMPUTER_PLAYER || mBoard[2] === COMPUTER_PLAYER && mBoard[4] === COMPUTER_PLAYER && mBoard[6] === COMPUTER_PLAYER) return 3

        // Check for tie

        // Check for tie
        for (i in 0 until BOARD_SIZE) {
            // If we find a number, then no one has won yet
            if (mBoard[i] !== HUMAN_PLAYER && mBoard[i] !== COMPUTER_PLAYER) return 0
        }

        // If we make it through the previous loop, all places are taken, so it's a tie

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1

    }

    fun showStatus()
    {
        // Report the winner
        System.out.println();
        if (win == 1)
            mInfoTextView.setText("It's a tie")

        else if (win == 2)
        mInfoTextView.setText(HUMAN_PLAYER + " wins!")

        else if (win == 3)
        mInfoTextView.setText(COMPUTER_PLAYER + " wins!")

    }

    fun onNewGameClick(){
        mBoard = charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9')
        turn = 'X'
        win = 0

        // Reset the button texts and colors
        for (i in mBoardButtons.indices) {
            mBoardButtons[i].text = ""
            mBoardButtons[i].setTextColor(Color.BLACK)
        }

        // Reset the info label
        mInfoTextView.text = "New Game Started"

    }

    private fun getComputerMove() {
        var move: Int

        // First see if there's a move O can make to win
        for (i in 0 until BOARD_SIZE) {
            if (mBoard[i] !== HUMAN_PLAYER && mBoard[i] !== COMPUTER_PLAYER) {
                val curr = mBoard[i]
                mBoard[i] = COMPUTER_PLAYER
                if (checkForWinner() == 3) {
                    println("Computer is moving to " + (i + 1))
                    mBoardButtons[i].text = "O"
                    mBoardButtons[i].setTextColor(Color.rgb(0, 200, 0))
                    return
                } else mBoard[i] = curr
            }
        }

        // See if there's a move O can make to block X from winning
        for (i in 0 until BOARD_SIZE) {
            if (mBoard[i] !== HUMAN_PLAYER && mBoard[i] !== COMPUTER_PLAYER) {
                val curr = mBoard[i] // Save the current number
                mBoard[i] = HUMAN_PLAYER
                if (checkForWinner() == 2) {
                    mBoard[i] = COMPUTER_PLAYER
                    println("Computer is moving to " + (i + 1))
                    mBoardButtons[i].text = "O"
                    mBoardButtons[i].setTextColor(Color.rgb(0, 200, 0))
                    return
                } else mBoard[i] = curr
            }
        }

        // Generate random move
        do {
            move = Random.nextInt(BOARD_SIZE)
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER)

        println("Computer is moving to " + (move + 1))
        mBoard[move] = COMPUTER_PLAYER
        mBoardButtons[move].text = "O"
        mBoardButtons[move].setTextColor(Color.rgb(0, 200, 0))
    }
}

package edu.muthuselvam.ttt

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlin.random.Random
import android.media.MediaPlayer
import java.lang.Thread.sleep
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {

    // New Game Button
    private lateinit var newGameButton: Button
    private val TAG = "TIP App Activity"

    private lateinit var mHumanMediaPlayer: MediaPlayer
    private lateinit var mComputerMediaPlayer: MediaPlayer

    private lateinit var mPrefs: SharedPreferences


    // Array for the game board
    private var mBoard = charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9')

    // Various text displayed
    private lateinit var mInfoTextView: TextView
    private lateinit var humanWinCount: TextView
    private lateinit var computerWinCount: TextView
    private lateinit var tieCount: TextView

    // Game Variables
    private val BOARD_SIZE = 9
    private val HUMAN_PLAYER = 'X'
    private val COMPUTER_PLAYER = 'O'
    private var turn = 'X'
    private var win = 0
    private var move = -1
    private var humanWins = 0;
    private var computerWins = 0;
    private var tie = 0;

    // Buttons making up the board
    private lateinit var mBoardButtons: Array<Button>


    override fun onCreate(savedInstanceState: Bundle?) {

        mPrefs = getSharedPreferences("savedValues", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Setting a new game")
        Log.d(TAG, "Clearing the board")
        setContentView(R.layout.activity_main)

        //Add Toolbar to app
        var myToolbar: Toolbar? = null
        myToolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(myToolbar)

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
        Log.d(TAG, "Setting starting values")

        // Initialize info label
        mInfoTextView = findViewById(R.id.InforLabel)
        humanWinCount = findViewById(R.id.Label_D)
        computerWinCount = findViewById(R.id.Label_E)
        tieCount = findViewById(R.id.Label_F)


        // Set click listeners for buttons
        for (i in 0..<BOARD_SIZE) {
            mBoardButtons[i].setOnClickListener { HumanMove(i) }
        }

        // Set click listener for the "New Game" button
        newGameButton = findViewById(R.id.reset)
        newGameButton.setOnClickListener { onNewGameClick() }
        Log.d(TAG, "Current state of the board")
        displayBoard()

    }


    fun HumanMove(int: Int) {
        when (int) {
            int -> {
                if (win == 0) {
                    mHumanMediaPlayer.start()

                    if (turn == HUMAN_PLAYER) {
                        turn = COMPUTER_PLAYER
                        mInfoTextView.text = "Computer's Turn"
                        Toast.makeText(
                            this@MainActivity,
                            R.string.Computers_turn,
                            Toast.LENGTH_SHORT
                        ).show()
                        mBoard[int] = HUMAN_PLAYER
                        mBoardButtons[int].text = "X"
                        mBoardButtons[int].setTextColor(Color.rgb(200, 0, 0))
                        Log.d(TAG, "Human selected square$int")
                        displayBoard()
                        win = checkForWinner()
                        showStatus()
                        if (win == 0) {
                            sleep(2000)
                            getComputerMove()
                            turn = HUMAN_PLAYER
                            mInfoTextView.text = "Human's Turn"
                            Toast.makeText(
                                this@MainActivity,
                                R.string.Humans_turn,
                                Toast.LENGTH_SHORT
                            ).show()
                            displayBoard()
                            win = checkForWinner()
                            showStatus()
                        }
                    }
                }
            }

        }
    }


    fun displayBoard() {
        System.out.println();
        System.out.println(mBoard[0] + " | " + mBoard[1] + " | " + mBoard[2]);
        System.out.println("-----------");
        System.out.println(mBoard[3] + " | " + mBoard[4] + " | " + mBoard[5]);
        System.out.println("-----------");
        System.out.println(mBoard[6] + " | " + mBoard[7] + " | " + mBoard[8]);
        System.out.println();

    }

    fun checkForWinner(): Int {
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

    fun showStatus() {
        // Report the winner
        System.out.println();
        if (win == 1) {
            mInfoTextView.setText("It's a tie")
            tie++
            displayScores()
        } else if (win == 2) {
            mInfoTextView.setText(HUMAN_PLAYER + " wins!")
            humanWins++
            displayScores()
        } else if (win == 3) {
            mInfoTextView.setText(COMPUTER_PLAYER + " wins!")
            computerWins++
            displayScores()
        }

    }

    fun onNewGameClick() {

        displayScores()
        mBoard = charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9')
        turn = 'X'
        win = 0

        // Reset the button texts and colors
        for (i in mBoardButtons.indices) {
            mBoardButtons[i].text = ""
            mBoardButtons[i].setTextColor(Color.BLACK)
        }

        // Reset the info label
        mInfoTextView.text = "Game has been reset"

    }

    fun onResetGameClick() {
        humanWins=0
        computerWins=0
        tie=0
        displayScores()
        mBoard = charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9')
        turn = 'X'
        win = 0

        // Reset the button texts and colors
        for (i in mBoardButtons.indices) {
            mBoardButtons[i].text = ""
            mBoardButtons[i].setTextColor(Color.BLACK)
        }

        // Reset the info label
        mInfoTextView.text = "Game has been restarted"

    }


    private fun getComputerMove() {
        var move: Int


        // First see if there's a move O can make to win
        for (i in 0 until BOARD_SIZE) {
            if (mBoard[i] !== HUMAN_PLAYER && mBoard[i] !== COMPUTER_PLAYER) {
                val curr = mBoard[i]
                mBoard[i] = COMPUTER_PLAYER
                if (checkForWinner() == 3) {
                    Log.d(TAG, "Computer is making a winning move to" + (i + 1))
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
                    mBoardButtons[i].text = "O"
                    mBoardButtons[i].setTextColor(Color.rgb(0, 200, 0))
                    Log.d(TAG, "Computer is making a blocking move to" + (i + 1))
                    return
                } else mBoard[i] = curr
            }
        }

        // Generate random move
        do {
            move = Random.nextInt(BOARD_SIZE)
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER)
        Log.d(TAG, "Computer is making a random move to" + (move + 1))
        mBoard[move] = COMPUTER_PLAYER
        mBoardButtons[move].text = "O"
        mBoardButtons[move].setTextColor(Color.rgb(0, 200, 0))

        mComputerMediaPlayer.start()
    }

    fun displayScores() {
        humanWinCount.setText(Integer.toString(humanWins))
        computerWinCount.setText(Integer.toString(computerWins))
        tieCount.setText(Integer.toString(tie))
    }

    override fun onPause() {
        super.onPause()
        val ed = mPrefs.edit()

        mHumanMediaPlayer.release()
        mComputerMediaPlayer.release()

        ed.putInt("mHumanWins", humanWins)
        ed.putInt("mComputerWins", computerWins)
        ed.putInt("mTies", tie)

        //  ed.putString("mBoard$i", mBoard[i].toString())
        for (i in 0 until BOARD_SIZE) {
            // ed.putString("$i", tempBoard[i].toString())
            ed.putString("mBoard$i", mBoard[i].toString())
        }
        ed.apply()

    }

    override fun onResume() {
        super.onResume()

        mHumanMediaPlayer = MediaPlayer.create(applicationContext, R.raw.humansound)
        mComputerMediaPlayer = MediaPlayer.create(applicationContext, R.raw.compsound)
        var block = ""
        humanWins = mPrefs.getInt("mHumanWins", 0)
        computerWins = mPrefs.getInt("mComputerWins", 0)
        tie = mPrefs.getInt("mTies", 0)

        for (i in 0 until BOARD_SIZE) {
            block = mPrefs.getString("mBoard$i", "").toString()


            when (block) {
                "X" -> {
                    mBoardButtons[i].text = "X"
                    mBoardButtons[i].setTextColor(Color.rgb(200, 0, 0))


                }

                "O" -> {
                    mBoardButtons[i].text = "O"
                    mBoardButtons[i].setTextColor(Color.rgb(0, 200, 0))


                }
            }
        }
        displayBoard()
        displayScores()
        // Restore game status
        win = checkForWinner()
        showStatus()

    }

    // Create the Options Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true

    }

    // Handles menu item selections
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about_game -> {
                showCustomAlert()
                return true
            }
            R.id.new_game -> {
                Log.d(TAG,"The restart menu item was clicked")
                onResetGameClick()
                return true
            }
            R.id.quit -> {
                finish()
                return true
            }
        }
        return false
    }
    fun showCustomAlert() {
        val dialogView = layoutInflater.inflate(R.layout.about_layout, null)
        val customDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .show()
        val btDismiss = dialogView.findViewById<Button>(R.id.btDismissCustomDialog)
        btDismiss.setOnClickListener {
            customDialog.dismiss()
        }
    }

}

package es.iessaladillo.pedrojoya.stroop.ui.game

import android.os.Handler
import androidx.lifecycle.ViewModel
import kotlin.concurrent.thread


class GameViewModel(
    // TODO
) : ViewModel() {

    @Volatile
    private var isGameFinished: Boolean = false
    @Volatile
    private var currentWordMillis: Int = 0
    @Volatile
    private var millisUntilFinished: Int = 0
    private val handler: Handler = Handler()

    // TODO

    private fun onGameTimeTick(millisUntilFinished: Int) {
        // TODO
    }

    private fun onGameTimeFinish() {
        isGameFinished = true
        // TODO
    }

    fun nextWord() {
        // TODO
    }

    fun checkRight() {
        currentWordMillis = 0
        // TODO
    }

    fun checkWrong() {
        currentWordMillis = 0
        // TODO
    }

    fun startGameThread(gameTime: Int, wordTime: Int) {
        millisUntilFinished = gameTime
        currentWordMillis = 0
        isGameFinished = false
        val checkTimeMillis: Int = wordTime / 2
        thread {
            try {
                while (!isGameFinished) {
                    Thread.sleep(checkTimeMillis.toLong())
                    // Check and publish on main thread.
                    handler.post {
                        if (!isGameFinished) {
                            if (currentWordMillis >= wordTime) {
                                currentWordMillis = 0
                                nextWord()
                            }
                            currentWordMillis += checkTimeMillis
                            millisUntilFinished -= checkTimeMillis
                            onGameTimeTick(millisUntilFinished)
                            if (millisUntilFinished <= 0) {
                                onGameTimeFinish()
                            }
                        }
                    }
                }
            } catch (ignored: Exception) {
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        isGameFinished = true
    }

}
package es.iessaladillo.pedrojoya.stroop.ui.game

import android.app.Application
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.dao.GameDao
import es.iessaladillo.pedrojoya.stroop.data.dao.PlayerDao
import es.iessaladillo.pedrojoya.stroop.data.entity.Game
import es.iessaladillo.pedrojoya.stroop.data.entity.Player
import kotlin.concurrent.thread
import kotlin.random.Random


class GameViewModel(
    val gameDao: GameDao,
    val playerDao: PlayerDao,
    val application: Application
) : ViewModel() {

    @Volatile
    private var isGameFinished: Boolean = false
    @Volatile
    private var currentWordMillis: Int = 0
    @Volatile
    private var millisUntilFinished: Int = 0
    private val handler: Handler = Handler()

    val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    val colorList = listOf(Color.BLACK, Color.GRAY, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA)
    val wordList = listOf("Black", "Gray", "Red", "Green", "Blue", "Yellow", "Cyan", "Magenta")
    val word: MutableLiveData<String> = MutableLiveData(wordList.get(Random.nextInt(8)))
    val color: MutableLiveData<Int> = MutableLiveData(colorList.get(Random.nextInt(8)))
    val numWords: MutableLiveData<Int> = MutableLiveData(0)
    val numCorrects: MutableLiveData<Int> = MutableLiveData(0)
    val numIncorrects: MutableLiveData<Int> = MutableLiveData(0)
    val time: MutableLiveData<Int> = MutableLiveData(millisUntilFinished)
    val points: MutableLiveData<Int> = MutableLiveData(0)
    val attemptsRemaining: MutableLiveData<Int> = MutableLiveData()
    var wordTime: Int = 0
    val gameMode: String
    var gameTime: Int = 0
    var currentPlayerId: Long = 0
    val player: Player by lazy { getCurrentPlayer() }
    private fun getCurrentPlayer(): Player {
        return playerDao.queryPlayer(currentPlayerId)
    }
    val goToResult: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        currentPlayerId = settings.getLong("currentPlayer", -1)
        attemptsRemaining.value = settings.getString(
            application.getString(R.string.prefAttempts_key),
            application.getString(R.string.prefAttempts_defaultValue)
        )!!.toInt()
        wordTime = settings.getString(
            application.getString(R.string.prefWordTime_key),
            application.getString(R.string.prefWordTime_value_2000)
        )!!.toInt()
        gameMode = settings.getString(
            application.getString(R.string.prefGameMode_key),
            application.getString(R.string.prefGameMode_defaultValue)
        )!!
        gameTime = settings.getString(
            application.getString(R.string.prefGameTime_key),
            application.getString(R.string.prefGameTime_defaultValue)
        )!!.toInt()

    }

    private fun onGameTimeTick(millisUntilFinished: Int) {
        time.value = millisUntilFinished
    }

    private fun onGameTimeFinish() {
        isGameFinished = true
        insertGameIntoDatabase()
        navigateToResults()
    }

    private fun navigateToResults() {
        goToResult.value = true
    }

    private fun insertGameIntoDatabase() {
        val game = Game(
            0,
            player,
            gameMode,
            numWords.value!!,
            gameTime,
            points.value!!,
            numCorrects.value!!
        )
        gameDao.insertGame(game)
    }

    fun checkRight() {
        currentWordMillis = 0

        val currentWord = wordList.indexOf(word.value)
        val currentColor = colorList.indexOf(color.value)

        if (currentWord == currentColor) {
            numCorrects.value = numCorrects.value?.plus(1)
            points.value = points.value?.plus(10)
        } else {
            numIncorrects.value = numIncorrects.value?.plus(1)
            attemptsRemaining.value = attemptsRemaining.value?.minus(1)
        }
        whatsNext()
    }

    fun checkWrong() {
        currentWordMillis = 0
        val currentWord = wordList.indexOf(word.value)
        val currentColor = colorList.indexOf(color.value)

        if (currentWord != currentColor) {
            numCorrects.value = numCorrects.value?.plus(1)
            points.value = points.value?.plus(10)
        } else {
            numIncorrects.value = numIncorrects.value?.plus(1)
            attemptsRemaining.value = attemptsRemaining.value?.minus(1)
        }

        whatsNext()
    }

    private fun whatsNext() {
        if (attemptsRemaining.value!! < 1 && gameMode == "Attempts") {
            onGameTimeFinish()
        } else {
            nextWord()
        }
    }

    fun nextWord() {
        word.value = wordList.get(Random.nextInt(8))
        color.value = colorList.get(Random.nextInt(8))
        numWords.value = numWords.value?.plus(1)
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
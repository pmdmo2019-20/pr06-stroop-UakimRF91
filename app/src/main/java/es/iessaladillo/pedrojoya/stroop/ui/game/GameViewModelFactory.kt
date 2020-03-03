package es.iessaladillo.pedrojoya.stroop.ui.game

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.iessaladillo.pedrojoya.stroop.data.dao.GameDao
import es.iessaladillo.pedrojoya.stroop.data.dao.PlayerDao

class GameViewModelFactory(private val gameDao: GameDao, private val playerDao: PlayerDao, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            GameViewModel(this.gameDao, this.playerDao, this.application) as T
        } else {
            throw IllegalArgumentException("Error")
        }
    }
}
package es.iessaladillo.pedrojoya.stroop.ui.player

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import es.iessaladillo.pedrojoya.stroop.data.dao.PlayerDao
import es.iessaladillo.pedrojoya.stroop.data.entity.Player

class PlayerViewModel(
    private val playerDao: PlayerDao,
    private val application: Application
) : ViewModel() {

    val players: LiveData<List<Player>> = queryAllPlayers()
    val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    private val _currentPlayerId: MutableLiveData<Long> = MutableLiveData()
    val currentPlayerId: LiveData<Long>
        get() = _currentPlayerId

    private val _currentPlayer: MutableLiveData<Player> = MutableLiveData()
    val currentPlayer: LiveData<Player>
        get() = _currentPlayer

    init {
        _currentPlayerId.value = settings.getLong("currentPlayer", -1)
    }

    fun queryAllPlayers(): LiveData<List<Player>> {
        return playerDao.queryAllPlayers()
    }

    fun queryPlayer(playerId: Long) = playerDao.queryPlayer(playerId)


    fun setCurrentplayerId(playerId: Long) {
        _currentPlayerId.value = playerId
    }

    fun isPlayerSelected() : Boolean {
        return playerDao.existsPlayerWithThisId(getCurrentPlayerId()) != 0
    }

    fun getCurrentPlayerId() : Long {
        return settings.getLong("currentPlayer", -1)
    }
}
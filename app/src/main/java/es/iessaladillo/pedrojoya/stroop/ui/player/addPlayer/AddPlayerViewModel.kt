package es.iessaladillo.pedrojoya.stroop.ui.player.addPlayer

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.iessaladillo.pedrojoya.stroop.base.Event
import es.iessaladillo.pedrojoya.stroop.data.dao.PlayerDao
import es.iessaladillo.pedrojoya.stroop.data.entity.Player
import kotlin.concurrent.thread

class AddPlayerViewModel(val playerDao: PlayerDao, val application: Application) : ViewModel() {

    private val _onBack: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val onBack: LiveData<Event<Boolean>> get() = _onBack

    private val _message: MutableLiveData<Event<String>> = MutableLiveData()
    val message: LiveData<Event<String>> get() = _message

    private val _currentPlayerId: MutableLiveData<Long> = MutableLiveData()
    val currentPlayerId: LiveData<Long>
        get() = _currentPlayerId


    fun addPlayer(playerName: String, playerImg: Int) {
        thread {
            try {
                playerDao.insertPlayer(Player(0, playerName, playerImg))
                _onBack.postValue(Event(true))
            } catch (e: Exception) {
                _message.postValue(Event("Each player name must be unique"))
            }
        }
    }

    fun setCurrentPlayerId(playerId: Long) {
        _currentPlayerId.value = playerId
    }


}

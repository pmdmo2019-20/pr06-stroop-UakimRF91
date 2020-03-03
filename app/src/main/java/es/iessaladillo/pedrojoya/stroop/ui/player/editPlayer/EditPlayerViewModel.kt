package es.iessaladillo.pedrojoya.stroop.ui.player.editPlayer

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.iessaladillo.pedrojoya.stroop.base.Event
import es.iessaladillo.pedrojoya.stroop.data.dao.PlayerDao
import es.iessaladillo.pedrojoya.stroop.data.entity.Player
import java.lang.Exception
import kotlin.concurrent.thread

class EditPlayerViewModel(private val playerDao: PlayerDao, val application: Application) : ViewModel() {

    private val _onBack: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val onBack: LiveData<Event<Boolean>>
        get() = _onBack

    private val _message: MutableLiveData<Event<String>> = MutableLiveData()
    val message: LiveData<Event<String>>
        get() = _message

    private val _currentPlayerAvatar: MutableLiveData<Int> = MutableLiveData()
    val currentPlayerAvatar: LiveData<Int>
        get() = _currentPlayerAvatar

    fun updatePlayer(playerId: Long, playerName: String, playerImg: Int) {
        thread {
            try {
                playerDao.updatePlayer(Player(playerId, playerName, playerImg))
                _onBack.postValue(Event(true))

            } catch (e: Exception) {
                _message.postValue(Event("ERROR"))
            }
        }
    }

    fun queryPlayer(playerId: Long): Player {
        return playerDao.queryPlayer(playerId)
    }

    fun setCurrentPlayerAvatar(avatar: Int) {
        _currentPlayerAvatar.value = avatar
    }
}
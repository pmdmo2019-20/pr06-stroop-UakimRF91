package es.iessaladillo.pedrojoya.stroop.ui.player

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.iessaladillo.pedrojoya.stroop.data.dao.PlayerDao

class PlayerViewModelFactory (private val repository: PlayerDao, private val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PlayerViewModel::class.java!!)) {
            PlayerViewModel(this.repository, this.application) as T
        } else {
            throw IllegalArgumentException("Error")
        }
    }

}
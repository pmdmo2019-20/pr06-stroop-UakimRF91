package es.iessaladillo.pedrojoya.stroop.ui.player.addPlayer

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.iessaladillo.pedrojoya.stroop.data.dao.PlayerDao

class AddPlayerViewModelFactory  (private val repository: PlayerDao, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddPlayerViewModel::class.java)) {
            AddPlayerViewModel(this.repository, this.application) as T
        } else {
            throw IllegalArgumentException("Error")
        }
    }
}
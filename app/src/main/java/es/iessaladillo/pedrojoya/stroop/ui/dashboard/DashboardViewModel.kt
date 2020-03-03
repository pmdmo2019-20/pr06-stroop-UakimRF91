package es.iessaladillo.pedrojoya.stroop.ui.dashboard

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import es.iessaladillo.pedrojoya.stroop.data.dao.PlayerDao

class DashboardViewModel(val playerDao: PlayerDao, val application: Application) : ViewModel() {

    //Acceso a shared preferences
    val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    //El id del jugador actual que tenemos en shared preferences
    fun getCurrentPlayerId() : Long {
        return settings.getLong("currentPlayer", -1)
    }

    //El resId de la imagen del jugador actual
    fun getImagePlayerResId() : Int {
        return playerDao.queryPlayer(getCurrentPlayerId()).imageId
    }

    //El nombre del jugador actual
    fun getNamePlayer() : String {
        return playerDao.queryPlayer(getCurrentPlayerId()).playerName
    }

    //Devuelve si hay alguien con un id correspondiente al del jugador actual de shared preferences
    fun isPlayerSelected() : Boolean {
        return playerDao.existsPlayerWithThisId(getCurrentPlayerId()) != 0
    }


}

package es.iessaladillo.pedrojoya.stroop.ui.ranking

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.dao.GameDao
import es.iessaladillo.pedrojoya.stroop.data.entity.Game

class RankingViewModel (val gameDao: GameDao, val application: Application) : ViewModel() {

    val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }
    var onItemClickListener: ((Int) -> Unit)? = null
    val mode: MutableLiveData<String> = MutableLiveData()

    private val _ranking: MutableLiveData<List<Game>> = MutableLiveData()
    val ranking: LiveData<List<Game>>
        get() = _ranking

    init {
        mode.value = settings.getString(
            application.getString(R.string.prefRankingFilter_key),
            application.getString(R.string.prefRankingFilter_defaultValue)
        )
    }

    fun queryAllGames() {
        _ranking.value = gameDao.queryAllGames()
    }

    fun queryAttemptsGames() {
        _ranking.value = gameDao.queryAttemptsGames()
    }

    fun queryTimeGames() {
        _ranking.value = gameDao.queryTimeGames()
    }

    fun queryRankings() {
        when (mode.value) {
            "All" -> {
                queryAllGames()
            }
            "Attempts" -> {
                queryAttemptsGames()
            }
            "Time" -> {
                queryTimeGames()
            }
        }
    }
}

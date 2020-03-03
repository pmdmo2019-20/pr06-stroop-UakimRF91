package es.iessaladillo.pedrojoya.stroop.ui.ranking.rankingDetail

import android.app.Application
import androidx.lifecycle.ViewModel
import es.iessaladillo.pedrojoya.stroop.data.dao.GameDao
import es.iessaladillo.pedrojoya.stroop.data.entity.Game


class RankingDetailViewModel(private val playerDao: GameDao, val application: Application) : ViewModel()  {

    fun queryRanking(idRanking: Long) : Game {
        return playerDao.queryGame(idRanking)
    }

}
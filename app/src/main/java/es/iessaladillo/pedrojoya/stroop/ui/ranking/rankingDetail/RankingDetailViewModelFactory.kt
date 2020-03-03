package es.iessaladillo.pedrojoya.stroop.ui.ranking.rankingDetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.iessaladillo.pedrojoya.stroop.data.dao.GameDao

class RankingDetailViewModelFactory(
    private val repository: GameDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RankingDetailViewModel::class.java)) {
            RankingDetailViewModel(this.repository, this.application) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
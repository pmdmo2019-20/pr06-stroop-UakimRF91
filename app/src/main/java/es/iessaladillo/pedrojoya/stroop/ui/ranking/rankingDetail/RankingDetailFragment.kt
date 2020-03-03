package es.iessaladillo.pedrojoya.stroop.ui.ranking.rankingDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.OnToolbarAvailableListener
import es.iessaladillo.pedrojoya.stroop.data.StroopDatabase
import es.iessaladillo.pedrojoya.stroop.data.entity.Game
import kotlinx.android.synthetic.main.result_fragment.*

class RankingDetailFragment : Fragment(R.layout.result_fragment) {

    private val rankingId: Long by lazy {
        requireArguments().getLong(getString(R.string.RANKING_ID_ARGS))
    }

    private val viewModel: RankingDetailViewModel by viewModels {
        RankingDetailViewModelFactory(
            StroopDatabase.getInstance(requireContext()).gameDao,
            requireActivity().application
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAppBar()
        setupViews()
    }

    private fun setupViews() {
        val game : Game = viewModel.queryRanking(rankingId)
        imgActualPlayer.setImageResource(game.player.imageId)
        lblPlayerName.text = game.player.playerName
        lblNumCorrects.text = game.numCorrects.toString()
        lblNumIncorrects.text = (game.words - game.numCorrects).toString()
        lblPoints.text = game.points.toString()
    }

    private fun setupAppBar() {
        toolbar.inflateMenu(R.menu.no_icon_menu)
        (requireActivity() as OnToolbarAvailableListener).onToolbarCreated(toolbar)
    }

}

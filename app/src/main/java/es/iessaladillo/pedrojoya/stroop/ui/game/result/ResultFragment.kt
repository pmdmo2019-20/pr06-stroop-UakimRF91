package es.iessaladillo.pedrojoya.stroop.ui.game.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.OnToolbarAvailableListener
import kotlinx.android.synthetic.main.result_fragment.*


class ResultFragment : Fragment(R.layout.result_fragment) {

    private val avatarId: Int by lazy {
        requireArguments().getInt(getString(R.string.GAME_AVATAR_ID_PLAYER))
    }
    private val playerName: String? by lazy {
        requireArguments().getString(getString(R.string.GAME_NAME_PLAYER))
    }
    private val numCorrect: Int by lazy {
        requireArguments().getInt(getString(R.string.GAME_NUM_CORRECT))
    }
    private val numIncorrect: Int by lazy {
        requireArguments().getInt(getString(R.string.GAME_NUM_INCORRECT))
    }
    private val points: Int by lazy {
        requireArguments().getInt(getString(R.string.GAME_POINTS))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()

    }

    private fun setupViews() {
        setupAppBar()
        imgActualPlayer.setImageResource(avatarId)
        lblPlayerName.text = playerName
        lblNumCorrects.text = numCorrect.toString()
        lblNumIncorrects.text = numIncorrect.toString()
        lblPoints.text = points.toString()
    }

    private fun setupAppBar() {
        toolbar.inflateMenu(R.menu.no_icon_menu)
        (requireActivity() as OnToolbarAvailableListener).onToolbarCreated(toolbar)
    }

}

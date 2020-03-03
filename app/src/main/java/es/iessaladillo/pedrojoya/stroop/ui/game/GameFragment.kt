package es.iessaladillo.pedrojoya.stroop.ui.game

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController

import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.StroopDatabase
import kotlinx.android.synthetic.main.game_fragment.*


class GameFragment : Fragment(R.layout.game_fragment) {

    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(
            StroopDatabase.getInstance(this.requireContext()).gameDao,
            StroopDatabase.getInstance(this.requireContext()).playerDao,
            requireActivity().application
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeGame()
        setupViews()
    }

    private fun setupViews() {
        setupBtns()
        observeViewModels()
    }

    private fun observeViewModels() {
        viewModel.run {
            word.observe(viewLifecycleOwner) {
                lblWord.text = it
            }
            color.observe(viewLifecycleOwner) {
                lblWord.setTextColor(it)
            }
            numWords.observe(viewLifecycleOwner) {
                lblNumWords.text = it.toString()
            }
            numCorrects.observe(viewLifecycleOwner) {
                lblNumCorrects.text = it.toString()
            }
            points.observe(viewLifecycleOwner) {
                lblPoints.text = it.toString()
            }
            time.observe(viewLifecycleOwner) {
                progressBar.progress = it
            }
            goToResult.observe(viewLifecycleOwner) {
                if (it) {
                    navigateToResult()
                }
            }
        }
    }

    private fun navigateToResult() {
        findNavController().navigate(R.id.navigateToResultDestination, bundleOf(
            getString(R.string.GAME_AVATAR_ID_PLAYER) to viewModel.player.imageId,
            getString(R.string.GAME_NAME_PLAYER) to viewModel.player.playerName,
            getString(R.string.GAME_NUM_CORRECT) to viewModel.numCorrects.value!!,
            getString(R.string.GAME_NUM_INCORRECT) to viewModel.numIncorrects.value!!,
            getString(R.string.GAME_POINTS) to viewModel.points.value!!
        ))
    }

    private fun initializeGame() {
        progressBar.max = viewModel.gameTime
        progressBar.progress = viewModel.gameTime
        viewModel.startGameThread(viewModel.gameTime, viewModel.wordTime)
    }


    private fun setupBtns() {
        imgRight.setOnClickListener {
            viewModel.checkRight()
            //lblPointsLabel.text = viewModel.player.playerName
        }

        imgWrong.setOnClickListener {
            viewModel.checkWrong()
        }
    }


}

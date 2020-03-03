package es.iessaladillo.pedrojoya.stroop.ui.player

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.OnToolbarAvailableListener
import es.iessaladillo.pedrojoya.stroop.data.StroopDatabase
import es.iessaladillo.pedrojoya.stroop.data.entity.Player
import es.iessaladillo.pedrojoya.stroop.extensions.invisibleUnless
import es.iessaladillo.pedrojoya.stroop.ui.dialogs.about.AboutDialog
import kotlinx.android.synthetic.main.player_fragment.*

class PlayerFragment : Fragment(R.layout.player_fragment) {

    private lateinit var playerAdapter: PlayerFragmentAdapter
    val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }
    private val viewmodel: PlayerViewModel by viewModels {
        PlayerViewModelFactory(
            StroopDatabase.getInstance
                (this.requireContext()).playerDao, requireActivity().application
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupBtn()
        setupAppbar()
        setupPlayerAdapter()
        setupRecyclerView()
        observeLiveData()
        setupCurrentPlayer()
    }

    private fun setupBtn() {
        fabAddPlayer.setOnClickListener { navigateToAddPlayer() }
        imgNoPlayer.setOnClickListener { navigateToAddPlayer() }
        lblNoPlayers.setOnClickListener { navigateToAddPlayer() }
        btnEdit.setOnClickListener { navigateToEdit(viewmodel.currentPlayerId.value!!) }
    }

    private fun setupAppbar() {
        toolbar.inflateMenu(R.menu.help_menu)
        (requireActivity() as OnToolbarAvailableListener).onToolbarCreated(toolbar)
        setupHelpButton()
    }

    private fun setupHelpButton() {
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.help) {
                showConfirmationDialog()
            }
            true
        }
    }

    private fun showConfirmationDialog() {
        AboutDialog(
            getString(R.string.player_selection_title),
            getString(R.string.player_selection_help_description)
        ).show(requireFragmentManager(), "AboutDialog")
    }

    private fun setupPlayerAdapter() {
        playerAdapter = PlayerFragmentAdapter(requireContext()).also {
            it.onItemClickListener = {
                selectCurrentPlayer(it)
            }
        }
    }

    private fun selectCurrentPlayer(position: Int) {
        settings.edit {
            putLong("currentPlayer", playerAdapter.playerList[position].playerId)
        }
        viewmodel.setCurrentplayerId(playerAdapter.playerList[position].playerId)
    }

    @SuppressLint("ResourceType")
    private fun setupRecyclerView() {
        lstPlayers.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(
                activity,
                getString(R.integer.player_selection_numColumns).toInt()
            )
            adapter = playerAdapter
        }
    }

    private fun observeLiveData() {
        viewmodel.players.observe(viewLifecycleOwner) {
            checkIfNoPlayersDisappears(it.size)
            showPlayers(it)
        }
    }

    private fun checkIfNoPlayersDisappears(numPlayers: Int) {
        imgNoPlayer.invisibleUnless(numPlayers > 0)
        lblNoPlayers.invisibleUnless(numPlayers > 0)
    }

    private fun showPlayers(players: List<Player>) {
        lstPlayers.post {
            playerAdapter.submitList(players)
        }

    }

    private fun setupCurrentPlayer() {
        viewmodel.currentPlayerId.observe(viewLifecycleOwner) {
            if (viewmodel.isPlayerSelected()) {
                val player = viewmodel.queryPlayer(it)
                lblActualPlayer.text = player.playerName
                imgActualPlayer.setImageResource(player.imageId)
                btnEdit.visibility = View.VISIBLE
            } else {
                lblActualPlayer.text = getString(R.string.player_selection_no_player_selected)
                imgActualPlayer.setImageResource(R.drawable.logo)
            }
        }
    }

    private fun navigateToAddPlayer() {
        findNavController().navigate(R.id.navigateToAddPlayer)
    }

    private fun navigateToEdit(playerId: Long) {
        findNavController().navigate(
            R.id.navigateToEditPlayer,
            bundleOf(getString(R.string.PLAYER_ID_ARGS) to playerId)
        )
    }

}

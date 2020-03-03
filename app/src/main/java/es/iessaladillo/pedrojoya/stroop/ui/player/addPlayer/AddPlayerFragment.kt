package es.iessaladillo.pedrojoya.stroop.ui.player.addPlayer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.avatars
import es.iessaladillo.pedrojoya.stroop.base.OnToolbarAvailableListener
import es.iessaladillo.pedrojoya.stroop.base.observeEvent
import es.iessaladillo.pedrojoya.stroop.data.StroopDatabase
import es.iessaladillo.pedrojoya.stroop.extensions.hideSoftKeyboard
import es.iessaladillo.pedrojoya.stroop.ui.dialogs.about.AboutDialog
import kotlinx.android.synthetic.main.add_player_fragment.*

class AddPlayerFragment : Fragment(R.layout.add_player_fragment) {

    private lateinit var addPlayerAdapter: AddPlayerFragmentAdapter

    private val viewModel: AddPlayerViewModel by viewModels {
        AddPlayerViewModelFactory(
            StroopDatabase.getInstance(this.requireContext()).playerDao,
            requireActivity().application
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupAppbar()
        setupAdapter()
        setupRecyclerView()
        loadPlayers(avatars)
        fabSave.setOnClickListener { save() }
        observeEvents()


    }

    private fun observeEvents() {
        viewModel.message.observeEvent(this) {
            Snackbar.make(lstAvatars, it, Snackbar.LENGTH_LONG).show()
        }
        viewModel.onBack.observeEvent(this){
            if(it){
                requireActivity().onBackPressed()
            }
        }
        viewModel.currentPlayerId.observe(viewLifecycleOwner){
            imgAddActualPlayer.setImageResource(it.toInt())
        }
    }

    private fun save() {
        if (!lblActualPlayerAdd.text.toString().isBlank()) {
            if (viewModel.currentPlayerId.value != null) {
                viewModel.addPlayer(lblActualPlayerAdd.text.toString(), viewModel.currentPlayerId.value!!.toInt())
                lblActualPlayerAdd.hideSoftKeyboard()
            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.player_edition_no_avatars_yet),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.player_edition_no_nickname_yet),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun loadPlayers(avatars: List<Int>) {
        addPlayerAdapter.submitList(avatars)
    }


    private fun setupAppbar() {
        toolbarAdd.inflateMenu(R.menu.help_menu)
        (requireActivity() as OnToolbarAvailableListener).onToolbarCreated(toolbarAdd)
        setupHelpButton()
    }

    private fun setupHelpButton() {
        toolbarAdd.setOnMenuItemClickListener {
            if (it.itemId == R.id.help) {
                showConfirmationDialog()
            }
            true
        }
    }

    private fun showConfirmationDialog() {
        AboutDialog(
            getString(R.string.player_creation_title),
            getString(R.string.player_creation_help_description)
        ).show(requireFragmentManager(), "AboutDialog")
    }

    @SuppressLint("ResourceType")
    private fun setupRecyclerView() {
        lstAvatars.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, getString(R.integer.player_creation_numColumns).toInt())
            adapter = addPlayerAdapter
        }
    }

    private fun setupAdapter() {
        addPlayerAdapter = AddPlayerFragmentAdapter()
            .also {
                it.onItemClickListener = { position -> selectAvatar(position) }
            }
    }

    private fun selectAvatar(position: Int) {
        viewModel.setCurrentPlayerId(avatars[position].toLong())
    }
}
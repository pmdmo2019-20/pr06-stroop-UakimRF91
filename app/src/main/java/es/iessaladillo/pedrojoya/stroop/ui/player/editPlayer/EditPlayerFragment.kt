package es.iessaladillo.pedrojoya.stroop.ui.player.editPlayer


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
import es.iessaladillo.pedrojoya.stroop.ui.dialogs.delete.DeleteDialog
import kotlinx.android.synthetic.main.edit_player_fragment.*

class EditPlayerFragment : Fragment(R.layout.edit_player_fragment) {

    private lateinit var playerEditFragment: EditPlayerFragmentAdapter

    private val viewmodel: EditPlayerViewModel by viewModels {
        EditPlayerViewModelFactory(
            StroopDatabase.getInstance(this.requireContext()).playerDao,
            requireActivity().application
        )
    }
    private val playerId: Long by lazy {
        requireArguments().getLong(getString(R.string.PLAYER_ID_ARGS))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
        viewmodel.setCurrentPlayerAvatar(avatars.indexOf(viewmodel.queryPlayer(playerId).imageId))
    }

    private fun setupViews() {
        setupAppbar()
        setupAdapter()
        setupRecyclerView()
        showPlayers(avatars)
        setupLbl()
        fabSave.setOnClickListener { save() }
        observeEvents()


    }

    private fun setupLbl() {
        imgEditActualPlayer.setImageResource(viewmodel.queryPlayer(playerId).imageId)
        lblActualPlayerEdit.setText(viewmodel.queryPlayer(playerId).playerName)
    }

    private fun observeEvents() {
        viewmodel.message.observeEvent(this) {
            Snackbar.make(lstAvatarEdit, it, Snackbar.LENGTH_SHORT).show()
        }
        viewmodel.onBack.observeEvent(this) {
            if (it) {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun save() {
        viewmodel.currentPlayerAvatar.observe(viewLifecycleOwner) {
            if (lblActualPlayerEdit.text.isBlank()) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.player_edition_no_nickname_yet),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                viewmodel.updatePlayer(
                    playerId,
                    lblActualPlayerEdit.text.toString(),
                    avatars[viewmodel.currentPlayerAvatar.value!!]
                )
                lblActualPlayerEdit.hideSoftKeyboard()
            }
        }
    }

    private fun showPlayers(avatars: List<Int>) {
        playerEditFragment.submitList(avatars)
    }

    private fun setupAppbar() {
        toolbarEdit.inflateMenu(R.menu.help_delete_menu)
        (requireActivity() as OnToolbarAvailableListener).onToolbarCreated(toolbarEdit)
        setupButtons()
    }

    private fun setupButtons() {
        setupHelpButton()
        setupDeleteButton()
    }

    private fun setupDeleteButton() {
        toolbarEdit.setOnMenuItemClickListener {
            if (it.itemId == R.id.delete) {
                showDeletionDialog()
            }
            true
        }
    }

    private fun setupHelpButton() {
        toolbarEdit.setOnMenuItemClickListener {
            if (it.itemId == R.id.help) {
                showConfirmationDialog()

            }
            true
        }
    }

    private fun showDeletionDialog() {
        DeleteDialog(
            getString(R.string.delete_title),
            getString(R.string.player_deletion_message),
            StroopDatabase.getInstance(requireContext()).playerDao
        ).show(requireFragmentManager(), "DeleteDialog")
    }

    private fun showConfirmationDialog() {
        AboutDialog(
            getString(R.string.player_edition_title),
            getString(R.string.player_edition_help_description)
        ).show(requireFragmentManager(), "AboutDialog")
    }

    @SuppressLint("ResourceType")
    private fun setupRecyclerView() {
        lstAvatarEdit.run {
            setHasFixedSize(true)
            layoutManager =
                GridLayoutManager(activity, getString(R.integer.player_edition_numColumns).toInt())
            adapter = playerEditFragment
        }
    }

    private fun setupAdapter() {
        playerEditFragment = EditPlayerFragmentAdapter()
            .also {
                it.onItemClickListener = { position -> selectAvatar(position) }
            }
    }

    private fun selectAvatar(position: Int) {
        viewmodel.setCurrentPlayerAvatar(position)
        viewmodel.currentPlayerAvatar.observe(viewLifecycleOwner) {
            imgEditActualPlayer.setImageResource(avatars[it])
        }
        //lstAvatars[position].viewCheck.visibility = View.VISIBLE
    }
}

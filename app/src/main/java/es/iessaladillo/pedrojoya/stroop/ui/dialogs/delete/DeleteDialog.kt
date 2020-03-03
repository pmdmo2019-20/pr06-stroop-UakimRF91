package es.iessaladillo.pedrojoya.stroop.ui.dialogs.delete

import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.dao.PlayerDao
import es.iessaladillo.pedrojoya.stroop.data.entity.Player
import es.iessaladillo.pedrojoya.stroop.ui.main.MainActivity
import kotlin.concurrent.thread

class DeleteDialog(val title: String, val message: String, val playerDao: PlayerDao) : DialogFragment() {

    val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.player_deletion_yes)) { _, _ ->
                deleteCurrentPlayer()
                navigateToDashboard()
            }
            .setNegativeButton(getString(R.string.player_deletion_no)) { _, _ -> }
            .create()

    private fun navigateToDashboard() {
        (requireActivity() as MainActivity).navController.navigateUp()
        (requireActivity() as MainActivity).navController.navigateUp()
    }

    private fun deleteCurrentPlayer() {
        playerDao.deletePlayer(getCurrentPlayer())
    }

    private fun getCurrentPlayer(): Player {
        return playerDao.queryPlayer(getCurrentPlayerId())
    }

    fun getCurrentPlayerId() : Long {
        return settings.getLong("currentPlayer", -1)
    }

}
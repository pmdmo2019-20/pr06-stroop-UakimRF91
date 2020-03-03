package es.iessaladillo.pedrojoya.stroop.ui.settings

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.OnToolbarAvailableListener
import es.iessaladillo.pedrojoya.stroop.ui.dialogs.about.AboutDialog
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment: Fragment(R.layout.settings_fragment) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupAppbar()
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
            getString(R.string.settings_title),
            getString(R.string.settings_help_description)
        ).show(requireFragmentManager(), "AboutDialog")
    }
}
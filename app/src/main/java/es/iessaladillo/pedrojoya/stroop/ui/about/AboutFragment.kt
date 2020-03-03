package es.iessaladillo.pedrojoya.stroop.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment

import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.OnToolbarAvailableListener
import es.iessaladillo.pedrojoya.stroop.ui.dialogs.about.AboutDialog
import kotlinx.android.synthetic.main.about_fragment.*

class AboutFragment : Fragment(R.layout.about_fragment) {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAppBar()
    }

    private fun setupAppBar() {
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
            getString(R.string.about_title),
            getString(R.string.assistant_aboutDescription)
        ).show(requireFragmentManager(), "AboutDialog")
    }
}

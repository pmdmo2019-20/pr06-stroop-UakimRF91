package es.iessaladillo.pedrojoya.stroop.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import es.iessaladillo.pedrojoya.stroop.R

class SettingsListFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences,rootKey)
    }




}

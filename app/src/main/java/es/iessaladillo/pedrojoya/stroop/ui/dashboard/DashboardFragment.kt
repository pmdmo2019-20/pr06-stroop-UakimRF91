package es.iessaladillo.pedrojoya.stroop.ui.dashboard

import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.OnToolbarAvailableListener
import es.iessaladillo.pedrojoya.stroop.data.StroopDatabase
import es.iessaladillo.pedrojoya.stroop.data.entity.DashOption
import es.iessaladillo.pedrojoya.stroop.ui.dialogs.about.AboutDialog
import kotlinx.android.synthetic.main.dashboard_fragment.*

class DashboardFragment : Fragment(R.layout.dashboard_fragment) {


    private lateinit var dashboardAdapter: DashboardFragmentAdapter

    private val viewModel: DashboardViewModel by viewModels {
        DashboardViewModelFactory(
            StroopDatabase.getInstance(requireContext()).playerDao,
            requireActivity().application
        )
    }

    val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupDashboardAdapter()
        setupRecyclerView()
        setupAppBar()
        loadPlayer()
    }

    //Indicamos qué hace el botón help de la toolbar
    private fun setupHelpButton() {
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.help) {
                showConfirmationDialog()
            }
            true
        }
    }

    //Diálogo que se muestra a pulsar botón help
    //Se crea un nuevo AboutDialog y se le pasa el texto por parámetros
    private fun showConfirmationDialog() {
        AboutDialog(
            getString(R.string.dashboard_title),
            getString(R.string.dashboard_help_description)
        ).show(requireFragmentManager(), "AboutDialog")
    }

    //Para cargar un jugador en el dashboard
    /*
    En el viewmodel comprobamos si el id que él tiene como jugador actual existe (-1 si no tiene ninguno)
    Si tiene un id válido de un jugador, obtenemos su imagen y nombre, y las mostramos
    Si no tiene ningún id válido, se muestra el que está por defecto
    Ponemos -1 por si es un player eliminado, de tal manera que al entrar en juego no juegue y redirija a player.
     */
    private fun loadPlayer() {
        if (viewModel.isPlayerSelected()) {
            imgCurrentPlayer.setImageResource(viewModel.getImagePlayerResId())
            lblCurrentPlayerName.text = viewModel.getNamePlayer()
        } else {
            settings.edit {
                putLong("currentPlayer", -1L)
            }
        }
    }

    //Inflamos la Appbar e inicializamos el botón help
    private fun setupAppBar() {
        toolbar.inflateMenu(R.menu.help_menu)
        (requireActivity() as OnToolbarAvailableListener).onToolbarCreated(toolbar)
        setupHelpButton()
    }

    //Inicializamos el adaptador para el recyclerview de dashboard
    /*
     Primero cargamos todos los elementos a mostrar en la lista
     A continuación creamos el objeto adaptador pasándole la lista.
     Also, si hacemos click en un item llamamos al onItemClickListener del adaptador y establecemos qué hacer en cada click
     */
    private fun setupDashboardAdapter() {
        val dashOptionList = arrayListOf(
            DashOption(
                R.drawable.ic_play_black_24dp,
                getString(R.string.dashboard_lblPlay),
                R.color.playOption
            ),
            DashOption(
                R.drawable.ic_settings_black_24dp,
                getString(R.string.dashboard_lblSettings),
                R.color.settingsOption
            ),
            DashOption(
                R.drawable.ic_ranking_black_24dp,
                getString(R.string.dashboard_lblRanking),
                R.color.rankingOption
            ),
            DashOption(
                R.drawable.ic_assistant_black_24dp,
                getString(R.string.dashboard_lblAssistant),
                R.color.assistantOption
            ),
            DashOption(
                R.drawable.ic_player_black_24dp,
                getString(R.string.dashboard_lblPlayer),
                R.color.playerOption
            ),
            DashOption(
                R.drawable.ic_about_black_24dp,
                getString(R.string.dashboard_lblAbout),
                R.color.aboutOption
            )
        )
        dashboardAdapter = DashboardFragmentAdapter(dashOptionList).also {
            it.onItemClickListener = { position ->
                navigateToPosition(position)
            }
        }

    }

    //Inicializamos el hueco del xml destinado a mostrar el recycler view
    private fun setupRecyclerView() {
        lstDashOptions.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 2)
            adapter = dashboardAdapter
        }
    }

    //Según la posición que ocupe el item clicado, se navega a un fragmento u otro.
    private fun navigateToPosition(position: Int) {
        when (position) {
            0 -> {
                if (viewModel.getCurrentPlayerId() == -1L) {
                    findNavController().navigate(R.id.navigateToPlayer)
                } else {
                    findNavController().navigate(R.id.navigateToGame)
                }
            }
            1 -> findNavController().navigate(R.id.navigateToSettings)
            2 -> findNavController().navigate(R.id.navigateToRanking)
            3 -> findNavController().navigate(R.id.navigateToAssistant)
            4 -> findNavController().navigate(R.id.navigateToPlayer)
            5 -> findNavController().navigate(R.id.navigateToAbout)
        }
    }


}
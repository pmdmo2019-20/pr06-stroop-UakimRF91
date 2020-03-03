package es.iessaladillo.pedrojoya.stroop.ui.ranking

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.OnToolbarAvailableListener
import es.iessaladillo.pedrojoya.stroop.data.StroopDatabase
import es.iessaladillo.pedrojoya.stroop.ui.dialogs.about.AboutDialog
import kotlinx.android.synthetic.main.ranking_fragment.*

class RankingFragment : Fragment(R.layout.ranking_fragment) {

    lateinit var rankingAdapter: RankingFragmentAdapter
    var idSelectedRanking: Long = 0
    val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }
    private val viewModel: RankingViewModel by viewModels {
        RankingViewModelFactory(
            StroopDatabase.getInstance(requireContext()).gameDao,
            requireActivity().application
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupRankingAdapter()
        setupRecyclerView()
        setupAppbar()
        initializeSpinner()
        observeViewModels()
        prueba()
    }

    private fun initializeSpinner() {
        //Indicamos qué debe hacer el spinner cuando se modifica su valor
        //Cuando se cambie de valor, hay que cambiar el livedata "mode" y poner el que le corresponde del array
        spnGameMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.mode.value =
                    resources.getStringArray(R.array.ranking_spnGameMode_values)[position]
            }
        }
        //Indicamos que el valor inicial del spinner sea el que tenemos en el livedata "mode"
        spnGameMode.setSelection(
            resources.getStringArray(R.array.ranking_spnGameMode_values).indexOf(
                viewModel.mode.value
            )
        )
    }

    private fun showRankings() {
        viewModel.queryRankings()
    }

    private fun prueba() {
        lblGameMode.setOnClickListener {
            viewModel.queryAllGames()
        }
    }

    private fun observeViewModels() {
        viewModel.ranking.observe(viewLifecycleOwner) {
            rankingAdapter.submitList(it)
        }

        viewModel.mode.observe(viewLifecycleOwner) {
            showRankings()
        }
    }

    private fun setupRankingAdapter() {
        rankingAdapter = RankingFragmentAdapter().also {
            it.onItemClickListener = {
                idSelectedRanking = rankingAdapter.gamesList[it].gameId
                showRankingDetail()
            }
        }
    }






    private fun showRankingDetail() {
        findNavController().navigate(
            R.id.navigateToRankingDetailDestination,
            bundleOf(
                getString(R.string.RANKING_ID_ARGS) to idSelectedRanking
            )
        )
    }


    private fun setupRecyclerView() {
        lstGames.run {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = rankingAdapter
        }
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
            getString(R.string.ranking_title),
            getString(R.string.ranking_help_description)
        ).show(requireFragmentManager(), "AboutDialog")
    }

}

package es.iessaladillo.pedrojoya.stroop.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.base.OnToolbarAvailableListener
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), OnToolbarAvailableListener {

    //Obtenemos el NavController
    val navController: NavController by lazy {
        findNavController(R.id.navHostFragment)
    }
    //Configuración de la Appbar
    private val appBarConfiguration: AppBarConfiguration =
        AppBarConfiguration.Builder(R.id.dashboardDestination).build()
    //Para obtener los ajustes de shared preferences
    val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    //**Métodos que sobreescribimos al implementar OnToolbarAvailableListener**
    //Indicamos que la toolbar será proporcionada por cada fragmento
    override fun onToolbarCreated(toolbar: Toolbar) {
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
    override fun onToolbarDestroyed() {}

    //Especifica qué hace cuando la información va a empezar a ser mostrada
    //En nuestro caso va a mostrar assistantDestination si es la primera vez que se inicia.
    override fun onResume() {
        super.onResume()
        //Si no hay nada, coge true por defecto. Acto seguido se pone false, y ya nunca más se mostrará
        if (settings.getBoolean("FirstExecution", true)){
            navController.navigate(R.id.assistantDestination)
            settings.edit().putBoolean("FirstExecution", false).apply()
        }
    }

}

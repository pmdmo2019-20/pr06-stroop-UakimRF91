package es.iessaladillo.pedrojoya.stroop.ui.player

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.entity.Player
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_player.*
//He creado el adaptador para que me pasen el contexto y así poder acceder a SharedPreferences
class PlayerFragmentAdapter(context: Context) :
    RecyclerView.Adapter<PlayerFragmentAdapter.ViewHolder>() {

    val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    var onItemClickListener: ((Int) -> Unit)? = null

    var playerList : List<Player> = arrayListOf()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.card_player, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    fun submitList(newList : List<Player>){
        playerList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player : Player = playerList[position]
        holder.bind(player)
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            containerView.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
                //checkIfGreenBar(playerList[adapterPosition])
            }
        }

        fun bind(player: Player) {
            player.run {
                lblPlayer.text= player.playerName
                imgPlayer.setImageResource(player.imageId)
                checkIfGreenBar(player)
            }
        }

        private fun checkIfGreenBar(player: Player) {
            if (player.playerId == settings.getLong("currentPlayer", -1L)) {
                barIsSelected.visibility = View.VISIBLE
            } else {
                barIsSelected.visibility = View.INVISIBLE
            }
        }
    }
}
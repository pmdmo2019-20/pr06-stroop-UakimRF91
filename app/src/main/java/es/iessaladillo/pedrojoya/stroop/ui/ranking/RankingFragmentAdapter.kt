package es.iessaladillo.pedrojoya.stroop.ui.ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.entity.Game
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_ranking.*

class RankingFragmentAdapter:
    RecyclerView.Adapter<RankingFragmentAdapter.ViewHolder>()  {

    var gamesList : List<Game> = arrayListOf()
    var onItemClickListener: ((Int) -> Unit)? = null
    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.card_ranking, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return gamesList.size
    }

    fun submitList(newList : List<Game>){
        gamesList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game : Game = gamesList[position]
        holder.bind(game)
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            containerView.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }
        }

        fun bind(game: Game) {
            game.run {
                imgPlayer.setImageResource(game.player.imageId)
                lblPlayerName.text = player.playerName
                lblGameMode.text = gameMode
                lblTime.text = time.toString()
                lblNumWords.text = words.toString()
                lblNumCorrects.text = numCorrects.toString()
                lblPoints.text = points.toString()
            }
        }
    }

}
package es.iessaladillo.pedrojoya.stroop.ui.player.editPlayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_avatar.*

class EditPlayerFragmentAdapter() :
    RecyclerView.Adapter<EditPlayerFragmentAdapter.ViewHolder>() {

    private var avatarList: List<Int> = arrayListOf()
    var onItemClickListener: ((Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.card_avatar, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return avatarList.size
    }
    fun submitList(newList : List<Int>){
        avatarList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val avatar: Int = avatarList[position]
        holder.bind(avatar)
    }


    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {



        init {
            containerView.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }

        }

        fun bind(avatar:Int) {
            imgAvatar.setImageResource(avatar)
            viewIsSelected.visibility= View.INVISIBLE
        }
    }
}

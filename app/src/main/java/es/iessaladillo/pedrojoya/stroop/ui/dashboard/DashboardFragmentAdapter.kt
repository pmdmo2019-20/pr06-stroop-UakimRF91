package es.iessaladillo.pedrojoya.stroop.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.entity.DashOption
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_dash_option.*

class DashboardFragmentAdapter(var itemList: ArrayList<DashOption>) :
    RecyclerView.Adapter<DashboardFragmentAdapter.ViewHolder>() {

    var onItemClickListener: ((Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.card_dash_option, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: DashOption = itemList[position]
        holder.bind(item)
    }


    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            containerView.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }
        }

        fun bind(item: DashOption) {
            item.run {
                imgBtn.setImageResource(imgId)
                lblBtn.text = text
                containerView.run {
                    imgBtn.setColorFilter(context.resources.getColor(colorId))
                    lblBtn.setTextColor(context.resources.getColor(colorId))
                }
            }
        }
    }
}
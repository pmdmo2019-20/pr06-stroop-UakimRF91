package es.iessaladillo.pedrojoya.stroop.ui.assistant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.entity.Slide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.page_assistant_slide.*
import kotlinx.android.synthetic.main.page_assistant_slide.view.*


class AssistantFragmentAdapter(val slideList: ArrayList<Slide>) :
    RecyclerView.Adapter<AssistantFragmentAdapter.ViewHolder>() {

    private var currentSlide: Int = 0
    var onItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.page_assistant_slide, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return slideList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val page: Slide = slideList[position]
        currentSlide = position
        holder.bind(page)
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            btnFinish.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }
        }

        fun bind(slide: Slide) {
            slide.run {
                containerView.imgIcon.setImageResource(slide.imageId)
                containerView.lblContent.text = slide.text
                containerView.setBackgroundResource(slide.colorId)
                if (adapterPosition == slideList.size - 1) {
                    containerView.btnFinish.visibility = View.VISIBLE
                } else {
                    containerView.btnFinish.visibility = View.INVISIBLE
                }
            }
        }
    }

}
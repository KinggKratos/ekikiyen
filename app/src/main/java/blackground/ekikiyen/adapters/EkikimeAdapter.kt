package blackground.ekikiyen.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import blackground.ekikiyen.R
import blackground.ekikiyen.data.Ekikime
import blackground.ekikiyen.databinding.ViewEkikimeItemBinding
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import org.ocpsoft.prettytime.PrettyTime

class EkikimeAdapter(private val items: ArrayList<Ekikime> = ArrayList(),
                     private val onCardClick: IOnCardClick) : RecyclerView.Adapter<EkikimeAdapter.ViewHolder>() {
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener { onCardClick.onClick(currentItem.cardNumber) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewEkikimeItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                        R.layout.view_ekikime_item, parent, false)

        return ViewHolder(binding)
    }


    fun update(list: ArrayList<Ekikime>?) {
        if (list == null) return

        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding: ViewEkikimeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ekikime: Ekikime) {
            binding.cardNumber.text = ekikime.cardNumber.replaceRange(6, 12, "---")
            val prettyTime = PrettyTime()
            binding.timeAgo.text = prettyTime.format(ekikime.createdAt)

            val usageRemaining = 2 - ekikime.usage

            // create drawable for times remaining
            val colorGenerator = ColorGenerator.MATERIAL
            val textDrawable = TextDrawable.builder()
                    .buildRound(usageRemaining.toString(), colorGenerator.randomColor)

            binding.usageRemaining.setImageDrawable(textDrawable)
            if (usageRemaining == 0) {
                binding.root?.isEnabled = false
                binding.root?.alpha = 0.4f
            } else {
                binding.root?.isEnabled = true
                binding.root?.alpha = 1.0f
            }
        }
    }

    interface IOnCardClick {
        fun onClick(cardNumber: String)
    }
}
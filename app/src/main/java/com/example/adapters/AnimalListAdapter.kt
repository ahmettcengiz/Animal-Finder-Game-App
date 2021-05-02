package com.example.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.animalfindergame.R
import com.example.objects.Animal
import kotlinx.android.synthetic.main.adapter_row_design.view.*

class AnimalListAdapter(val items: List<Animal>,private val onClickListener: OnClickListener) : RecyclerView.Adapter<AnimalListAdapter.MyViewHolder>() {

    interface OnClickListener {
        fun onItemClick(objects: Animal)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.adapter_row_design, parent, false)
        return MyViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = items[position]
        holder.itemView.animalImageView.setBackgroundResource(currentItem.imageUrl)

        holder.itemView.setOnClickListener {
            onClickListener.onItemClick(currentItem)

        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }


}
package com.example.dangdangee.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dangdangee.R
import com.example.dangdangee.databinding.RvItemListBinding

class CustomAdapter(val item : ArrayList<BoardModel>) : RecyclerView.Adapter<CustomAdapter.Viewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_list,parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: CustomAdapter.Viewholder, position: Int) {
        holder.title.text=item.get(position).title
        holder.writer.text=item.get(position).uid
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.rv_title)
        val writer = itemView.findViewById<TextView>(R.id.rv_writer)
    }

}
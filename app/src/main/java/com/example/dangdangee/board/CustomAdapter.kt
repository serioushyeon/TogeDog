package com.example.dangdangee.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dangdangee.R

class CustomAdapter(val item : ArrayList<MainViewModel>) : RecyclerView.Adapter<CustomAdapter.Viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.Viewholder {
        val v =LayoutInflater.from(parent.context).inflate(R.layout.rv_item_list,parent,false)
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: CustomAdapter.Viewholder, position: Int) {
        holder.bindItem(item[position])
    }

    override fun getItemCount() : Int{
        return item.size
    }

    inner class Viewholder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(item : MainViewModel){
            val title = itemView.findViewById<TextView>(R.id.rv_title)
            title!!.text = item.title

            val writer = itemView.findViewById<TextView>(R.id.rv_writer)
            writer!!.text = item.uid

        }
    }
}
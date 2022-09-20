package com.example.dangdangee.board

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpCookie.parse
import java.net.URI
import java.net.URL
import java.util.logging.Level.parse

class CustomAdapter(val item : ArrayList<BoardModel>) : RecyclerView.Adapter<CustomAdapter.Viewholder>() {
    private val boardKeyList = arrayListOf<String>()
    private val boardDataList = arrayListOf<BoardModel>()

    private val TAG = HomeFragment::class.java.simpleName


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_list,parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: CustomAdapter.Viewholder, position: Int) {
        getData()
        val context = holder.itemView.context
        val imView = item.get(position).imUrl
        Log.d("checkim",item.get(position).imUrl)
        Log.d("checkekey",item.get(position).ekey)
        Log.d("checkuid",item.get(position).uid)
        Log.d("checkdogname",item.get(position).dogname)
        Log.d("checkbreed",item.get(position).breed)
        Log.d("checklostday",item.get(position).lostday)
        Log.d("checkcontent",item.get(position).content)

        CoroutineScope(Dispatchers.Main).launch {
            holder.apply {
                Glide.with(context)
                    .load(imView)
                    .into(holder.image)
            }


        }
        holder.title.text=item.get(position).title
        Log.d("check33", item.get(position).title)
        holder.writer.text=item.get(position).ekey


        holder.itemView.setOnClickListener{
            onClick(context,position)
        }
    }



    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.rv_title)
        val writer = itemView.findViewById<TextView>(R.id.rv_writer)
        val image = itemView.findViewById<ImageView>(R.id.rv_dogProfile)
    }


    fun onClick(context: Context, position: Int) {
        val intent = Intent(context,PostActivity::class.java)
        intent.putExtra("key",boardKeyList[position])
        context.startActivity(intent)
    }


    fun getData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                boardKeyList.reverse()
                boardDataList.reverse()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }
    
}


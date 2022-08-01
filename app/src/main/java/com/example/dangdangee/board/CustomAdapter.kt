package com.example.dangdangee.board



import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CustomAdapter(val item : ArrayList<BoardModel>) : RecyclerView.Adapter<CustomAdapter.Viewholder>() {
    private val boardDataList = arrayListOf<BoardModel>()
    private val boardKeyList = arrayListOf<String>()
    private val TAG = HomeFragment::class.java.simpleName
    lateinit var  Cadapter : CustomAdapter
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
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView?.context,PostActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            holder.itemView.context.startActivity(intent)
        }
    }



    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.rv_title)
        val writer = itemView.findViewById<TextView>(R.id.rv_writer)
    }


    fun getData(){
        val database = Firebase.database
        val boardRef = database.getReference("board")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                boardDataList.clear()
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                Cadapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }

}
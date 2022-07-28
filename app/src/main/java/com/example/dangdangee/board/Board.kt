package com.example.dangdangee.board

import android.content.Context
import android.content.Intent
import android.graphics.ColorSpace
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.board.BoardListLVAdapter
import com.example.dangdangee.board.BoardModel
import com.example.dangdangee.databinding.ActivityBoardBinding
import com.example.dangdangee.map.MapBottomSheetFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Board : AppCompatActivity() {
    /*private lateinit var binding: ActivityBoardBinding

    lateinit var  LVAdaper : BoardListLVAdapter

    private val boardDataList = mutableListOf<BoardModel>()

    private val TAG = Board::class.java.simpleName
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        /*binding = DataBindingUtil.setContentView(this,R.layout.activity_board)
        LVAdaper = BoardListLVAdapter(boardDataList)
        binding.boardListView.adapter=LVAdaper
        getData()*/
        val rv : RecyclerView = findViewById(R.id.rv_postList)
        val items = ArrayList<MainViewModel>()
        val rvAdapter = CustomAdapter(items)
        rv.adapter = rvAdapter

        rv.layoutManager = LinearLayoutManager(this)
    }

    
    /*fun getData(){
        val database = Firebase.database
        val boardRef = database.getReference("board")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                boardDataList.clear()
                for(dataModel in dataSnapshot.children){

                    Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                }
                LVAdaper.notifyDataSetChanged()
               // Log.d(TAG,boardDataList.toString())
            }



            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }*/



}
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
    private lateinit var binding: ActivityBoardBinding

    lateinit var  LVAdaper : BoardListLVAdapter

    private val boardDataList = mutableListOf<BoardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list = mutableListOf<BoardModel>()

        getData()
    }

    fun getData(){
        val database = Firebase.database
        val boardRef = database.getReference("board")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                }
                Log.d("Board",boardDataList.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {

                Log.w("Board", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }



}
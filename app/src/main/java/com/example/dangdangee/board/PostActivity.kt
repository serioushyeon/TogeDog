package com.example.dangdangee.board

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.databinding.ActivityPostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_post)
        val key = intent.getStringExtra("key")
        Toast.makeText(this,key,Toast.LENGTH_LONG).show()

        getBoardData(key.toString())
    }

    private fun getBoardData(key: String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                Log.d(TAG, dataSnapshot.toString())

                binding.tvTitle.text = dataModel!!.title

                binding.tvWriter.text = dataModel!!.uid

                binding.tvBreed.text = dataModel!!.breed

                binding.tvTime.text = dataModel!!.lostday

                binding.tvText.text = dataModel!!.content
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

}
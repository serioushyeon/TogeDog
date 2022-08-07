package com.example.dangdangee.board

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.dangdangee.MainActivity
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBAuth
import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.databinding.ActivityBoardBinding
import com.example.dangdangee.databinding.ActivityBoardEditBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key: String
    private lateinit var binding: ActivityBoardEditBinding
    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_edit)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)
        binding.btnConfirm2.setOnClickListener {
            editBoardData(key)
        }
        binding.ivProfile2.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }


    }

    private fun editBoardData(key: String){

        FBRef.boardRef
            .child(key)
            .setValue(BoardModel(
                binding.evTitle2.text.toString(),
                FBAuth.getUid(),
                binding.evBreed2.text.toString(),
                binding.evTime2.text.toString(),
                binding.evText2.text.toString(),
                FBAuth.getTime()
            ))
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    private fun getBoardData(key: String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(ContentValues.TAG, dataSnapshot.toString())

                    binding.evTitle2.setText(dataModel?.title)
                    binding.evWriter2.setText(dataModel?.uid)
                    binding.evBreed2.setText(dataModel?.breed)
                    binding.evTime2.setText(dataModel?.lostday)
                    binding.evText2.setText(dataModel?.content)


                }catch (e: Exception){
                    Log.w(ContentValues.TAG, "삭제완료")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
    private fun getImageData(key: String){
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.ivProfile2

        storageReference.downloadUrl.addOnCompleteListener({task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            }else{

            }
        })

    }




}

package com.example.dangdangee.board

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.dangdangee.MainActivity
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBAuth
import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.databinding.ActivityBoardEditBinding
import com.example.dangdangee.map.MarkerRegisterActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key: String
    private lateinit var binding: ActivityBoardEditBinding
    private var isImageUpload = false
    private lateinit var mid: String

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
//고생

    }

    private fun editBoardData(key: String){
        val title2 = binding.evTitle2.text.toString()
        val eid2 = FBAuth.getDisplayName()
        val ukey2 = FBAuth.getUid()
        val dogname2 = binding.evDogName2.text.toString()
        val breed2 = binding.evBreed2.text.toString()
        val lostday2 = binding.evTime2.text.toString()
        val content2 = binding.evText2.text.toString()
        val time2 = FBAuth.getTime()


        FBRef.boardRef
            .child(key)
            .setValue(BoardModel(
                title2,
                eid2,
                ukey2,
                dogname2,
                breed2,
                lostday2,
                content2,
                time2
            )
            )       
        var mapRef = Firebase.database.getReference("Marker")
        FBRef.boardRef.child(key).child("mid").get().addOnSuccessListener {
            mid = it.value.toString()
            mapRef.child(mid).removeValue()
        }
        val intent = Intent(this, MarkerRegisterActivity::class.java)
        intent.putExtra("tag", "F") //최초 등록 태그
        intent.putExtra("key",key)
        intent.putExtra("breed", breed2)
        intent.putExtra("name", dogname2)
        startActivity(intent)
        finish()
    }

    private fun getBoardData(key: String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(ContentValues.TAG, dataSnapshot.toString())

                    binding.evTitle2.setText(dataModel?.title)
                    binding.evWriter2.text = dataModel?.ekey
                    binding.evDogName2.setText(dataModel?.dogname)
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
        val storageReference = Firebase.storage.reference.child("$key.png")

        // ImageView in your Activity
        val imageViewFromFB = binding.ivProfile2

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {

            }
        }

    }
}

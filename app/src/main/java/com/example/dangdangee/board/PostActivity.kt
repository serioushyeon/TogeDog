package com.example.dangdangee.board

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBAuth
import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.auth.JoinActivity
import com.example.dangdangee.comment.CommentLVAdapter
import com.example.dangdangee.comment.CommentModel
import com.example.dangdangee.databinding.ActivityPostBinding
import com.example.dangdangee.map.MarkerRegisterActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class PostActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPostBinding
    private lateinit var key: String
    private lateinit var mid: String
    private lateinit var commentkey: String
    private val commentDataList = mutableListOf<CommentModel>()
    private val commentKeyList = mutableListOf<String>()
    private lateinit var  commentAdapter : CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        setStatusBarColor(R.color.main_color)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_post)
        key = intent.getStringExtra("key").toString()

        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }

        getBoardData(key)
        getImageData(key)

        binding.commentBtn.setOnClickListener {
            insertComment(key)
        }

        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter= commentAdapter

        getCommentData(key)

        binding.commentLV.setOnItemClickListener{
                parent,view, position, id->
            //keyList에 있는 key 받아오rl
            commentkey = commentKeyList[position]
            showCDialog()
        }

        binding.btnPathRegister.setOnClickListener{
            val intent = Intent(this, MarkerRegisterActivity::class.java)
            intent.putExtra("key",key)
            intent.putExtra("tag", "P")
            startActivity(intent)
        }

    }

    private fun getCommentData(key: String) {
        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    commentDataList.clear()
                    for (dataModel in dataSnapshot.children) {
                        Log.d(TAG, dataModel.toString())
                        dataModel.key
                        val item = dataModel.getValue(CommentModel::class.java)
                        commentDataList.add(item!!)
                        commentKeyList.add(dataModel.key.toString())

                    }
                    //어뎁터 동기화
                    commentAdapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Log.d(TAG, "삭제완료")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost : onCancelled", databaseError.toException())
            }

        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)

    }



    private fun showCDialog(){
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog2,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("삭제하시겠습니까?")
            val alertDialog = mBuilder.show()

            alertDialog.findViewById<Button>(R.id.removeBtn2)?.setOnClickListener{
                FBRef.commentRef.child(key).child(commentkey).removeValue()
                Toast.makeText(this,"삭제완료",Toast.LENGTH_LONG).show()
                finish()
            }
    }

    private fun showDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")
        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editbtn)?.setOnClickListener{
            val intent = Intent(this,BoardEditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)
        }
        alertDialog.findViewById<Button>(R.id.deletebtn)?.setOnClickListener{
            var mapRef = Firebase.database.getReference("Marker")
            FBRef.boardRef.child(key).child("mid").get().addOnSuccessListener {
                mid = it.value.toString()
                mapRef.child(mid).removeValue()
            } //게시글 삭제 시 마커도 삭제
            FBRef.boardRef.child(key).removeValue()
            finish()
        }
0    }

    private fun getImageData(key: String){
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.ivPostProfile

        storageReference.downloadUrl.addOnCompleteListener({task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB) } else{ } })
    }

    private fun getBoardData(key: String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(TAG, dataSnapshot.toString())
                    binding.tvTitle.text = dataModel?.title
                    binding.tvWriter.text = dataModel?.uid
                    binding.tvBreed.text = dataModel?.breed
                    binding.tvTime.text = dataModel?.lostday
                    binding.tvText.text = dataModel?.content
                    binding.tvRealtime.text = dataModel?.time

                    val myuid = FBAuth.getUid()
                    val writerUid = dataModel?.uid
                    if(myuid.equals(writerUid)){
                        binding.boardSettingIcon.isVisible = true
                    }else{

                    }

                }catch (e: Exception){
                    Log.w(TAG, "삭제완료")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
    fun insertComment(key: String){
        val comment = binding.commentArea.text.toString()
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(CommentModel(comment,FBAuth.getUid(),FBAuth.getTime()
            )
            )

        Toast.makeText(this,"댓글 입력 완료",Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")

    }

    private fun setStatusBarColor(color: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, color))
    }

}
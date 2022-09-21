package com.example.dangdangee.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBAuth
import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.databinding.ActivityBoardWriteBinding
import com.example.dangdangee.map.MarkerRegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardWriteBinding

    private val TAG = BoardWriteActivity::class.java.simpleName

    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_write)

        binding.evWriter.text = FBAuth.getDisplayName() // 작성자 닉네임으로 바꿔줌

        binding.pingping.setOnClickListener {
            val title = binding.evTitle.text.toString()
            val ukey = FBAuth.getUid()
            val eid = FBAuth.getDisplayName()
            val dogname = binding.evDogName.text.toString()
            val breed = binding.evBreed.text.toString()
            val lostday = binding.evTime.text.toString()
            val content = binding.evText.text.toString()
            val time = FBAuth.getTime()

            Log.d(TAG,title)
            Log.d(TAG,content)

            //파이어 베이스 storge에 이미지를 저장
            //게시글을 클릭했을떄, 게시글에 대한 정보 전달
            //이미지 이름을 key값으로 저장
            val key = FBRef.boardRef.push().key.toString()

            //board
            //  -key
            //      -boardModel(title, content, uid, time)

            if(isImageUpload) {

                val storage = Firebase.storage
                val storageRef = storage.reference
                val mountainsRef = storageRef.child(key+".png")

                val imageView = binding.ivProfile
                imageView.isDrawingCacheEnabled = true
                imageView.buildDrawingCache()
                val bitmap = (imageView.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                var uploadTask = mountainsRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
                }

                val urlTask = uploadTask.continueWithTask { task->
                    if (!task.isSuccessful){
                        task.exception?.let{
                            throw it
                        }
                    }
                    mountainsRef.downloadUrl
                }.addOnCompleteListener{ task->
                    if(task.isSuccessful){
                        val downloadUri = task.result
                        val imuri = downloadUri.toString()
                        FBRef.boardRef
                            .child(key)
                            .setValue(BoardModel(title,eid,ukey,dogname,breed,lostday,content,time,imuri))
                        Log.d("check", downloadUri.toString())
                    }
                }

            }
            /*FBRef.boardRef
                .child(key)
                .setValue(BoardModel(title,eid,ukey,dogname,breed,lostday,content,time))*/

            finish()
            val intent = Intent(this, MarkerRegisterActivity::class.java)
            intent.putExtra("tag", "F") //최초 등록 태그
            intent.putExtra("key",key)
            intent.putExtra("breed", breed)
            intent.putExtra("name", dogname)
            startActivity(intent)
        }

        binding.ivProfile.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }
    }



    private fun imageUpload(key : String){
        // Get the data from an ImageView as bytes
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key+".png")

        val imageView = binding.ivProfile
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

        val urlTask = uploadTask.continueWithTask { task->
            if (!task.isSuccessful){
                task.exception?.let{
                    throw it
                }
            }
            mountainsRef.downloadUrl
        }.addOnCompleteListener{ task->
            if(task.isSuccessful){
                 val downloadUri = task.result


            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            binding.ivProfile.setImageURI(data?.data)
        }
    }
}
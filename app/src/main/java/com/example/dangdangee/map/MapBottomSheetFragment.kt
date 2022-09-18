package com.example.dangdangee.map

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.board.BoardModel
import com.example.dangdangee.board.PostActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MapBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var mapref: DatabaseReference
    lateinit var name : String
    lateinit var address : String
    lateinit var breed : String
    lateinit var img : Drawable
    lateinit var key : String

    var flag = true //true면 경로 보기 버튼 뜸, false면 경로 보기 버튼 안 뜸(이미 경로 액티비티일 때)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_map_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapref = Firebase.database.getReference("Marker")
        val imgview = view.findViewById<ImageView>(R.id.info_image_pet)
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.info_to_post).setOnClickListener {
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)
        }
        if(flag){
            view.findViewById<Button>(R.id.info_to_route_map).setOnClickListener{
                    val intent = Intent(activity, PathActivity::class.java)
                    intent.putExtra("key", key)
                    activity?.startActivity(intent)
                }

        }
        else
            view.findViewById<Button>(R.id.info_to_route_map).isVisible = false

        view.findViewById<TextView>(R.id.info_address).text = address
        view.findViewById<TextView>(R.id.info_name).text = name
        view.findViewById<TextView>(R.id.info_kind).text = breed
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = view.findViewById<ImageView>(R.id.info_image_pet)

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {
            }
        }

        imgview.background = ShapeDrawable(OvalShape())
        imgview.clipToOutline = true
    }

}
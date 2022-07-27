package com.example.dangdangee.map

import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.dangdangee.R
import com.example.dangdangee.databinding.FragmentMapBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MapBottomSheetFragment : BottomSheetDialogFragment() {

    internal lateinit var name : String
    internal lateinit var address : String
    internal lateinit var kind : String
    internal lateinit var img : Drawable
    private val binding by lazy { FragmentMapBottomSheetBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_map_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.info_to_post).setOnClickListener {
            println("post")
            //getSupportFragmentManager().beginTransaction().replace(컨테이너, 게시판프래그먼트).commit();
        }
        view.findViewById<Button>(R.id.info_to_route_map).setOnClickListener {
            println("path")
            //getSupportFragmentManager().beginTransaction().replace(컨테이너, 경로프래그먼트).commit();
        }
        view.findViewById<TextView>(R.id.info_address).text = address
        view.findViewById<TextView>(R.id.info_name).text = name
        view.findViewById<TextView>(R.id.info_kind).text = kind
        val imgview = view.findViewById<ImageView>(R.id.info_image_pet)
        imgview.setImageDrawable(img)
        imgview.background = ShapeDrawable(OvalShape())
        imgview.clipToOutline = true
    }
}
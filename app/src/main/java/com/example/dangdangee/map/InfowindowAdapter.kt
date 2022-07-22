package com.example.dangdangee.map

import android.content.Context
import android.icu.text.IDNA
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dangdangee.R
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.InfoWindow.DefaultViewAdapter


class InfowindowAdapter (private val mContext: Context, private val mParent: ViewGroup, private val name : String, private val address: String, private val kind : String):
    InfoWindow.DefaultViewAdapter(mContext) {
    /*
    // (1) 아이템 레이아웃과 결합
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfowindowAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.map_infowindow, parent, false)
        return ViewHolder(view)
    }
    // (3) View에 내용 입력
    override fun onBindViewHolder(holder: InfowindowAdapter.ViewHolder, position: Int) {
        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.postBtn.setOnClickListener {
            println("postbtn")
        }
        holder.pathBtn.setOnClickListener {
            println("pathbtn")
        }
    }
    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    // (4) 레이아웃 내 View 연결
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val pathBtn: Button = itemView.findViewById(R.id.info_to_route_map)
        val postBtn: Button = itemView.findViewById(R.id.info_to_post)
    }

    override fun getItemCount(): Int {
        return 2
    }*/

    override fun getContentView(infoWindow: InfoWindow): View {
     val view =
         LayoutInflater.from(mContext).inflate(R.layout.map_infowindow, mParent, false) as View

     val txtTitle = view.findViewById<View>(R.id.info_title) as TextView
     val imagePoint = view.findViewById<View>(R.id.info_image_pet) as ImageView
     val txtAddr = view.findViewById<View>(R.id.info_address) as TextView
     val txtKind = view.findViewById<View>(R.id.info_male_female) as TextView
     txtTitle.text = name
     imagePoint.setImageResource(R.drawable.dag)
     txtAddr.text = address
     txtKind.text = kind
     return view
 }

}
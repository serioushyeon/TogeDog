package com.example.dangdangee.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.dangdangee.R

import com.example.dangdangee.board.BoardModel


class CommentLVAdapter(val commentList : MutableList<CommentModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //view를 가져와서 연결
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.comment_list_item, parent, false)
        }
        val title = view?.findViewById<TextView>(R.id.titleArea)
        val time = view?.findViewById<TextView>(R.id.timeArea)
        //val delete = view?.findViewById<ImageView>(R.id.commentSettingIcon)
        val content = commentList[position]
        title!!.text = content.commentTitle
        time!!.text = content.commentCreatedTime
        //delete!!.setImageResource(content.commentDelete)
        return view!!
    }




}

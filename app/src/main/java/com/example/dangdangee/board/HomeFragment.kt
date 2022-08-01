
package com.example.dangdangee.board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.GridLayoutManager
import com.example.dangdangee.MainActivity
import com.example.dangdangee.R

import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    lateinit var  Cadapter : CustomAdapter
    private val TAG = HomeFragment::class.java.simpleName
    private val boardDataList = arrayListOf<BoardModel>()
    private val boardKeyList = arrayListOf<String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): LinearLayout {
        val binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.rvPostList.apply {
            getData()
            layoutManager = GridLayoutManager(requireContext(),2)
            setHasFixedSize(true)
            Cadapter = CustomAdapter(boardDataList)
            binding.rvPostList.adapter = Cadapter
        }


        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(getActivity(), BoardWriteActivity::class.java)
           startActivity(intent)
        }
        return binding.root
    }



    fun getData(){
        val database = Firebase.database
        val boardRef = database.getReference("board")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                boardDataList.clear()
                for(dataModel in dataSnapshot.children){

                    Log.d(TAG,dataModel.toString())

                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                 Cadapter.notifyDataSetChanged()
                 Log.d(TAG,boardDataList.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }
}
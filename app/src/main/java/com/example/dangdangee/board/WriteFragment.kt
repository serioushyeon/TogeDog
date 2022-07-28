package com.example.dangdangee.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBAuth
import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.databinding.FragmentWriteBinding

class WriteFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentWriteBinding.inflate(inflater,container,false)

        binding.btnConfirm.setOnClickListener {
            val title = binding.evTitle.text.toString()
            val content = binding.evText.text.toString()
            val breed = binding.evBreed.text.toString()
            val lostday = binding.evTime.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            FBRef.boardRef.push().setValue(MainViewModel(title,content,uid,time))

            findNavController().navigate(R.id.action_writeFragment_to_homeFragment)
        }

        return binding.root
    }
}
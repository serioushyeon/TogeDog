package com.example.dangdangee.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dangdangee.R
import com.example.dangdangee.databinding.FragmentPostBinding

class PostFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostBinding.inflate(inflater,container,false)


        binding.apply {
            tvTitle.text = .title
            tvWriter.text = post.writer
            tvBreed.text = post.breed
            tvTime.text = post.time
            tvText.text = post.text
        }

        binding.buttonOK.setOnClickListener {
            findNavController().navigate(R.id.action_postFragment_to_homeFragment)
        }

        /*binding.btnRemovePost.setOnClickListener {
            viewModel.removePost(viewModel.getPos())
            findNavController().navigate(R.id.action_postFragment_to_homeFragment)
        }

        binding.btnUpdatePost.setOnClickListener {
            viewModel.confirm = "update" // confirm 버튼의 값을 update로 전환
            findNavController().navigate(R.id.action_postFragment_to_writeFragment)
        }*/

        return binding.root
    }
}
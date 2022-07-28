
package com.example.dangdangee.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dangdangee.R
import com.example.dangdangee.databinding.FragmentHomeBinding



class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        val items = ArrayList<MainViewModel>()
        val rvAdapter = CustomAdapter(items)
        binding.rvPostList.adapter = rvAdapter

    }
        /*binding.rvPostList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = CustomAdapter(requireContext(), viewModel)
        }

        binding.floatingActionButton.setOnClickListener { // 메뉴 버튼 click - 생성창
            findNavController().navigate(R.id.action_homeFragment_to_writeFragment)
        }

        return binding.root*/



}
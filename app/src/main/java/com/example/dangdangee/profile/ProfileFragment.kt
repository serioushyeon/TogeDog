package com.example.dangdangee.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.dangdangee.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout{
        val binding = FragmentProfileBinding.inflate(inflater,container,false)
        binding.profileEdit.setOnClickListener {
            startActivity(Intent(activity,ProfileEditActivity::class.java))
        }
        return binding.root
    }

}
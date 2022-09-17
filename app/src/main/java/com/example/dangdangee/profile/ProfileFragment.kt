package com.example.dangdangee.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.dangdangee.auth.LoginActivity
import com.example.dangdangee.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout{
        val binding = FragmentProfileBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        binding.logOutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity,LoginActivity::class.java))
        }
        return binding.root
    }
}
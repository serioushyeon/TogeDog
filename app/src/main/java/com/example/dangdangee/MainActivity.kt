package com.example.dangdangee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dangdangee.board.HomeFragment
import com.example.dangdangee.databinding.ActivityMainBinding
import com.example.dangdangee.map.MainMapFragment

import com.example.dangdangee.profile.ProfileFragment
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(){
    private val mapFragment: MainMapFragment by lazy { MainMapFragment() }
    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val profileFragment: ProfileFragment by lazy { ProfileFragment() }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.bottom_containers, mapFragment).commit()
        val navigationBarView = binding.bottomNavigationview
        navigationBarView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_map -> {
                    supportFragmentManager.beginTransaction().replace(R.id.bottom_containers, mapFragment)
                        .commit()
                    return@OnItemSelectedListener true
                }
                R.id.bottom_board -> {
                    supportFragmentManager.beginTransaction().replace(R.id.bottom_containers, homeFragment)
                        .commit()
                    return@OnItemSelectedListener true
                }
                R.id.bottom_profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.bottom_containers, profileFragment)
                        .commit()
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }
}
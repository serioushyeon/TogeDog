package com.example.dangdangee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dangdangee.databinding.ActivityMainBinding
import com.example.dangdangee.map.MainMapFragment
import com.example.dangdangee.map.MarkerRegisterFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(){
    private var mapFragment: MainMapFragment = MainMapFragment()
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
                    return@OnItemSelectedListener true
                }
                R.id.bottom_profile -> {
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }
}
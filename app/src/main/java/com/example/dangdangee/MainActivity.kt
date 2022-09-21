package com.example.dangdangee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.dangdangee.board.HomeFragment
import com.example.dangdangee.databinding.ActivityMainBinding
import com.example.dangdangee.map.MainMapFragment
import com.example.dangdangee.profile.ProfileFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(){
    private val mapFragment: MainMapFragment by lazy { MainMapFragment() }
    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val profileFragment: ProfileFragment by lazy { ProfileFragment() }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    var backKeyPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.bottom_containers, mapFragment).commit()
        val navigationBarView = binding.bottomNavigationview
        navigationBarView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mainMapFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.bottom_containers, mapFragment)
                        .commit()
                    return@OnItemSelectedListener true
                }
                R.id.postFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.bottom_containers, homeFragment)
                        .commit()
                    return@OnItemSelectedListener true
                }
                R.id.profileFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.bottom_containers, profileFragment)
                        .commit()
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }

    override fun onBackPressed() {
        //super.onBackPressed()

        if(System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            return;
        } else {
            finishAffinity()
        }
    }
}
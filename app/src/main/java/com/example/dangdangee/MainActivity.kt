package com.example.dangdangee

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.dangdangee.board.HomeFragment
import com.example.dangdangee.databinding.ActivityMainBinding
import com.example.dangdangee.map.MainMapFragment
import com.example.dangdangee.map.MarkerRegisterFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(){
    private val mapFragment: MainMapFragment by lazy { MainMapFragment() }
    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setStatusBarColor(R.color.main_color)

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
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }

    private fun setStatusBarColor(color: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, color))
    }
}
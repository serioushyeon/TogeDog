package com.example.dangdangee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.dangdangee.auth.IntroActivity
import com.example.dangdangee.auth.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setStatusBarColor(R.color.main_color)

        Handler().postDelayed({
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        },3000)
    }

    private fun setStatusBarColor(color: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, color))
    }
}
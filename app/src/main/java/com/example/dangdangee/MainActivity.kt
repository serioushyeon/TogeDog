package com.example.dangdangee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.dangdangee.auth.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*
        val intent = Intent(this, MainMapActivity::class.java)
        startActivity(intent)
*/

        findViewById<Button>(R.id.logoutBtn).setOnClickListener {
            auth.signOut()

            val intent = Intent(this, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TOP //회원가입하면 뒤에있는 엑티비티 없애기
            startActivity(intent)
        }
    }
}
package com.example.dangdangee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.dangdangee.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize Firebase Auth
        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val joinBtnCliced = findViewById<Button>(R.id.joinBtn)
        joinBtnCliced.setOnClickListener {


            //첫번째 방법
            //val email = findViewById<EditText>(R.id.emailArea)
            //val pwd = findViewById<EditText>(R.id.pwdArea)

            //두번째 방법
            val email=binding.emailArea
            val pwd = binding.pwdArea

            auth.createUserWithEmailAndPassword(
                email.text.toString(),
                pwd.text.toString()

            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"ok",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,"no",Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}

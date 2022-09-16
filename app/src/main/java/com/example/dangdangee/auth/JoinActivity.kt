package com.example.dangdangee.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil

import com.example.dangdangee.MainActivity
import com.example.dangdangee.R
import com.example.dangdangee.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_join)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)
        setStatusBarColor(R.color.main_color)

        auth = Firebase.auth
        binding.joinBtn.setOnClickListener {
            //if (validation()) {
                val email = binding.emailArea.text.toString()
                val password = binding.passwordArea1.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP //회원가입하면 뒤에있는 엑티비티 없애기
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, "no", Toast.LENGTH_SHORT).show()
                        }
                    }

           // }
        }
    }


    private fun validation(): Boolean {
        if (binding.nicknameArea.text.isEmpty()) {
            Toast.makeText(this, "닉네임을 입력해주세여", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.emailArea.text.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세여", Toast.LENGTH_SHORT).show()
            return false
        } else if(binding.passwordArea1.text.isEmpty()){
            Toast.makeText(this, "비밀번호1를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        } else if(binding.passwordArea2.text.isEmpty()){
            Toast.makeText(this, "비밀번호2를 확인해주세요", Toast.LENGTH_SHORT).show()
            return false
        } else if(!binding.passwordArea1.text.equals(binding.passwordArea2.text)){
            Toast.makeText(this, "비밀번호를 똑같이 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        } else if(binding.passwordArea1.text.length < 6){
            Toast.makeText(this, "비밀번호를 6자리 이상으로 입력해주세용", Toast.LENGTH_SHORT).show()
            return false
        }

        return true

    }

    private fun setStatusBarColor(color: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, color))
    }

}
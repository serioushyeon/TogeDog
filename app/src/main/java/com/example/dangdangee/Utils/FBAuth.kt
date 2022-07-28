package com.example.dangdangee.Utils

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {

    companion object{
        private lateinit var auth: FirebaseAuth

        fun getUid() : String{

            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()

        }

        fun getTime() : String{
            val currentDataTime = Calendar.getInstance().time
            val dataFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss",Locale.KOREA).format(currentDataTime)

            return dataFormat
        }
    }
}
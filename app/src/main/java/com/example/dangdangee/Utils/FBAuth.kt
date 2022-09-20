package com.example.dangdangee.Utils

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

object FBAuth {

        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        fun getUid() : String{
            return auth.currentUser?.uid.toString()
        }

        fun getEmail() : String{
            return auth.currentUser?.email.toString()
        }

        fun getTime() : String{
            val currentDataTime = Calendar.getInstance().time
            val dataFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss",Locale.KOREA).format(currentDataTime)

            return dataFormat
        }
}
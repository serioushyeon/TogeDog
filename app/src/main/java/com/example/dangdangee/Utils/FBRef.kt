package com.example.dangdangee.Utils

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object{

        private val database = Firebase.database

        val boardRef = database.getReference("board")

        val commentRef = database.getReference("comment")

        val userRef = database.getReference("user")

    }
}
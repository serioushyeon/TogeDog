package com.example.dangdangee.Utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object FBRef {

        private val database = Firebase.database

        val boardRef = database.getReference("board")

        val commentRef = database.getReference("comment")

        val mapRef = database.getReference("Marker")

        val userRef = database.getReference("User")


}
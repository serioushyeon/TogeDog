package com.example.dangdangee.board

import com.example.dangdangee.Utils.FBRef

data class BoardModel (
    val keyphoto : String = FBRef.boardRef.push().key.toString()+"png",
    val title : String = "",
    val content : String = "",
    val breed : String = "",
    val lostday : String = "",
    val uid : String = "",
    val time : String = ""
)
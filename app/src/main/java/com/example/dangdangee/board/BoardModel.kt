package com.example.dangdangee.board

data class BoardModel (
    val title : String = "",
    val ekey : String ="",
    val uid : String = "",
    val dogname : String = "",
    val breed : String = "",
    val lostday : String = "",
    val content : String = "",
    val time : String = "",
    val mid : String = "" //게시글 삭제 시 마커도 삭제하기 위함
)

package com.example.dangdangee.map

import com.naver.maps.map.overlay.Marker

data class MapModel(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val tag: String = "", //등록 태그 or 경로 태그 구분
    val name: String = "",
    val address: String = "",
    val breed: String = "",
    val key: String = "", //게시글의 파이어베이스 key 값, 게시글 등록 시 저장
    val time: String ="", // 시간별로 경로
    val img: String = "",
    var marker: Marker? = null, //마커
    var mid : String = "" //마커의 파이어베이스 키값
)
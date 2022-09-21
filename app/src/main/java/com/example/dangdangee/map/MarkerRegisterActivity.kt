package com.example.dangdangee.map


import android.content.ContentValues
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBAuth
import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.board.BoardModel
import com.example.dangdangee.databinding.ActivityMarkerRegisterBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import java.io.IOException
import java.lang.Exception
import java.util.*

class MarkerRegisterActivity : AppCompatActivity(), OnMapReadyCallback{
    private lateinit var key: String
    private lateinit var tag : String
    private lateinit var mid : String
    private  lateinit var name : String
    private  lateinit var breed : String

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val binding by lazy { ActivityMarkerRegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //현재 위치 사용 처리
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.register_map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.register_map_view, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    //위도 경도 주소로 변환하여 보여줌
    private fun getAddress(lat: Double, lng: Double): String {
        val geoCoder = Geocoder(applicationContext, Locale.KOREA)
        val address: ArrayList<Address>
        var addressResult = "주소를 가져 올 수 없습니다."
        try {
            //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
            //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
            address = geoCoder.getFromLocation(lat, lng, 1) as ArrayList<Address>
            if (address.size > 0) {
                // 주소 받아오기
                val currentLocationAddress = address[0].getAddressLine(0)
                    .toString()
                addressResult = currentLocationAddress
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressResult
    }

    //맵 레디 콜백
    override fun onMapReady(naverMap: NaverMap) {
        key = intent.getStringExtra("key").toString()
        tag = intent.getStringExtra("tag").toString()
        name = intent.getStringExtra("name").toString()
        breed = intent.getStringExtra("breed").toString()
        //getBoardData(key,Mtitle,Mbreed)
        val tv = binding.registerTvLocation //주소 표시
        val fab = binding.registerFloatingbtn //추가 플로팅 버튼

        this.naverMap = naverMap

        //지도 영역 처리
        naverMap.minZoom = 5.0 //최소 줌
        val northWest = LatLng(31.43, 122.37) //서북단
        val southEast = LatLng(44.35, 132.0) //동남단
        naverMap.extent = LatLngBounds(northWest, southEast) //지도 영역을 국내 위주로 축소

        //현재 위치 사용 처리
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow //위치 변해도 지도 안움직임
        naverMap.uiSettings.isLocationButtonEnabled = true //현재 위치 버튼 활성화

        //이동 중이면 표시, 확인 버튼 비활성화
        naverMap.addOnCameraChangeListener { _, _ ->
            tv.run {
                text = "위치 이동 중"
                setTextColor(Color.parseColor("#c4c4c4"))
            }
            fab.run {
                isVisible = false
            }//이동 중이면 등록 버튼 비활성화
        }

        // 카메라의 움직임 종료에 대한 이벤트 리스너
        // 좌표 -> 주소 변환 텍스트 세팅, 버튼 활성화
        naverMap.addOnCameraIdleListener {
            tv.run {
                text = getAddress(
                    naverMap.cameraPosition.target.latitude,
                    naverMap.cameraPosition.target.longitude
                )
                setTextColor(Color.parseColor("#2d2d2d"))
            }
            fab.run {
                isVisible = true
            }//이동 끝나면 등록 버튼 비활성화
        }

        //등록 버튼 누르면 위도 경도를 MainMapActivity로 전달
        fab.setOnClickListener {
            if(!tv.text.equals("주소를 가져 올 수 없습니다.")) {
                //마커 추가
                addMarkerDB(MapModel(naverMap.cameraPosition.target.latitude,naverMap.cameraPosition.target.longitude,tag, name, tv.text.toString(), breed, key, FBAuth.getTime()))
                if(tag == "F")
                    FBRef.boardRef.child("$key/mid").setValue(mid) //게시글에 마커 정보 추가
                finish() //등록 후 뒤로가기
            }
        }
    }
    //마커 파이어베이스 등록 함수
    private fun addMarkerDB(mapModel: MapModel){
        key = intent.getStringExtra("key").toString()
        mid = FBRef.mapRef.push().key.toString() //마커 등록하면서 키값 알아내기
        FBRef.mapRef.child(mid).setValue(mapModel)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}


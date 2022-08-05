package com.example.dangdangee.map

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dangdangee.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource


class MainMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mapref: DatabaseReference
    private lateinit var locationSource: FusedLocationSource //현재 위치 정보를 위한 것
    private lateinit var naverMap: NaverMap //지도
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        mapref = Firebase.database.getReference("Marker")
        val markerListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val marker = dataSnapshot.getValue(MapModel::class.java)
                if(marker!!.tag == "F") {
                    addMarker(marker, naverMap)
                }
            }
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")
                val marker = dataSnapshot.getValue(MapModel::class.java)
                updateMarker(marker!!)
            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)
                val marker = dataSnapshot.getValue(MapModel::class.java)
                removeMarker(marker!!)
            }
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
            }
        }
        mapref.addChildEventListener(markerListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //맵을 띄울 프래그먼트 설정
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.main_map_frame) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.main_map_frame, it).commit()
            }
        mapFragment.getMapAsync(this)
        return inflater.inflate(R.layout.fragment_main_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.registerbtn).setOnClickListener {
            activity?.startActivity(Intent(activity, MarkerRegisterActivity::class.java))
        }
    }

    //맵 레디 콜백
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        //현재 위치 사용 하기 위한 처리
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        //지도 영역 처리
        naverMap.minZoom = 5.0 //최소 줌
        val northWest = LatLng(31.43, 122.37) //서북단
        val southEast = LatLng(44.35, 132.0) //동남단
        naverMap.extent = LatLngBounds(northWest, southEast) //지도 영역을 국내 위주로 축소

        //현재 위치 처리
        naverMap.locationSource = locationSource //현재 위치 사용
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow //위치 변해도 지도 안움직임
        naverMap.uiSettings.isLocationButtonEnabled = true //현재 위치 버튼 활성화
        val locationOverlay = naverMap.locationOverlay //위치 오버레이
        locationOverlay.isVisible = true // 가시성 true
        locationOverlay.position = LatLng(37.5670135, 126.9783740) //오버레이 위치
    }

    //마커 & 정보창 등록 함수
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addMarker(mapModel: MapModel, navermap : NaverMap){
        val marker = Marker()
        marker.position = LatLng(mapModel.lat, mapModel.lng)
        marker.tag = mapModel.tag //최초 등록 or 경로 구분 태그
        marker.captionText = mapModel.name //캡션, 동물 이름
        marker.icon = OverlayImage.fromResource(R.drawable.ic_baseline_pets_24) //아이콘

        marker.width = Marker.SIZE_AUTO //자동 사이즈
        marker.height = Marker.SIZE_AUTO //자동사이즈
        marker.isIconPerspectiveEnabled = true //원근감

        marker.captionRequestedWidth = 200 //캡션 길이
        marker.captionMinZoom = 12.0 //캡션 보이는 범위

        marker.anchor = PointF(0.5f, 0.5f) //마커 이미지가 선택 지점 중앙에 위치하도록

        marker.map = navermap

        //bottomsheet로 정보 띄움
        val fragmentManager = parentFragmentManager
        marker.onClickListener = Overlay.OnClickListener {
            val bottomSheet = MapBottomSheetFragment()
            bottomSheet.name = mapModel.name
            bottomSheet.address = mapModel.address
            bottomSheet.breed = mapModel.breed
            bottomSheet.img = resources.getDrawable(R.drawable.map_sample_dog, null)
            bottomSheet.key = mapModel.key //수정 필요
            bottomSheet.show(fragmentManager, bottomSheet.tag)
            false
        }
        mapModel.marker = marker
    }
    fun updateMarker(mapModel: MapModel){
       /* mapModel.marker?.map = null
        addMarker(mapModel, naverMap)*/
    }
    fun removeMarker(mapModel: MapModel){
        //mapModel.marker?.map = null
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
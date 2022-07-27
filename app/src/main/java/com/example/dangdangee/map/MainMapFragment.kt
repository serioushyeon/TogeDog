package com.example.dangdangee.map

import android.annotation.SuppressLint
import android.graphics.Color.*
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dangdangee.R
import com.example.dangdangee.databinding.FragmentMainMapBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource


class MainMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var locationSource: FusedLocationSource //현재 위치 정보를 위한 것
    private lateinit var naverMap: NaverMap //지도
    private var markers = ArrayList<Marker>() //마커를 담는 배열, 나중에는 데이터베이스에서 마커 정보 가져와야 할 듯
    private val binding by lazy { FragmentMainMapBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("onCreateView")
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
        println("onViewCreated")
        val path = PathOverlay()
        view.findViewById<Button>(R.id.pathbtn).setOnClickListener {
            if(path.map == null){
                path.coords = listOf(
                    LatLng(37.57152, 126.97714),
                    LatLng(37.56607, 126.98268),
                    LatLng(37.56445, 126.97707),
                    LatLng(37.55855, 126.97822)
                )
                path.width = 30
                path.outlineWidth = 0
                path.patternImage = OverlayImage.fromResource(R.drawable.path_pattern)
                path.patternInterval =
                    resources.getDimensionPixelSize(R.dimen.overlay_pattern_interval)
                path.color = rgb(63,121,255)
                path.map = naverMap
            }
            else path.map = null
        }
    }

    //맵 레디 콜백
    override fun onMapReady(naverMap: NaverMap) {
        println("onMapReady")
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

        //마커 추가
        addMarker(LatLng(37.57152, 126.97714), "0", naverMap, "댕댕영")
        addMarker(LatLng(37.56607, 126.98268), "1", naverMap, "댕댕일")
        addMarker(LatLng(37.56445, 126.97707), "2", naverMap, "댕댕이")
        addMarker(LatLng(37.55855, 126.97822), "3", naverMap, "댕댕삼")
    }
    //마커 & 정보창 등록 함수
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addMarker(latlng : LatLng, tag : String, naverMap: NaverMap, caption: String){
        val marker = Marker()
        marker.tag = tag
        marker.position = latlng
        marker.map = naverMap
        marker.icon = OverlayImage.fromResource(R.drawable.ic_baseline_pets_24) //아이콘 , 추후 이미지 받아와서 처리?
        marker.width = Marker.SIZE_AUTO //자동 사이즈
        marker.height = Marker.SIZE_AUTO //자동사이즈
        marker.isIconPerspectiveEnabled = true //원근감
        marker.captionText = caption //캡션
        marker.captionRequestedWidth = 200 //캡션 길이
        marker.captionMinZoom = 12.0 //캡션 보이는 범위
        marker.anchor = PointF(0.5f, 0.5f)

        //마커 등록 시험 & 누르면 bottomsheet로 정보 띄움
        val fragmentManager = parentFragmentManager
        marker.onClickListener = Overlay.OnClickListener {
            val bottomSheet = MapBottomSheetFragment()
            bottomSheet.name = marker.captionText
            bottomSheet.address = "서울특별시"
            bottomSheet.kind = "푸들"
            bottomSheet.img = resources.getDrawable(R.drawable.map_sample_dog, null)
            bottomSheet.show(fragmentManager, bottomSheet.tag)
            false
        }

        //마커를 마커 배열에 추가
        markers.add(marker)

    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
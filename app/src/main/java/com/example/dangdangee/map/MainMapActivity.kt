package com.example.dangdangee.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dangdangee.R
import com.example.dangdangee.auth.IntroActivity
import com.example.dangdangee.board.Board
import com.example.dangdangee.board.BoardWriteActivity
import com.example.dangdangee.databinding.ActivityMainMapBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.*
import com.naver.maps.map.util.FusedLocationSource


class MainMapActivity : AppCompatActivity() , OnMapReadyCallback{
    private lateinit var mapView: MapView //지도를 표시할 뷰
    private lateinit var locationSource: FusedLocationSource //현재 위치 정보를 위한 것
    private lateinit var naverMap: NaverMap //지도
    private lateinit var viewModel : MapViewModel //아직 쓸모 없음, 태그 번호 부여시 사용했음
    private var markers = ArrayList<Marker>() //마커를 담는 배열, 나중에는 데이터베이스에서 마커 정보 가져와야 할 듯
    private val binding by lazy { ActivityMainMapBinding.inflate(layoutInflater) }
    private var infoWindow : InfoWindow = InfoWindow()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth

        //네이버 지도 불러오기 준비
        mapView = binding.mainMapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        //현재 위치 사용 하기 위한 처리
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        // ViewModel
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]

        //마커 등록 액티비티에서 마커 등록 시 결과로 위도 경도 받아와서 마커 추가
        val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val latitude = it.data?.getDoubleExtra("latitude", 0.0) ?: "".toDouble()
            val longitude = it.data?.getDoubleExtra("longitude", 0.0) ?: "".toDouble()
            if(longitude != 0.0 && latitude != 0.0){
                val resultMarker = Marker().apply {
                    tag = viewModel.count.value.toString()
                    viewModel.increaseCount()
                    position = LatLng(latitude, longitude)
                    map = naverMap
                    icon = OverlayImage.fromResource(R.drawable.ic_baseline_pets_24) //아이콘, 나중에 이미지 받아오기?
                    width = Marker.SIZE_AUTO //자동 사이즈
                    height = Marker.SIZE_AUTO //자동사이즈
                    isIconPerspectiveEnabled = true //원근감
                    captionRequestedWidth = 200 //캡션 길이
                    captionMinZoom = 12.0 //캡션 보이는 범위
                } //게시글 만들어지면 캡션, 이미지, 태그번호? 받아와야할듯 나중에 함수로 재작성하기
                resultMarker.onClickListener = Overlay.OnClickListener {
                    val rootView = findViewById<View>(R.id.fragment_container) as ViewGroup
                    val adapter = InfowindowAdapter(applicationContext, rootView, resultMarker.captionText, "서울특별시", "푸들")
                    infoWindow.adapter = adapter
                    infoWindow.position = resultMarker.position
                    //투명도 조정
                    infoWindow.alpha = 0.9f
                    //인포창 표시
                    if (resultMarker.infoWindow == null) {
                        // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                        infoWindow.open(resultMarker)
                    } else {
                        // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                        infoWindow.close()
                    }
                    false
                }
                markers.add(resultMarker)
            }
        }

        //등록하기 버튼 누르면 마커 등록 액티비티 시작
        val btn = binding.registerbtn
        btn.setOnClickListener {
            val intent = Intent(this, MarkerRegisterActivity::class.java)
            activityResult.launch(intent)
        }
        val boardbtn = binding.boardBtn
        boardbtn.setOnClickListener {
            val intent = Intent(this, Board::class.java)
            startActivity(intent)
        }
        val writebtn = binding.wbtn
        writebtn.setOnClickListener {
            val intent = Intent(this, BoardWriteActivity::class.java)
            startActivity(intent)
        }
        val pathbtn = binding.pathbtn
        val path = PathOverlay()
        pathbtn.setOnClickListener {
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
                path.color = Color.GREEN
                path.map = naverMap
            }
            else path.map = null
        }

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TOP //회원가입하면 뒤에있는 엑티비티 없애기
            startActivity(intent)
        }

    }

    //권한 요청
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //마커가 태그번호와 일치하면 보여줌, 아니면 가시성 false 함수
    private fun searchmarker(tag : String) {
        for(i in markers.indices){
            markers[i].isVisible = markers[i].tag == tag
        }
    }

    //마커 & 정보창 등록 함수
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

        //마커 등록 시험 & 누르면 태그 이름 뜨는 이벤트
        marker.onClickListener = Overlay.OnClickListener {
            val rootView = findViewById<View>(R.id.fragment_container) as ViewGroup
            val adapter = InfowindowAdapter(applicationContext, rootView, marker.captionText, "서울특별시", "푸들")
            infoWindow.onClickListener = Overlay.OnClickListener {
                true
            }
            infoWindow.adapter = adapter
            infoWindow.position = marker.position
            //투명도 조정
            infoWindow.alpha = 0.9f
            //인포창 표시
            if (marker.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow.open(marker)
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow.close()
            }
            false
        }

        //마커를 마커 배열에 추가
        markers.add(marker)

    }

    //맵 레디 콜백
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
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
        addMarker(LatLng(37.57152, 126.97714), "0", naverMap, "제로")
        addMarker(LatLng(37.56607, 126.98268), "0", naverMap, "제로")
        addMarker(LatLng(37.56445, 126.97707), "0", naverMap, "제로")
        addMarker(LatLng(37.55855, 126.97822), "0", naverMap, "제로")
    }

    public override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    public override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
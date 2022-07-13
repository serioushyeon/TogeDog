package com.example.dangdangee

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dangdangee.auth.IntroActivity
import com.example.dangdangee.board.Board
import com.example.dangdangee.board.BoardWriteActivity
import com.example.dangdangee.databinding.ActivityIntroBinding
import com.example.dangdangee.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource


class MainActivity : AppCompatActivity() , OnMapReadyCallback{

    lateinit var mapView: MapView //지도를 표시할 뷰
    private lateinit var locationSource: FusedLocationSource //현재 위치 정보를 위한 것
    private lateinit var naverMap: NaverMap //지도
    private lateinit var viewModel : MapViewModel //아직 쓸모 없음, 태그 번호 부여시 사용했음
    private var markers = ArrayList<Marker>() //마커를 담는 배열, 나중에는 데이터베이스에서 마커 정보 가져와야 할 듯
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //네이버 지도 불러오기 준비
        mapView = findViewById<View>(R.id.map_view) as MapView
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
                var resultMarker = Marker().apply {
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
                    //captionText = "" 나중에 게시글 등록시 이름 받아와서 캡션 추가
                    onClickListener = Overlay.OnClickListener {
                        Toast.makeText(application, tag.toString(), Toast.LENGTH_SHORT).show()
                        false
                    }
                } //게시글 만들어지면 캡션, 이미지, 태그번호? 받아와야할듯 나중에 함수로 재작성하기
                markers.add(resultMarker)
            }
        }

        //등록하기 버튼 누르면 마커 등록 액티비티 시작
        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener {
            val intent = Intent(this, MarkerRegisterActivity::class.java)
            activityResult.launch(intent)
        }

        val writebtn = findViewById<Button>(R.id.wbtn)
        writebtn.setOnClickListener {
            val intent = Intent(this, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        val boardbtn = findViewById<Button>(R.id.boardBtn)
        boardbtn.setOnClickListener {
            val intent = Intent(this, Board::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.logoutBtn).setOnClickListener {
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

    //마커 등록 함수
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
            Toast.makeText(application, marker.tag.toString(), Toast.LENGTH_SHORT).show()
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
        addMarker(LatLng(37.5670135, 126.9783740), "0", naverMap, "제로")
        addMarker(LatLng(37.6670135, 126.9783740), "0", naverMap, "제로")
        addMarker(LatLng(37.7670135, 126.9783740), "0", naverMap, "제로")
        addMarker(LatLng(37.8670135, 126.9783740), "0", naverMap, "제로")
        addMarker(LatLng(37.9670135, 126.9783740), "0", naverMap, "제로")

        //마커 태그가 0인 것만 표시 (특정 마커만 표시)
        val btn2 = findViewById<Button>(R.id.button2)
        btn2.setOnClickListener {
            searchmarker("0")
        }

        //마커 태그가 1인 것만 표시 (특정 마커만 표시)
        val btn3 = findViewById<Button>(R.id.button3)
        btn3.setOnClickListener {
            searchmarker("1")
        }
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
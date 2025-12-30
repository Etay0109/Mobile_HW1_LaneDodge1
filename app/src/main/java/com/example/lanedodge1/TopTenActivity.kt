package com.example.lanedodge1

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.lanedodge1.interfaces.CallbackMapReady
import com.example.lanedodge1.ui.MapFragment
import com.example.lanedodge1.ui.TopTenListFragment
import com.example.lanedodge1.interfaces.CallbackRecordClicked

class TopTenActivity : AppCompatActivity() {

    private lateinit var topTen_FRAME_list: FrameLayout
    private lateinit var topTen_FRAME_map: FrameLayout

    private lateinit var listFragment: TopTenListFragment
    private lateinit var mapFragment: MapFragment

    private var isMapReady = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_ten)

        findViews()
        initViews()
    }

    private fun findViews() {
        topTen_FRAME_list = findViewById(R.id.main_FRAME_topTenList)
        topTen_FRAME_map = findViewById(R.id.main_FRAME_map)
    }

    private fun initViews() {
        listFragment = TopTenListFragment()
        mapFragment = MapFragment()

        mapFragment.callbackMapReady =
            object : CallbackMapReady {
                override fun mapReady() {
                    isMapReady = true
                }
            }

        listFragment.callbackRecordClicked =
            object : CallbackRecordClicked {
                override fun recordClicked(lat: Double, lon: Double) {
                    if (isMapReady) {
                        val currentMapFragment =
                            supportFragmentManager.findFragmentById(
                                R.id.main_FRAME_map
                            ) as MapFragment

                        currentMapFragment.zoom(lat, lon)

                    }
                }
            }
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_topTenList, listFragment)
            .add(R.id.main_FRAME_map, mapFragment)
            .commit()
    }
}

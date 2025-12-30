package com.example.lanedodge1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lanedodge1.R
import com.example.lanedodge1.interfaces.CallbackMapReady
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions



class MapFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    var callbackMapReady: CallbackMapReady? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val v = inflater.inflate(
            R.layout.fragment_map,
            container,
            false)

        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager
            .beginTransaction()
            .replace(R.id.google_map, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
        return v
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        callbackMapReady?.mapReady()

        val start = LatLng(32.0853, 34.7818)
        googleMap?.addMarker(MarkerOptions().position(start))
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(start, 10f)
        )
    }

    fun zoom(lat: Double, lon: Double) {
        val map = googleMap ?: return

        val point = LatLng(lat, lon)
        map.clear()
        map.addMarker(MarkerOptions().position(point))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(point, 14f)
        )
    }
}


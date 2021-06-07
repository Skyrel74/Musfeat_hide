package com.example.musfeat.view.map

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musfeat.AppConstants
import com.example.musfeat.R
import com.example.musfeat.view.MainActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_map.*
import moxy.MvpAppCompatFragment
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


@AndroidEntryPoint
class MapFragment : MvpAppCompatFragment(R.layout.fragment_map), MapView {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.title = getString(R.string.map_title)
        (activity as MainActivity).showNavView(true)
        (activity as MainActivity).showBackBtn(false)

        if (!EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
            EasyPermissions.requestPermissions(
                requireActivity(), "Разрешить доступ к геолокации?",
                AppConstants.RC_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MapKitFactory.setApiKey(AppConstants.MAP_KIT_API_KEY)
        MapKitFactory.initialize(requireContext())
        val mapKit = MapKitFactory.getInstance()
        mapKit.createLocationManager().requestSingleUpdate(object : LocationListener {

            @AfterPermissionGranted(AppConstants.RC_LOCATION)
            override fun onLocationUpdated(location: Location) {
                mapView?.map?.move(
                    CameraPosition(location.position, 14.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 0F),
                    null
                )
            }

            override fun onLocationStatusUpdated(location: LocationStatus) {}
        })

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }
}

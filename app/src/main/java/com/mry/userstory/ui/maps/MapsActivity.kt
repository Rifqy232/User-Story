package com.mry.userstory.ui.maps

import android.content.res.Resources
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.mry.userstory.R
import com.mry.userstory.data.CustomResult
import com.mry.userstory.databinding.ActivityMapsBinding
import com.mry.userstory.utils.ViewModelFactory
import java.io.IOException
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsViewModel: MapsViewModel by viewModels {
        ViewModelFactory(this@MapsActivity)
    }
    private val boundBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()
        getLocations()
    }

    private fun getLocations() {
        mapsViewModel.getStoriesLocation(1).observe(this) { result ->
            Log.d("TES", "Loading")
            if (result != null) {
                when (result) {
                    is CustomResult.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is CustomResult.Success -> {
                        binding.progressBar.visibility = View.GONE
                        result.data.listStory.forEach {
                            val latLng = LatLng(it.lat as Double, it.lon as Double)
                            val addressName = getAddressName(it.lat, it.lon)
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(it.name)
                                    .snippet(addressName)
                            )
                            boundBuilder.include(latLng)
                        }

                        val bounds: LatLngBounds = boundBuilder.build()
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds,
                                resources.displayMetrics.widthPixels,
                                resources.displayMetrics.heightPixels,
                                50
                            )
                        )
                    }

                    is CustomResult.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Error happened", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        try {
            @Suppress("DEPRECATION")
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                Log.d(TAG, "getAddressName: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return addressName
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Toast.makeText(this, "Style parsing failed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Resources.NotFoundException) {
            Toast.makeText(this, "Can't find style. Error: " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private val TAG = MapsActivity::class.java.simpleName
    }
}
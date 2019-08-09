package com.patel.gomap.ui
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.stetho.Stetho
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.patel.gomap.R
import com.patel.gomap.adapter.RecyclerViewAdapter
import com.patel.gomap.model.PinData
import com.patel.gomap.viewModel.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), OnMapReadyCallback, LocationEngineListener, PermissionsListener,
    RecyclerViewAdapter.OnItemClickListener{
    companion object{
        var requestGranted = true
    }

    private lateinit var mAdapter: RecyclerViewAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewModel : MainViewModel
    private lateinit var mapBoxMap: MapboxMap
    private var arrivalMarker : Marker? = null
    private val marker = MarkerOptions()
    private var mDisposable =  CompositeDisposable()
    private lateinit var originPosition: com.mapbox.geojson.Point
    lateinit var originLocation: Location
    private lateinit var destination : com.mapbox.geojson.Point
    private var locationLayerPlugin: LocationLayerPlugin? = null
    private lateinit var permissionsManager: PermissionsManager
    private var locationEngine: LocationEngine? = null
    private var load = false
    var providerFactory : ViewModelProviderFactory? = null
        @Inject set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Mapbox.getInstance(this,getString(R.string.token))
        mRecyclerView =recyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        Stetho.initializeWithDefaults(this)


    }
    override fun onMapReady(mapboxMap: MapboxMap) {
        mapBoxMap= mapboxMap
        mViewModel = ViewModelProviders.of(this, providerFactory).get(MainViewModel::class.java)
        mViewModel.PinDataAPICall().observe(this, object: Observer<List<PinData>>{
            override fun onChanged(t: List<PinData>) {
                for(i in t.indices){
                    mDisposable.add(mViewModel.addPinData(t[i])
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe())
                }
                mAdapter = RecyclerViewAdapter(applicationContext, t)
                mRecyclerView.adapter = mAdapter
                mAdapter.setOnItemClickListener(this@MainActivity)
            }

        })
        locationPermission()
    }

    override fun itemClick(position: Int) {
        if(requestGranted && load) {
            mViewModel.allPinData.observe(this, object : Observer<List<PinData>> {
                override fun onChanged(t: List<PinData>) {
                    if (arrivalMarker != null) {
                        mapBoxMap.removeMarker(arrivalMarker!!)
                    }
                    destination = com.mapbox.geojson.Point.fromLngLat(t[position].longitude, t[position].latitude)
                    originPosition =
                        com.mapbox.geojson.Point.fromLngLat(originLocation.longitude, originLocation.latitude)
                    marker.position = LatLng(t[position].latitude, t[position].longitude)
                    arrivalMarker = mapBoxMap.addMarker(marker)
                    val pos: CameraPosition =
                        CameraPosition.Builder().target(LatLng(t[position].latitude, t[position].longitude)).zoom(11.0)
                            .build()
                    mapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos), 1000)
                }

            })
        }
    }
    @SuppressWarnings("missingPermission")
    private fun locationPermission(){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            locationEngine = LocationEngineProvider(this).obtainBestLocationEngineAvailable()
            locationEngine!!.priority = LocationEnginePriority.HIGH_ACCURACY
            locationEngine!!.activate()
            locationEngine!!.addLocationEngineListener(this)
            locationLayerPlugin = LocationLayerPlugin(mapView, mapBoxMap, locationEngine)
            locationLayerPlugin!!.setLocationLayerEnabled(true)
            locationLayerPlugin!!.cameraMode = CameraMode.TRACKING
            locationLayerPlugin!!.renderMode = RenderMode.NORMAL

        }else{
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    private fun setCameraLocation(location: Location){
        mapBoxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,
            location.longitude), 11.0))
    }

    override fun onLocationChanged(location: Location?) {
        if(location != null){
            originLocation = location
            setCameraLocation(location)
            load = true
        }
    }

    @SuppressWarnings("missingPermission")
    override fun onConnected() {
        locationEngine!!.requestLocationUpdates()
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(applicationContext, "This app needs you current location", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if(granted){
            locationPermission()
        }else{
            requestGranted = false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    @SuppressWarnings("missingPermission")
    override fun onStart() {
        super.onStart()
        if(locationEngine != null) {
            locationEngine!!.requestLocationUpdates()
        }
        if(locationLayerPlugin != null) {
            locationLayerPlugin!!.onStart()
        }
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        if(locationLayerPlugin != null) {
            locationLayerPlugin!!.onStop()
        }
        if(locationEngine != null) {
            locationEngine!!.removeLocationUpdates()
        }
        mDisposable.clear()
        mapView.onStop()
    }


    override fun onDestroy() {
        super.onDestroy()
        if(locationEngine != null){
            locationEngine!!.deactivate()
        }
        mDisposable.add(mViewModel.deleteAllPinData().
            subscribeOn(Schedulers.io()).
            observeOn(AndroidSchedulers.mainThread()).
            subscribe())
        mDisposable.clear()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}

package com.rishav.pokemonapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        loadPokemon()
    }

    private val accessLocation = 123
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23)
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), accessLocation)
                return
            }
        getLocation()
    }

    @SuppressLint("ShowToast")
    fun getLocation() {
        Toast.makeText(this, "Accessing your location", Toast.LENGTH_LONG)

        var myLocation = MyLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)
        myThread().start()
    }

    @SuppressLint("ShowToast")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            accessLocation -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getLocation()
                else
                    Toast.makeText(this, "Need to grant location permission", Toast.LENGTH_LONG)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    var location: Location? = null

    inner class MyLocationListener : LocationListener {

        constructor() {
            location = Location("Start")
            location!!.longitude = 0.0
            location!!.latitude = 0.0
        }

        override fun onLocationChanged(p0: Location) {
            location = p0
        }

    }

    var oldLocation: Location? = null

    inner class myThread : Thread {
        constructor() : super() {
            oldLocation = Location("Start")
            oldLocation!!.latitude = 0.0
            oldLocation!!.longitude = 0.0
        }

        override fun run() {
            while (true) {
                try {
                    if (oldLocation!!.distanceTo(location) == 0f)
                        continue

                    oldLocation = location

                    runOnUiThread {
                        mMap!!.clear()
                        // Add a marker in Sydney and move the camera
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").snippet("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))

                        listPokemon.forEach {
                            if (it.isCatch == false) {
                                val pokemonLocation = LatLng(it.latitude!!, it.longitude!!)
                                mMap.addMarker(MarkerOptions().position(pokemonLocation).title(it.name).snippet(it.description + "power: " + it.power).icon(BitmapDescriptorFactory.fromResource(it.image!!)))
                            }

                        }
                    }
                    Thread.sleep(1000)
                } catch (ex: Exception) {

                }
            }
        }
    }

    private var listPokemon = ArrayList<Pokemon>()

    fun loadPokemon() {
        listPokemon.add(Pokemon(R.drawable.charmander, "charmander", "From Japan", 55.0, 37.33, 122.0, false))
        listPokemon.add(Pokemon(R.drawable.bulbasaur, "bulbasaur", "From USA", 90.0, 37.798, -122.4329, false))
        listPokemon.add(Pokemon(R.drawable.squirtle, "squirtle", "From USA", 33.0, 37.2203, -122.4883, false))
    }
}

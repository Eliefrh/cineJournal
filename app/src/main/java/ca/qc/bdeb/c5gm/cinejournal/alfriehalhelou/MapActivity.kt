package ca.qc.bdeb.c5gm.cinejournal.alfriehalhelou

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapActivity : AppCompatActivity(), MapListener {
    lateinit var mMap: MapView
    lateinit var controller: IMapController
    lateinit var mMyLocationOverlay: MyLocationNewOverlay
    private var userMarker: org.osmdroid.views.overlay.Marker? = null
    private val LOCATION_PERMISSION_CODE = 1
    private var savedUserMarkerLatitude: Double? = null
    private var savedUserMarkerLongitude: Double? = null

    override fun onStart() {
        super.onStart()
        //demander la permission de la localisation
        requestLocationPermission()

    }
    override fun onResume() {
        super.onResume()

        // Restaurer l'état de l'utilisateur si le marqueur existe
        if (savedUserMarkerLatitude != null && savedUserMarkerLongitude != null) {
            val startPoint = GeoPoint(savedUserMarkerLatitude!!, savedUserMarkerLongitude!!)
            userMarker = org.osmdroid.views.overlay.Marker(mMap)
            userMarker?.position = startPoint
            mMap.overlays.add(userMarker)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        var toolbar: Toolbar = findViewById(R.id.toolbar_map)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Map"
            setDisplayHomeAsUpEnabled(true)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


    }

    //Demander la permission de la localisation
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE
            )
        } else {
            initializeMap()
        }
    }

    // Création de la carte et recherche de la localisation après l'obtention de l'autorisation.
    private fun initializeMap() {
        mMap = findViewById(R.id.osmmap)

        Configuration.getInstance().load(
            applicationContext, getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )


        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.mapCenter
        mMap.setMultiTouchControls(true)
        mMap.getLocalVisibleRect(Rect())

        mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mMap)
        controller = mMap.controller
//        controller.setCenter(GeoPoint(45.5017, -73.5673))
        mMyLocationOverlay.enableMyLocation()
//        mMyLocationOverlay.enableFollowLocation()
        mMyLocationOverlay.isDrawAccuracyEnabled = true
        mMyLocationOverlay.runOnFirstFix {
            runOnUiThread {
                controller.setCenter(GeoPoint(45.5017, -73.5673))
            }
        }
        controller.setZoom(12.0)
        mMap.overlays.add(mMyLocationOverlay)
        mMap.setMultiTouchControls(true)
        mMap.addMapListener(this)

        //Ajouter les pins sur la carte
        addHardcodedPins()

        // Recherche de la localisation actuelle de l'utilisateur
        val buttonGetLocation = findViewById<Button>(R.id.buttonObtenirLocation)
        val disableButton = intent.getBooleanExtra("disableButton", false)
        buttonGetLocation.isEnabled = !disableButton
        buttonGetLocation.setOnClickListener {
            if (mMyLocationOverlay.myLocation != null) {
                val latitude = mMyLocationOverlay.myLocation.latitude
                val longitude = mMyLocationOverlay.myLocation.longitude
                Log.d("Location", "Latitude: $latitude, Longitude: $longitude")

                val resultIntent = Intent()
                resultIntent.putExtra("latitude", latitude)
                resultIntent.putExtra("longitude", longitude)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(
                    this, "La localisation n'est pas encore disponible", Toast.LENGTH_SHORT
                ).show()
            }
        }


        val buttonMyLocationView = findViewById<Button>(R.id.buttonMyLocationView)
//        val disableButton = intent.getBooleanExtra("disableButton", false)
        buttonMyLocationView.isEnabled = !disableButton
        buttonMyLocationView.setOnClickListener {

            centerMapToMyLocation()
        }

    }


    //Ajouter les pins sur la carte
    private fun addHardcodedPins() {
        val database: AppDatabase = AppDatabase.getDatabase(applicationContext)
        lifecycleScope.launch {
            val film = withContext(Dispatchers.IO) { database.FilmDao().getPosition() }

            for (i in film) {
                val latitude = i.latitude
                val longitude = i.longitude

                val pinMarker = org.osmdroid.views.overlay.Marker(mMap)
                pinMarker.icon = ResourcesCompat.getDrawable(
                    resources, R.mipmap.ic_position_film_foreground, null
                )
                if (latitude != null && longitude != null) {
                    pinMarker.position = GeoPoint(latitude, longitude)
                }
                mMap.overlays.add(pinMarker)
            }
        }
    }


    override fun onScroll(event: ScrollEvent?): Boolean {
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap()
            } else {
                Toast.makeText(
                    this,
                    "Veuillez activer la permission de localisation pour utiliser cette application.",
                    Toast.LENGTH_LONG
                ).show()
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    showPermissionSettingsDialog()
                } else {
                    requestLocationPermission()
                }
            }
        }
    }

    private fun centerMapToMyLocation() {
        if (mMyLocationOverlay.myLocation != null) {
            val myLocation = mMyLocationOverlay.myLocation
            val myLocationPoint = GeoPoint(myLocation.latitude, myLocation.longitude)
            controller.animateTo(
                myLocationPoint, 15.0, 2000L
            )
        }
    }

    private fun showPermissionSettingsDialog() {

        AlertDialog.Builder(this).setTitle("Permission requise").setMessage(
            "Veuillez activer l'autorisation de localisation dans les paramètres de " +
                    "l'application pour utiliser cette fonctionnalité."
        ).setPositiveButton("Paramètres") { _, _ ->
            openAppSettings()
        }.setNegativeButton("Annuler") { dialog, _ ->
            dialog.dismiss()
        }.show()
    }

    //ouvrir les parametres du telephone
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}


package sa.edu.tuwaiq.hagzy.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import sa.edu.tuwaiq.hagzy.R
import sa.edu.tuwaiq.hagzy.databinding.ActivityMainBinding
import sa.edu.tuwaiq.hagzy.repositories.ApiServiceRepository
import sa.edu.tuwaiq.hagzy.repositories.RoomServiceRepository
import sa.edu.tuwaiq.hagzy.view.main.PhotosViewModel

private const val TAG = "MainActivity"
private const val REQUEST_CODE_LP = 101
private const val REQUEST_CODE_GPS_AD = 102

   /* the main functionality to call photo by location (longitude,latitude)
       to achieve that we need user permission to access the location,
       also we need the GPS enabled.
       otherwise, we get most resent photo on flicker
       */


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var mLocationManager : LocationManager


    // Creating an instance of the PhotosViewModel to update the lat and long values
    private val viewModel: PhotosViewModel by viewModels()

    // Location provider variable
    private lateinit var fusedLocationProviderClint: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // check for Gps before call photo
        if(isGpsEnabled())
            getPhotoByLocation()
        else
            alertGpsDialog()

        ApiServiceRepository.init(this) // init for the Repository then we use it any where

        //initialization of the database repository
        RoomServiceRepository.init(this)

        // using binding -->> no need for findViewById method
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClint = LocationServices.getFusedLocationProviderClient(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
        // to link the nav bottom with nav host
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()

    }


    //*********************************************************************************************//
    //*********************************************************************************************//

    // This function check for result came from checkLocationPermission() function then call photos
    // so it check if user granted or denied the permission
    // granted -> getPhotoByLocation ,, denied -> callRecentPhotos (most Recent Photos in flicker)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE_LP -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted -> Get Photo baseOn user Location
                    getPhotoByLocation()
                } else {
                    // request is cancelled, so get most Recent Photos from flicker API (RecentPhotos don't depend on location)
                    viewModel.callRecentPhotos()
                }
                return
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------//

    // This function get the location then get the photo by that location
    private fun getPhotoByLocation() {

        Log.d(TAG, "fetchLocation")


        checkLocationPermission()
        var task = fusedLocationProviderClint.lastLocation

        Log.d(TAG, "task" + task.exception)


        task.addOnSuccessListener { location ->

            Log.d(TAG, "addOnSuccessListener")

            try {

                if (location != null) {
                    Log.d(TAG, "!= null")

                    // Assign the lat and long variables in the view model, why?
                    // We need to pass these data to the main/home fragment
                    viewModel.latitude = location.latitude
                    viewModel.longitude = location.longitude

                    Log.d(TAG, "log ${location.longitude} ,  lat ${location.latitude}")

                    viewModel.callPhotos() // because we want the call after getting the location of the user so we add callPhotos() method here
                } else {
                    Log.d(TAG, "null : log ${location?.longitude} ,  lat ${location?.latitude}")
                    getPhotoByLocation()
                }

            } catch (e: Exception) {

                Log.d(TAG, " catch handling : ${e.message}")

            }

        }

    }

    //-----------------------------------------------------------------------------------------------------------//

    // This function check Location Permission
    // if not granted -> ask for Permission
    private fun checkLocationPermission(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LP
            )
//            Log.d(TAG, "return ActivityCompat.requestPermissions")
//            return
        }

    }

    //*********************************************************************************************//
    //*********************************************************************************************//

    // This function check if GPS enabled or not
    private fun isGpsEnabled():Boolean {

        // Calling Location Manager
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Checking GPS is enabled
        val mGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        return mGPS
    }

    //-----------------------------------------------------------------------------------------------------------//

    // This function show alert Dialog to ask user to on GPS
    // press OK -> GPS settings will open
    // press cancel -> callRecentPhotos (most Recent Photos in flicker)
    private fun alertGpsDialog() {

        val gpsAlertDialog = AlertDialog.Builder(this@MainActivity)
        gpsAlertDialog.setIcon(R.drawable.location_icon) //set alertdialog icon
        gpsAlertDialog.setTitle(R.string.alert_Dialog_title) //set alertdialog title
        gpsAlertDialog.setMessage(R.string.alert_Dialog_message) //set alertdialog message
        gpsAlertDialog.setPositiveButton(R.string.alert_Dialog_positiveButton) { dialog, id ->
            //if user chose OK
            Log.d(TAG, "alertGpsDialog : OK")
            startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),REQUEST_CODE_GPS_AD)
        }
        gpsAlertDialog.setNegativeButton(R.string.alert_Dialog_negativeButton) { dialog, id ->
            //if user chose NO
            Log.d(TAG, "alertGpsDialog : Cancel")
            viewModel.callRecentPhotos()
        }
        gpsAlertDialog.show()

    }

    //-----------------------------------------------------------------------------------------------------------//

    // This function check if user enabled GPS or not (after Intent finish)
    // GPS on -> getPhotoByLocation ,, GPS off -> callRecentPhotos (most Recent Photos in flicker)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG,"onActivityResult")

        when(requestCode){

            REQUEST_CODE_GPS_AD -> if(isGpsEnabled()){
                getPhotoByLocation()
                Log.d(TAG,"onActivityResult requestCode if")
            }else{
                viewModel.callRecentPhotos()
            }

        }

    }

    }
package sa.edu.tuwaiq.hagzy.view

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import sa.edu.tuwaiq.hagzy.R
import sa.edu.tuwaiq.hagzy.databinding.ActivityMainBinding
import sa.edu.tuwaiq.hagzy.repositories.ApiServiceRepository
import sa.edu.tuwaiq.hagzy.repositories.RoomServiceRepository
import sa.edu.tuwaiq.hagzy.view.main.PhotosViewModel

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    // Creating an instance of the PhotosViewModel to update the lat and long values
    private val viewModel: PhotosViewModel by viewModels()

    // Location provider variable
    private lateinit var fusedLocationProviderClint: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ApiServiceRepository.init() // init for the Repository then we use it any where

        //initialization of the database repository
        RoomServiceRepository.init(this)

        // using binding -->> no need for findViewById method
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClint = LocationServices.getFusedLocationProviderClient(this)

        fetchLocation()

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

    private fun fetchLocation() {

        Log.d(TAG, "fetchLocation")

        val task = fusedLocationProviderClint.lastLocation

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        task.addOnSuccessListener { location ->

            Log.d(TAG, "addOnSuccessListener")

            if (location != null) {
                Log.d(TAG, "!= null")

                // Assign the lat and long variables in the view model, why?
                // We need to pass these data to the main/home fragment
                viewModel.latitude = location.latitude
                viewModel.longitude = location.longitude

                viewModel.callPhotos() // because we want the call when app start so we add it in onViewCreated

                Log.d(TAG, "log ${viewModel.longitude} ,  lat ${viewModel.latitude}")
            } else {
                Log.d(TAG, " null")
                Log.d(TAG, "log ${location?.longitude} ,  lat ${location?.latitude}")

            }
        }

    }


}
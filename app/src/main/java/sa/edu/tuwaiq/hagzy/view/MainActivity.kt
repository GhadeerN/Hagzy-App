package sa.edu.tuwaiq.hagzy.view

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import sa.edu.tuwaiq.hagzy.databinding.ActivityMainBinding
import sa.edu.tuwaiq.hagzy.repositories.ApiServiceRepository
import sa.edu.tuwaiq.hagzy.view.main.PhotosViewModel

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Creating an instance of the PhotosViewModel to update the lat and long values
    private val viewModel: PhotosViewModel by viewModels()

    // Location provider variable
    private lateinit var fusedLocationProviderClint : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ApiServiceRepository.init(this) // init for the Repository then we use it any where

        // using binding -->> no need for findViewById method
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClint = LocationServices.getFusedLocationProviderClient(this)

        fetchLocation()

    }

    private fun fetchLocation() {

        Log.d(TAG,"fetchLocation")

        val task = fusedLocationProviderClint.lastLocation

        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }

        task.addOnSuccessListener {location ->

            Log.d(TAG, "addOnSuccessListener")

            if (location != null) {
                Log.d(TAG, "!= null")

                // Assign the lat and long variables in the view model, why?
                // We need to pass these data to the main/home fragment
                viewModel.latitude = location.latitude
                viewModel.longitude = location.longitude

                Log.d(TAG, "log ${location.longitude} ,  lat ${location.latitude}")
            } else {
                Log.d(TAG, " null")
                Log.d(TAG, "log ${location?.longitude} ,  lat ${location?.latitude}")

            }
        }

    }


}
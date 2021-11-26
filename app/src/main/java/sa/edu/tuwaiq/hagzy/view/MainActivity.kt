package sa.edu.tuwaiq.hagzy.view

import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import sa.edu.tuwaiq.hagzy.databinding.ActivityMainBinding
import sa.edu.tuwaiq.hagzy.repositories.ApiServiceRepository

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedPrefEditor: SharedPreferences.Editor

    // Maps variables
    private lateinit var fusedLocationProviderClint : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // SharedPref initialization - to set the values for the lat and long
//        sharedPref = this.getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE)
//        sharedPrefEditor = sharedPref.edit()

        ApiServiceRepository.init(this) // init for the Repository then we use it any where

        // using binding -->> no need for findViewById method
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClint = LocationServices.getFusedLocationProviderClient(this)


        binding.buttonLocation.setOnClickListener {
            Log.d(TAG,"buttonLocation")
            fetchLocation()
        }

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

        task.addOnSuccessListener {

            Log.d(TAG,"addOnSuccessListener")

            if (it != null){
                Log.d(TAG,"!= null")

                Log.d(TAG,"log ${it.longitude} ,  lat ${it.latitude}")
            }else{
                Log.d(TAG," null")
                Log.d(TAG,"log ${it?.longitude} ,  lat ${it?.latitude}")

            }

        }

    }


}
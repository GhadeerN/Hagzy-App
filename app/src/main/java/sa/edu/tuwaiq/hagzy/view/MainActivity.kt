package sa.edu.tuwaiq.hagzy.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import sa.edu.tuwaiq.hagzy.R
import sa.edu.tuwaiq.hagzy.databinding.ActivityMainBinding
import sa.edu.tuwaiq.hagzy.repositories.ApiServiceRepository
import sa.edu.tuwaiq.hagzy.view.main.PhotosViewModel

private const val TAG = "MainActivity"
private const val REQUEST_CODE = 101
class MainActivity : AppCompatActivity() {

    private lateinit var mLocationManager : LocationManager


    private lateinit var binding: ActivityMainBinding

    // Creating an instance of the PhotosViewModel to update the lat and long values
    private val viewModel: PhotosViewModel by viewModels()

    // Location provider variable
    private lateinit var fusedLocationProviderClint: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        ApiServiceRepository.init(this) // init for the Repository then we use it any where

        // using binding -->> no need for findViewById method
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClint = LocationServices.getFusedLocationProviderClient(this)

       // Calling Location Manager
         mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Checking GPS is enabled
        val mGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!mGPS){

            val mAlertDialog = AlertDialog.Builder(this@MainActivity)
            mAlertDialog.setIcon(R.drawable.location_icon) //set alertdialog icon
            mAlertDialog.setTitle("turn location on !") //set alertdialog title
            mAlertDialog.setMessage("you have enable the location so we get you nearby photo!") //set alertdialog message
            mAlertDialog.setPositiveButton("Yes") { dialog, id ->
                //perform some tasks here
                Toast.makeText(this@MainActivity, "Yes", Toast.LENGTH_SHORT).show()
                startActivityForResult(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),0)
            }
            mAlertDialog.setNegativeButton("No") { dialog, id ->
                //perform som tasks here
                Toast.makeText(this@MainActivity, "No", Toast.LENGTH_SHORT).show()
            }
            mAlertDialog.show()


        }else{

            fetchLocation()

        }
                 Log.d(TAG,"$mGPS")


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG,"onActivityResult")

        when(requestCode){

            0 -> if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                fetchLocation()
                Log.d(TAG,"onActivityResult requestCode if")
            }else{
                fetchPhoto()

            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    fetchLocation()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    fetchPhoto()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun fetchPhoto() {
         // Assign the lat and long variables in the view model, why?
        // We need to pass these data to the main/home fragment

        viewModel.callRecentPhotos() // because we want the call after getting the location of the user so we add callPhotos() method here

    }


    private fun fetchLocation() {

            Log.d(TAG, "fetchLocation")

            checkPermission(REQUEST_CODE)
            Log.d(TAG, "after parmition ")

            var task = fusedLocationProviderClint.lastLocation

            Log.d(TAG, "task" + task.exception)


            task.addOnSuccessListener { location ->

                Log.d(TAG, "addOnSuccessListener")

                //TODO 2 add try and catch
                try {

                    if (location != null) {
                        Log.d(TAG, "!= null")

                        // Assign the lat and long variables in the view model, why?
                        // We need to pass these data to the main/home fragment
                        viewModel.latitude = location.latitude
                        viewModel.longitude = location.longitude

                        Log.d(
                            TAG,
                            "viewModel.longitude ${viewModel.longitude} ,  viewModel.latitude ${viewModel.latitude}"
                        )

                        Log.d(TAG, "log ${location.longitude} ,  lat ${location.latitude}")

                        //TODO : 1 add viewModel.callPhotos() , erase it from HomeFragment2
                        //Event
                        viewModel.callPhotos() // because we want the call after getting the location of the user so we add callPhotos() method here
                    } else {
                        Log.d(TAG, " null")
                        Log.d(TAG, "log ${location?.longitude} ,  lat ${location?.latitude}")
                        fetchLocation()
                    }

                } catch (e: Exception) {

                    Log.d(TAG, " catch handling : ${e.message}")

                }

            }

        }

   fun checkPermission(requestCode: Int){

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
               requestCode
           )
           Log.d(TAG, "return ActivityCompat.requestPermissions")
           return
       }

   }

}
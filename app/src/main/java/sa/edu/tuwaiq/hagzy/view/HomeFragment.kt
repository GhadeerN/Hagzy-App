package sa.edu.tuwaiq.hagzy.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels

import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices

import sa.edu.tuwaiq.hagzy.R
import sa.edu.tuwaiq.hagzy.databinding.FragmentHomeBinding
import sa.edu.tuwaiq.hagzy.view.adapters.PhotosRecyclerViewAdapter
import sa.edu.tuwaiq.hagzy.view.main.PhotosViewModel

private const val TAG = "Home_Fragment"
private const val REQUEST_CODE_LP = 101
private const val REQUEST_CODE_GPS_AD = 102



/* the main functionality to call photo by location (longitude,latitude)
    to achieve that we need user permission to access the location,
    also we need the GPS enabled.
    otherwise, we get default photo with default latitude & longitude (of Hail, Saudi Arabia )

    */

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var photoAdapter: PhotosRecyclerViewAdapter
    private val photoViewModel: PhotosViewModel by activityViewModels()
    private lateinit var mLocationManager : LocationManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // we pass productViewModel to use it in favoriteToggleButton in adapter
        photoAdapter = PhotosRecyclerViewAdapter(requireActivity(), photoViewModel)
        binding.homeRecyclerView.adapter = photoAdapter



        // check for Gps before call photo
        if(isGpsEnabled())
            getPhotoByLocation()
        else
            alertGpsDialog()

        observers()

    }




    //------------------------------------------------------------------------------------------------//

    private fun observers() {
        photoViewModel.photosLiveData.observe(viewLifecycleOwner, {

            Log.d(TAG, "photosLiveData observers ")
            binding.homeProgressBar.animate().alpha(0f).setDuration(1000)
            photoAdapter.submitList(it.photos.photo)
            binding.homeRecyclerView.animate().alpha(1f)
        })

        photoViewModel.databaseLiveData.observe(viewLifecycleOwner, {
            binding.homeProgressBar.animate().alpha(0f).setDuration(1000)
            photoAdapter.submitList(it)
            binding.homeRecyclerView.animate().alpha(1f)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favorite_item -> {
                findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.main_activity_top_app_bar, menu)
    }

    //------------------------------------------------------------------------------------------------//

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

        Log.d(TAG,"onRequestPermissionsResult")
        when (requestCode) {
            REQUEST_CODE_LP -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted -> Get Photo baseOn user Location
                    getPhotoByLocation()
                } else {

                    // request is cancelled, so get the  Default Photo (with Default latitude,longitude)
                    photoViewModel.callDefaultPhoto()
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

        // Location provider variable
        val fusedLocationProviderClint = LocationServices.getFusedLocationProviderClient(requireActivity())
        var task = fusedLocationProviderClint.lastLocation

        Log.d(TAG, "task" + task.exception)


        task.addOnSuccessListener { location ->

            Log.d(TAG, "addOnSuccessListener")

            try {

                if (location != null) {
                    Log.d(TAG, "!= null")

                    // Assign the lat and long variables in the view model, why?
                    // We need to pass these data to the main/home fragment
                    photoViewModel.latitude = location.latitude
                    photoViewModel.longitude = location.longitude

                    Log.d(TAG, "log ${location.longitude} ,  lat ${location.latitude}")

                    photoViewModel.callPhotos() // because we want the call after getting the location of the user so we add callPhotos() method here
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
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
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
        mLocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Checking GPS is enabled
        val mGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        return mGPS
    }

    //-----------------------------------------------------------------------------------------------------------//

    // This function show alert Dialog to ask user t o on GPS
    // press OK -> GPS settings will open
    // press cancel -> callRecentPhotos (most Recent Photos in flicker)
    private fun alertGpsDialog() {

        val gpsAlertDialog = AlertDialog.Builder(requireActivity())
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
            // request is cancelled, so get the  Default Photo (with Default latitude,longitude)
            photoViewModel.callDefaultPhoto()
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
                // request is cancelled, so get the  Default Photo (with Default latitude,longitude)
                Log.d(TAG,"onActivityResult requestCode else")

                photoViewModel.callDefaultPhoto()
            }

        }

    }

}
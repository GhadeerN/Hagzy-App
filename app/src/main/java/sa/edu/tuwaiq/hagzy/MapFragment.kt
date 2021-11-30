package sa.edu.tuwaiq.hagzy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import sa.edu.tuwaiq.hagzy.view.main.MapViewModel
import sa.edu.tuwaiq.hagzy.view.main.PhotosViewModel

private const val TAG = "MapFragment"

class MapFragment : Fragment() {

    private val viewModel: PhotosViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()

    // Location provider variable
    private lateinit var fusedLocationProviderClint: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        fusedLocationProviderClint =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        Log.d(TAG, "Viewmodel -> lat: ${viewModel.latitude}, long: ${viewModel.longitude}")

        /* In order to display the map, we added a fragment view inside the fragment_map.xml (R.id.map_fragment).
           This code is to access the map fragment view that lays inside our fragment_map.xml layout.
           Notice: To access a nested fragment we use the childFragmentManager. */
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?

        // Display the google map inside the (R.id.map_fragment) view.
        mapFragment!!.getMapAsync { googleMap ->
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            /* Take the user current location (latitude, longitude) from the PhotoViewModel - Everytime our users will open the map,
               their current location will shown via the red pin/marker. */
            val latitude = viewModel.latitude
            val longitude = viewModel.longitude
            Log.d(
                TAG,
                "Inside the mapAsync -> lat: ${viewModel.latitude}, long: ${viewModel.longitude}"
            )

            placeMarker(googleMap, latitude, longitude)

            setLocationOnClick(googleMap)
        }
        return rootView
    }

    /* This function will listen to the user on click event
    * It takes the GoogleMap instance to listen to the click event. */
    private fun setLocationOnClick(map: GoogleMap) {
        Log.d(TAG, "OnClickListener: Outside")
        map.setOnMapClickListener {
            placeMarker(map, it.latitude, it.longitude)
            Log.d(TAG, "OnClickListener: Inside. Lat: ${it.latitude}, Long: ${it.longitude}")

            // On the user click event -> it will take the new lat and long then it will store it on the map view model
            mapViewModel.latitude = it.latitude
            mapViewModel.longitude = it.longitude

            findNavController().navigate(R.id.action_mapFragment_to_mapResultsFragment)

        }
    }

    /* This function will place the red marker on the map, based on the provided lat and long. */
    private fun placeMarker(googleMap: GoogleMap, lat: Double, long: Double) {
        // 1. Clear old markers from the map
        googleMap.clear()

        // 2. Decide on camera position and the amount of zoom (12%) in the map
        val cameraPosition = CameraPosition.builder()
            .target(LatLng(lat, long))
            .zoom(12f)
            .bearing(0f)
            .tilt(45f)
            .build()

        // 3. Animate the camera position to the specified location (lat, long) - this will give a nice animation when the user pick a new location
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        // 4. Place a marker on the map based on given position
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(lat, long))
//                .title("Your location")
        )
    }
}
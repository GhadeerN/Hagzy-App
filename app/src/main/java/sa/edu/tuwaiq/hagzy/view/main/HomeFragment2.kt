package sa.edu.tuwaiq.hagzy.view.main

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import sa.edu.tuwaiq.hagzy.R
import sa.edu.tuwaiq.hagzy.databinding.ActivityMainBinding
import sa.edu.tuwaiq.hagzy.databinding.FragmentHome2Binding
import sa.edu.tuwaiq.hagzy.repositories.LATE_KEY
import sa.edu.tuwaiq.hagzy.repositories.LONG_KEY
import sa.edu.tuwaiq.hagzy.repositories.SHARED_PREF_FILE
import sa.edu.tuwaiq.hagzy.view.adapters.PhotosRecyclerViewAdapter
import java.lang.Exception

private const val TAG = "HomeFragment2"
private const val LAT = "37.7994"
private const val LON = "122.3950"

class HomeFragment2 : Fragment() {

    private lateinit var binding: FragmentHome2Binding

    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedPrefEditor: SharedPreferences.Editor

    private lateinit var photoAdapter: PhotosRecyclerViewAdapter
    private val photoViewModel : PhotosViewModel by activityViewModels()

    // Maps variables
    private lateinit var fusedLocationProvider: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())

//        sharedPref = requireActivity().getSharedPreferences(SHARED_PREF_FILE, AppCompatActivity.MODE_PRIVATE)
//        sharedPrefEditor = sharedPref.edit()

        binding = FragmentHome2Binding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // we pass productViewModel to use it in favoriteToggleButton in adapter
        photoAdapter = PhotosRecyclerViewAdapter()
        binding.recyclerView1.adapter = photoAdapter


        observers()

        //Event
          //photoViewModel.callPhotos() // because we want the call when app start so we add it in onViewCreated

    }

    // this function to observe the changes in the live data (observe for call)
    private fun observers(){
        photoViewModel.photosLiveData.observe(viewLifecycleOwner,{
            binding.photoProgressBar1.animate().alpha(0f).setDuration(1000)
            photoAdapter.submitList(it.photos.photo)
            binding.recyclerView1.animate().alpha(1f)

        })

    }
}
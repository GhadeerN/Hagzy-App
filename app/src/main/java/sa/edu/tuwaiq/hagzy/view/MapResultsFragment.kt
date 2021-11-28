package sa.edu.tuwaiq.hagzy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import sa.edu.tuwaiq.hagzy.R
import sa.edu.tuwaiq.hagzy.databinding.FragmentMapResultsBinding
import sa.edu.tuwaiq.hagzy.view.adapters.PhotosRecyclerViewAdapter
import sa.edu.tuwaiq.hagzy.view.main.MapViewModel

class MapResultsFragment : Fragment() {

    private lateinit var binding: FragmentMapResultsBinding
    private val mapViewModel: MapViewModel by activityViewModels()
    private lateinit var photoAdapter: PhotosRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoAdapter = PhotosRecyclerViewAdapter()
        binding.mapResultRecyclerView.adapter = photoAdapter

        mapViewModel.callPhotos(mapViewModel.latitude, mapViewModel.longitude)

        observers()
    }

    // this function to observe the changes in the live data (observe for call)
    private fun observers() {
        mapViewModel.mapResultsLiveData.observe(viewLifecycleOwner, { data ->
            data?.let {
                binding.mapResultsProgressBar.animate().alpha(0f).setDuration(1000)
                photoAdapter.submitList(it.photos.photo)
                binding.mapResultRecyclerView.animate().alpha(1f)
            }
            mapViewModel.mapResultsLiveData.postValue(null)
        })

    }
}
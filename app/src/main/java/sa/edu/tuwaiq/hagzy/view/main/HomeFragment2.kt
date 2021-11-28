package sa.edu.tuwaiq.hagzy.view.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import sa.edu.tuwaiq.hagzy.databinding.FragmentHome2Binding
import sa.edu.tuwaiq.hagzy.view.adapters.PhotosRecyclerViewAdapter

private const val TAG = "HomeFragment2"
private const val LAT = "37.7994"
private const val LON = "122.3950"

class HomeFragment2 : Fragment() {

    private lateinit var binding: FragmentHome2Binding

    private lateinit var photoAdapter: PhotosRecyclerViewAdapter
    private val photoViewModel: PhotosViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHome2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // we pass productViewModel to use it in favoriteToggleButton in adapter
        photoAdapter = PhotosRecyclerViewAdapter(requireActivity())
        binding.recyclerView1.adapter = photoAdapter

        observers()

//        //Event
//        photoViewModel.callPhotos() // because we want the call when app start so we add it in onViewCreated

    }

    // this function to observe the changes in the live data (observe for call)
    private fun observers() {
        photoViewModel.photosLiveData.observe(viewLifecycleOwner, {
            binding.photoProgressBar1.animate().alpha(0f).setDuration(1000)
            photoAdapter.submitList(it.photos.photo)
            binding.recyclerView1.animate().alpha(1f)

        })

        photoViewModel.databaseLiveData.observe(viewLifecycleOwner, {
            binding.photoProgressBar1.animate().alpha(0f).setDuration(1000)
            photoAdapter.submitList(it)
            binding.recyclerView1.animate().alpha(1f)
        })

    }
}
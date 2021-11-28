package sa.edu.tuwaiq.hagzy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import sa.edu.tuwaiq.hagzy.R
import sa.edu.tuwaiq.hagzy.databinding.ActivityMainBinding
import sa.edu.tuwaiq.hagzy.databinding.FragmentHomeBinding
import sa.edu.tuwaiq.hagzy.model.PhotoModel
import sa.edu.tuwaiq.hagzy.view.adapters.PhotosRecyclerViewAdapter
import sa.edu.tuwaiq.hagzy.view.main.PhotosViewModel


class Home_Fragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var photoAdapter: PhotosRecyclerViewAdapter
    private val photoViewModel: PhotosViewModel by activityViewModels()


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
        photoAdapter = PhotosRecyclerViewAdapter(requireActivity())
        binding.homeRecyclerView.adapter = photoAdapter

        observers()

        //Event
        photoViewModel.callPhotos() // because we want the call when app start so we add it in onViewCreated
    }

    private fun observers() {
        photoViewModel.photosLiveData.observe(viewLifecycleOwner, {

            binding.homeProgressBar.animate().alpha(0f).setDuration(1000)
            photoAdapter.submitList(it.photos.photo)
            binding.homeRecyclerView.animate().alpha(1f)
        })
    }

}
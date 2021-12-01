package sa.edu.tuwaiq.hagzy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
//import androidx.navigation.fragment.findNavController
import sa.edu.tuwaiq.hagzy.databinding.FragmentFavoriteBinding
import sa.edu.tuwaiq.hagzy.view.adapters.FavoriteRecyclerViewAdapter
import sa.edu.tuwaiq.hagzy.view.main.FavoriteViewModel

private const val TAG = "FavoriteFragment"

class FavoriteFragment : Fragment() {


    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    private lateinit var favoriteAdapter: FavoriteRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // call fun observers()
        observers()

        // we pass productViewModel to use it in favoriteToggleButton in adapter
        favoriteAdapter = FavoriteRecyclerViewAdapter()
        binding.favoriteRecyclerView.adapter = favoriteAdapter


        //Event
        favoriteViewModel.callFavorite() // because we want the call when app start so we add it in onViewCreated
    }

    fun observers() {
        favoriteViewModel.favoriteLiveData.observe(viewLifecycleOwner, {

            binding.favoriteProgressBar.animate().alpha(0f).setDuration(1000)
            favoriteAdapter.submitList(it)
            binding.favoriteRecyclerView.animate().alpha(1f)
        })

        favoriteViewModel.favoriteErrorLiveData.observe(viewLifecycleOwner, {
            binding.favoriteProgressBar.animate().alpha(0f).setDuration(1000)
        })
    }


}
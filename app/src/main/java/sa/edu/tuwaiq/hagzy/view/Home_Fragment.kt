package sa.edu.tuwaiq.hagzy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sa.edu.tuwaiq.hagzy.R
import sa.edu.tuwaiq.hagzy.databinding.ActivityMainBinding
import sa.edu.tuwaiq.hagzy.databinding.FragmentHomeBinding
import sa.edu.tuwaiq.hagzy.model.PhotoModel


class Home_Fragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private var allPhotos = listOf<PhotoModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

}
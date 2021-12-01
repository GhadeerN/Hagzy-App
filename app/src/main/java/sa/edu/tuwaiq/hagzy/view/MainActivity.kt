package sa.edu.tuwaiq.hagzy.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import sa.edu.tuwaiq.hagzy.R
import sa.edu.tuwaiq.hagzy.databinding.ActivityMainBinding
import sa.edu.tuwaiq.hagzy.repositories.ApiServiceRepository
import sa.edu.tuwaiq.hagzy.repositories.RoomServiceRepository

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Hagzy)
        super.onCreate(savedInstanceState)


        ApiServiceRepository.init() // init for the Repository then we use it any where

        //initialization of the database repository
        RoomServiceRepository.init(this)

        // using binding -->> no need for findViewById method
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
        // to link the nav bottom with nav host
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()

    }

    }
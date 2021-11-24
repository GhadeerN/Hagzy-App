package sa.edu.tuwaiq.hagzy.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sa.edu.tuwaiq.hagzy.R
import sa.edu.tuwaiq.hagzy.repositories.ApiServiceRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ApiServiceRepository.init(this) // init for the Repository then we use it any where

        setContentView(R.layout.activity_main)




    }
}
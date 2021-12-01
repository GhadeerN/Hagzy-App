package sa.edu.tuwaiq.hagzy.view.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.hagzy.model.Photo
import sa.edu.tuwaiq.hagzy.model.PhotoModel
import sa.edu.tuwaiq.hagzy.repositories.ApiServiceRepository
import sa.edu.tuwaiq.hagzy.repositories.RoomServiceRepository
import java.lang.Exception

private const val TAG = "FavoriteViewModel"

class FavoriteViewModel : ViewModel() {
    private val apiRepo = ApiServiceRepository.get()
    private val roomRepo = RoomServiceRepository.get()

    // Getting instance from Room Service Repository with companion object function
    private val databaseRepo = RoomServiceRepository.get()

    // for get the live data
    val favoriteLiveData = MutableLiveData<List<Photo>>()

    // live data for error
    val favoriteErrorLiveData = MutableLiveData<String>()

    //live data for the database
    val databaseLiveData = MutableLiveData<List<Photo>>()


    fun callFavorite() {

        // we need Scope with the suspend function
        //viewModelScope -->> the Scope  end after the function end
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, this.toString())

            // send request
            try {
                val response = roomRepo.getFavoritePhotos()
                Log.d(TAG, "favorite posted response: $response")
                response.run {
                    favoriteLiveData.postValue(this)
//                        databaseLiveData.postValue(this)
                    Log.d(TAG, this.toString())

                    // Save response in local database
                    Log.d(TAG, this.toString())
                }

            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                favoriteErrorLiveData.postValue(e.message.toString())

                // in case of an error caused by the internet this line will send an error response to view.

                databaseLiveData.postValue(databaseRepo.getPhotos())
                Log.d(TAG, e.message.toString())
                Log.d(TAG, "Database - catch: ${databaseRepo.getPhotos()}")

            }
        }

    }
    // Update favorite image - toggle button
    fun updateFavoritePhoto(photo: Photo) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepo.updatePhoto(photo)
        }
    }
}
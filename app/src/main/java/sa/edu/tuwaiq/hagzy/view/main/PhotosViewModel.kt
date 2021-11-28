package sa.edu.tuwaiq.hagzy.view.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.hagzy.model.Photo
import sa.edu.tuwaiq.hagzy.model.PhotoModel
import sa.edu.tuwaiq.hagzy.model.Photos
import sa.edu.tuwaiq.hagzy.repositories.ApiServiceRepository
import sa.edu.tuwaiq.hagzy.repositories.RoomServiceRepository
import java.lang.Exception

private const val TAG = "PhotosViewModel"
class PhotosViewModel: ViewModel(){

    private val apiRepo = ApiServiceRepository.get()

    // Getting instance from Room Service Repository with companion object function
    private val databaseRepo = RoomServiceRepository.get()

    // for get the live data
    val  photosLiveData = MutableLiveData<PhotoModel>()

    // live data for error
    val photosErrorLiveData = MutableLiveData<String>()

    //live data for the database
    val databaseLiveData = MutableLiveData<List<Photo>>()



    // lat and long variables for the location
    var latitude = 0.0
    var longitude = 0.0

    // for just call request
    fun callPhotos(){

        // we need Scope with the suspend function
        //viewModelScope -->> the Scope  end after the function end
        viewModelScope.launch (Dispatchers.IO){

            try {
                // send request
                val response = apiRepo.getPhotos(latitude, longitude)

                if (response.isSuccessful){
                    response.body()?.run {
                        Log.d(TAG,this.toString())
                        photosLiveData.postValue(this)

                        // Save response in local database
                        databaseRepo.insertPhotos(photos.photo)
                        Log.d(TAG,this.toString())
                    }
                }else{
                    Log.d(TAG,"else"+response.message())
                    photosErrorLiveData.postValue(response.message())

                    // in case of internet failure for getting the data  this line will send an error response to view.
                    databaseLiveData.postValue(databaseRepo.getPhotos())
                    Log.d(TAG,"else"+response.message())

                    Log.d(TAG, "Database: ${databaseRepo.getPhotos()}")
                }
            }catch (e:Exception){
                Log.d(TAG,e.message.toString())
                photosErrorLiveData.postValue(e.message.toString())

                // in case of an error caused by the internet this line will send an error response to view.

                databaseLiveData.postValue(databaseRepo.getPhotos())
                Log.d(TAG,e.message.toString())
                Log.d(TAG, "Database - catch: ${databaseRepo.getPhotos()}")

            }
        }
    }

}
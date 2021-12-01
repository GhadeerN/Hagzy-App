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
private const val LAT = 27.523647 // latitude of Hail, Saudi Arabia
private const val LON = 41.696632 // longitude of Hail, Saudi Arabia

class PhotosViewModel : ViewModel() {

    private val apiRepo = ApiServiceRepository.get()

    // Getting instance from Room Service Repository with companion object function
    private val databaseRepo = RoomServiceRepository.get()

    // for get the live data
    val photosLiveData = MutableLiveData<PhotoModel>()

    // live data for error
    val photosErrorLiveData = MutableLiveData<String>()

    //live data for the database
    val databaseLiveData = MutableLiveData<List<Photo>>()

    // Map result live data
    val mapResultsLiveData = MutableLiveData<PhotoModel>()


    // Lat and long variables for the user current location
    var latitude = 0.0
    var longitude = 0.0

    // Lat and long variables the new selected location on the map
    var mapLat = 0.0
    var mapLong = 0.0

    //
    var page = 1

    // for just call request
    fun callPhotos() {

        // we need Scope with the suspend function
        //viewModelScope -->> the Scope  end after the function end
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "log ${longitude} ,  lat ${latitude}")
            try {
                // send request
                val response = apiRepo.getPhotos(latitude, longitude, page)


                if (response.isSuccessful) {
                    response.body()?.run {
                        Log.d(TAG, this.toString())
                        photosLiveData.postValue(this)
                        page++
                        Log.d(TAG, photosLiveData.toString())

                        // TODO Delete old stored photos, Why? because it will add the new location photo to the old one
//                        databaseRepo.deleteAllPhotos()

                        // Save response in local database
                        databaseRepo.insertPhotos(photos.photo)
                        Log.d(TAG, this.toString())
                    }
                } else {
                    Log.d(TAG, "else" + response.message())
                    photosErrorLiveData.postValue(response.message())

                    // in case of internet failure for getting the data  this line will send an error response to view.
                    databaseLiveData.postValue(databaseRepo.getPhotos())
                    Log.d(TAG, "else" + response.message())

                    Log.d(TAG, "Database: ${databaseRepo.getPhotos()}")
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                photosErrorLiveData.postValue(e.message.toString())

                // in case of an error caused by the internet this line will send an error response to view.

                databaseLiveData.postValue(databaseRepo.getPhotos())
                Log.d(TAG, e.message.toString())
                Log.d(TAG, "Database - catch: ${databaseRepo.getPhotos()}")

            }
        }
    }

    /* This function is to call the photos based on the lat and long of the new location specified
    *  by our user on the map.
    *  The callPhoto will take 2 parameters: lat and long, then it will post the result into the
    *  mapResultsLiveData variable.
    *  Note: Creating a function with the same name, but different number of parameters with different
    *  data type, called function overloading.  */
    fun callPhotos(lat: Double, long: Double) {

        viewModelScope.launch (Dispatchers.IO){
            try {
                // send request
                val response = apiRepo.getPhotos(lat, long, page)
                Log.d(TAG, "HERE Map: LAT: $lat, LONG: $long")
                if (response.isSuccessful){
                    response.body()?.run {
                        Log.d(TAG,this.toString())
                        mapResultsLiveData.postValue(this)
                    }
                }else{
                    Log.d(TAG,"else"+response.message())
                    photosErrorLiveData.postValue(response.message())
                }
            }catch (e: Exception){
                Log.d(TAG,e.message.toString())
                photosErrorLiveData.postValue(e.message.toString())
            }
        }
    }

    // for just call request
    fun callDefaultPhoto() {

        Log.d(TAG, " callDefaultPhoto log ${longitude} ,  lat ${latitude}")

        // we need Scope with the suspend function
        //viewModelScope -->> the Scope  end after the function end
        viewModelScope.launch(Dispatchers.IO) {

            try {
                // send request
                val response = apiRepo.getPhotos(LAT, LON, page)

                if (response.isSuccessful) {
                    response.body()?.run {
                        Log.d(TAG, this.toString())
                        photosLiveData.postValue(this)
                    }
                } else {
                    Log.d(TAG, "else" + response.message())
                    photosErrorLiveData.postValue(response.message())
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                photosErrorLiveData.postValue(e.message.toString())
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
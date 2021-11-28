package sa.edu.tuwaiq.hagzy.view.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.hagzy.model.PhotoModel
import sa.edu.tuwaiq.hagzy.repositories.ApiServiceRepository
import java.lang.Exception

private const val TAG = "PhotosViewModel"


class PhotosViewModel: ViewModel(){

    private val apiRepo = ApiServiceRepository.get()

    // for get the live data
    val  photosLiveData = MutableLiveData<PhotoModel>()

    // live data for error
    val photosErrorLiveData = MutableLiveData<String>()

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

                Log.d(TAG,"normal ")
                Log.d(TAG,"latitude $latitude   longitude $longitude ")

                if (response.isSuccessful){
                    response.body()?.run {
                        Log.d(TAG,this.toString())
                        photosLiveData.postValue(this)
                    }
                }else{
                    Log.d(TAG,"else"+response.message())
                    photosErrorLiveData.postValue(response.message())
                }
            }catch (e:Exception){
                Log.d(TAG,e.message.toString())
                photosErrorLiveData.postValue(e.message.toString())
            }
        }
    }

    // for just call request
    fun callRecentPhotos(){

        // we need Scope with the suspend function
        //viewModelScope -->> the Scope  end after the function end
        viewModelScope.launch (Dispatchers.IO){

            try {
                // send request
                val response = apiRepo.getRecentPhotos()

                if (response.isSuccessful){
                    response.body()?.run {
                        Log.d(TAG,this.toString())
                        photosLiveData.postValue(this)
                    }
                }else{
                    Log.d(TAG,"else"+response.message())
                    photosErrorLiveData.postValue(response.message())
                }
            }catch (e:Exception){
                Log.d(TAG,e.message.toString())
                photosErrorLiveData.postValue(e.message.toString())
            }
        }
    }

}
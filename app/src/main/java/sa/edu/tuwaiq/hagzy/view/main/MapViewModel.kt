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

private const val TAG = "MapViewModel"
class MapViewModel: ViewModel() {
    private val apiRepo = ApiServiceRepository.get()

    // lat and long variables for the location
    var latitude = 0.0
    var longitude = 0.0

    // for map results live data
    val mapResultsLiveData = MutableLiveData<PhotoModel>()

    // live data for error
    val mapResultsErrorLiveData = MutableLiveData<String>()

    /* This function is to call the photos based on the lat and long of the new location specified
    *  by our user on the map.
    *  The callPhoto will take 2 parameters: lat and long, then it will post the result into the
    *  mapResultsLiveData variable.
    *  */

    fun callPhotos(lat: Double, long: Double) {
        viewModelScope.launch (Dispatchers.IO){

            try {
                // send request
                val response = apiRepo.getPhotos(lat, long)
                Log.d(TAG, "HERE Map: LAT: $lat, LONG: $long")
                if (response.isSuccessful){
                    response.body()?.run {
                        Log.d(TAG,this.toString())
                        mapResultsLiveData.postValue(this)
                    }
                }else{
                    Log.d(TAG,"else"+response.message())
                    mapResultsErrorLiveData.postValue(response.message())
                }
            }catch (e: Exception){
                Log.d(TAG,e.message.toString())
                mapResultsErrorLiveData.postValue(e.message.toString())
            }
        }
    }
}
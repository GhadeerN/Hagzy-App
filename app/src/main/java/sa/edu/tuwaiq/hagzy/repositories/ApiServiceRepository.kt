package sa.edu.tuwaiq.hagzy.repositories

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sa.edu.tuwaiq.hagzy.api.FlickerApi


private const val BASE_URL = "https://api.flickr.com/services"

class ApiServiceRepository(val context: Context) {

    private val retrofitService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitApi =retrofitService.create(FlickerApi::class.java)
     // this request gets the photos from the flickr api with it's latitude and longitude
    suspend fun getPhotos(lat:Double,lon:Double) = retrofitApi.getPhotos(lat,lon) // lat:Latitude,lon:Longitude

//--------------------------------------------//

    // to initialize and get the repository we use the companion object
    //singleton (single object)
    companion object{
        private var instance:ApiServiceRepository?= null

        fun init (context: Context){
            if(instance == null)
                instance = ApiServiceRepository(context)
        }
        fun get(): ApiServiceRepository{
            return instance?: throw  Exception("ApiServiceRepository must be initialized")
        }
    }

}
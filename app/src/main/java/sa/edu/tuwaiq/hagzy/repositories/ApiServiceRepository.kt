package sa.edu.tuwaiq.hagzy.repositories

import android.content.Context
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query
import sa.edu.tuwaiq.hagzy.api.FlickerApi



private const val BASE_URL = "https://api.flickr.com"
const val SHARED_PREF_FILE = "LatLong"
const val LATE_KEY = "latitude"
const val LONG_KEY = "longitude"

class ApiServiceRepository() {

    //To Solve use jsonreader.setlenient(true) to accept malformed json at line 1 column 1 path $
//    private var gson = GsonBuilder()
//        .setLenient()
//        .create()
//
//    private var retrofitService = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create(gson))
//        .build()


    private val retrofitService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitApi = retrofitService.create(FlickerApi::class.java)

    // this request gets the photos from the flickr api with it's latitude and longitude
    suspend fun getPhotos(latitude: Double, longitude: Double, page: Int) =
        retrofitApi.getPhotos(latitude, longitude) // lat:Latitude,lon:Longitude

//--------------------------------------------//



    // to initialize and get the repository we use the companion object
    //singleton (single object)
    companion object {
        private var instance: ApiServiceRepository? = null

        fun init() {
            if (instance == null)
                instance = ApiServiceRepository()
        }

        fun get(): ApiServiceRepository {
            return instance ?: throw  Exception("ApiServiceRepository must be initialized")
        }
    }

}
package sa.edu.tuwaiq.hagzy.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import sa.edu.tuwaiq.hagzy.model.PhotoModel

interface FlickerApi {

    //----------------------------- For Photos -----------------------------------------------------------//


    //https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=f1c0ab5110600a9f1116e57088c308d1&lat=37.7994&lon=122.3950&format=json&nojsoncallback=1&extras=url_m,owner_name,views&per_page=10
    @GET("/services/rest/?method=flickr.photos.search&api_key=f1c0ab5110600a9f1116e57088c308d1&format=json&nojsoncallback=1&extras=url_m,owner_name,views&per_page=10") // we always want page0 so we wrote in link
    // suspend fun -->> for using curtain later
    suspend fun getPhotos(
        @Query("lat") lat:String,
        @Query("lon") lon:String
    ) : Response<PhotoModel> // use Response<PhotoModel> so we have  Response state (success or not(status code))


}
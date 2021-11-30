package sa.edu.tuwaiq.hagzy.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import sa.edu.tuwaiq.hagzy.model.PhotoModel

interface FlickerApi {

    //----------------------------- For Photos -----------------------------------------------------------//


    // Get Photo by Location
    //https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=f1c0ab5110600a9f1116e57088c308d1&lat=37.7994&lon=122.3950&format=json&extras=url_m,owner_name,views,date_upload,date_taken&per_page=20&nojsoncallback=1
    @GET("/services/rest/?method=flickr.photos.search&api_key=f1c0ab5110600a9f1116e57088c308d1&format=json&extras=url_m,owner_name,views,date_upload,date_taken&per_page=20&nojsoncallback=1") // we always want page0 so we wrote in link
    // suspend fun -->> for using curtain later
    suspend fun getPhotos(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<PhotoModel> // use Response<PhotoModel> so we have  Response state (success or not(status code))


    // Get Recent Photo

    //https://www.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=f1c0ab5110600a9f1116e57088c308d1&extras=url_s%2Curl_m%2Cowner_name&format=json&nojsoncallback=1
    @GET("/services/rest/?method=flickr.photos.getRecent&api_key=f1c0ab5110600a9f1116e57088c308d1&extras=url_s%2Curl_m%2Cowner_name&format=json&nojsoncallback=1") // we always want page0 so we wrote in link
    // suspend fun -->> for using curtain later
    suspend fun getRecentPhotos(): Response<PhotoModel> // use Response<PhotoModel> so we have  Response state (success or not(status code))

}
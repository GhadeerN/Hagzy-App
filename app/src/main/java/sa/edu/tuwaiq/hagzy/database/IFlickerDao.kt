package sa.edu.tuwaiq.hagzy.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import retrofit2.Response
import retrofit2.http.GET
import sa.edu.tuwaiq.hagzy.model.Photo


@Dao
interface IFlickerDao {


    //insert parameters in the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<Photo>)

//     this functions selects the data and gets us all the photo data
    @Query("SELECT * FROM photo")
    suspend fun getPhotos(): List<Photo>

    // getting the favorite products

    @Query("SELECT * FROM photo WHERE isFavorite" )
    suspend fun getFavoritePhotos() : List<Photo>


}
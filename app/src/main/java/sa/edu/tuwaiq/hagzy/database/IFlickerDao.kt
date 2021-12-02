package sa.edu.tuwaiq.hagzy.database

import androidx.room.*
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

    @Query("DELETE FROM photo")
    suspend fun deletePhotos()

    @Update
    suspend fun updatePhoto(photo: Photo)

}
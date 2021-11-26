package sa.edu.tuwaiq.hagzy.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import sa.edu.tuwaiq.hagzy.model.Photo


@Dao
interface IFlickerDao {


    //insert parameters in the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<Photo>)



}
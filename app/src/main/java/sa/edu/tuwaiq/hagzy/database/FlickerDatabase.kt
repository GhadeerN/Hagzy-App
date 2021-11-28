package sa.edu.tuwaiq.hagzy.database

import androidx.room.Database
import androidx.room.RoomDatabase
import sa.edu.tuwaiq.hagzy.model.Photo

//annotation for the data base and giving it its entity
@Database(entities = [Photo::class], version = 3)
//inheritance of the RoomDatabase to make this class a RoomDatabase
abstract class FlickerDatabase: RoomDatabase() {

    //a function of the Dao that will initialize it in the database
    abstract fun photosDao(): IFlickerDao
}
package sa.edu.tuwaiq.hagzy.repositories

import android.content.Context
import androidx.room.Room
import sa.edu.tuwaiq.hagzy.database.FlickerDatabase
import sa.edu.tuwaiq.hagzy.model.Photo
import java.lang.Exception


private const val DATABASE_NAME = "photos-database"

class RoomServiceRepository(context: Context) {


    // creating an instance of the database
    //Build the database
    //in case of any changes in the version (fallbackTo....)
    private val database = Room.databaseBuilder(context, FlickerDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration().build()


    //using the database abstract method to get an instance of the Dao
    private val iflickrDao = database.photosDao()

    //getting the methods from the Dao
    suspend fun insertPhotos(photos: List<Photo>) = iflickrDao.insertPhotos(photos)

    suspend fun getPhotos() = iflickrDao.getPhotos()


    /** companion object to create an instance of the repository the first function will
     * initialize this repository and the second one will help us get an instance of this repository
     * anywhere we want
     */

    companion object {
        // check if  this repository was not initialized  "null"
        private var instance: RoomServiceRepository? = null

        //then use this function to initialize it
        fun init(context: Context) {
            if (instance == null)
                instance = RoomServiceRepository(context)
        }

        /**as explained before, this function will return an instance of the repository
        and if there wasn't any it will throw an exception */

        fun get(): RoomServiceRepository {
            return instance ?: throw Exception("RoomService Repository Must Be Initialized!")
        }
    }

}
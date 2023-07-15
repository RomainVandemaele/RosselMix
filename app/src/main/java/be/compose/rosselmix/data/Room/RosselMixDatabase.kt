package be.compose.rosselmix.data.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import be.compose.rosselmix.data.Room.Dao.NewsDao
import be.compose.rosselmix.data.Room.Entities.News

@Database(entities = [News::class], version = 1, exportSchema = false)
abstract class RosselMixDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "news-database"
        var instance : RosselMixDatabase? = null

        fun instance(context: Context): RosselMixDatabase {
            if(instance != null) return instance!!
            instance =  Room.databaseBuilder(
                context,
                RosselMixDatabase::class.java,
                DATABASE_NAME
            ).build()
            return instance!!
        }
    }



    abstract fun newsDao(): NewsDao


}
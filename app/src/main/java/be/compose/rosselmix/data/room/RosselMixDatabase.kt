package be.compose.rosselmix.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [News::class, Newspaper::class] , version = 1, exportSchema = true)
abstract class RosselMixDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "news-database"
        private var instance : RosselMixDatabase? = null

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
    abstract fun newspaperDao(): NewspaperDao


}
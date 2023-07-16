package be.compose.rosselmix.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewspaperDao {

    companion object {
        const val TABLE_NAME = "newspaper"
        const val COLUMN_DATE = "date"
        const val COLUMN_CODE = "code"
        const val COLUMN_AUTH = "auth"
    }

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Flow<List<Newspaper>>

    @Insert
    suspend fun insertAll(vararg newspapers: Newspaper)

}
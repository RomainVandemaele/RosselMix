package be.compose.rosselmix.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    companion object {
        const val TABLE_NAME = "news"
        const val COLUMN_TITLE = "title"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_THUMBNAIL_URL = "thumbnail_url"
        const val COLUMN_ARTICLE_URL = "article_url"
    }

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Flow<List<News>>

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insert(news: News)


    @Query("DELETE FROM $TABLE_NAME WHERE $COLUMN_TITLE = :title")
    suspend fun delete(title: String)
}
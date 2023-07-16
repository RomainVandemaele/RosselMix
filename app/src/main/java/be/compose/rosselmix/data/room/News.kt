package be.compose.rosselmix.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class News (
    @PrimaryKey val title : String ,
    @ColumnInfo(name = "author") val author : String?,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl : String,
    @ColumnInfo(name = "article_url") val articleUrl : String,
)




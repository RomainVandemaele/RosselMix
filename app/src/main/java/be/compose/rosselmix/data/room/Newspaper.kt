package be.compose.rosselmix.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "newspaper")
data class Newspaper(
    @PrimaryKey val date : String,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "auth") val auth: String,
)

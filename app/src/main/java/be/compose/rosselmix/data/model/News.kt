package be.compose.rosselmix.data.model

import java.util.Date

data class News(
    val category : String = "",
    val title : String = "",
    val description : String = "",
    val url : String = "",
    val thumbnailUrl : String = "",
    val date : Date = Date(),
    val author : String? = "",
)
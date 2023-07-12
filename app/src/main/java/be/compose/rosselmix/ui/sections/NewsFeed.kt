package be.compose.rosselmix.ui.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewsFeed() {
    Column {
        Text(
            text = "News Feed",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(10) {
                NewsItem()
            }
        }
    }
}

@Composable
fun NewsItem(title : String = "Nothing happened") {
    Text(text = title)
    //TODO("Not yet implemented")
}

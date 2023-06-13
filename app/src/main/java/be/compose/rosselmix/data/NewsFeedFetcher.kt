package be.compose.rosselmix.data

import android.util.Log
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.net.URL

class NewsFeedFetcher {
    private val url = "https://www.lesoir.be/rss2/2/cible_principale?status=1"

    suspend fun fetchNewsFeed() {
        coroutineScope {
            withContext(Dispatchers.IO) {
                val feed = SyndFeedInput().build(XmlReader(URL(url)))
                Log.d("READ RSS",feed.publishedDate.toString())
            }
        }

    }
}
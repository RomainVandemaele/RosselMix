package be.compose.rosselmix.data

import android.util.Log
import be.compose.rosselmix.data.model.News
import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.feed.synd.SyndFeed
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
                feed.entries.forEach {
                    Log.d("READ RSS",it.title)
                }
                //val news = feed.toNews()
                //Log.d("READ RSS",feed.publishedDate.toString())
            }
        }

    }
}

fun SyndEntry.toNews()  : News {
    return News(
        category = this.categories.first().name,
        title = this.title,
        description = this.description.value,
        url = this.link,
        thumbnailUrl = this.enclosures.first().url,
        date = this.publishedDate,
        author = this.author
    )
}
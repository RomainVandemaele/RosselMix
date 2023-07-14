package be.compose.rosselmix.data

import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.ui.res.stringResource
import be.compose.rosselmix.R
import be.compose.rosselmix.data.model.News
import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import com.rometools.rome.io.impl.Atom10Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class NewsFeedFetcher {
    private val url = "https://www.lesoir.be/rss2/2/cible_principale?status=1"



    public suspend fun downloadXml() {
        var result: String? = null

        coroutineScope {
            withContext(Dispatchers.IO) {
                result = try {
                    loadXmlFromNetwork(url)
                }catch (e : IOException) {
                    //stringResource(id = R.string.connection_error)
                    "No connection"
                }catch( e: XmlPullParserException)  {
                    //stringResource(id = R.string.xml_error)
                    "Wrong XML"
                }
                withContext(Dispatchers.Main) {

                    //result?.let { parseXml(it) }
                }
            }
        }

        //lifecycleScope.launch(Dispatchers.IO) {}
        //return URL(url).readText()
    }

    // Uploads XML from stackoverflow.com, parses it, and combines it with
// HTML markup. Returns HTML string.
    @Throws(XmlPullParserException::class, IOException::class)
    private fun loadXmlFromNetwork(urlString: String): String {

        val stream = downloadUrl(urlString)
        val entries : List<News> = NewsParser().parse(stream!!) ?: emptyList()
//        val entries: List<News> = downloadUrl(urlString)?.use { stream ->
//            // Instantiates the parser.
//            NewsParser().parse(stream)
//        } ?: emptyList()

        entries.forEach {n ->
            Log.d("READ RSS",n.category)
            Log.d("READ RSS",n.title)
            Log.d("READ RSS",n.description)
            Log.d("READ RSS",n.url)
            Log.d("READ RSS", n.date.toString())
            Log.d("READ RSS",n.thumbnailUrl)
            Log.d("READ RSS",n.author.toString())
            Log.d("READ RSS","------------------")
        }

        return "ok"
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    @Throws(IOException::class)
    private fun downloadUrl(urlString: String): InputStream? {
        val url = URL(urlString)
        return (url.openConnection() as? HttpURLConnection)?.run {
            readTimeout = 10000
            connectTimeout = 15000
            requestMethod = "GET"
            doInput = true
            // Starts the query.
            connect()
            inputStream
        }
    }

}


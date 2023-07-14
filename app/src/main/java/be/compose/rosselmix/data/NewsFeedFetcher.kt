package be.compose.rosselmix.data

import be.compose.rosselmix.data.model.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


data class FetcherResponse(
    var news: List<News> = emptyList(),
    var errorMessage: String? = null
)

class NewsFeedFetcher {


    private val URL_BASE_ = "https://www.lesoir.be/rss2/"
    private val URL_ADD_ON = "/cible_principale?status=1"



    public suspend fun downloadXml(
        callback: (r: FetcherResponse) -> Unit,
        code: Int
    )
    {
        var result: FetcherResponse

        coroutineScope {
            withContext(Dispatchers.IO) {
                result = try {
                    loadXmlFromNetwork(code)
                }catch (e : IOException) {
                    //stringResource(id = R.string.connection_error)
                    FetcherResponse(errorMessage = "No connection")
                }catch( e: XmlPullParserException)  {
                    FetcherResponse(errorMessage = "Wrong XML")
                }catch (e: Exception) {
                    FetcherResponse(errorMessage = "Unknown error")
                }
                callback(result)

                //withContext(Dispatchers.Main) { //result?.let { parseXml(it) } }
            }
        }

        //lifecycleScope.launch(Dispatchers.IO) {}
        //return URL(url).readText()
    }

    // Uploads XML from stackoverflow.com, parses it, and combines it with
// HTML markup. Returns HTML string.
    @Throws(XmlPullParserException::class, IOException::class)
    private fun loadXmlFromNetwork(code: Int): FetcherResponse {

        val stream = downloadUrl("$URL_BASE_$code$URL_ADD_ON")
        val entries : List<News> = NewsParser().parse(stream!!) ?: emptyList()
        return FetcherResponse(entries)
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


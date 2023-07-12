package be.compose.rosselmix.data

import android.util.Xml
import androidx.compose.ui.res.stringResource
import be.compose.rosselmix.R
import be.compose.rosselmix.data.model.News
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

/**
 * Class to parse the xml file that contains news to transform it into a list of [News]
 */
class NewsParser {

    companion object {
        private const val CATEGORY = "category";
        private const val TITLE = "title";
        private const val DESCRIPTION = "description";
        private const val URL = "link";
        private const val THUMBNAIL = "media:content";
        private const val DATE = "pubDate";
        private const val AUTHOR = "dc-creator";

        private const val ENTRY = "item";
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream) : List<News> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream,null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    private fun readFeed(parser: XmlPullParser): List<News> {

        val news = mutableListOf<News>()
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "channel") { parser.nextTag() }

            if(parser.name == ENTRY) {
                news.add(readItem(parser))
            } else {
                skip(parser)
            }
        }
        return news
    }


    /**
     * Read an item tag and all its internal tags   and fields needed to create a [News] object
     */
    private fun readItem(parser: XmlPullParser): News {
        parser.require(XmlPullParser.START_TAG, null, ENTRY)
        var category : String = "";
        var title : String = "";
        var description : String = "";
        var url : String = "";
        var thumbnailUrl : String = "";
        var date : Date = Date();
        var author : String = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.eventType != XmlPullParser.START_TAG) continue

            when(parser.name) {
                CATEGORY -> category = retrieveString(parser, CATEGORY)
                TITLE-> title = retrieveString(parser, TITLE)
                DESCRIPTION -> description = retrieveString(parser, DESCRIPTION)
                URL -> url = retrieveString(parser, URL)
                THUMBNAIL -> thumbnailUrl = readThumbnailUrl(parser)
                DATE -> date = readDate(parser)
                AUTHOR -> author = retrieveString(parser, AUTHOR)
                else -> skip(parser)
            }

        }
        return News(category, title, description, url, thumbnailUrl, date, author)
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if(parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun retrieveString(parser: XmlPullParser, tag : String): String {
        parser.require(XmlPullParser.START_TAG, null, tag)
        val stringData =  readText(parser)
        parser.require(XmlPullParser.END_TAG, null, tag)
        return stringData
    }

    private fun readDate(parser: XmlPullParser): Date {
        parser.require(XmlPullParser.START_TAG, null, DATE)
        val stringData =  readText(parser)
        parser.require(XmlPullParser.END_TAG, null, DATE)
        val formatter: DateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")
        return formatter.parse(stringData)!!
    }



    private fun readThumbnailUrl(parser: XmlPullParser): String {
        var thumbnailUrl = ""
        parser.require(XmlPullParser.START_TAG, null, THUMBNAIL)
        val tag = parser.name
        val type = parser.getAttributeValue(null, "type")

        if( tag == THUMBNAIL) {
            if(type == "image/jpeg" ) {
                thumbnailUrl = parser.getAttributeValue(null, "url")
                parser.nextTag()
                while (parser.name != THUMBNAIL) { parser.nextTag() }
            }
        }

        parser.require(XmlPullParser.END_TAG, null, THUMBNAIL)
        return thumbnailUrl
    }


    /**
     * Skip tag that we don't need by keeping track of the depth of the tag to stop only when we reach the end of the initial tag
     */
    @Throws(IOException::class, XmlPullParserException::class)
    private fun skip(parser: XmlPullParser) {
        if(parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while(depth != 0) {
            when(parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

}
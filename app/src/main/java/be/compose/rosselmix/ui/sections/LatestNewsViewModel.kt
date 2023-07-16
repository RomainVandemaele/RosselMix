package be.compose.rosselmix.ui.sections

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.compose.rosselmix.data.FetcherResponse
import be.compose.rosselmix.data.NewsFeedFetcher
import be.compose.rosselmix.data.Room.RosselMixDatabase
import be.compose.rosselmix.data.model.News
import be.compose.rosselmix.utils.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.streams.toList

class LatestNewsViewModel() : ViewModel() {

    private val selectedUrl: MutableStateFlow<String?> = MutableStateFlow(null)

    private val allNews = mutableListOf<News>()

    private val _state = MutableStateFlow<LatestNewsViewState>(LatestNewsViewState())
    val state : StateFlow<LatestNewsViewState>
        get() = _state

    init {
        viewModelScope.launch {
            val jobs = mutableListOf<Job>()
            withContext(Dispatchers.IO) {
                for(category in Category.values()) {
                    jobs.add(viewModelScope.launch { NewsFeedFetcher().downloadXml(::loadNews,category.code) })
                }
            }
            jobs.joinAll()
            allNews.stream().distinct().sorted { o1 , o2 -> o2.date.compareTo(o1.date) }.toList().take(15).let {
                _state.value = _state.value.copy(
                    latestNews = it,
                    loading = false
                )
            }

        }

    }

    private fun loadNews(response : FetcherResponse) {
        if (response.errorMessage != null) return
        allNews.addAll(response.news)
    }

    fun selectArticle(url: String?) {
        selectedUrl.value = url
        _state.value = _state.value.copy(selectedArticle = selectedUrl.value)
    }

    private fun getDao(context : Context) = RosselMixDatabase.instance(context).newsDao()

    public fun bookMarkNews(context : Context, news : News) {
        viewModelScope.launch {
            getDao(context).insert(be.compose.rosselmix.data.Room.Entities.News(news.title, news.author, news.thumbnailUrl, news.url))
        }
    }

}

/**
 * Represents the state of the  news feed screen.
 * @param latestNews The list of news to display
 * @param loading Whether the news feed is loading
 * @param errorMessage An error message to display if the news feed failed to load
 */
data class LatestNewsViewState(
    val latestNews: List<News> = emptyList(),
    val selectedArticle: String? = null,
    val loading: Boolean = true,
    val errorMessage: String? = null
)
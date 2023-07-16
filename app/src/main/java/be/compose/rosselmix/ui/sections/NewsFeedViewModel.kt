package be.compose.rosselmix.ui.sections

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import be.compose.rosselmix.data.FetcherResponse
import be.compose.rosselmix.data.NewsFeedFetcher
import be.compose.rosselmix.data.room.RosselMixDatabase
import be.compose.rosselmix.data.model.News
import be.compose.rosselmix.utils.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsFeedViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val selectedCategory = MutableStateFlow(Category.BREAKING)
    private val selectedUrl: MutableStateFlow<String?> = MutableStateFlow(null)




    @OptIn(SavedStateHandleSaveableApi::class)
    var selectedUrl2: String by savedStateHandle.saveable {

        mutableStateOf("")
    }


    private val _state = MutableStateFlow<NewsFeedViewState>(NewsFeedViewState())
    val state : StateFlow<NewsFeedViewState>
        get() = _state

    init {
        viewModelScope.launch {
            NewsFeedFetcher().downloadXml(::loadState,selectedCategory.value.code)
        }
    }

    private fun getDao(context : Context) = RosselMixDatabase.instance(context).newsDao()

    public fun bookMarkNews(context : Context, news : News) {
        viewModelScope.launch {
            getDao(context).insert(
                be.compose.rosselmix.data.room.News(
                    news.title,
                    news.author,
                    news.thumbnailUrl,
                    news.url
                )
            )
        }
    }


    fun loadState(response : FetcherResponse) {
        if (response.errorMessage == null) {
            _state.value = _state.value.copy(
                newsFeed = response.news,
                loading = false
            )
        }else {
            _state.value =  _state.value.copy(
                loading = false,
                errorMessage = response.errorMessage
            )
        }
    }

    fun selectCategory(categoryCode: Int) {
        selectedCategory.value = Category.values().find { it.code == categoryCode }!!
        _state.value = _state.value.copy(selectedCategory = selectedCategory.value)
        viewModelScope.launch {
            NewsFeedFetcher().downloadXml(::loadState,selectedCategory.value.code)
        }
    }

    fun selectArticle(url: String?) {
        selectedUrl.value = url
        _state.value = _state.value.copy(selectedArticle = selectedUrl.value)
    }


}

/**
 * Represents the state of the  news feed screen.
 * @param newsFeed The list of news to display
 * @param loading Whether the news feed is loading
 * @param errorMessage An error message to display if the news feed failed to load
 */
data class NewsFeedViewState(
    val newsFeed: List<News> = emptyList(),
    val selectedCategory: Category = Category.BREAKING,
    val selectedArticle: String? = null,
    val loading: Boolean = true,
    val errorMessage: String? = null
)


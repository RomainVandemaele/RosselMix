package be.compose.rosselmix.ui.sections

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.compose.rosselmix.data.FetcherResponse
import be.compose.rosselmix.data.NewsFeedFetcher
import be.compose.rosselmix.data.model.News
import kotlinx.coroutines.launch

class NewsFeedViewModel : ViewModel() {

    private val _state = mutableStateOf<NewsFeedViewState>(NewsFeedViewState())
    val state : State<NewsFeedViewState>
        get() = _state

    init {
        viewModelScope.launch {
            NewsFeedFetcher().downloadXml(::loadState)
        }
    }

    fun loadState(response : FetcherResponse) {
        if (response.errorMessage == null) {
            _state.value = NewsFeedViewState(
                newsFeed = response.news,
                loading = false
            )
        }else {
            _state.value = NewsFeedViewState(
                loading = false,
                errorMessage = response.errorMessage
            )
        }
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
    val loading: Boolean = true,
    val errorMessage: String? = null
)


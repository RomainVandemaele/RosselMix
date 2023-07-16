package be.compose.rosselmix.ui.sections

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.compose.rosselmix.data.room.RosselMixDatabase
import be.compose.rosselmix.data.room.News
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookmarkViewModel(val context: Context) : ViewModel() {

    private val _state = MutableStateFlow<BookmarkedNewsViewState>(BookmarkedNewsViewState())
    private val selectedUrl: MutableStateFlow<String?> = MutableStateFlow(null)

    val state : StateFlow<BookmarkedNewsViewState>
        get() = _state

    init {
        loadBookmarkedNews(context)
    }

    private fun loadBookmarkedNews(context: Context) {
        viewModelScope.launch {
            getDao(context).getAll().collect() {
                _state.value = _state.value.copy(bookmarkedNews = it)
            }
        }
    }

    fun selectArticle(url: String?) {
        selectedUrl.value = url
        _state.value = _state.value.copy(selectedArticle = selectedUrl.value)
    }

    private fun getDao(context : Context) = RosselMixDatabase.instance(context).newsDao()

    fun removeBookMark(context: Context, news: News) {
        viewModelScope.launch {
            getDao(context).delete(news.title)
        }
    }

    companion object {
        //TODO : better factory than can be used ad parameter in viewModel() function
        fun factory(context: Context) = BookmarkViewModel(context)
    }

}

data class BookmarkedNewsViewState(
    val bookmarkedNews: List<News> = emptyList(),
    val selectedArticle: String? = null
)

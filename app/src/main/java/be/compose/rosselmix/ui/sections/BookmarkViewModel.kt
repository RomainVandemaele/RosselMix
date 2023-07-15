package be.compose.rosselmix.ui.sections

import androidx.lifecycle.ViewModel
import be.compose.rosselmix.data.model.News
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BookmarkViewModel : ViewModel() {

    private val _state = MutableStateFlow<BookmarkedNewsViewState>(BookmarkedNewsViewState())
    private val selectedUrl: MutableStateFlow<String?> = MutableStateFlow(null)

    val state : StateFlow<BookmarkedNewsViewState>
        get() = _state

    init {
        //TODO: Load bookmarked news from database
    }

    fun selectArticle(url: String?) {
        selectedUrl.value = url
        _state.value = _state.value.copy(selectedArticle = selectedUrl.value)
    }


}

data class BookmarkedNewsViewState(
    val bookmarkedNews: List<News> = emptyList(),
    val selectedArticle: String? = null
)

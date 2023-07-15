package be.compose.rosselmix.ui.sections

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import be.compose.rosselmix.R
import be.compose.rosselmix.data.model.News

@Composable
fun Bookmark(
    navController: NavHostController,
    viewModel: BookmarkViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val bookmarkedNews = viewModel.state.collectAsState()
    if(bookmarkedNews.value.selectedArticle != null) {
        WebViewArticle(url = bookmarkedNews.value.selectedArticle!!) {
            viewModel.selectArticle(null)
        }
    }else {
        BookmarkScreen(bookmarkedNews.value.bookmarkedNews)
    }
}

@Composable
fun BookmarkScreen(news: List<News>) {
    Header(title = R.string.bookmarks)
    LazyColumn() {
        items(news.size) {
            NewsItem(
                news[it].title,
                news[it].thumbnailUrl,
                news[it].author)
            {
                //TODO
            }
            NewsDivider()
        }
    }
}
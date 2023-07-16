package be.compose.rosselmix.ui.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import be.compose.rosselmix.R
import be.compose.rosselmix.data.room.News

@Composable
fun Bookmark(
    navController: NavHostController,
    viewModel: BookmarkViewModel
) {
    val context = LocalContext.current

    val bookmarkedNews = viewModel.state.collectAsState()
    if(bookmarkedNews.value.selectedArticle != null) {
        WebViewArticle(url = bookmarkedNews.value.selectedArticle!!) {
            viewModel.selectArticle(null)
        }
    }else {
        BookmarkScreen(
            bookmarkedNews.value.bookmarkedNews,
            onItemClick = { url -> viewModel.selectArticle(url) },
            onBookmarkClick = { news: News -> viewModel.removeBookMark(context,news) }
        )
    }
}

@Composable
fun BookmarkScreen(news: List<News>, onItemClick: (String) -> Unit, onBookmarkClick: (News) -> Unit) {
    Column() {
        Spacer(modifier = Modifier.height(16.dp))
        Header(title = R.string.bookmarks)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn() {
            items(news.size) {
                NewsItem(
                    news[it].title,
                    news[it].thumbnailUrl,
                    news[it].author,
                    onItemClick = { onItemClick(news[it].articleUrl)},
                    onBookmarkClick = { onBookmarkClick(news[it]) },
                    outlinedBookmark = true
                )
                NewsDivider()
            }
        }
    }

}
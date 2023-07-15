package be.compose.rosselmix.ui.sections

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebViewClient
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import be.compose.rosselmix.R
import be.compose.rosselmix.ui.theme.DarkBlue
import be.compose.rosselmix.ui.theme.LightBlue
import be.compose.rosselmix.utils.Category
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

private const val URL = "https://leseng.rosselcdn.net/sites/default/files/dpistyles_v2/ena_16_9_medium/2023/07/13/node_525253/30290591/public/2023/07/13/b9730411500z.1_20220328074704_000%2Bg3kk6gif9.1-0.jpeg?itok=J3loYTOu1689231371"

@Composable
fun NewsFeed(
    viewModel: NewsFeedViewModel
) {
    Column {

        val state by  viewModel.state
        //WebViewArticle(url = "https://www.lesoir.be/525628/article/2023-07-14/reforme-fiscale-voici-ce-qui-est-sur-la-table-ce-samedi")

        if(state.loading) {
            LoadingScreen()
        }else {

            if(state.selectedArticle != null) {
                WebViewArticle(
                    url = state.selectedArticle!!
                ) { viewModel.selectArticle(null) }
            }else {
                CategoryChooser(onItemClick = viewModel::selectCategory, state.selectedCategory )
                val news = state.newsFeed
                LazyColumn {
                    items(news.size) { i ->
                        Column {
                            NewsItem(
                                news[i].title,
                                news[i].thumbnailUrl,
                                news[i].author,
                                onItemClick = {viewModel.selectArticle(news[i].url) })
                            NewsDivider()
                        }
                    }
                }
            }

        }


    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun CategoryChooser(
    onItemClick: (Int) -> Unit,
    selectedCategory: Category
) {
    LazyRow() {
        items(Category.values().size) {
            val cat = Category.values()[it]
            val selected = cat == selectedCategory
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = cat.topic,
                color = if(selected) Color.Red else  DarkBlue,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(8.dp)
                    .clickable {
                        onItemClick(cat.code)
                    }
            )
        }

    }
}


@Composable
fun LoadingScreen() {
    Image(
        painter = painterResource(id = R.drawable.lesoir),
        contentDescription = "Loading screen with the logo of Le Soir",
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
}

@Composable
fun WebViewArticle(url: String, onDispose: () -> Unit) {

    val state = rememberWebViewState(url)

    AndroidView(factory = {
        WebView(it).apply {
            webViewClient = WebViewClient()
            loadUrl(url)
        }

    })
    
    //val state = rememberWebViewState(url)
//    WebView(
//        state = state,
//        //onCreated = {it.settings.javaScriptEnabled = true},
//        captureBackPresses = false,
//        onCreated = { it.settings.javaScriptEnabled = true },
//        onDispose = { Log.d("DISPOSE","disposed");onDispose() }
//    )

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItem(
    title : String,
    thumbnailUrl : String,
    author: String?,
    onItemClick: () -> Unit)
{
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(16.dp)
            .clickable(onClick = {
//                val webpage: Uri = Uri.parse("https://www.lesoir.be/525628/article/2023-07-14/reforme-fiscale-voici-ce-qui-est-sur-la-table-ce-samedi")
//                val intent = Intent(Intent.ACTION_VIEW, webpage)
//                startActivity(context,intent,null)
                onItemClick()
            }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,


    ) {
        Column (
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text =  if ( author.isNullOrBlank())"" else "${stringResource(id = R.string.author_prefix)} $author",
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = DarkBlue
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        AsyncImage(
            model = thumbnailUrl,
            contentDescription = "News illustration",
            modifier = Modifier.weight(0.5f),
            error = painterResource(id = R.drawable.lesoir) //placeholder
            )

    }

    Spacer(
        Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
    )


}

@Composable
fun NewsDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = LightBlue
    )
}


@Composable
@Preview
fun NewsItemPreview() {
    NewsItem("Nothing happened", URL,"BELGA", {  })
}

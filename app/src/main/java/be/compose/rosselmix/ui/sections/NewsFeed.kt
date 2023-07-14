package be.compose.rosselmix.ui.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import be.compose.rosselmix.R
import be.compose.rosselmix.ui.theme.DarkBlue
import be.compose.rosselmix.ui.theme.LightBlue
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

private const val URL = "https://leseng.rosselcdn.net/sites/default/files/dpistyles_v2/ena_16_9_medium/2023/07/13/node_525253/30290591/public/2023/07/13/b9730411500z.1_20220328074704_000%2Bg3kk6gif9.1-0.jpeg?itok=J3loYTOu1689231371"

@Composable
fun NewsFeed(
    viewModel: NewsFeedViewModel
) {
    Column {
        Text(
            text = "News Feed",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp),
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            textAlign = TextAlign.Center
        )
        val state by  viewModel.state

        if(state.loading) {
            LoadingScreen()
        }else {
            val news = state.newsFeed
            LazyColumn {
                items(news.size) { i ->
                    Column {
                        NewsItem(news[i].title, news[i].thumbnailUrl,news[i].author ?: "")
                        NewsDivider()
                    }
                }
            }
        }


    }
}


@Composable
fun LoadingScreen() {
    Image(
        painter = painterResource(id = R.drawable.lesoir),
        contentDescription = "Loading screen with the logo of Le Soir",
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItem(
    title : String,
    thumbnailUrl : String,
    author: String)
{
    Row(
        modifier = Modifier.padding(16.dp),
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
                text =  stringResource(id = R.string.author_prefix) +  author,
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
    NewsItem("Nothing happened", URL,"BELGA")
}

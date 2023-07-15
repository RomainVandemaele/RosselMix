package be.compose.rosselmix.ui.sections

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import be.compose.rosselmix.R
import be.compose.rosselmix.data.model.News
import be.compose.rosselmix.ui.theme.DarkBlue
import coil.compose.AsyncImage
import java.util.Date
import kotlin.time.ExperimentalTime

@Composable
fun LastestNews(
    navController: NavHostController,
    viewModel: LatestNewsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val state = viewModel.state.collectAsState()

    if(state.value.loading) {
        LoadingScreen()
    }else {
        if(state.value.selectedArticle != null) {
            WebViewArticle(url = state.value.selectedArticle!!) {
                viewModel.selectArticle(null)
            }
        }else {
            Column(
                modifier = androidx.compose.ui.Modifier
                    .padding(16.dp)
            ) {
                Header(R.string.lastest)
                Spacer(modifier = Modifier.height(24.dp))
                NewsDivider()
                Spacer(modifier = Modifier.height(16.dp))
                NewsList(
                    state.value.latestNews,
                    onClick = viewModel::selectArticle
                )
            }
        }
    }
}

@Composable
fun NewsList(
    news : List<News>,
    onClick : (String) -> Unit = {} ) {
    LazyColumn() {
        items(news.size) {
            LatestNewsItem(
                news[it].title,
                news[it].thumbnailUrl,
                news[it].date,
                news[it].author)
                {
                    onClick(news[it].url)
                }
            NewsDivider()
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun LatestNewsItem(
    title: String,
    thumbnailUrl: String,
    date: Date,
    author : String?,
    onClick : () -> Unit  )
{


    val dateFormat = String.format( "%02d:%02d", date.hours, date.minutes)
    Row(
        horizontalArrangement = Arrangement.SpaceAround,

        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() }

    ) {

        Row (modifier = Modifier.weight(0.2f)){

            Text(
                text = dateFormat,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                color = DarkBlue,
            )

            Column() {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = DarkBlue,
                    modifier = Modifier
                        .size(5.dp)
                        .padding(vertical = 5.dp)
                )

                Divider(
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .height(70.dp)
                        .width(2.dp),
                    color = DarkBlue
                )
            }

        }


        Column(
            modifier = Modifier.weight(0.55f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                maxLines = 3,
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text =  if ( author.isNullOrBlank())"" else "${stringResource(id = R.string.author_prefix)} $author",
                style = MaterialTheme.typography.bodySmall,
                color = DarkBlue
            )
        }
        AsyncImage(
            model = thumbnailUrl,
            alignment = Alignment.Center,
            contentDescription = "News illustration",
            modifier = Modifier.weight(0.3f),
            error = painterResource(id = R.drawable.lesoir) //placeholder
        )
    }

}

@Composable
fun Header(@StringRes title : Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Divider(
            modifier = Modifier
                .width(24.dp)
                .weight(1f)
        )
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.headlineLarge,
            color = DarkBlue,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1.2f)
        )
        Divider(modifier = Modifier
            .width(24.dp)
            .weight(1f))
    }

}

@Composable
@Preview
fun HeaderPreview() {
    Header(R.string.lastest)
}

@Composable
@Preview
fun NewsPreview() {
    LatestNewsItem(
        "Title",
        "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
        Date(),
        "Author"
    ) {}

}


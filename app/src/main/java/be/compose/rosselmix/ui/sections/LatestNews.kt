package be.compose.rosselmix.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import be.compose.rosselmix.R
import be.compose.rosselmix.data.model.News
import be.compose.rosselmix.ui.theme.DarkBlue
import java.util.Date

@Composable
fun LastestNews(
    navController: NavHostController,
    viewModel: LatestNewsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val state = viewModel.state.collectAsState()
    if(state.value.loading) {
        Text(text = "Loading")
    }else {
        Column(
            modifier = androidx.compose.ui.Modifier
                .padding(16.dp)
        ) {
            Header()
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

@Composable
fun NewsList(
    news : List<News>,
    onClick : (String) -> Unit = {} ) {
    LazyColumn() {
        items(news.size) {
            NewsItem(
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

@Composable
fun NewsItem(
    title: String,
    thumbnailUrl: String,
    date: Date,
    author : String?,
    onClick : () -> Unit  )
{
    Text(text = title)
}

@Composable
fun Header() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Divider(
            modifier = Modifier.width(24.dp)
                .weight(1f)
        )
        Text(
            text = stringResource(id = R.string.lastest),
            style = MaterialTheme.typography.headlineLarge,
            color = DarkBlue,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1.2f)
        )
        Divider(modifier = Modifier.width(24.dp)
            .weight(1f))
    }

}



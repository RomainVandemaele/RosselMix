package be.compose.rosselmix.ui.sections

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import be.compose.rosselmix.R
import be.compose.rosselmix.ui.theme.DarkBlue
import be.compose.rosselmix.ui.theme.LightBlue
import be.compose.rosselmix.ui.theme.RosselMixShapes
import be.compose.rosselmix.ui.theme.White
import be.compose.rosselmix.utils.Category
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun NewsFeed(
    navController: NavHostController,
    viewModel: NewsFeedViewModel = viewModel()

) {
    Column {


        val state by  viewModel.state.collectAsState()


        if(state.loading) {
            LoadingScreen()
        }else {

            if(state.selectedArticle != null) {
                //TODO : navigate to webView
                WebViewArticle(
                    url = state.selectedArticle!!
                ) { viewModel.selectArticle(null) }
            }else {
                CategoryChooser(onItemClick = viewModel::selectCategory, state.selectedCategory )
                val news = state.newsFeed
                LazyColumn {
                    items(news.size) { i ->
                        Column {
                            if(i==0)  {
                                MainNews(
                                    news[i].title,
                                    news[i].thumbnailUrl,
                                    news[i].author,
                                    onItemClick = {viewModel.selectArticle(news[i].url) })
                            }else {
                                NewsItem(
                                    news[i].title,
                                    news[i].thumbnailUrl,
                                    news[i].author,
                                    onItemClick = {viewModel.selectArticle(news[i].url) })
                            }
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
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(Category.values().size) {
            val cat = Category.values()[it]
            val selected = cat == selectedCategory
            Text(
                text = cat.topic,
                color = if(selected) White else  DarkBlue,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        onItemClick(cat.code)
                    }
                    .clip(RosselMixShapes.large)
                    .background(
                        if (!selected) MaterialTheme.colorScheme.background else DarkBlue
                    )
            )
        }

    }
}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        var progress by rememberSaveable { mutableStateOf(0.1f) }
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )

        CircularProgressIndicator(
            progress = animatedProgress,
            color = DarkBlue,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
        )
        progress = ( progress  + 0.1f) % 1f
    }
//    Image(
//        painter = painterResource(id = R.drawable.load_screen),
//        contentDescription = "Loading screen with the logo of Le Soir",
//        modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//    )
}

@Composable
fun WebViewArticle(url: String, onDispose: () -> Unit) {

    val state = rememberWebViewState(url)

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.08f)
                .background(DarkBlue),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onDispose() }
            ) {
                Icon( Icons.Outlined.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = stringResource(id = R.string.journal_name),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.labelLarge.fontSize
            )
            IconButton(onClick = { TODO("Share url") }) {
                Icon( Icons.Outlined.Share, contentDescription = "Share", tint = Color.White)
            }
        }

        WebView(
            modifier = Modifier
                .weight(0.92f),
            state = state,
            //onCreated = {it.settings.javaScriptEnabled = true},
            captureBackPresses = false,
            onCreated = {  }
        )


    }

}

@Composable
fun share(data : String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, data)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(LocalContext.current,shareIntent,null)
}

@Composable
fun MainNews(
    title : String,
    thumbnailUrl : String,
    author: String?,
    onItemClick: () -> Unit)
{
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .clickable(onClick = onItemClick)
    ) {
        AsyncImage(
            model = thumbnailUrl,
            contentDescription = "News illustration",
            contentScale = ContentScale.FillWidth,
            modifier  = Modifier
                .fillMaxSize(),
            error = painterResource(id = R.drawable.lesoir) //placeholder
        )

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp),

        )

        Text(
            text =  if ( author.isNullOrBlank())"" else "${stringResource(id = R.string.author_prefix)} $author",
            style = MaterialTheme.typography.bodySmall,
            color = DarkBlue,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
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
    val URL = "https://leseng.rosselcdn.net/sites/default/files/dpistyles_v2/ena_16_9_medium/2023/07/13/node_525253/30290591/public/2023/07/13/b9730411500z.1_20220328074704_000%2Bg3kk6gif9.1-0.jpeg?itok=J3loYTOu1689231371"
    NewsItem("Nothing happened", URL,"BELGA", {  })
}

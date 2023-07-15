package be.compose.rosselmix

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import be.compose.rosselmix.ui.navigation.Destinations
import be.compose.rosselmix.ui.sections.Bookmark
import be.compose.rosselmix.ui.sections.LastestNews
import be.compose.rosselmix.ui.sections.NewsFeed
import be.compose.rosselmix.ui.sections.NewsFeedViewModel
import be.compose.rosselmix.ui.sections.NewsPaper
import be.compose.rosselmix.ui.theme.RosselMixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RosselMixApp() {

    RosselMixTheme {

        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()


        val navBackStackEntry by navController.currentBackStackEntryAsState()


        NavHost(navController = navController, startDestination = Destinations.NEWS_ROUTE) {
            composable(Destinations.NEWS_ROUTE) { NewsFeed(NewsFeedViewModel(),navController) }
            composable(Destinations.LASTEST_ROUTE) { LastestNews(navController) }
            composable(Destinations.NEWSPAPER_ROUTE) { NewsPaper(navController) }
            composable(Destinations.BOOKMARKS_ROUTE) { Bookmark(navController) }
            //TODO: add other routes like webview and pdfview with url as parameter
        }
        //Scaffold() {}

        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NewsFeed(NewsFeedViewModel(), navController)

        }
    }

}
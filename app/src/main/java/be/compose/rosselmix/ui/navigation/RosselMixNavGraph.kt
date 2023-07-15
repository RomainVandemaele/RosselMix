package be.compose.rosselmix.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import be.compose.rosselmix.R
import be.compose.rosselmix.ui.sections.Bookmark
import be.compose.rosselmix.ui.sections.LastestNews
import be.compose.rosselmix.ui.sections.NewsFeed
import be.compose.rosselmix.ui.sections.NewsFeedViewModel
import be.compose.rosselmix.ui.sections.NewsPaper

sealed class Destinations(val route: String, @StringRes val resourceId: Int) {
    object NEWS : Destinations("news", R.string.news)
    object LATEST_NEWS : Destinations("latest", R.string.lastest)
    object NEWSPAPER : Destinations("newspaper", R.string.newspaper)
    object BOOKMARKS : Destinations("bookmarks", R.string.bookmarks)
}

@Composable
fun RosselMoxNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.NEWS
) {

    val actions = remember(navController) { MainActions(navController) }

    NavHost(navController = navController, startDestination = Destinations.NEWS.route) {
        composable(Destinations.NEWS.route) {
            NewsFeed(
                NewsFeedViewModel(),
                navController)
        }
        composable(Destinations.LATEST_NEWS.route) {
            LastestNews(navController)
        }
        composable(Destinations.NEWSPAPER.route) {
            NewsPaper(navController)
        }
        composable(Destinations.BOOKMARKS.route) {
            Bookmark(navController)
        }
        //TODO: add other routes like webview and pdfview with url as parameter
    }

}

/**
* Models the navigation actions in the app.
*/
class MainActions(navController: NavHostController) {

   // val navigateToWebview: () -> Unit = { navController.navigateUp() }
}
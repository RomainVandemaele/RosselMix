package be.compose.rosselmix.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import be.compose.rosselmix.R
import be.compose.rosselmix.ui.sections.Bookmark
import be.compose.rosselmix.ui.sections.BookmarkViewModel
import be.compose.rosselmix.ui.sections.LastestNews
import be.compose.rosselmix.ui.sections.NewsFeed
import be.compose.rosselmix.ui.sections.Newspaper
import be.compose.rosselmix.ui.sections.NewspaperViewModel

sealed class Destinations(val route: String, @StringRes val stringId: Int, @DrawableRes val iconId: Int ) {
    object NEWS : Destinations("news", R.string.news, R.drawable.ic_home_foreground)
    object LATEST_NEWS : Destinations("latest", R.string.lastest, R.drawable.ic_fil_info_foreground)
    object NEWSPAPER : Destinations("newspaper", R.string.newspaper, R.drawable.ic_journal_foreground)
    object BOOKMARKS : Destinations("bookmarks", R.string.bookmarks, R.drawable.ic_bookmark_foreground)
}

@Composable
fun RosselMoxNavGraph(
    navController: NavHostController = rememberNavController(),
    innerPadding: PaddingValues,
    startDestination: String = Destinations.NEWS.route
) {

    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Destinations.NEWS.route) {
            NewsFeed(navController)
        }
        composable(Destinations.LATEST_NEWS.route) {
            LastestNews(navController)
        }
        composable(Destinations.NEWSPAPER.route) {
            Newspaper(navController, NewspaperViewModel.factory(LocalContext.current) )
        }
        composable(Destinations.BOOKMARKS.route) {
            Bookmark(navController, BookmarkViewModel.factory(LocalContext.current) )
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
package be.compose.rosselmix

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import be.compose.rosselmix.data.Room.RosselMixDatabase
import be.compose.rosselmix.ui.navigation.Destinations
import be.compose.rosselmix.ui.navigation.RosselMoxNavGraph
import be.compose.rosselmix.ui.theme.DarkBlue
import be.compose.rosselmix.ui.theme.RosselMixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RosselMixApp() {

    RosselMixTheme {

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()


        val items = listOf(
            Destinations.NEWS,
            Destinations.LATEST_NEWS,
            Destinations.NEWSPAPER,
            Destinations.BOOKMARKS
        )
        //instanciate the database
        RosselMixDatabase.instance(LocalContext.current)

        Scaffold(
            bottomBar = {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.iconId),
                                    contentDescription = item.route,
                                    tint = DarkBlue,
                                    modifier = Modifier.size(28.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(id = item.stringId),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = DarkBlue
                                )
                            },
                            selected = navBackStackEntry?.destination?.route == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.startDestinationId)
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) {innerpadding ->
            RosselMoxNavGraph(navController = navController,innerpadding)
        }

        // A surface container using the 'background' color from the theme
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            NewsFeed(NewsFeedViewModel(), navController)
//
//        }
    }

}
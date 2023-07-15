package be.compose.rosselmix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import be.compose.rosselmix.data.NewsFeedFetcher
import be.compose.rosselmix.ui.sections.NewsFeed
import be.compose.rosselmix.ui.sections.NewsFeedViewModel
import be.compose.rosselmix.ui.theme.RosselMixTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            RosselMixTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val coroutineScope = rememberCoroutineScope()
//                    coroutineScope.launch {
//                        NewsFeedFetcher().downloadXml(loadNews)
//                    }

                    //Greeting("Android")
                    NewsFeed(NewsFeedViewModel())

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RosselMixTheme {
        Greeting("Android")
    }
}
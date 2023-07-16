package be.compose.rosselmix.ui.sections

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import be.compose.rosselmix.R
import be.compose.rosselmix.data.room.Newspaper
import be.compose.rosselmix.ui.theme.DarkBlue
import be.compose.rosselmix.ui.theme.LightBlue
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState

@Composable
fun Newspaper(navController: NavHostController, viewModel: NewspaperViewModel) {



    val state = viewModel.state.collectAsState()

    if(state.value.selectedNewspaper != null) {
        //TODO : download pdf and open newspaper in pdfView
        val testUrl = "https://www.publicprocurement.be/sites/default/files/documents/man_um_enterprises_en_20200331_0.pdf"
        Log.d("Newspaper pdf ", "Newspaper: ${state.value.selectedNewspaper}")
        val pdfState = rememberVerticalPdfReaderState(
            resource = ResourceType.Remote(state.value.selectedNewspaper!!),
            isZoomEnable = true
        )

        VerticalPDFReader(state = pdfState, modifier = Modifier.fillMaxSize().background(LightBlue))
    }else {
        NewspaperScreen(
            state.value.newspapers,
            onItemClick = { url : String -> viewModel.selectNewspaper(url) }
        )
    }
}



@Composable
fun NewspaperScreen(newspapers: List<Newspaper>, onItemClick: (String) -> Unit) {
    Column {
        Header(title = R.string.newspapers)
        LazyColumn()   {
            items(newspapers.size) {
                NewspaperItem( newspapers[it].date ,onItemClick = { onItemClick( urlBuilder(newspapers[it])) } )
                NewsDivider()
            }
        }
    }
}

fun urlBuilder(newspaper: Newspaper) : String {
    val url = StringBuilder()
    val dateInfo = newspaper.date.split("/")
    //val x = "https://journal.metrotime.be/rm/prod/free/Metro%3AFR%3Aweb%2C2023-07-14%2CALL/ME_JOURNAL/2023-07-14/FULL/pdf_d-20230713-H0JJQY.pdf?id=d-20230713-H0JJQY&auth=a7559"
    url.append("https://journal.metrotime.be/rm/prod/free/Metro%3AFR%3Aweb%2C")
        .append("${dateInfo[2]}-${dateInfo[1]}-${dateInfo[0]}")
        .append("%2CALL/ME_JOURNAL/")
        .append("${dateInfo[2]}-${dateInfo[1]}-${dateInfo[0]}")
        .append("/FULL/pdf_d-")
        .append("${dateInfo[2]}${dateInfo[1]}${dateInfo[0]}-")
        .append("${newspaper.code}.pdf?id=d-")
        .append("${dateInfo[2]}${dateInfo[1]}${dateInfo[0].toInt() - 1}-") //TODO fix instead of -1 day before
        .append("${newspaper.code}&auth=")
        .append(newspaper.auth)
    return url.toString()
}


@Composable
fun NewspaperItem(date: String, onItemClick: () -> Unit) {
    val dateInfo = date.split("/")
    Row(

        modifier = Modifier
            .clickable(onClick = onItemClick)
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(painter = painterResource(id = R.drawable.metro_logo), contentDescription = stringResource(id = R.string.metro_logo))
        Text(
            text =  stringResource(id = R.string.newspaper_intro)   + " ${dateInfo[0]}/${dateInfo[1]}/${dateInfo[2]}",
            style = MaterialTheme.typography.bodyMedium,
            color = LightBlue
        )
        IconButton(onClick = onItemClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_open_full_foreground),
                contentDescription = stringResource(id = R.string.open_full_newspaper) ,
                tint = DarkBlue
            )
        }

    }
}
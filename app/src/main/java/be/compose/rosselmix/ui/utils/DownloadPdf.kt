package be.compose.rosselmix.ui.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService


@Composable
fun DownloadPdf(url : String, title : String) {

    val request =
        DownloadManager
            .Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle("Downloading PDF")
            .setDescription("Downloading $title")
            .setVisibleInDownloadsUi(true)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$title.pdf")
            .setMimeType("application/pdf")


    val context = LocalContext.current

    val mgr = context.getSystemService<DownloadManager>()

    //val downloadManager = getSystemService(context2,context) as DownloadManager

    val refId =  mgr!!.enqueue(request)
    Toast.makeText(LocalContext.current, "PDF Download Started", Toast.LENGTH_SHORT).show();


}
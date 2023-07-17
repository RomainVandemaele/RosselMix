package be.compose.rosselmix.ui.utils

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.os.Environment.getDataDirectory
import androidx.core.net.toUri


interface Downloader {
    fun downloadFile(url : String, title : String) : Long
}

class AndroidDownloader(private val context: Context ) : Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java) as DownloadManager
    override fun downloadFile(url: String, title: String): Long {
        val requet = DownloadManager.Request(url.toUri())
            .setMimeType("application/pdf")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("Downloading journal PDF")
            .setDescription("Downloading $title")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$title.pdf") //TODO fix for recent API > 32 scoped storage
            //.setDestinationInExternalFilesDir(context, context.filesDir.absolutePath, "$title.pdf")

        return downloadManager.enqueue(requet)

    }
}



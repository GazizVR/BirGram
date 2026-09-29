package org.gaziz.birgram.feature.chat.ui.mapper

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.Locale

fun getUriForFile(
    context: Context,
    file: File
): Uri {
    val authority = "${context.packageName}.fileProvider"
    return FileProvider.getUriForFile(context,authority,file)
}

fun Int.toDurationStr(): String {
    val totalSeconds = if (this < 0) 0 else this
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }
}
package org.gaziz.birgram.feature.chat.ui.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

private const val FILE_PROVIDER_SUFFIX = "fileProvider"

fun Context.getUriForFile(file: File): Uri {
    val authority = "${packageName}.$FILE_PROVIDER_SUFFIX"
    return FileProvider.getUriForFile(this,authority,file)
}

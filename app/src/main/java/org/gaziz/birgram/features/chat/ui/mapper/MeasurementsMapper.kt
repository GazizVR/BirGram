package org.gaziz.birgram.features.chat.ui.mapper

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

fun LocalDate.formatMonthDay(locale: Locale = Locale.getDefault()): String {
    val currentYear = Year.now().value

    val formatter = if (year == currentYear) {
        DateTimeFormatter.ofPattern("MMMM d", locale)
    } else {
        DateTimeFormatter.ofPattern("MMMM d, yyyy", locale)
    }

    return format(formatter)
}

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun LocalDateTime.toTimeString(): String = format(timeFormatter)

fun Long.toByteCount(): String? {
    if (this <= 0) return null

    val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()

    val index = digitGroups.coerceAtMost(units.lastIndex)
    val value = this / 1024.0.pow(index.toDouble())

    return if (index == 0) {
        "$this B"
    } else {
        String.format(Locale.US, "%.1f %s", value, units[index])
    }
}

fun String.toFileType(): String {

    val mime = this.lowercase().trim()

    return when {
        mime == "application/pdf" -> "PDF"
        mime == "application/msword" -> "DOC"
        mime == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "DOCX"
        mime == "application/vnd.ms-excel" -> "XLS"
        mime == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> "XLSX"
        mime == "application/vnd.ms-powerpoint" -> "PPT"
        mime == "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> "PPTX"
        mime == "text/plain" -> "TXT"
        mime == "text/html" -> "HTML"
        mime == "text/csv" -> "CSV"
        mime == "application/json" -> "JSON"
        mime == "application/zip" -> "ZIP"
        mime == "application/x-rar-compressed" || mime == "application/vnd.rar" -> "RAR"
        mime == "application/x-7z-compressed" -> "7Z"

        mime == "image/png" -> "PNG"
        mime == "image/jpeg" || mime == "image/jpg" -> "JPG"
        mime == "image/webp" -> "WEBP"
        mime == "image/gif" -> "GIF"
        mime == "image/svg+xml" -> "SVG"
        mime == "image/heic" || mime == "image/heif" -> "HEIC"

        mime == "audio/mpeg" || mime == "audio/mp3" -> "MP3"
        mime == "audio/wav" || mime == "audio/x-wav" -> "WAV"
        mime == "audio/ogg" -> "OGG"
        mime == "video/mp4" -> "MP4"
        mime == "video/x-matroska" -> "MKV"
        mime == "video/quicktime" -> "MOV"
        mime == "video/webm" -> "WEBM"

        mime.startsWith("image/") -> mime.substringAfter("image/").uppercase()
        mime.startsWith("video/") -> mime.substringAfter("video/").uppercase()
        mime.startsWith("audio/") -> mime.substringAfter("audio/").uppercase()
        mime.startsWith("text/") -> "TXT"

        else -> ""
    }
}

fun getUriForFile(
    context: Context,
    file: File
): Uri {
    val authority = "${context.packageName}.fileProvider"
    return FileProvider.getUriForFile(context,authority,file)
}

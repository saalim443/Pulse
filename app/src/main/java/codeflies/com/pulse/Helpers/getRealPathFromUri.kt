package codeflies.com.pulse.Helpers


import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import java.io.File

fun getRealPathFromUri(context: Context, uri: Uri): String? {
    var realPath: String? = null

    // For API level 19 and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
        val documentId = DocumentsContract.getDocumentId(uri)
        when {
            isExternalStorageDocument(uri) -> {
                val split = documentId.split(":")
                if (split.size >= 2) {
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        realPath = "${context.getExternalFilesDir(null)}/${split[1]}"
                    }
                }
            }
            isDownloadsDocument(uri) -> {
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    documentId.toLong()
                )
                realPath = getDataColumn(context, contentUri, null, null)
            }
            isMediaDocument(uri) -> {
                val split = documentId.split(":")
                if (split.size >= 2) {
                    val type = split[0]
                    val contentUri: Uri = when (type) {
                        "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        else -> Uri.EMPTY
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    realPath = getDataColumn(context, contentUri, selection, selectionArgs)
                }
            }
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        // For older versions or other content schemes
        realPath = getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        // For file-based URIs
        realPath = uri.path
    }

    return realPath
}




private fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
    context.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), selection, selectionArgs, null)?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)
            return it.getString(columnIndex)
        }
    }
    return null
}

private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

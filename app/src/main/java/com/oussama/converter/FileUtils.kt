package com.oussama.converter

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object FileUtils {

    fun getPath(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        var cursor: Cursor? = null

        return try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor?.moveToFirst()
            columnIndex?.let { cursor.getString(it) }
        } catch (e: Exception) {
            null
        } finally {
            cursor?.close()
        }
    }
}


package com.example.foody.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.navigation.NavAction
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.navOptions
import com.example.foody.R
import java.io.OutputStream

fun NavController.navigateWithDefaultAnimation(directions: NavDirections) {
    navigate(directions, navOptions {
        anim {
            enter = R.anim.nav_slide_in_right
            exit = R.anim.nav_slide_out_left
            popEnter = R.anim.nav_slide_in_left
            popExit = R.anim.nav_slide_out_right
        }
    })
}

fun Bitmap.toImageUri(context: Context, fileName: String): Uri? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val filename = "$fileName.jpg"
        var fos: OutputStream?
        var imageUri: Uri?
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        //use application context to get contentResolver
        val contentResolver = context.contentResolver

        contentResolver.also { resolver ->
            imageUri =
                resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver?.openOutputStream(it) }
        }

        fos?.use { compress(Bitmap.CompressFormat.JPEG, 70, it) }

        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        imageUri?.let { contentResolver?.update(it, contentValues, null, null) }

        return imageUri
    } else return null
}
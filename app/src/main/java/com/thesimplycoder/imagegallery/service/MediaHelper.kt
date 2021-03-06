package com.thesimplycoder.imagegallery.service

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.thesimplycoder.imagegallery.model.ImageGalleryUiModel

class MediaHelper {
    companion object {
        private val ALLOWED_IMAGE_TYPE: Array<String> = arrayOf("png", "jpg", "jpeg", "dng")

        fun getImageGallery(context: Context): MutableMap<String, ArrayList<ImageGalleryUiModel>> {
            val fetchImageGalleryList: MutableMap<String, ArrayList<ImageGalleryUiModel>> =
                mutableMapOf()
            val projection =
                arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA)

            val images: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor? =
                context.contentResolver.query(images, projection, null, null, null)

            if (cursor?.moveToFirst() == true) {
                var bucket: String
                var uri: String
                val bucketColumn: Int = cursor.getColumnIndex(projection[0])
                val imageUriColumn:Int =cursor.getColumnIndex(projection[1])

                do{
                    bucket = cursor.getString(bucketColumn)
                    uri =cursor.getString(imageUriColumn)
                    if(fetchImageGalleryList[bucket] == null){
                        fetchImageGalleryList[bucket] = ArrayList()
                    }
                    if(uri.substring(uri.length - 3, uri.length) in ALLOWED_IMAGE_TYPE || uri.substring( uri.length - 4, uri.length) in ALLOWED_IMAGE_TYPE){
                        fetchImageGalleryList[bucket]?.add(ImageGalleryUiModel(imageUri = uri))
                    }
                }while(cursor.moveToNext())
            }
            cursor?.close()

            return fetchImageGalleryList
        }
    }
}
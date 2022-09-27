package com.arabapps.hamaki.api

import android.graphics.Bitmap
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream


class RequestbodyUtils {
companion object{

    fun convertToRequestBody(part: String?): RequestBody? {
        return if (part.isNullOrEmpty()) null else {
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                part
            )
        }
    }

    fun getFileDataFromBitmap(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun convertToRequestBodyPart(
        key: String,
        mFile: RequestBody?
    ): MultipartBody.Part? {
        return if (mFile == null) null else MultipartBody.Part.createFormData(
            key,
            "$key.jpg",
            mFile
        )
    }
}
}
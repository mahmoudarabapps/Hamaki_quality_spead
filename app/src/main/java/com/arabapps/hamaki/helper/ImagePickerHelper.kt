package com.sasco.user.helper

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.arabapps.hamaki.api.RequestbodyUtils
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.IOException


class ImagePickerHelper {
    companion object {


        private fun getCaptureImageOutputUri(context: Context): Uri? {
            var outputFileUri: Uri? = null
            val getImage: File? = context.getExternalCacheDir()
            if (getImage != null) {
                outputFileUri = Uri.fromFile(File(getImage.getPath(), "profile.png"))
            }
            return outputFileUri
        }

        fun getPickImageChooserIntent(context: Context): Intent? {
            // Determine Uri of camera image to save.
            val outputFileUri: Uri? = getCaptureImageOutputUri(context)
            val allIntents: MutableList<Intent> = ArrayList()
            val packageManager: PackageManager = context.getPackageManager()

            // collect all camera intents
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val listCam =
                packageManager.queryIntentActivities(captureIntent, 0)
            for (res in listCam) {
                val intent = Intent(captureIntent)
                intent.component = ComponentName(
                    res.activityInfo.packageName,
                    res.activityInfo.name
                )
                intent.setPackage(res.activityInfo.packageName)
                if (outputFileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                }
                allIntents.add(intent)
            }

            // collect all gallery intents
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            val listGallery =
                packageManager.queryIntentActivities(galleryIntent, 0)
            for (res in listGallery) {
                val intent = Intent(galleryIntent)
                intent.component = ComponentName(
                    res.activityInfo.packageName,
                    res.activityInfo.name
                )
                intent.setPackage(res.activityInfo.packageName)
                allIntents.add(intent)
            }

            // the main intent is the last in the list (fucking android) so pickup the useless one
            var mainIntent: Intent? = allIntents[allIntents.size - 1]
            for (intent in allIntents) {
                if (intent.component?.getClassName() == "com.android.documentsui.DocumentsActivity"
                ) {
                    mainIntent = intent
                    break
                }
            }
            allIntents.remove(mainIntent)

            // Create a chooser from the main intent
            val chooserIntent = Intent.createChooser(mainIntent, "Select source")

            // Add all other intents
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray())
            return chooserIntent
        }

        fun pickImage(context: Activity) {
            context.startActivityForResult(getPickImageChooserIntent(context), 200)
        }

        fun getPickImageResultUri(data: Intent?, context: Context): Uri? {
            var isCamera = true
            if (data != null) {
                val action = data.action
                isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
            }
            return if (isCamera) getCaptureImageOutputUri(context) else data!!.data
        }

        fun pickedImage(
            context: Context,
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ): Bitmap? {
            var bitmap: Bitmap?
            if (resultCode == Activity.RESULT_OK) {
                if (getPickImageResultUri(data, context) != null) {
                    val picUri: Uri? = getPickImageResultUri(data, context)
                    try {
                        bitmap =
                            MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri)
                        bitmap = rotateImageIfRequired(bitmap, picUri!!)
                        return bitmap
                    } catch (e: Exception) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                context.getContentResolver(),
                                data?.data
                            )
                            return bitmap
                        } catch (ee: Exception) {
                        }
                        e.printStackTrace()
                    }
                } else {
                    bitmap = data?.extras!!["data"] as Bitmap?
                    return bitmap
                }
            }
            return null
        }

        @Throws(IOException::class)
        private fun rotateImageIfRequired(img: Bitmap?, selectedImage: Uri): Bitmap? {
            val ei = selectedImage.getPath()?.let { ExifInterface(it) }
            val orientation: Int = ei!!.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            return when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
                else -> img
            }
        }

        private fun rotateImage(img: Bitmap?, degree: Int): Bitmap? {
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            val rotatedImg =
                Bitmap.createBitmap(img!!, 0, 0, img.width, img.height, matrix, true)
            img.recycle()
            return rotatedImg
        }

        fun pickImage(context: Fragment) {
            val options =
                arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder = AlertDialog.Builder(context.context)
            builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
                if (options[item] == "Take Photo") {
                    if (ContextCompat.checkSelfPermission(
                            context.requireContext(), android.Manifest.permission.CAMERA
                        ) ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        context.startActivityForResult(takePicture, 137)
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            context.requestPermissions(
                                arrayOf<String>(android.Manifest.permission.CAMERA),
                                123
                            )
                        }
                    }
                } else if (options[item] == "Choose from Gallery") {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    context.startActivityForResult(intent, 432)
                } else if (options[item] == "Cancel") {
                    dialog.dismiss()
                }
            })
            builder.show()
        }

        fun toRequestbody(bitmap: Bitmap): RequestBody? {
            if (bitmap == null)
                return null
            return RequestbodyUtils.getFileDataFromBitmap(bitmap)?.let {
                RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    it
                )
            }
        }
    }
}
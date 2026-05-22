package de.jstrecker.businesscard

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import java.io.File
import java.io.FileOutputStream
import androidx.core.net.toUri

private lateinit var androidContext: Context

fun initPlatformContext(context: Context) {
    androidContext = context
}

class AndroidPlatformActions : PlatformActions {
    override fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        androidContext.startActivity(intent)
    }

    override fun makeCall(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, "tel:$phone".toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        androidContext.startActivity(intent)
    }

    override fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO, "mailto:$email".toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        androidContext.startActivity(intent)
    }

    @Composable
    override fun rememberImagePicker(onImagePicked: (String) -> Unit): () -> Unit {
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                val inputStream = androidContext.contentResolver.openInputStream(it)
                val file = File(androidContext.filesDir, "profile_image.jpg")
                inputStream?.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                onImagePicked(file.absolutePath)
            }
        }
        return {
            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }
}

actual fun getPlatformActions(): PlatformActions = AndroidPlatformActions()

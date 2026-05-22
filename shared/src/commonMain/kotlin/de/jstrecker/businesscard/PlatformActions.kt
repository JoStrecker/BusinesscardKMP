package de.jstrecker.businesscard

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

interface PlatformActions {
    fun openUrl(url: String)
    fun makeCall(phone: String)
    fun sendEmail(email: String)
    @Composable
    fun rememberImagePicker(onImagePicked: (String) -> Unit): () -> Unit
}

expect fun getPlatformActions(): PlatformActions

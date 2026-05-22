package de.jstrecker.businesscard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

class IOSPlatformActions : PlatformActions {
    override fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }

    override fun makeCall(phone: String) {
        val nsUrl = NSURL.URLWithString("tel:$phone")
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }

    override fun sendEmail(email: String) {
        val nsUrl = NSURL.URLWithString("mailto:$email")
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }

    @Composable
    override fun rememberImagePicker(onImagePicked: (String) -> Unit): () -> Unit {
        // Implementation for iOS image picker would require a bit more setup with delegates.
        // For now, I'll provide a stub or basic implementation if possible.
        // Actually, in KMP Compose, we usually use a library or a complex delegate setup.
        return {
             // TODO: Implement iOS Image Picker
        }
    }
}

actual fun getPlatformActions(): PlatformActions = IOSPlatformActions()
